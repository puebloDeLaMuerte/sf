package smlfr;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileNameExtensionFilter;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.*;
import smimport.ImportThread;
import smimport.SM_JSONCreator;

import SMUtils.FileManagerSheduledUpdateThread;
import SMUtils.FrameStyle;
import SMUtils.Lang;
import SMUtils.NewProjectDialog;
import SMUtils.SM_Frames;
import SMUtils.SysInfo;
import SMUtils.artworkActionType;
import SMUtils.awFileSize;
import SMupdateModel.ArtworkUpdateRequestEvent;
import SMupdateModel.ArtworkUpdateRequestListener;
import SMupdateModel.UpdateEvent;
import SMupdateModel.UpdateListener;
import SMupdateModel.UpdateType;
import SMupdateModel.WallColorUpdateRequestEvent;
import SMupdateModel.WallUpdateRequestEvent;

public class SM_FileManager extends PApplet implements ArtworkUpdateRequestListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5625682259511650553L;
	
	private SM_JSONCreator 					creator;
//	private ImageIcon						icon;
	private JFileChooser					fc;

	private File 							preferencesPath;
	private File 							museumPath;
	private File							projectPath;
	private File							tempProjectPath;
	private File							resourcesPath;

	private JSONObject						preferences;
	private JSONObject 						museum;
	private JSONObject						project;
	
	private String							currentProjectName;

	private SmlFr							base;
	EventListenerList						updateListeners;
	EventListenerList						updateListeners_ArrViews;

	private boolean							loaded = false;
	private boolean							savedirty = false;
	
	private UpdateType									queueType;
	private int											updatesQueued;
	private ArrayList<LinkedHashMap<String, Object>>	dataQueue;
	

	public SM_FileManager(SmlFr _base, ImageIcon _icon) {
		
		base = _base;
//		icon = _icon;
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
				
		
		resourcesPath   = new File("resources");
		preferencesPath = new File("resources/prefs.txt");
		tempProjectPath = new File(resourcesPath.getAbsolutePath()+"/tmp");
		preferences = loadPrefs();
		museumPath = new File("resources/"+preferences.getString("museumData"));
		museum = loadMuseumData();

		// update Queue
		
		updatesQueued = 0;
		dataQueue = new ArrayList<LinkedHashMap<String, Object>>();
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
	
	public void setSaveDirty(boolean _sd) {
		savedirty = _sd;
		if (base.lib != null) {
			base.lib.setSaveDirtyMark(_sd);
		}
		
		SysInfo.printHeapStats();
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
			case WOOD_DARK_GREY:
				i = loadImage("resources/Frames/Rahmen_Holz_Dunkelgrau_1.png");
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
			default:
				// no need to load something if frameStyle is STANDART or NONE. STANDART is related to one of these frames elsewhere.
				break;
			}
			f.setFrameImg(s, i);
		}
	}
	
	public PImage getShadowImage() {
		return loadImage("resources/shadow/Schatten.png");
	}
	
	public PImage getLightGfx() {
		return loadImage("resources/light/Light_Cone_White.png");
	}
	
	public ImageIcon getQuestionIcon() {
		return base.getQuestionIcon();
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

	private synchronized JSONObject loadPrefs() {
		
		JSONObject j;
		if ( preferencesPath.exists() ) {
			try {
				j = loadJSONObject(preferencesPath);
			} catch (Exception e) {
				
				javax.swing.JOptionPane.showMessageDialog(this,
						Lang.couldntLoadPrefs, Lang.warning,
						javax.swing.JOptionPane.WARNING_MESSAGE, base.getWarningIcon());
				System.exit(1);
				j = null;
			}
		}else{
			javax.swing.JOptionPane.showMessageDialog(this,
					Lang.couldntFindPrefs, Lang.warning,
					javax.swing.JOptionPane.WARNING_MESSAGE, base.getWarningIcon());
			j = null;
		}
		return j;
	}
	
	public synchronized int getMidHeight() {
		return preferences.getInt("midHeight");
	}

	public synchronized File getCollectionFile() {
		
		if( hasCollection() ) {
			try {
				File col = new File(preferences.getString("collectionPath"));
				if( col.exists() ) return col;
				else return null;
				
			} catch (Exception e) {
				return null;
			}
		} else return null;
	}

	public synchronized String getCollectionName() {
		
		if( !hasCollection() ) return null;
		
		String cString = preferences.getString("collectionPath");
		
		int idx1 = cString.lastIndexOf("/")+1;
		int idx2 = cString.lastIndexOf(".");
		
		return cString.substring(idx1, idx2);
		
		
	}
	
	public synchronized boolean hasCollection() {
		
		if( preferences != null ) {
			try {
				String f = preferences.getString("collectionPath");
				if( new File(f).exists()) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		} else return false;
	}
	
	public synchronized int getDisplayMeasuresMillis() {
		
		
		int m;
		try {			
			m = preferences.getInt("measureDisplayMillis");
		} catch (Exception e) {
			m = 800;
		}
		
		return m;
	}
	
	public synchronized int getShadowAmount() {
		
		int s;
		try {			
			s = preferences.getInt("shadowAmount");
		} catch (Exception e) {
			s = 10;
		}
		
		return s;
		
	}
	
	public synchronized int getShadowOfsetAmount() {
		
		int s;
		try {			
			s = preferences.getInt("shadowOfsetAmount");
		} catch (Exception e) {
			s = 5;
		}
		
		return s;
		
	}
	
	public synchronized int getScrollPaneSpeed() {
		
		int s;
		try {			
			s = preferences.getInt("scrollPaneSpeed");
		} catch (Exception e) {
			System.err.println("FM: no scrollPaneSpeed found in prefs");
			s = 16;
		}
		
		return s;
	}
	
	// MUSEUM:

	public JSONObject loadMuseumData() {

		if( !museumPath.exists() ) {
			javax.swing.JOptionPane.showMessageDialog(this, Lang.couldntFindMuseum, Lang.warning, javax.swing.JOptionPane.WARNING_MESSAGE, base.getWarningIcon());
			System.exit(1);
		}


		JSONObject j;
		try {
			j = loadJSONObject(museumPath);
		} catch (Exception e) {

			javax.swing.JOptionPane.showMessageDialog(this, Lang.couldntLoadMuseumData, "couldn't load...", javax.swing.JOptionPane.WARNING_MESSAGE, base.getWarningIcon());
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
	
	public synchronized String[] getRoomsInArchitecture() {
		
		String[] rooms;
		JSONObject arch = getArchitecture();
		
		JSONArray r = arch.getJSONArray("rooms");
		rooms = new String[r.size()];
		
		int i = 0;
		for (String rr : r.getStringArray()) {
			rooms[i] = rr;
			i++;
		}

		return rooms;
	}

	public synchronized JSONObject getRoomFromArchitecture( String _room) {

		if( museum != null ) {
			JSONObject o = museum.getJSONObject("architecture").getJSONObject(_room);
			return o;
		} else {
			return null;
		}
	}
	
	public synchronized String[] getRoomRealNamesInArchitecture() {
		
		String[] realNames;
		String[] rooms = getRoomsInArchitecture();
		realNames = new String[rooms.length];
		
		int i = 0;
		for (String r : rooms) {
			JSONObject jr = getRoomFromArchitecture(r);
			realNames[i] = jr.getString("roomRealName");
			i++;
		}
		
		return realNames;
	}

	public synchronized File getFilePathForRoom(String _room) {
		
//		String museumIdentifier = museumPath.getAbsolutePath().substring(museumPath.getAbsolutePath().lastIndexOf("/")+1,museumPath.getAbsolutePath().length()-4);
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
		
		try{
			saveJSONObject(project, projectPath.getAbsolutePath());
			setSaveDirty(false);
			deleteTempProject(null);
			return true;
		} catch( Exception e ) {
			return false;
		}
		
	}
	
	private void deleteTempProject(File _tmp) {
		
		File tmp;
		if (_tmp == null) {
			tmp = tempProjectPath;
		} else {
			tmp = _tmp;
		}
		
		if(tmp.exists()) {
			tmp.delete();
			System.out.println("FILE MANAGER: temp file deleted: "+tmp.getAbsolutePath());
		}
		else System.err.println("FILE MANAGER: temp file didnt exist!");
		
	}
	
	private void saveTempProject() {
		setSaveDirty(true);
		
		String tempProjFileName = projectPath.getAbsoluteFile().getName()+".tmp";
		
		tempProjectPath = new File(resourcesPath+"/tmp/"+tempProjFileName);

		JSONObject tmpJ = new JSONObject();
		tmpJ.setJSONObject("projectFile", project);
		tmpJ.setString("projectPath", projectPath.getAbsolutePath());
		
		saveJSONObject(tmpJ, tempProjectPath.getAbsolutePath());
		
	}

	public File checkForTemp(File _f) {
		
		File[] allFiles = tempProjectPath.listFiles();
		
		String loadingProjName = _f.getAbsoluteFile().getName();
		loadingProjName = loadingProjName.substring(0,  loadingProjName.indexOf('.'));
		
		String loadingProjPath = _f.getAbsolutePath();
		
		for (File file : allFiles){
			
			String fileString = file.getAbsoluteFile().getName();
			
			if (fileString.endsWith(".tmp")) {
				
				fileString = fileString.substring(0, fileString.indexOf('.'));
				String tmpProjectDenotedPath = loadJSONObject(file).getString("projectPath");
				
				if(
						fileString.equals(loadingProjName) &&
						file.lastModified() > _f.lastModified() &&
						tmpProjectDenotedPath.equals(loadingProjPath)
				   ) {
					
					return file;
				}
			}
		}
		
		return null;
	}
	
	public synchronized String[] getPreviousProject() {

		String[] pp = new String[2];

		pp[0] = preferences.getJSONObject("previousProject").getString("projectName");
		pp[1] = preferences.getJSONObject("previousProject").getString("projectPath");

		
		return pp;
	}

	public File newProject() {
		
		String projectName = null;
		String[] _selectedRooms = null;
		
		while (projectName == null || _selectedRooms.length == 0) {
			NewProjectDialog d = new NewProjectDialog(getRoomsInArchitecture(), getRoomRealNamesInArchitecture(), base.getIcon());
			
			int r = d.showDialog();
		
			System.err.println(r);
			
			if( r == 2 || r == -1 ) return null;
			
			projectName = d.getProjectName();
			_selectedRooms = d.getSelectedRooms();
			
			
		}
		
		// early exit:
		if( projectName == null || _selectedRooms.length == 0) return null;
				
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle(Lang.newProjectLocation_1+projectName+Lang.newProjectLocation_2);
		int i = fc.showOpenDialog(this);
		
		if( i == JFileChooser.APPROVE_OPTION) {

			File selected = fc.getSelectedFile();
			
			if( selected == null ) {
				
				selected = fc.getCurrentDirectory();
			}
			
			// early exit:
			if( selected == null ) return null;
			
			File projectFileSaveLoc = new File(selected+"/"+projectName);
			File artLibSaveLocation = new File(selected+"/"+projectName+"/"+projectName+"_lib");
			
//			projectPath = new File(projectFileSaveLoc.getAbsolutePath()+"/"+projectName+".sfp");
			
			System.out.println("FILE MANAGER: NEW: projectFileSaveLoc: "+projectFileSaveLoc.getAbsolutePath());
			System.out.println("FILE MANAGER: NEW: artLibSaveLocation: "+artLibSaveLocation.getAbsolutePath());
			


			JSONObject theNewProj = creator.makeNewProjectFile(projectName, _selectedRooms);
//			String[] importedAws;

			JPanel panel = new JPanel();
			BoxLayout l = new BoxLayout(panel, BoxLayout.Y_AXIS);
			panel.setLayout(l);
			
			ButtonGroup group = new ButtonGroup();
			JRadioButton  btn_awks = new JRadioButton(Lang.importArtworks);
			JRadioButton  btn_coll = new JRadioButton(Lang.importColl);
			JRadioButton  btn_both = new JRadioButton(Lang.importBoth);
			group.add(btn_awks);
			group.add(btn_coll);
			group.add(btn_both);
			btn_awks.setSelected(true);
			
//			JCheckBox importCollection = new JCheckBox(Lang.includeCollection);
			JLabel label1 = new JLabel(Lang.importNow_1);
			JLabel label2 = new JLabel(Lang.importNow_2);
			
			panel.add(label1);
			panel.add(Box.createRigidArea(new Dimension(0,3)));
			panel.add(label2);
			panel.add(Box.createRigidArea(new Dimension(0,13)));
//			panel.add(importCollection);
			panel.add(btn_awks);
			panel.add(Box.createRigidArea(new Dimension(0,3)));
			panel.add(btn_coll);
			panel.add(Box.createRigidArea(new Dimension(0,3)));
			panel.add(btn_both);
			panel.validate();
			
			
			int q = -1;
			
			while( q == -1 ) {
				q = javax.swing.JOptionPane.showOptionDialog(null, panel, Lang.importNowTitle, javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, base.getIcon(), Lang.importNowBtns, 2);
			}
			
			if( q == 1) {
				
//				JSONArray lib = new JSONArray();
				
				if( btn_awks.isSelected() || btn_both.isSelected() ) {
					
//					importedAws = base.in.batchImport(artLibSaveLocation, false, false);
					
					ImportThread th = new ImportThread("importThread");
					th.prepareBatchImport(base.in, artLibSaveLocation, false, false);
					th.setWaitForProject(base.wm);
					th.start();
					
					// wait for results
//					while( !th.hasResults() ) {
//						try {
//							Thread.sleep(80);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					importedAws = th.getImportedArtworks();
//					
//					for( String aw : importedAws) {
//						lib.append(aw);
//					}
				}
				
				if( btn_coll.isSelected() || btn_both.isSelected() ) {
					
//					importedAws = base.in.batchImport(artLibSaveLocation, true, false);

					ImportThread th = new ImportThread("importThread");
					th.prepareBatchImport(base.in, artLibSaveLocation, true, false);
					th.setWaitForProject(base.wm);
					th.start();
					
					// wait for results
//					while( !th.hasResults() ) {
//						try {
//							Thread.sleep(80);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					importedAws = th.getImportedArtworks();
//					
//					for( String aw : importedAws) {
//						lib.append(aw);
//					}					
				}
				
				// early exit
//				if( lib.size() == 0) return null;
				
//				theNewProj.setJSONArray("artLibrary", lib);
			}

			String pFileName = projectFileSaveLoc.getAbsolutePath()+"/"+projectName+".sfp";
			System.out.println("FILE MANAGER: THE PROJECT WILL BE SAVED AS: "+pFileName);
			
			
			saveJSONObject(theNewProj, pFileName);

			updatePrefs("fileChooserCurrentDirectory", projectFileSaveLoc.getAbsolutePath());

			return new File(pFileName);
			
		} else {
			return null;
		}


	}

	public synchronized void loadProject(File _f) {

		// if no file specified: Load Dialog
		
		if( _f == null ) {
			fc.setDialogTitle(Lang.loadProjectTitle);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int i = fc.showOpenDialog(this);
			if( i == JFileChooser.APPROVE_OPTION) {
				
				_f = fc.getSelectedFile();
				
				
			} else { loaded = false; return; }
		}
		
		
		// check for .tmp files that are named like the current proj to be loaded
		
		File tempF = checkForTemp(_f);
	
		if(tempF != null) {
			int i = javax.swing.JOptionPane.showOptionDialog(null, Lang.restoreProjectMessage, Lang.restoreProjectTitle, javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, base.warn, Lang.restoreProjectOptions, 0);
						
			switch (i) {
			case 0:
				loadFromTmp(tempF);
				return;
				
			case -1:
				return;
				
			case 1:
				deleteTempProject(tempF);
				break;
			}
			
		}
		
		

		loadRegular(_f);
	}
	
	public synchronized String getCurrentProjectName() {
		if( project != null) return project.getString("projectName");
		else return "no project loaded";
	}
	
	private synchronized void loadRegular(File _f) {
		
		if( _f.exists() ) {
			System.out.println("FILE MANAGER: loading this project:\n"+_f.getAbsolutePath());
			project = loadJSONObject(_f);
			loaded = true;
		}
		else {
			String msg = Lang.couldntLoadProject_1 + _f.getName() + Lang.couldntLoadProject_2 + _f.getPath();
			javax.swing.JOptionPane.showMessageDialog(this,msg, Lang.warning, javax.swing.JOptionPane.WARNING_MESSAGE, base.getWarningIcon());
			loaded = false;
		}
		
		
		if( loaded ) {
			JSONObject p = new JSONObject();
			p.setString("projectPath", _f.getAbsolutePath());
			p.setString("projectName", getCurrentProjectName());
			updatePrefs("previousProject", p.toString());
			
			projectPath = _f;
			System.out.println("FILE MANAGER: projectPath: "+projectPath.getAbsolutePath());
			currentProjectName = p.getString("projectName");
		}
	}
	
	private synchronized void loadFromTmp(File _f) {
		
		JSONObject tmpData = loadJSONObject(_f);
		
		project = tmpData.getJSONObject("projectFile");
		projectPath = new File(tmpData.getString("projectPath"));
		currentProjectName = getCurrentProjectName();
		tempProjectPath = _f;
		
		JSONObject p = new JSONObject();
		p.setString("projectPath", tmpData.getString("projectPath"));
		p.setString("projectName", getCurrentProjectName());
		updatePrefs("previousProject", p.toString());
		
		savedirty = true;
		loaded = true;
	}
	
	
//	
//	public synchronized long getCollectionTimestamp() {
//		if( hasCollection() ) {
//			try {
//				return project.getInt("collectionTimestamp");
//			} catch (Exception e) {
//				return -1;
//			}
//		} else return 0;
//	}
	
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
		
		if(project != null ) {
		
			return project.getJSONArray("artLibrary").getStringArray();
		}
		else return new String[0];
		
	}
	
	
	public synchronized File getArtLibraryPath() {
		String tmp = projectPath.getAbsolutePath();
		return new File(tmp.substring(0, tmp.lastIndexOf('.'))+"_lib");
	}
	
	
	// TODO synchronize??
	public String getProjectName() {
		return currentProjectName;
	}
	
	
	// TODO synchronize??
	public String getProjectFolderPath() {
		return projectPath.getParent();
	}
	
	
	// ARTWORK
	
	public synchronized JSONObject loadArtwork(String _name) {
		
		//   try to load Artwork.
		//   Try from project first.
		//   if it fails, try from collection.
		//   if all fails: display error and return null.
		
//		String filePath = projectPath.getAbsolutePath().substring(0, projectPath.getAbsolutePath().length()-4)+"_lib/"+_name+".sfa";
		File projectArtworkFile = getJSONFilePathForArtwork(_name);
		JSONObject aw;
		
		if( projectArtworkFile != null ) {
			
			aw = loadJSONObject(projectArtworkFile.getAbsolutePath());
			
		} else {
							
			javax.swing.JOptionPane.showMessageDialog(this, Lang.couldntLoadArtwork+_name, "couldn't load ...", javax.swing.JOptionPane.WARNING_MESSAGE, base.getWarningIcon());
			System.exit(1);
			aw=null;
			
		}
		return aw;
	}
	
//	public synchronized File getJSONFilePartForCollectionArtwork( String _name ) {
//		
//		String filePath = getCollectionPath() + "/" + getCollectionName() + "/imported/" + _name + ".sfa";
//		
//		File file = new File(filePath);
//		if( file.exists() ) return file;
//		else return null;	
//	}
	
	
	public synchronized File getJSONFilePathForArtwork(String _name) {
		
		if( projectPath != null ) {
			String filePath = projectPath.getAbsolutePath().substring(0, projectPath.getAbsolutePath().length()-4)+"_lib/"+_name+".sfa";
			File file = new File(filePath);
			if( file.exists() ) return file;
			else return null;
		}
		else return null;
	}
	
	
	public synchronized File getImageFilePathForArtwork(String _artwork, SMUtils.awFileSize _size) {
		
		
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
	

	public synchronized void requestImport() {
		
		File artLibraryPath = new File(getProjectFolderPath() + "/" +getProjectName()+"_lib");
		base.in.startImport(artLibraryPath);
	}
	
	
	public synchronized void deleteArtwork (SM_Artwork _aw) {
		
		// remove from internal array
				
		base.artworks.remove(_aw.getName());
				
		// remove from project-Library
		
		JSONArray lib = project.getJSONArray("artLibrary");

		int i = 0;
		for(String awString : lib.getStringArray()) {
			
			if( awString.equals(_aw.getName())) break;
			i++;
		}
		lib.remove(i);

		project.setJSONArray("artLibrary", lib);
		
		// remove Files
		
		File artworkFile	= new File(projectPath.getParentFile().getAbsolutePath()+"/"+currentProjectName+"_lib/"+_aw.getName()+".sfa");
		File artworkFolder	= new File(projectPath.getParentFile().getAbsolutePath()+"/"+currentProjectName+"_lib/"+_aw.getName());
		File thumb 			= new File(projectPath.getParentFile().getAbsolutePath()+"/"+currentProjectName+"_lib/"+_aw.getName()+"/"+_aw.getName()+"_thumb.png");
		File med			= new File(projectPath.getParentFile().getAbsolutePath()+"/"+currentProjectName+"_lib/"+_aw.getName()+"/"+_aw.getName()+"_med.png");
		File full			= new File(projectPath.getParentFile().getAbsolutePath()+"/"+currentProjectName+"_lib/"+_aw.getName()+"/"+_aw.getName()+"_full.png");
		
		artworkFile.delete();
		thumb.delete();
		med.delete();
		full.delete();
		artworkFolder.delete();
				
		
		saveProject();
	}
	
	
 	public synchronized void importedArtworksIntoProject(String[] importedAws) {
		
		
		if( importedAws == null || importedAws.length == 0 ) return;
		
		// create new Artwork-Objects and append to the current Artworks[]
		
		for (int i = 0; i < importedAws.length; i++) {
			
			base.artworks.put(importedAws[i], new SM_Artwork( loadArtwork(importedAws[i]), getImageFilePathForArtwork(importedAws[i], awFileSize.MEDIUM), base.frameGfxs ));
		}
		
		
		// Append the imported Artworks to the project-File-Library
		
		for (int i = 0; i < importedAws.length; i++) {
			JSONArray projLib = project.getJSONArray("artLibrary");
			projLib.append(importedAws[i]);
			project.setJSONArray("artLibrary", projLib);
		}
		setSaveDirty(true);
		saveProject();
		
		// Update the library
		
		unregisterUpdateListener(base.lib);
		base.lib.setVisible(false);
		base.lib.dispose();
		base.lib = base.wm.createLibrary(base.artworks, base.wm.getState());
		registerUpdateListener(base.lib);
		
	}
 	
	
	public synchronized void changeArtworkData(SM_Artwork aw, LinkedHashMap<String, Object> data) {
		
		
		String awName 		= (String)	data.get("Name");
		int[] awSize		= (int[])	data.get("size");
		int[] frameSize		= (int[])	data.get("frame");
		int[] pptSize		= (int[])	data.get("ppt");
		
		boolean light;
		if( data.get("light").toString().equalsIgnoreCase("true") ) {
			light = true;
		} else {
			light = false;
		}
		
		boolean shadow;
		if( data.get("shadow").toString().equalsIgnoreCase("true")) {
			shadow = true;
		} else {
			shadow = false;
		}
		
		// wallPosition Offset (if awSize changes)
		
		int[] sizeOffset = new int[2];
		
		sizeOffset[0] = (aw.getArtworkSize()[0] - awSize[0]) / 2;
		sizeOffset[1] = (aw.getArtworkSize()[1] - awSize[1]) / 2;
		
		int[] newPos = aw.getArtworkWallPos();
		newPos[0] += sizeOffset[0];
		newPos[1] -= sizeOffset[1];
		
		
		
		// changes in AW ( File + Object )
		
		File awPath = getJSONFilePathForArtwork(awName);
		JSONObject awFile = loadJSONObject(awPath);
		
		// change awSize (+PosInWall)
		
		JSONArray as = new JSONArray();
		as.append(awSize[0]);
		as.append(awSize[1]);
		
		awFile.setJSONArray("size", as);
		
		aw.setArtworkSize(awSize);
		aw.setArtworkWallPos(newPos);
		
		// change frameSize
		
		JSONArray fs = new JSONArray();
		if( frameSize.length > 0 ) {
			fs.append(frameSize[0]);
			fs.append(frameSize[1]);
			fs.append(frameSize[2]);
			fs.append(frameSize[3]);
			
			if( aw.getFrameStyle() == FrameStyle.NONE ) {
				aw.setFrameStyle(FrameStyle.STANDART);
			}
			if( awFile.getString("frameStyle").equals(FrameStyle.NONE.toString()) ) {
				awFile.setString("frameStyle", FrameStyle.STANDART.toString());
			}
		}
		awFile.setJSONArray("frameSize", fs);
		
		aw.setFrameMeasure(frameSize);
		
		// change Passepartout
		
		JSONArray ps = new JSONArray();
		if( pptSize.length > 0 ) {
			ps.append(pptSize[0]);
			ps.append(pptSize[1]);
			ps.append(pptSize[2]);
			ps.append(pptSize[3]);
		}
		awFile.setJSONArray("pasSize", ps);
		
		aw.setPassepartoutMeasure(pptSize);
		
		saveJSONObject(awFile, awPath.getAbsolutePath());

		
		// changes in Project File (+Object)
		
		String wall = aw.getWall();
		String room = aw.getRoom();
		
		JSONArray rooms = project.getJSONArray("rooms");
		
		for( int i = 0; i< rooms.size(); i++ ) {
			JSONObject r = rooms.getJSONObject(i);
			
			if( r.getString("roomName").equals(room)) {
				
				JSONArray aws = r.getJSONObject(wall).getJSONArray("artworks");
				
				for( int x = 0; x < aws.size(); x++ ) {
					
					JSONObject jsAW = aws.getJSONObject(x);
					
					if( jsAW.getString("invNr").equals(awName) ) {
						
						
						jsAW.setBoolean("light", light);
						jsAW.setBoolean("shadow", shadow);
						
						break;
						
					}
					
				}
			}
		}

		setSaveDirty(true);
		
		aw.setLight(light);
		aw.setShadow(shadow);
		
	}
	

	// UPDATE Event handling

	public synchronized void registerUpdateListener(UpdateListener _listener) {
		
		if(_listener.getClass() == (SM_WallArrangementView.class)) {
			
			updateListeners_ArrViews.add(UpdateListener.class, _listener);
		} else {
			updateListeners.add(UpdateListener.class, _listener);
		}

	}
	
	
	public synchronized void unregisterUpdateListener(UpdateListener _listener) {
		if(_listener.getClass() == (SM_WallArrangementView.class)) {

			updateListeners_ArrViews.remove(UpdateListener.class, _listener);
		} else {
			updateListeners.remove(UpdateListener.class, _listener);
		}
		//		updateListeners.remove(UpdateListener.class, _listener);
	}
	
	
	public synchronized void unregisterArrViewAWUpdateListeners() {
		
//		for( int i = updateListeners_ArrViews.getListenerCount(); i > 0; i--) {
//
//			updateListeners_ArrViews.remove(t, l)
//		}
		
		
		Object[] lstnrs = updateListeners_ArrViews.getListeners(UpdateListener.class);
		
		
		for( Object l : lstnrs ) {
			UpdateListener ls = (UpdateListener)l;
			updateListeners_ArrViews.remove(UpdateListener.class, ls);
		}
		

	}
	
	
	public synchronized void unregisterViewManagerUpdateListeners() {

		System.out.println("FILE MANAGER: Trying to remove "+updateListeners_ArrViews.getListenerCount()+" Listeners");
		
		Object[] lstnrs = updateListeners.getListeners(UpdateListener.class);
		
		System.out.println("FILE MANAGER: i have gotten "+lstnrs.length+" listeners as array");
		
		for( Object l : lstnrs ) {
			UpdateListener ls = (UpdateListener)l;
			
			if(ls.getClass().equals(SM_ViewManager.class)) {
				System.out.println("FILE MANAGER: removing view manager...");
				updateListeners.remove(UpdateListener.class, ls);
			}
		}
		
		System.out.println("FILE MANAGER: removed some, now its "+updateListeners.getListenerCount()+" Listeners");
	}
	
	
	@Override
	public synchronized boolean updateRequested(ArtworkUpdateRequestEvent e) {
		
		if( queueType != null &&  e.getType() != queueType ) return false;
		
		
		queueType = e.getType();
		
		if( e.isMultiple() ) {
			updatesQueued += e.getMultiple();
		} else {
			updatesQueued++;
		}
		
		LinkedHashMap<String, Object> data;
		String eventAW       = e.getName();
		SM_Artwork thisAw = base.getArtwork(this, (eventAW));
		
		
		switch (e.getType()) {
		case POS_IN_WALL:

			
			// Update Artwork
						
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
			data = new LinkedHashMap<String, Object>();
			
			data.put("artwork", eventAW);
			data.put("wall", notifyWall);
			
			dataQueue.add(data);
			
//			e2 = new UpdateEvent(this, eventAW, UpdateType.POS_IN_WALL, data);
			
			setSaveDirty(true);
			saveTempProject();
			
			break;
			
			
		case FRAME_STYLE:

			// Update Artwork
			
			thisAw.setFrameStyle(e.getNewFrameStyle());
						
			// Update Artwork-Json
			
			File artLibraryPath = new File(getProjectFolderPath() + "/" +getProjectName()+"_lib");
			File thisAwFile = new File(artLibraryPath.getAbsolutePath() + "/" +eventAW + ".sfa");
			
			JSONObject thisAWjson = loadJSONObject(thisAwFile);
			thisAWjson.setString("frameStyle", e.getNewFrameStyle().name() );
			saveJSONObject(thisAWjson, thisAwFile.getAbsolutePath());
			
			String wall = base.artworks.get(eventAW).getWall();
			data = new LinkedHashMap<String, Object>();
			
			data.put("artwork", eventAW);
			data.put("wall", wall);
			
			dataQueue.add(data);
			
//			e2 = new UpdateEvent(this, eventAW, UpdateType.FRAME_STYLE, data);
			
			break;

		case GENERAL_AW_DATA:
			
			changeArtworkData(thisAw, e.getAwData() );
			
			String w = base.artworks.get(eventAW).getWall();
			data = new LinkedHashMap<String, Object>();
			data.put("artwork", eventAW);
			data.put("wall", w);
			
			dataQueue.add(data);
			
//			e2 = new UpdateEvent(this, eventAW, UpdateType.GENERAL_AW_DATA, data);

			break;
			
		default:
			
//			e2 = new UpdateEvent(this, eventAW, UpdateType.BLANK, null);
			break;
		}
		
		
		// fire update? or wait for queue?
		
		updatesQueued--;
		
		if( updatesQueued == 0 ){
			
			FileManagerSheduledUpdateThread s = new FileManagerSheduledUpdateThread(queueType, dataQueue, updateListeners, updateListeners_ArrViews);
			
			// reset queue
			queueType = null;
			dataQueue = new ArrayList<LinkedHashMap<String, Object>>();

			// start the thread
			s.start();
			System.out.println("FILE MANAGER: update sheduled");
			
			
//			UpdateEvent e2;
//
//			if( dataQueue.size() == 0 ) {  // fire blank
//				e2 = new UpdateEvent(this, UpdateType.BLANK, null);
//			}
//
//			e2 = new UpdateEvent(this, queueType, dataQueue);
//
//
//			for(UpdateListener lsnr : updateListeners.getListeners(UpdateListener.class) ) {
//				lsnr.doUpdate(e2);
//
//			}
//			for(UpdateListener lsnr : updateListeners_ArrViews.getListeners(UpdateListener.class) ) {
//				lsnr.doUpdate(e2);
//
//			}
//
		}
		
		
		return true;
	}
	
	
	@Override
	public synchronized void updateRequested(WallUpdateRequestEvent e) {
		
		System.out.println("FILE MANAGER: update requested");

		System.out.println("   the artwork name: "+  e.getName());
		System.out.println("   the target room: "+e.getTargetRoom());
		
		String targetWall = "w_"+e.getTargetRoom()+"_"+e.getTargetWall();
		String originWall = "w_"+e.getOriginRoom()+"_"+e.getOriginWall();

		
		System.out.println("   the Target is: "+targetWall);
		System.out.println("   the Origin is: "+originWall);
		
		// update artwork
		// update room (Target)
		
		SM_Artwork thisAw = base.getArtwork(this, (e.getName()));
		
		
		if ( ! e.getTargetRoom().equalsIgnoreCase("Library")) {
			SM_Room thisRm = base.getRoom(this, e.getTargetRoom());
			if (!thisRm.hasArtworkOnWall(e.getName(), e.getTargetWall())) {
				thisAw.setWall(this, targetWall, thisRm.getRealName());
				thisRm.addArtworkToWall(this, thisAw, e.getTargetWall());
			}
		} else {
			thisAw.setWall(this, null, null);
		}
		
		
		// update room (origin)
		
		if( ! e.getOriginRoom().equalsIgnoreCase("Library")) {
			
			System.out.println("FILE MANAGER: Getting This Wall: "+e.getOriginWall() +" in this room: "+e.getOriginRoom());
			
			SM_Wall originSM_Wall = (SM_Wall)base.getRoom(this, e.getOriginRoom()).getWalls().get((e.getOriginWall()));
			
			originSM_Wall.artwork(artworkActionType.REMOVE, e.getName(), null);
			
//			originSM_Wall.artwork(SM_Wall.artworkActionType.REMOVE, e.getName(), null);
			
//			originSM_Wall.removeArtwork(e.getName());
			
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
		data.put("name", e.getName());
		data.put("wall_1", targetWall);
		data.put("wall_2", originWall);
		
		ArrayList<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String,Object>>(1);
		dataList.add(data);
		
		UpdateEvent e2 = new UpdateEvent(this, UpdateType.WALL, dataList);
		
		System.out.println("FILE MANAGER: fire update event" );
		for(UpdateListener lsnr : updateListeners.getListeners(UpdateListener.class) ) {
			lsnr.doUpdate(e2);
//			System.out.print("fire regular  " + lsnr.getClass());
//			
//			if( lsnr.getClass() == SM_Wall.class ) {
//				System.out.println( ((SM_Wall)lsnr).getWallName() );
//				
//			}
//			else System.out.println();
		}
		for(UpdateListener lsnr : updateListeners_ArrViews.getListeners(UpdateListener.class) ) {
			lsnr.doUpdate(e2);
//			System.out.println("FILE MANAGER: fire arrView  " + lsnr.getClass());
		}
	}

	
	@Override
	public synchronized void updateRequested(WallColorUpdateRequestEvent e) {
		
		System.out.println();
		System.out.println("FILE MANAGER: the fileManager has received an update request: WallColor");
		System.out.println("FILE MANAGER: from: " + e.getSource().getClass().getName());
		System.out.println("FILE MANAGER: for room: "+ e.getRoomName());
		if( e.isSingleWall() ) System.out.println("FILE MANAGER: it's for this wall only: " + e.getWallCharacter());
		else System.out.println("FILE MANAGER: for the entire room");
		Color temp = new Color(e.getColor());
		System.out.println("FILE MANAGER: color: "+e.getColor()+": ["+temp.getRed()+","+temp.getGreen()+","+temp.getBlue()+"]");
		if( e.isPreview() ) System.out.println("FILE MANAGER: it is a preview");
		else System.out.println("FILE MANAGER: it's for real!");
		if( e.isOriginalColorRequested()) System.out.println("FILE MANAGER: originalColor is Requested");
		
		/**
		 * 
		 *  Lieber Pip aus der Zukunft.
		 * 
		 *  Durch die unten stehende abfrage aus dem Event: e.isOriginalColorRequested() kann man sich
		 *  das ganze e.isPreview() sparen. Alles wird dadurch einfacher: hier, aber auch im Update-Type,
		 *  der sich ja durch alles m�gliche durchzieht!
		 * 
		 *  Man m�sste daf�r zun�chst im WallColorChooser die stellen durch den OriginalColorCallback ersetzen,
		 *  die bisher die alte ".isPreview" Methode benutzen... dann die ganze Kette von da aufrollen.
		 * 
		 * 	Das k�nnte man mal �ndern, wenn der singleWallColor Commit gemacht ist!
		 * 
		 * 	dein pip
		 * 
		 */
		
		
		if( e.isOriginalColorRequested()) {
			
			System.out.println("FILE MANAGER: isOriginalColorRequested");
			
			UpdateEvent o = new UpdateEvent(this, UpdateType.ORIGINAL_COLOR, null);
			
			for(UpdateListener lsnr : updateListeners.getListeners(UpdateListener.class) ) {
				lsnr.doUpdate(o);
			}
			for(UpdateListener lsnr : updateListeners_ArrViews.getListeners(UpdateListener.class) ) {
				lsnr.doUpdate(o);
			}
		}
		else

		
		if( !e.isSingleWall() ) {
			
			System.out.println("FILE MANAGER: isRoom");
			
			if(!e.isPreview()) {
				
				System.out.println("FILE MANAGER: is NO preview");
			
				// Set Room Color:
				
				SM_Room thisRoom = base.getRoom(this, e.getRoomName());
				thisRoom.setRoomcolor(e.getColor());
				
				// update JSON:
				
				for(int i=0; i<project.getJSONArray("rooms").size(); i++) {
					JSONObject r = project.getJSONArray("rooms").getJSONObject(i);
					if(r.getString("roomName").equalsIgnoreCase(e.getRoomName())) {
		
						r.setInt("roomColor", e.getColor());
					}
				}
				setSaveDirty(true);
				saveTempProject();
				
				LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("roomName", e.getRoomName());
				
				ArrayList<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String,Object>>(1);
				dataList.add(data);
				
				UpdateEvent e2 = new UpdateEvent(this, UpdateType.ROOM_COLOR, dataList);
				
				for(UpdateListener lsnr : updateListeners.getListeners(UpdateListener.class) ) {
					lsnr.doUpdate(e2);
				}
				for(UpdateListener lsnr : updateListeners_ArrViews.getListeners(UpdateListener.class) ) {
					lsnr.doUpdate(e2);
				}
			}
			
			else // send preview event:
			{
				System.out.println("FILE MANAGER: isPreview");
				
				LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("roomName", e.getRoomName());
				data.put("color", e.getColor());
				
				ArrayList<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String,Object>>(1);
				dataList.add(data);
				
				UpdateEvent e2 = new UpdateEvent(this, UpdateType.ROOM_COLOR_PREVIEW, dataList);
				
				for(UpdateListener lsnr : updateListeners.getListeners(UpdateListener.class) ) {
					lsnr.doUpdate(e2);
				}
				for(UpdateListener lsnr : updateListeners_ArrViews.getListeners(UpdateListener.class) ) {
					lsnr.doUpdate(e2);
				}
			}
		}
		
		if( e.isSingleWall() ) {
			
			System.out.println("FILE MANAGER: FM: isSingleWall");

			
			if( e.isDeleteWallColor() ) {
				
				System.out.println("FILE MANAGER: DELETE SAYS THE FILE MANAGER");
				
				// update Wall-Object
				
				base.getRoom(this, e.getRoomName()).getWalls().get(e.getWallCharacter()).removeColor();
				
				// update JSON:
				
				for(int i=0; i<project.getJSONArray("rooms").size(); i++) {
					JSONObject r = project.getJSONArray("rooms").getJSONObject(i);
					if(r.getString("roomName").equalsIgnoreCase(e.getRoomName())) {
						
						
						String wallName = "w_" + e.getRoomName() + "_" + e.getWallCharacter();
						
						JSONObject wall = r.getJSONObject( wallName );
						
//						wall.setInt("colorInt", e.getColor() );
						wall.setString("colorInt", "none");
						
						
					}
				}
				setSaveDirty(true);
				saveTempProject();
				
				UpdateEvent o = new UpdateEvent(this, UpdateType.ORIGINAL_COLOR, null);

				for(UpdateListener lsnr : updateListeners.getListeners(UpdateListener.class) ) {
					lsnr.doUpdate(o);
				}
				for(UpdateListener lsnr : updateListeners_ArrViews.getListeners(UpdateListener.class) ) {
					lsnr.doUpdate(o);
				}
				
			}
			
			else
			
			{
			
				if( ! e.isPreview() ) {
					
					System.out.println("FILE MANAGER: is NO preview");
					
					// update JSON
					
					for(int i=0; i<project.getJSONArray("rooms").size(); i++) {
						JSONObject r = project.getJSONArray("rooms").getJSONObject(i);
						if(r.getString("roomName").equalsIgnoreCase(e.getRoomName())) {
							
							
							String wallName = "w_" + e.getRoomName() + "_" + e.getWallCharacter();
							
							JSONObject wall = r.getJSONObject( wallName );
							
							wall.setInt("colorInt", e.getColor() );
							
							
						}
					}
					setSaveDirty(true);
					saveTempProject();
					
					// update Wall
					
					base.getRoom(this, e.getRoomName()).getWalls().get(e.getWallCharacter()).setColor(e.getColor());
					
					
					LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
					data.put("roomName", e.getRoomName());
					data.put("wallChar", e.getWallCharacter());
					data.put("color", e.getColor());
					
					ArrayList<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String,Object>>(1);
					dataList.add(data);
					
					UpdateEvent e2 = new UpdateEvent(this, UpdateType.WALL_COLOR, dataList);
					
					for(UpdateListener lsnr : updateListeners.getListeners(UpdateListener.class) ) {
						lsnr.doUpdate(e2);
					}
					for(UpdateListener lsnr : updateListeners_ArrViews.getListeners(UpdateListener.class) ) {
						lsnr.doUpdate(e2);
					}
				}
				
				else // send preview event only
					
				{
					
					System.out.println("FILE MANAGER: isPreview");

					
					LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
					data.put("roomName", e.getRoomName());
					data.put("wallChar", e.getWallCharacter());
					data.put("color", e.getColor());
					
					ArrayList<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String,Object>>(1);
					dataList.add(data);
					
					UpdateEvent e2 = new UpdateEvent(this, UpdateType.WALL_COLOR_PREVIEW, dataList);
					
					for(UpdateListener lsnr : updateListeners.getListeners(UpdateListener.class) ) {
						lsnr.doUpdate(e2);
					}
					for(UpdateListener lsnr : updateListeners_ArrViews.getListeners(UpdateListener.class) ) {
						lsnr.doUpdate(e2);
					}
				}
			}
		}
	}
	
	
	public void requestQuit() {
		
		System.err.println("QUIT REQUESTED");
		
		if (savedirty) {
			int choose = javax.swing.JOptionPane.showOptionDialog(null,
					Lang.wantToSaveBeforExit, Lang.wantToSaveBeforExitTitle,
					javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
					javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, getQuestionIcon(),
					Lang.saveOnExitOptions, 2);
			
			System.out.println("FILE MANAGER: returnvalue: "+choose);
			
			switch (choose) {
			case 1:
				deleteTempProject(null);
				System.exit(0);
				break;
			case 2:
				saveProject();
				System.exit(0);
				break;

			default:
				break;
			}
		}
		else {
			System.err.println("FILE MANAGER: exiting");
			System.exit(0);
		}
		System.err.println("FILE MANAGER: QUIT REQUEST SHOULD BE HANDELED! They however were not this time!");
	}


	



	
	
}





















