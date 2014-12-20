package smlfr;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import processing.core.PApplet;
import processing.data.*;

import SMUtils.JsonCreator;
import SMUtils.Lang;
import SMUtils.Rooms;

public class SM_FileManager extends PApplet {
	
	private JsonCreator 	creator;
	private JFileChooser	fc;

	private File 			preferencesPath;
	private File 			museumPath;
	
	private JSONObject		preferences;
	private JSONObject 		museum;
	private JSONObject		project;


	public SM_FileManager() {
		
		creator = new JsonCreator(this);
		fc = new JFileChooser();
		
		
		File res = new File("resources");
		if( !res.exists() ) {
			javax.swing.JOptionPane.showMessageDialog(this,Lang.couldntFindResourceFolder, "couldn't find...", javax.swing.JOptionPane.WARNING_MESSAGE);
			System.exit(1);
		}
		
		
		// init file paths and load files:
		preferencesPath = new File("resources/prefs.txt");		
		preferences = loadPrefs();
		museumPath = new File("resources/"+preferences.getString("museumData"));
		museum = loadMuseumData();
				
		
		System.out.println("\nTHE MUSEUM FROM FILE:\n\n"+museum.toString());
		System.out.println("\nTHE PREFERENCES FROM FILE:\n\n"+preferences.toString());
	}

	
	

	
	// PREFERENCES
	
	private synchronized void updatePrefs(String _key, String _value) {
		
		if( _key.equalsIgnoreCase("previousProject") ) {
			try {

				JSONObject o = JSONObject.parse(_value);
				preferences.setJSONObject("_key", o);
				
			} catch(Exception e) {}
		}
		else {
			preferences.setString(_key, _value);	
		}
		
		// TODO load the new project as project for this programm
		
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
	
	// PROJECT

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
		if( _f.exists() ) {
			project = loadJSONObject(_f);
		}
		else {
			String msg = Lang.couldntLoadProject_1 + _f.getName() + Lang.couldntLoadProject_2 + _f.getPath();
			javax.swing.JOptionPane.showMessageDialog(this,msg, "couldn't find...", javax.swing.JOptionPane.WARNING_MESSAGE);

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
}





















