package smlfr;

import java.io.File;
import java.util.LinkedHashMap;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileNameExtensionFilter;

import artworkUpdateModel.ArtworkUpdateEvent;
import artworkUpdateModel.ArtworkUpdateListener;
import artworkUpdateModel.ArtworkUpdateRequestEvent;
import artworkUpdateModel.ArtworkUpdateType;
import artworkUpdateModel.WallUpdateRequestEvent;
import artworkUpdateModel.ArtworkUpdateRequestListener;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.*;
import smimport.SM_JSONCreator;

import SMUtils.FrameStyle;
import SMUtils.Lang;
import SMUtils.SM_Frames;

public class SM_FileManager extends PApplet implements ArtworkUpdateRequestListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5625682259511650553L;
	private SM_JSONCreator 	creator;
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
	EventListenerList		updateListeners_ArrViews;

	private boolean			loaded = false;
	private boolean			savedirty = false;
	

	public SM_FileManager(SmlFr _base, ImageIcon _icon) {
		
		base = _base;
		icon = _icon;
		creator = new SM_JSONCreator(this);
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				Lang.SM_filetypes, "sfp");
		fc.setFileFilter(filter);

		updateListeners = new EventListenerList();
		updateListeners_ArrViews = new EventListenerList();
		
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

	public synchronized boolean isSaveDirty() {
		return savedirty;
	}
	
	private void setSaveDirty(boolean _sd) {
		savedirty = _sd;
		base.lib.setSaveDirtyMark(_sd);
	}
	
	public void loadFrames(SM_Frames f) {
		for(FrameStyle s : FrameStyle.values()){
			PImage i = null;
			switch (s) {
			case WOOD_DARK_BROWN:
				i = loadImage("resources/Frames/Rahmen_Holz_Dunkelbraun_1.png") ;
				break;
			case WOOD_MEDIUM_BROWN:
				i = loadImage("resources/Frames/Rahmen_Holz_Hellbraun_1.png");
				break;
			case WOOD_LIGHT_BROWN:
				i = loadImage("resources/Frames/Rahmen_Holz_Hellbraun_2.png");
				break;
			case WOOD_BLACK:
				i = loadImage("resources/Frames/Rahmen_Holz_Schwarz_1.png");
				break;
			case WOOD_LIGHT_GREY:
				i = loadImage("resources/Frames/Rahmen_Holz_Dunkelgrau_2.png");
				break;
			case POMP_GOLD_1:
				i = loadImage("resources/Frames/Rahmen_Prunk_Gold_1.png");
				break;
			case POMP_GOLD_2:
				i = loadImage("resources/Frames/Rahmen_Prunk_Gold_2.png");
				break;
			case POMP_GOLD_3:
				i = loadImage("resources/Frames/Rahmen_Prunk_Gold_3.png");
				break;
			case POMP_GOLD_BLACK:
				i = loadImage("resources/Frames/Rahmen_Prunk_Gold_Schwarz_1.png");
				break;
			case POMP_GOLD_AND_BLACK:
				i = loadImage("resources/Frames/Rahmen_Prunk_Schwarz_Gold_1.png");
				break;
			}
			f.setFrameImg(s, i);
		}
	}
	
	public PImage getShadowImage() {
		return loadImage("resources/shadow/Schatten.png");
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
		String filePath4Room = museumPath.getAbsolutePath().substring(0,museumPath.getAbsolutePath().length()-4)+"/"+_room;
		return new File(filePath4Room);
	}
	
	// PROJECT
	
	public synchronized boolean requestSave() {
		
		if( savedirty ) {
			return saveProject();
			
		}
		else return false;
		
		
	}
	
	private boolean saveProject() {
		if(savedirty) {
			try{
				saveJSONObject(project, projectPath.getAbsolutePath());
				setSaveDirty(false);
				return true;
			} catch( Exception e ) {
				return false;
			}
		}
		return false;
	}
	
	private void saveTempProject() {
		setSaveDirty(true);
		
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

	public File newProject() {

		String[] _selectedRooms = new String[] { "S1", "S2", "S3"};
		
		String projectName = JOptionPane.showInputDialog(null,
				  Lang.newProjectName,
				  Lang.newProjectNameTitle,
				  JOptionPane.QUESTION_MESSAGE);
		
		if(projectName == null ) return null;
		
		System.out.println("the NAME NEAM NESM NESAM  ----->> "+projectName);
		
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle(Lang.newProjectLocation_1+projectName+Lang.newProjectLocation_2);
		int i = fc.showOpenDialog(this);
		
		if( i == JFileChooser.APPROVE_OPTION) {

			System.out.println( "schau genau hier!  ::--->>"+fc.getSelectedFile());
			
			File projectFileSaveLoc = new File(fc.getSelectedFile()+"/"+projectName);
			File artLibSaveLocation = new File(fc.getSelectedFile()+"/"+projectName+"/"+projectName+"_lib");
			
			System.out.println("projectFileSaveLoc: "+projectFileSaveLoc.getAbsolutePath());
			System.out.println("artLibSaveLocation: "+artLibSaveLocation.getAbsolutePath());
			


			JSONObject theNewProj = creator.makeNewProjectFile(projectName, _selectedRooms); 
			String[] importedAws;

			int q = javax.swing.JOptionPane.showOptionDialog(null, Lang.importNowTitle, Lang.importNow, javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, Lang.importNowBtns, 2);

			switch (q) {
			case 0:
				System.out.println("NEIN GESAGT!");
				break;
			case 1:
				
				importedAws = base.in.batchImport(artLibSaveLocation);
				
				JSONArray lib = new JSONArray();
				
				for( String aw : importedAws) {
					lib.append(aw);
				}
				
				theNewProj.setJSONArray("artLibrary", lib);
				
				break;
			default:
				break;
			}
			
			String pFileName = projectFileSaveLoc.getAbsolutePath()+"/"+projectName+".sfp";
			System.out.println("THE PROJECT WILL BE SAVED AS: "+pFileName);
			
			
			saveJSONObject(theNewProj, pFileName);

			updatePrefs("fileChooserCurrentDirectory", projectFileSaveLoc.getAbsolutePath());

			return new File(projectFileSaveLoc.getAbsolutePath()+"/"+projectName+".sfp");
			
		} else {
			return null;
		}


	}

	public synchronized void loadProject(File _f) {

		if( _f == null ) {
			fc.setDialogTitle(Lang.loadProjectTitle);
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
	
	public synchronized File getArtLibraryPath() {
		String tmp = projectPath.getAbsolutePath();
		return new File(tmp.substring(0, tmp.lastIndexOf('.'))+"_lib");
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

	public synchronized void registerUpdateListener(ArtworkUpdateListener _listener) {
		
		if(_listener.getClass() == (SM_WallArrangementView.class)) {
			
			updateListeners_ArrViews.add(ArtworkUpdateListener.class, _listener);
		} else {
			updateListeners.add(ArtworkUpdateListener.class, _listener);
		}

	}
	
	public synchronized void unregisterUpdateListener(ArtworkUpdateListener _listener) {
		if(_listener.getClass() == (SM_WallArrangementView.class)) {

			updateListeners_ArrViews.remove(ArtworkUpdateListener.class, _listener);
		} else {
			updateListeners.remove(ArtworkUpdateListener.class, _listener);
		}
		//		updateListeners.remove(ArtworkUpdateListener.class, _listener);
	}
	
	public synchronized void unregisterArrViewAWUpdateListeners() {
		
//		for( int i = updateListeners_ArrViews.getListenerCount(); i > 0; i--) {
//			
//			updateListeners_ArrViews.remove(t, l)
//		}
		
		
		Object[] lstnrs = updateListeners_ArrViews.getListeners(ArtworkUpdateListener.class);
		
		
		for( Object l : lstnrs ) {
			ArtworkUpdateListener ls = (ArtworkUpdateListener)l;
			updateListeners_ArrViews.remove(ArtworkUpdateListener.class, ls);
		}
		

	}
	
	public synchronized void unregisterViewManagerUpdateListeners() {

		System.out.println("Trying to remove "+updateListeners_ArrViews.getListenerCount()+" Listeners");
		
		Object[] lstnrs = updateListeners.getListeners(ArtworkUpdateListener.class);
		
		System.out.println("i have gotten "+lstnrs.length+" listeners as array");
		
		for( Object l : lstnrs ) {
			ArtworkUpdateListener ls = (ArtworkUpdateListener)l;
			
			if(ls.getClass().equals(SM_ViewManager.class)) {
				System.out.println("removing view manager...");
				updateListeners.remove(ArtworkUpdateListener.class, ls);
			}
		}
		
		System.out.println("removed some, now its "+updateListeners.getListenerCount()+" Listeners");
	}
	
	public synchronized void updateRequested(ArtworkUpdateRequestEvent e) {
		
		ArtworkUpdateEvent e2;
		String eventAW       = e.getName();
		
		switch (e.getType()) {
		case POS_IN_WALL:

			
			// Update Artwork
			
			SM_Artwork thisAw = base.getArtwork(this, (eventAW));
			thisAw.setTotalWallPos(e.getNewPosX(), e.getNewPosY());
			
			// Update Project-Json
			
			for(int i=0; i<project.getJSONArray("rooms").size(); i++) {
				JSONObject r = project.getJSONArray("rooms").getJSONObject(i);
				
				if( thisAw.getWall().contains(r.getString("roomName")) ) {
					JSONArray aws = r.getJSONObject(thisAw.getWall()).getJSONArray("artworks");
					
					for(int ii = 0; ii < aws.size(); ii++) {
						if(aws.getJSONObject(ii).getString("invNr").equalsIgnoreCase(e.getName()) ) {
							JSONArray nPos = new JSONArray();
							nPos.setInt(0, e.getNewPosX());
							nPos.setInt(1, e.getNewPosY());
							aws.getJSONObject(ii).setJSONArray("pos", nPos);
						}
					}
					
				}
				
			}
			String notifyWall = base.artworks.get(eventAW).getWall();
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("wall", notifyWall);

			e2 = new ArtworkUpdateEvent(this, eventAW, ArtworkUpdateType.POS_IN_WALL, data);
			break;

		default:
			e2 = new ArtworkUpdateEvent(this, eventAW, ArtworkUpdateType.BLANK, null);

			break;
		}
		
		setSaveDirty(true);
		saveTempProject();
		
		System.out.println(e2);
		
		for(ArtworkUpdateListener lsnr : updateListeners.getListeners(ArtworkUpdateListener.class) ) {
			lsnr.artworkUpdate(e2);
			System.out.println("fire regular  " + lsnr.getClass());
		}
		for(ArtworkUpdateListener lsnr : updateListeners_ArrViews.getListeners(ArtworkUpdateListener.class) ) {
			lsnr.artworkUpdate(e2);
			System.out.println("fire arrView  " + lsnr.getClass());
		}
		
	}
	
	@Override
	public synchronized void updateRequested(WallUpdateRequestEvent e) {
		

		System.out.println("the artwork name: "+  e.getName());
		System.out.println("the target room: "+e.getTargetRoom());
		
		String targetWall = "w_"+e.getTargetRoom()+"_"+e.getTargetWall();
		String originWall = "w_"+e.getOriginRoom()+"_"+e.getOriginWall();

		
		System.out.println("the Target is: "+targetWall);
		System.out.println("the Origin is: "+originWall);
		
		// update artwork
		// update room (Target)
		
		SM_Artwork thisAw = base.getArtwork(this, (e.getName()));
		
		
		if ( ! e.getTargetRoom().equalsIgnoreCase("Library")) {
			SM_Room thisRm = base.getRoom(this, e.getTargetRoom());
			if (!thisRm.hasArtworkOnWall(e.getName(), e.getTargetWall())) {
				thisAw.setWall(this, targetWall);
				thisRm.addArtworkToWall(this, thisAw, e.getTargetWall());
			}
		} else {
			thisAw.setWall(this, null);
		}
		
		
		// update room (origin)
		
		if( ! e.getOriginRoom().equalsIgnoreCase("Library")) {
			
			System.out.println("Getting This Wall: "+e.getOriginWall() +" in this room: "+e.getOriginRoom());
			
			SM_Wall originSM_Wall = (SM_Wall)base.getRoom(this, e.getOriginRoom()).getWalls().get((e.getOriginWall()));
			originSM_Wall.removeArtwork(e.getName());
		}
		
		// update Json
		
		for(int i=0; i<project.getJSONArray("rooms").size(); i++) {
			JSONObject r = project.getJSONArray("rooms").getJSONObject(i);
			if(r.getString("roomName").equalsIgnoreCase(e.getTargetRoom())) {
				r.getJSONObject(targetWall).getJSONArray("artworks").append(thisAw.getAsJsonObjectForProject(this));
			}
			if(r.getString("roomName").equalsIgnoreCase(e.getOriginRoom())) {
				
				SM_Wall originSM_Wall = (SM_Wall)base.getRoom(this, e.getOriginRoom()).getWalls().get((e.getOriginWall()));

				JSONArray aws = r.getJSONObject(originSM_Wall.getWallName()).getJSONArray("artworks");
				for(int ii = 0; ii < aws.size(); ii++) {
					if(aws.getJSONObject(ii).getString("invNr").equalsIgnoreCase(e.getName())) {
						aws.remove(ii);
					}
				}
			}
		}
		
		setSaveDirty(true);
		saveTempProject();
		
		
		LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("wall_1", targetWall);
		data.put("wall_2", originWall);
		
		ArtworkUpdateEvent e2 = new ArtworkUpdateEvent(this, e.getName(), ArtworkUpdateType.WALL, data);
		System.out.println(e2);
		for(ArtworkUpdateListener lsnr : updateListeners.getListeners(ArtworkUpdateListener.class) ) {
			lsnr.artworkUpdate(e2);
			System.out.println("fire regular  " + lsnr.getClass());
		}
		for(ArtworkUpdateListener lsnr : updateListeners_ArrViews.getListeners(ArtworkUpdateListener.class) ) {
			lsnr.artworkUpdate(e2);
			System.out.println("fire arrView  " + lsnr.getClass());
		}
	}


	
	public void requestQuit() {
		
		if (savedirty) {
			int choose = javax.swing.JOptionPane.showOptionDialog(null,
					Lang.wantToSaveBeforExit, Lang.wantToSaveBeforExitTitle,
					javax.swing.JOptionPane.YES_NO_OPTION,
					javax.swing.JOptionPane.YES_NO_OPTION, null,
					Lang.saveOnExitOptions, 0);
			switch (choose) {
			case 0:

				break;
			case 1:
				System.out.println("eins gwlt");
				saveProject();
				break;

			default:
				break;
			}
		}
		System.exit(0);
	}

	
}





















