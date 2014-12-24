package smlfr;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import artworkUpdateModel.ArtworkUpdateEvent;
import artworkUpdateModel.ArtworkUpdateListener;
import artworkUpdateModel.ArtworkUpdateType;
import artworkUpdateModel.WallUpdateRequestEvent;
import artworkUpdateModel.ArtworkUpdateRequestListener;

import processing.core.PApplet;
import processing.data.*;

import SMUtils.JsonCreator;
import SMUtils.Lang;
import SMUtils.awFileSize;

public class SM_FileManager extends PApplet implements ArtworkUpdateRequestListener {

	private JsonCreator 	creator;
	private ImageIcon		icon;
	private JFileChooser	fc;

	private File 			preferencesPath;
	private File 			museumPath;
	private File			projectPath;
	private File			tempProjectPath;

	private JSONObject		preferences;
	private JSONObject 		museum;
	private JSONObject		project;
	
	private String			currentProjectName;

	private SmlFr			base;
	EventListenerList		updateListeners;

	private boolean			loaded = false;
	private boolean			savedirty = false;
	

	public SM_FileManager(SmlFr _base, ImageIcon _icon) {
		
		base = _base;
		icon = _icon;
		creator = new JsonCreator(this);
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				Lang.SM_filetypes, "sfp");
		fc.setFileFilter(filter);

		updateListeners = new EventListenerList();
		
		File res = new File("resources");
		if( !res.exists() ) {
			javax.swing.JOptionPane.showMessageDialog(this,Lang.couldntFindResourceFolder, "couldn't find...", javax.swing.JOptionPane.WARNING_MESSAGE);
			System.exit(1);
		}


		// init file paths and load files:
		preferencesPath = new File("resources/prefs.txt");
		tempProjectPath = preferencesPath;
		preferences = loadPrefs();
		museumPath = new File("resources/"+preferences.getString("museumData"));
		museum = loadMuseumData();


