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

import processing.data.JSONObject;



public class SmlFr extends JFrame{

	// Modules
	public SmlFr 						base;
	public SM_FileManager 				fm;
	public SM_WindowManager				wm;
	public SM_Library					lib;
	
	// Data
	public SM_Room[] 					rooms;
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
			fm = new SM_FileManager(warn);
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
			
			System.out.println("neues Projekt ausgewählt");
			break;
			
		case 1:
			System.out.println("projekt laden ausgewählt");
			fm.loadProject(null);
			break;
			
		case 2:
			System.out.println("letztes öffnen ausgewählt");
			fm.loadProject(new File(fm.getPreviousProject()[1]));
			break;
			
		default:
			System.exit(0);
			break;
		}
		
		// check if loading was successful
		if( !fm.isProjectLoaded() ) {
			base.initialize();
			return;
		}
		
		
		
		// init museum data for current project
		
		// // init rooms in Project from architecture (without project data)
		
		String[] s = fm.getRoomNamesInProject() ;
		System.out.println("the rooms in this project:");
		for(String ss : s) {
			System.out.println(ss);
		}
		
		rooms = new SM_Room[s.length];
		for( int ii=0; ii<s.length; ii++) {
			
			JSONObject room = fm.getRoomFromArchitecture(s[ii]);
			rooms[ii] = new SM_Room(base, s[ii], room);
		}

		// // init Artworks
		
		String[] aws = fm.getArtLibraryFromProject();
		artworks = new HashMap<String, SM_Artwork>();
		for(int a=0;a<aws.length; a++) {
			
			artworks.put(aws[a], new SM_Artwork( fm.loadArtwork(aws[a]) ));	
		}
		

		
//		System.err.println("ARTWORK!!!!!!!\n"+fm.loadArtwork("fidibus"));
		
		String[] keys = artworks.keySet().toArray(new String[0]);
		for( int z = 0; z<artworks.size(); z++ ) {
			artworks.get(keys[z]).sayHi();
		}
		
//		lib = new SM_Library();
//		lib.setVisible(true);
		
//		wm.testFrames();
		lib = wm.createLibrary(artworks);


		
	}
	

	
	public void sayHi() {
		
	}



}
