package smlfr;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import SMUtils.Lang;
import SMUtils.SM_Frames;
import SMUtils.awFileSize;
import SMUtils.progState;

import processing.data.JSONArray;
import processing.data.JSONObject;
import smimport.SM_Import;



public class SmlFr extends JFrame {


	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4943542664716794448L;
	
	
	// Modules
	public SmlFr 						base;
	public SM_Import					in;
	public SM_FileManager 				fm;
	public SM_WindowManager				wm;
	public SM_Library					lib;
	
	// Data
	public HashMap<String, SM_Room>		rooms;
	public HashMap<String, SM_Artwork>	artworks;
	
	public SM_Frames					frameGfxs;
	
	// utils
	private boolean				 		firstStart = true;
	public  ImageIcon					icon = null;
	public  ImageIcon					warn = null;

	
	public static void main(String _args[]) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SmlFr app = new SmlFr();
				app.initialize();
			}
		});
	}

	
	private void initialize() {
		
		
		if( firstStart ) {
			
			warn = new ImageIcon("resources/sf_warning_Transp.png");
			
			
			fm = new SM_FileManager(this, warn);
			wm = new SM_WindowManager(fm, this);
			in = new SM_Import(fm, base);
			
			frameGfxs = new SM_Frames();
			fm.loadFrames(frameGfxs);
			// gui stuff here, keep in mind that you might have to step away from transparent windows and such...
			
			icon = new ImageIcon("resources/sf_icon_Transp.png");
			javax.swing.JLabel label = new javax.swing.JLabel(new ImageIcon("resources/sf_transparent_150x150.png") );
			base = this;
			base.add(label);

//			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			base.setSize(150,150);
			Rectangle realscreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			base.setSize(realscreen.width, realscreen.height);

			// maybe have a rounded logo that's always displayed?
			base.setUndecorated(true);
			base.setBackground(new Color(1f,1f,1f));
//			base.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
//			base.setFocusable(false);
//			base.setFocusableWindowState(false);
//			base.setEnabled(false);
			base.setVisible(true);
			firstStart = false;
		}
		
		if( !wm.isLoading() ) return;

		
		
		
		// DIALOG Message: load a project - 
		String msg = Lang.initializeFromWhere_1+fm.getPreviousProject()[0]+Lang.initializeFromWhere_2;
		int i = javax.swing.JOptionPane.showOptionDialog(null, msg, Lang.initializeFromWhereTitle, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, icon, Lang.initializeFromWhereButtons, 2);
		
		switch (i) {
		case 0:
			
			
			File newProj = fm.newProject();
			fm.loadProject(newProj);
			System.out.println("neues Projekt ausgew�hlt");
			
			
			
			break;
			
		case 1:
			System.out.println("projekt laden ausgew�hlt");
			fm.loadProject(null);
			break;
			
		case 2:
			System.out.println("letztes �ffnen ausgew�hlt");
			fm.loadProject(new File(fm.getPreviousProject()[1]));
			break;
			
		default:
			System.exit(0);
			break;
		}
		
		// check if loading was successful
		// go for another round if necessary
		if( !fm.isProjectLoaded() ) {
			base.initialize();
			return;
		}
		
		
		
		/// TEMP TEMP TEMP
		
//		in.batchImport(fm.getArtLibraryPath());

		
		
		
		
		
		
		
		
		// MUSEUM INIT  data for rooms related to current project
		
		// // init rooms in Project from architecture (without project data)
		
		String[] s = fm.getRoomNamesInProject() ;
		System.out.println("the rooms in this project:");
		for(String ss : s) {
			System.out.println(ss);
		}
		
		rooms = new HashMap<String, SM_Room>();
		
		for( int ii=0; ii<s.length; ii++) {
			
			JSONObject room = fm.getRoomFromArchitecture(s[ii]);
			rooms.put(s[ii], new SM_Room(base, s[ii], room, fm.getFilePathForRoom(s[ii])));
		}

		
		
		// PROJECT INIT
		
		// // init Artworks (general)
		
		String[] aws = fm.getArtLibraryFromProject();
		artworks = new HashMap<String, SM_Artwork>();
		for(int a=0;a<aws.length; a++) {
			
			artworks.put(aws[a], new SM_Artwork( fm.loadArtwork(aws[a]), fm.getFilePathForArtwork(aws[a], awFileSize.MEDIUM), frameGfxs ));	
		}
		
	
		// // init Artworks (in Rooms)
		
		JSONArray jRooms = fm.getRoomsInProject();
		// rooms
		for( int r = 0; r<jRooms.size(); r++ ) {
			
			JSONObject jRoom = jRooms.getJSONObject(r);
			SM_Room sfRoom = rooms.get(jRoom.getString("roomName"));
			
			
			// walls
			for( Object w : sfRoom.getWalls().keySet() ) {
				
				SM_Wall smWall = (SM_Wall)sfRoom.getWalls().get(w);
				String thisWallName = smWall.getWallName();
				
				JSONObject jWall = jRoom.getJSONObject(thisWallName);
				
				smWall.setArtworks( jWall.getJSONArray("artworks") );
				
				
				Integer colorInt = 255;//jWall.getString("colorInt");  // TODO Implement Color Integer!
				String colorBrillux = jWall.getString("colorBrillux");
				
				smWall.setColor( colorInt, colorBrillux );
				
			}
			
			
		}
		
		
		
		
		
		
		
		
	
		
		wm.requestStateChange(progState.PROJECT, null);
	}
	
	public SM_FileManager getFileManager() {
		return fm;
	}
	
	public SM_Room getRoom(SM_FileManager fm, String key) {
		return  rooms.get(key);
	}

	public SM_Artwork getArtwork(SM_FileManager fm, String key) {
		return artworks.get(key);
	}
	
	public void sayHi() {
		
	}

	public ImageIcon getIcon() {
		return icon;
	}
	
	public ImageIcon getWarningIcon() {
		return warn;
	}

	public SM_WindowManager getWindowManager() {
		if( wm != null) return wm;
		else return null;
	}

	
}