		//		System.out.println("\nTHE MUSEUM FROM FILE:\n\n"+museum.toString());
		//		System.out.println("\nTHE PREFERENCES FROM FILE:\n\n"+preferences.toString());
	}


	// general

	public synchronized boolean isProjectLoaded() {
		return loaded;
	}
	
	public synchronized boolean isMuseumLoaded() {
		if( museum != null && preferences != null ) {
			return true;
		}
		else return false;
	}

	// PREFERENCES

	public synchronized void updatePrefs(String _key, String _value) {

		if( _key.equalsIgnoreCase("previousProject") ) {
			try {

				JSONObject o = JSONObject.parse(_value);
				preferences.setJSONObject("previousProject", o);

			} catch(Exception e) { e.printStackTrace(); }
		}
		else {
			preferences.setString(_key, _value);	
		}
		saveJSONObject(preferences, preferencesPath.getAbsolutePath());
	}

	public synchronized String getCurrentProjectName() {
		if( project != null) return project.getString("projectName");
		else return "no project loaded";
	}

	private synchronized JSONObject loadPrefs() {

		JSONObject j;
		if ( preferencesPath.exists() ) {
			try {
				j = loadJSONObject(preferencesPath);
			} catch (Exception e) {

				javax.swing.JOptionPane.showMessageDialog(this,
						Lang.couldntLoadPrefs, "couldn't load",
						javax.swing.JOptionPane.WARNING_MESSAGE);
				System.exit(1);
				j = null;
			}
		}else{
			javax.swing.JOptionPane.showMessageDialog(this, 
					Lang.couldntFindPrefs, "couldn't find file...", 
					javax.swing.JOptionPane.WARNING_MESSAGE);
			j = null;
		}
		return j;
	}

	// MUSEUM:

	public JSONObject loadMuseumData() {

		if( !museumPath.exists() ) {
			javax.swing.JOptionPane.showMessageDialog(this, Lang.couldntFindMuseum, "couldn't find...", javax.swing.JOptionPane.WARNING_MESSAGE);
			System.exit(1);
		}


		JSONObject j;
		try {
			j = loadJSONObject(museumPath);
		} catch (Exception e) {

			javax.swing.JOptionPane.showMessageDialog(this, Lang.couldntLoadMuseumData, "couldn't load...", javax.swing.JOptionPane.WARNING_MESSAGE);
			System.exit(1);
			j = null;
		}

		return j;
	}

	public synchronized JSONObject getMuseum() {
		return museum;
	}

	public synchronized String getMuseumName() {
		if( museum != null) {

			return museum.getString("museumName");
		}
		else return null;
	}

	public synchronized JSONObject getArchitecture() {
		return museum.getJSONObject("architecture");
	}

	public synchronized JSONObject getRoomFromArchitecture( String _room) {

		if( museum != null ) {
			JSONObject o = museum.getJSONObject("architecture").getJSONObject(_room);
			return o;
		} else {
			return null;
		}

	}

	public synchronized File getFilePathForRoom(String _room) {
		
		String museumIdentifier = museumPath.getAbsolutePath().substring(museumPath.getAbsolutePath().lastIndexOf("/")+1,museumPath.getAbsolutePath().length()-4);
		System.out.println(museumIdentifier);
		String filePath4Room = museumPath.getAbsolutePath().substring(0,museumPath.getAbsolutePath().length()-4)+"/"+_room;
		System.out.println(filePath4Room);
		return new File(filePath4Room);
	}
	
	// PROJECT
	
	private void saveTempProject() {
		savedirty = true;
		
		String tempProjFileName = projectPath.getAbsoluteFile().getName()+".tmp";
		
		tempProjectPath = new File(preferencesPath.getParent()+"/"+tempProjFileName);
		System.out.println("the tempProject was saved as "+tempProjectPath);
		saveJSONObject(project, tempProjectPath.getAbsolutePath());
	}

	public synchronized String[] getPreviousProject() {

		String[] pp = new String[2];

		pp[0] = preferences.getJSONObject("previousProject").getString("projectName");
		pp[1] = preferences.getJSONObject("previousProject").getString("projectPath");

		return pp;
	}

	public void newProject(String _name, String[] _selectedRooms) {

		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);

		int i = fc.showSaveDialog(this);
		if( i == JFileChooser.APPROVE_OPTION) {

			System.out.println(fc.getSelectedFile());

			saveJSONObject(creator.makeNewProjectFile(_name, _selectedRooms), fc.getSelectedFile()+".sfp");

			updatePrefs("fileChooserCurrentDirectory", fc.getSelectedFile().toString());

		} else {
			return;
		}


	}

	public synchronized void loadProject(File _f) {

		if( _f == null ) {
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int i = fc.showOpenDialog(this);
			if( i == JFileChooser.APPROVE_OPTION) {

				_f = fc.getSelectedFile();


			} else { loaded = false; return; }
		}

		if( _f.exists() ) {
			System.out.println("loading this project:\n"+_f.getAbsolutePath());
			project = loadJSONObject(_f);
			loaded = true;
		}
		else {
			String msg = Lang.couldntLoadProject_1 + _f.getName() + Lang.couldntLoadProject_2 + _f.getPath();
			javax.swing.JOptionPane.showMessageDialog(this,msg, "couldn't find...", javax.swing.JOptionPane.WARNING_MESSAGE);
			loaded = false;
		}


		if( loaded ) {
			JSONObject p = new JSONObject();
			p.setString("projectPath", _f.getAbsolutePath());
			p.setString("projectName", getCurrentProjectName());
			updatePrefs("previousProject", p.toString());

			projectPath = _f;
			System.out.println("projectPath: "+projectPath.getAbsolutePath());
			currentProjectName = p.getString("projectName");
		}
	}

	public synchronized String[] getRoomNamesInProject() {

		String[] rms;
		JSONArray rooms = project.getJSONArray("rooms");
		rms = new String[rooms.size()];

		for( int i=0; i< rooms.size(); i++) {

			rms[i] = rooms.getJSONObject(i).getString("roomName"); 
		}

		return rms;
	}
	
	public synchronized JSONArray getRoomsInProject() {
		
		if( loaded ) {
			return project.getJSONArray("rooms");
		}
		else {
			return null;
		}
	}
	
	public synchronized String[] getArtLibraryFromProject() {
		
		return project.getJSONArray("artLibrary").getStringArray();
		
	}
	
	// ARTWORK
	
	public synchronized JSONObject loadArtwork(String _name) {
		
		String filePath = projectPath.getAbsolutePath().substring(0, projectPath.getAbsolutePath().length()-4)+"_lib/"+_name+".sfa";
		JSONObject aw;
		if(new File(filePath).exists()) {
			aw = loadJSONObject(filePath);
		}
		else {
			javax.swing.JOptionPane.showMessageDialog(this, Lang.couldntLoadArtwork, "couldn't load ...", javax.swing.JOptionPane.WARNING_MESSAGE, icon);
			System.exit(1);
			aw=null;
		}
		return aw;
	}

	public synchronized File getFilePathForArtwork(String _artwork, SMUtils.awFileSize _size) {
		
		String sufx;
		switch (_size) {
		case THUMB:
			sufx = "_thumb";
			break;
		case MEDIUM:
			sufx = "_med";
			break;
		case LARGE:
			sufx = "_full";
			break;
			
		default:
			sufx = "_med";
			break;
		}
		
		String filePath = projectPath.getAbsolutePath().substring(0, projectPath.getAbsolutePath().length()-4)+"_lib/"+_artwork+"/"+_artwork+sufx+".png";

		
		
		return new File(filePath);
	}

	// UPDATE Event handling

	public void registerUpdateListener(ArtworkUpdateListener _listener) {
		updateListeners.add(ArtworkUpdateListener.class, _listener);
	}
	
	public void unregisterUpdateListener(ArtworkUpdateListener _listener) {
		updateListeners.remove(ArtworkUpdateListener.class, _listener);
	}
	
	@Override
	public synchronized void updateRequested(WallUpdateRequestEvent e) {
		

		System.out.println("the artwork name: "+  e.getName());
		System.out.println("the room: "+e.getRoom());
		
		String target = "w_"+e.getRoom()+"_"+e.getWall();

		System.out.println(target);
		
		// update artwork
		// update room
		
		SM_Artwork thisAw = base.getArtwork(this, (e.getName()));
		SM_Room thisRm = base.getRoom(this, e.getRoom());		

		if(! thisRm.hasArtwork(e.getName(), e.getWall()) ) {
			thisAw.setWall(this, target);
			thisRm.addArtworkToWall(this, thisAw, e.getWall());
		}
		
		// update Json
		
		for(int i=0; i<project.getJSONArray("rooms").size(); i++) {
			JSONObject r = project.getJSONArray("rooms").getJSONObject(i);
			if(r.getString("roomName").equalsIgnoreCase(e.getRoom())) {
				r.getJSONObject(target).getJSONArray("artworks").append(thisAw.getAsJsonObject(this));
			}
		}
		
		savedirty = true;
		saveTempProject();
		
		
		ArtworkUpdateEvent e2 = new ArtworkUpdateEvent(this, e.getName(), ArtworkUpdateType.WALL);
		for(ArtworkUpdateListener lsnr : updateListeners.getListeners(ArtworkUpdateListener.class) ) {
			lsnr.artworkUpdate(e2);
		}
	}

}





















