package smlfr;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import SMUtils.JsonCreator;
import SMUtils.Lang;

import processing.data.JSONObject;



public class SmlFr {

	// Modules
	public SmlFr 			base;
	public SM_FileManager 	fm;
	public SM_Room[] 		rooms;
	
	// Data
	/*  /
	 */
	/*
	 *
	 */
	

	
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
			break;
			
		case 2:
			System.out.println("letztes öffnen ausgewählt");

		default:
			break;
		}
		
		// init museum data
		
		
		// fm.newProject("some", new String[] { "S1" } );



		
		

	}
	
	public void sayHi() {
		
	}


	public String[] getViewAnglesAsStrings() {
		// TODO Auto-generated method stub
		return null;
	}
}
