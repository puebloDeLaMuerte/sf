package smlfr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.media.nativewindow.util.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.sun.codemodel.internal.JLabel;

import SMUtils.JsonCreator;
import SMUtils.Lang;
import SMUtils.progState;

import processing.data.JSONArray;
import processing.data.JSONObject;



public class SmlFr extends JFrame{

	// Modules
	public SmlFr 						base;
	public SM_FileManager 				fm;
	public SM_WindowManager				wm;
	public SM_Library					lib;
	
	// Data
	public HashMap<String, SM_Room>		rooms;
	public HashMap<String, SM_Artwork>	artworks;
	
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

			// gui stuff here, keep in mind that you might have to step away from transparent windows and such...
			
			icon = new ImageIcon("resources/sf_icon_Transp.png");
			javax.swing.JLabel label = new javax.swing.JLabel(new ImageIcon("resources/sf_transparent_150x150.png") );
			base = this;
			base.add(label);

//			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			base.setSize(150,150);
			// maybe have a rounded logo that's always displayed?
			base.setUndecorated(true);
			base.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
			base.setVisible(true);
			firstStart = false;
		}
		
		if( !wm.isLoading() ) return;

		
		
		
		// load a project
		String msg = Lang.initializeFromWhere_1+fm.getPreviousProject()[0]+Lang.initializeFromWhere_2;
		int i = javax.swing.JOptionPane.showOptionDialog(null, msg, Lang.initializeFromWhereTitle, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, icon, Lang.initializeFromWhereButtons, 2);
		
		switch (i) {
		case 0:
			
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
			rooms.put(s[ii], new SM_Room(base, s[ii], room));
		}

		
		
		// PROJECT INIT
		
		// // init Artworks (general)
		
		String[] aws = fm.getArtLibraryFromProject();
		artworks = new HashMap<String, SM_Artwork>();
		for(int a=0;a<aws.length; a++) {
			
			artworks.put(aws[a], new SM_Artwork( fm.loadArtwork(aws[a]) ));	
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



}
