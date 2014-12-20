package smlfr;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import SMUtils.JsonCreator;
import SMUtils.Lang;

import processing.data.JSONObject;



public class SmlFr extends JFrame{

	// Modules
	public SmlFr 			base;
	public SM_FileManager 	fm;
	
	// Data
	public SM_Room[] 		rooms;
	public SM_Artwork[]		artworks;
	
	// utils
	private boolean 		fistStart = true;
	

	
	public static void main(String _args[]) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SmlFr app = new SmlFr();
				app.initialize();
			}
		});
	}

	
	private void initialize() {
		
		base = this;
		base.setVisible(true);
		
		fm = new SM_FileManager();
		
		
		// load a project
		String msg = Lang.initializeFromWhere_1+fm.getPreviousProject()[0]+Lang.initializeFromWhere_2;
		int i = javax.swing.JOptionPane.showOptionDialog(null, msg, Lang.initializeFromWhereTitle, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, Lang.initializeFromWhereButtons, 2);
		
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

		default:
			break;
		}
		
		// check if loading was successful
		if( !fm.isProjectLoaded() ) {
			base.initialize();
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



	}
	

	
	public void sayHi() {
		
	}



}
