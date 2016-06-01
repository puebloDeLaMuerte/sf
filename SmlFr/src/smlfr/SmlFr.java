package smlfr;

import java.awt.*;
//import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
//import java.util.HashMap;
import java.util.HashMap;
//import java.util.HashMap;

import javax.swing.*;

//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import SMUtils.Lang;
import SMUtils.SM_Frames;
import SMUtils.SysInfo;
import SMUtils.awFileSize;
import SMUtils.progState;

import processing.data.JSONArray;
import processing.data.JSONObject;
import smimport.SM_Import;



public class SmlFr extends JFrame implements WindowFocusListener {


	
	

	private static final long serialVersionUID = -4943542664716794448L;
	public static final String versionString = "1.0.6";
	
	
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
	public  ImageIcon					question = null;

	
	public static void main(String _args[]) {

		
		try {
			File logLock = new File("resources/logs/no.log");

			if( !logLock.exists() ) {
				
				System.out.println("SimuFoehr launched");
				System.out.println("no log-lock has been detected, so the output goes to a file!");
				
				
				
				File logs = new File("resources/logs");
				if( !logs.exists() ) logs.mkdir();

				SimpleDateFormat dateForm = new SimpleDateFormat("yyyyMMdd-HHmmss");//dd/MM/yyyy

				String timestamp =  dateForm.format(new java.util.Date());

				PrintStream stream = new PrintStream(new BufferedOutputStream(new FileOutputStream(logs.getAbsolutePath()+"/sfLog"+timestamp+".txt")), true);

				System.setOut(stream);
				System.setErr(stream);


				System.out.println("stream: SimuFoehr launched");
				System.err.println("errstr: SimuFoehr launched");
				System.out.println("The version-String is: "+versionString);
				System.out.println();

//				SysInfo.displayMessage();
				SysInfo.printSysStats();
				SysInfo.printHeapStats();

			} else {
				System.out.println("SimuFoehr launched");
				System.out.println("The version-String is: "+versionString);
				System.out.println("a log-lock has been detected. Output goes to console...\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(_args.length >0) {
			System.out.println("main args:");
		
			for( String a : _args) {
				System.out.println(" - "+a);
			}
		} else {
			System.out.println("no main args");
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SmlFr app = new SmlFr();
				app.initialize();

				
			}
		});
		
	}

	
	private void initialize() {

		this.addWindowFocusListener(this);
		
		if( firstStart ) {

			
			icon = new ImageIcon("resources/icons/sf_icon_Transp.png");
			warn = new ImageIcon("resources/icons/sf_warning_Transp.png");
			question = new ImageIcon("resources/icons/sf_question_Transp.png");
			
			fm = new SM_FileManager(this, warn);
			wm = new SM_WindowManager(fm, this);
			in = new SM_Import(fm, this);
			
			frameGfxs = new SM_Frames();
			fm.loadFrames(frameGfxs);
			// gui stuff here, keep in mind that you might have to step away from transparent windows and such...
			
			JLabel label = new JLabel(new ImageIcon("resources/icons/sf_transparent_150x150.png") );
			base = this;
			
			base.add(label);
			//UPDATE// base.add(label);

//			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			base.setSize(150,150);
			
			Rectangle realscreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			
//			Toolkit tk = Toolkit.getDefaultToolkit();
			
//			Dimension realscreen = tk.getScreenSize();
			base.setSize(realscreen.width, realscreen.height);
			

			base.setUndecorated(true);
			base.setBackground(new Color(1f,1f,1f));
//			base.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
//			base.setFocusable(false);
//			base.setFocusableWindowState(false);
//			base.setEnabled(false);
			base.setVisible(true);
			firstStart = false;
			
			// The SHUTDOWN HOOK
			
//			final Thread mainThread = Thread.currentThread();
//				Runtime.getRuntime().addShutdownHook(new Thread() {
//					public void run() {
//
//						System.err.println("SHUTDOWNHOOK WAS CALLED!");
//						fm.requestQuit();
//
//						try {
//
//							mainThread.join();
//						} catch (InterruptedException e) {
//							System.err.println("THE MUCH DREADED ERROR HAS OCCURED");
//							e.printStackTrace();
//						}
//					}
//				});
		}
		
		if( !wm.isLoading() ) return;

		
		

		
		
		// DIALOG Message: load a project -
		
		String[] prev = fm.getPreviousProject();
		String msg = Lang.initializeFromWhere_1 + prev[0] + Lang.initializeFromWhere_2;
		
		
		int i = javax.swing.JOptionPane.showOptionDialog(null, msg, Lang.initializeFromWhereTitle, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, icon, Lang.initializeFromWhereButtons, 2);
		
		switch (i) {
		case 0:
						
			File newProj = fm.newProject();
			if(newProj != null ) fm.loadProject(newProj);
			break;
			
		case 1:
			fm.loadProject(null);
			break;
			
		case 2:
			File prevProj = new File(fm.getPreviousProject()[1]);
			
			if( prevProj.exists() ) {
				fm.loadProject( prevProj );
			} else {
				fm.loadProject(null);
			}
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
		System.out.print("the rooms in this project: ");
		for(String ss : s) {
			System.out.print(ss+", ");
		}
		System.out.println();
		
		rooms = new HashMap<String, SM_Room>();
		
		for( int ii=0; ii<s.length; ii++) {
			
			JSONObject room = fm.getRoomFromArchitecture(s[ii]);
			rooms.put(s[ii], new SM_Room(base, s[ii], room, fm.getFilePathForRoom(s[ii])));
			System.out.println("SMLFR: created: "+rooms.get(s[ii]).getName());
		}

		
		
		// PROJECT INIT
		
		// // init (general) Artworks
		
		System.out.println("SMLFR: creating artworks...");
		
		String[] aws = fm.getArtLibraryFromProject();
		artworks = new HashMap<String, SM_Artwork>();
		for(int a=0;a<aws.length; a++) {

			artworks.put(aws[a], new SM_Artwork( fm.loadArtwork(aws[a]), fm.getImageFilePathForArtwork(aws[a], awFileSize.MEDIUM), frameGfxs ));
//			System.out.println("SMLFR: created: " + artworks.get(aws[a]).getName());
		}
		
	
		// // init (in Rooms) Artworks
		
		System.out.println("SMLFR: putting Artworks in Rooms...");
		
		JSONArray jRooms = fm.getRoomsInProject();
		// rooms
		for( int r = 0; r<jRooms.size(); r++ ) {
			
			JSONObject jRoom = jRooms.getJSONObject(r);
			SM_Room sfRoom = rooms.get(jRoom.getString("roomName"));
			
			System.out.print(rooms.get(jRoom.getString("roomName"))+" ");
			
			// walls
			for( Object w : sfRoom.getWalls().keySet() ) {
				
				SM_Wall smWall = (SM_Wall)sfRoom.getWalls().get(w);
				String thisWallName = smWall.getWallName();
				
				JSONObject jWall = jRoom.getJSONObject(thisWallName);
				
				smWall.setArtworks( jWall.getJSONArray("artworks") );
				
			}
			
			
		}
		System.out.println();
		
		// // init WallColor for Rooms and Walls if present
		
		System.out.println("SMLFR: init wallColor...");
		
		jRooms = fm.getRoomsInProject();
		
		// rooms
		for( int r = 0; r<jRooms.size(); r++ ) {
			
			// get Room - set Room Color if in file:
			
			JSONObject jRoom = jRooms.getJSONObject(r);
			SM_Room sfRoom = rooms.get(jRoom.getString("roomName"));
			try {
				int c = jRoom.getInt("roomColor");
				sfRoom.setRoomcolor(c);
			} catch (Exception e) {
				try {
					String c = jRoom.getString("roomColor");
					
					if( c.equalsIgnoreCase("white") ) JOptionPane.showMessageDialog(null, Lang.prevVersion_WallColor, Lang.warning, JOptionPane.ERROR_MESSAGE);
				} catch (Exception e2) {

					JOptionPane.showMessageDialog(null, Lang.errorLoadingWallColor_1 + sfRoom.getRealName() + Lang.errorLoadingWallColor_2, Lang.warning, JOptionPane.ERROR_MESSAGE);
					
					jRoom.setInt("roomColor", new Color(1f, 1f, 1f).getRGB());
					fm.setSaveDirty(true);
					
				}
//				sfRoom.setRoomcolor(sfRoom.getRoomColor());
				sfRoom.setRoomcolor(new Color(1f, 1f, 1f).getRGB());
			}

			// get Walls - set Wall Color if in file:
			
			for( Object w : sfRoom.getWalls().keySet() ) {
				SM_Wall smWall = (SM_Wall)sfRoom.getWalls().get(w);
				String thisWallName = smWall.getWallName();
				JSONObject jWall = jRoom.getJSONObject(thisWallName);
				
				try {
					int c = jWall.getInt("colorInt");
					smWall.setColor(c);
				} catch (Exception e) {

					//smWall.setColor(smWall.getColor());
				}
			}

		}
		
		
		
		
		System.out.println("SMLFR: requesting state change now: PROJECT");
		
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
	

	public ImageIcon getIcon() {
		return icon;
	}
	
	public ImageIcon getWarningIcon() {
		return warn;
	}
	
	public ImageIcon getQuestionIcon() {
		return question;
	}

	public SM_WindowManager getWindowManager() {
		if( wm != null) return wm;
		else return null;
	}


	@Override
	public void windowGainedFocus(WindowEvent e) {
//		System.out.println("SMLFR: happy Focus");
//		this.toBack();
	}


	@Override
	public void windowLostFocus(WindowEvent e) {

	}


	
	




	
}
