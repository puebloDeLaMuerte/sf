package smlfr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
//import java.awt.dnd.DropTargetDragEvent;
//import java.awt.dnd.DropTargetDropEvent;
//import java.awt.dnd.DropTargetEvent;
//import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import SMUtils.Lang;
import SMUtils.SfFrame;
import SMUtils.artworkActionType;
import SMUtils.awFileSize;
import SMUtils.progState;
import SMupdateModel.ArtworkUpdateRequestEvent;
import SMupdateModel.ArtworkUpdateRequestListener;
import SMupdateModel.UpdateListener;
import SMupdateModel.WallColorUpdateRequestEvent;
import SMupdateModel.WallUpdateRequestEvent;
import processing.core.PApplet;

//import com.sun.tools.jdi.LinkedHashMap;

import processing.core.PImage;
import processing.core.PShape;
import processing.data.JSONArray;
import processing.data.JSONObject;



public class SM_Room {


	// From Museum-File
	private String								myRoomName;
	private String								myRealName;
	private SM_ViewAngle[] 						myViewAngles;
	private String								myDefaultViewAngle;
	private LinkedHashMap<Character, SM_Wall> 	myWalls;

	// upon init
	private SM_RoomProjectView					myProjectView;
	private SM_RoomArrangementView				myArrangementView;
	private SmlFr								base;
	private boolean								entered;
	private File								myFilePath;
	private int									myRoomColor;

	private ArtworkUpdateRequestListener		requestListener;


	public SM_Room(SmlFr _base, String _name, JSONObject _jRoom, File _filePath) {

		base = _base;
		myFilePath = _filePath;
		myRoomName = _name;
		myRealName = _jRoom.getString("roomRealName");
		myDefaultViewAngle = _jRoom.getString("defaultViewAngle");
		myRoomColor = 0;
		requestListener = base.getFileManager();

		/// Walls:
		
//		Iterator<?> it = _jRoom.keyIterator();
//		int count = 0;
//		while( it.hasNext() ) {
//			String str = (String)it.next();
//			if(str.startsWith("w_")) {
//				count++;
//			}
//		}
		
		
		myWalls = new LinkedHashMap<Character, SM_Wall>();
		
//		int count = 0;
		Iterator<?> it = _jRoom.keyIterator();
		
		while( it.hasNext() ) {
			String str = (String)it.next();
			if(str.startsWith("w_")) {
				char key = str.charAt(str.length()-1);
				myWalls.put(key, new SM_Wall(str, _jRoom.getJSONObject(str), this, base.fm));
//				count++;
			}
		}
		Map<Character, SM_Wall> tMap = new TreeMap<Character, SM_Wall>(myWalls);
		myWalls = new LinkedHashMap<Character, SM_Wall>(tMap);

		// Viewangles:
		
		JSONArray _viewAngles = _jRoom.getJSONArray("viewAngles");
		
		myViewAngles = new SM_ViewAngle[ _viewAngles.size()];
		for( int a = 0; a<myViewAngles.length; a++) {

			HashMap<String, Float[]> wallSkewInThisView = new HashMap<String, Float[]>();
			HashMap<String, Float[]> wallCropInThisView = new HashMap<String, Float[]>();
			
			String thisAngle = _viewAngles.getString(a);
			String thisShort = thisAngle.substring(thisAngle.lastIndexOf('_')+1, thisAngle.length());

			
			for(int x=0;x<thisShort.length();x++) {
				char thisWall = thisShort.charAt(x);

				String wallName = "w_"+myRoomName+"_"+thisWall;
				
				Float[] skews = new Float[10];
				Float[] crops = new Float[10];
				
				
				JSONArray relViews = _jRoom.getJSONObject(wallName).getJSONArray("relatedViews");
				for( int v=0; v<relViews.size(); v++) {
					if( relViews.getJSONObject(v).getString("viewName").equalsIgnoreCase(thisAngle) ){

						for(int f=0; f<10; f++) {
							skews[f] = relViews.getJSONObject(v).getJSONArray("viewSkew").getFloat(f);
						}
						for(int f=0; f<10; f++) {
							crops[f] = relViews.getJSONObject(v).getJSONArray("viewCrop").getFloat(f);
							if( crops[f] < 0 ) break;
						}
					}
				}
				
				wallSkewInThisView.put(wallName, skews);
				wallCropInThisView.put(wallName, crops);
				
			}

			myViewAngles[a] = new SM_ViewAngle(thisAngle, wallSkewInThisView, wallCropInThisView);

		}
	}

	public String sayHi() {
		return "Hi, this is room "+myRealName+" ("+myRoomName+") \nI have "+myViewAngles.length+" ViewAngles.\nI also have as many as "+myWalls.size()+" Walls.\n\n";
	}
	
public void XinitProjectView(Dimension _size, Point _loc, SmlFr base) {
		
		System.out.println("SM_ROOM: init ProjectView");

		myProjectView = new SM_RoomProjectView(_size.width, _size.height, base);
		
		File fl = base.fm.getFilePathForRoom(myRoomName);
		myProjectView.initFileAndRoom(fl, this);
		
		myProjectView.resize(_size.width, _size.height);
		myProjectView.setLocation(_loc.x, _loc.y);

		
		PApplet.runSketch(new String[] {PApplet.class.getName()}, myProjectView);
		

		
		while( !myProjectView.isSetupRun() ) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				System.err.println("wait on RoomProjectView: setup was interrupted " + e.getMessage());
			}
		}
		
//		myProjectView.frame.setTitle(myRealName);
//		myProjectView.frame.setVisible(true);

	}

	public void XinitArrangementView(Dimension _size, Point _loc, SmlFr base) {
		
		myArrangementView = new SM_RoomArrangementView(_size.width, _size.height, base);
		
		File fl = base.fm.getFilePathForRoom(myRoomName);
		myArrangementView.initFileAndRoomAndViewangles(fl, this, myViewAngles);
		
		myArrangementView.resize(_size.width, _size.height);
		
		PApplet.runSketch(new String[] {PApplet.class.getName()}, myArrangementView);
		
		myArrangementView.setWallsGfx();
		
		while( !myArrangementView.isSetupRun() ) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				System.err.println("wait on RoomProjectView: setup was interrupted " + e.getMessage());
			}
		}
		
		myArrangementView.setMenuExit();
		entered = true; // else no renderer will be loaded, aparently
	}

	public void initProjectView(Dimension _size, Point _loc, SmlFr base) {
		
		// the classic/OLD one...
		
//		JFrame f = new JFrame();
//		f.setLayout(new BorderLayout());
//		myProjectView = new SM_RoomProjectView();
//		f.add(myProjectView);
//		File fl = _fm.getFilePathForRoom(myRoomName);
//		myProjectView.init(f, new int[] { _size.width, _size.height }, fl, this);
//
////		f.setTitle(myRealName);
//		f.setUndecorated(true);
//		f.setVisible(true);
//		f.setSize(_size);
//		f.setLocation(_loc.width, _loc.height);
//		f.setResizable(true);
		
		SfFrame f = new SfFrame();
//		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.setLayout(new BorderLayout());
		f.setUndecorated(true);
		myProjectView = new SM_RoomProjectView(_size.width, _size.height, base);
//		f.setAlwaysOnTop(true);
		
		

//		Panel p = new Panel();
//		p.add(myProjectView);
//		f.add(p);
		
		f.add(myProjectView);
		File fl = base.fm.getFilePathForRoom(myRoomName);

		myProjectView.frame = f;
//		myProjectView.frame.setIgnoreRepaint(true);
		myProjectView.resize(_size.width, _size.height);
		myProjectView.setPreferredSize(_size);
		myProjectView.setMinimumSize(_size);
		myProjectView.frame.add(myProjectView);

		
//		System.err.println("Thread that calls init on ProjectView: " + Thread.currentThread().getName());
		
		myProjectView.init(fl, this);
		
		
		// we need to wair only if this code runs on any non-EDT Thread! If it's the EDT, don't wait
		// or else setup() in SfPApplet will never be reached!
		
//		while( !myProjectView.isSetupRun() ) {
//			try {
//				Thread.sleep(20);
//			} catch (InterruptedException e) {
//				System.err.println("wait on RoomProjectView: setup was interrupted " + e.getMessage());
//			}
//		}
		
		myProjectView.frame.pack();
		myProjectView.frame.setResizable(false);
		myProjectView.frame.setLocation(_loc.x, _loc.y);
		myProjectView.frame.setTitle(myRealName);
		myProjectView.frame.setVisible(true);
//		myProjectView.frame.setIgnoreRepaint(true);

		
////		f.setTitle(myRealName);
//		f.setUndecorated(true);
//		f.setVisible(true);
//		f.setSize(_size);
//		f.setLocation(_loc.width, _loc.height);
//		f.setResizable(true);
		
		
		
		//#########
//
//		JFrame f = new JFrame();
//		f.setLayout(new BorderLayout());
//		f.addWindowListener(this);
//		Dimension s = wm.getRaster();
////		s.width  *= 2;
////		s.height *=2;
//		SM_WallArrangementView wallArr = new SM_WallArrangementView((SM_Wall)view.myWalls.get(_wall), s, new Dimension(400, 10), this );
//
//
//		wallArr.frame = f;
//
//
//
//		wallArr.resize(wallArr.getSize());
//		wallArr.setPreferredSize(wallArr.getSize());
//		wallArr.setMinimumSize(wallArr.getSize());
//		wallArr.frame.add(wallArr);
//		wallArr.init();
//		wallArr.frame.pack();
//		wallArr.frame.setVisible(true);
//		wallArr.frame.setLocation(0, _of);
//		wallArr.frame.setTitle(Lang.wall+" "+wallArr.getWallName().substring(wallArr.getWallName().lastIndexOf('_')+1));
//
//

		
		
	}
	
//	public void initProjectView2(Dimension _size, Point _loc, SmlFr base) {
//		
//		starterThread thread = new starterThread();
//		thread.setName("RoomArr starter Thread");
//		thread.set(_size, _loc, base, this);
//		thread.start();
//	}
	
	public void initArrangementView(Dimension _size, Point _loc, SmlFr basw) {
		
		// the classic/OLD one...
		
//		JFrame f = new JFrame();
//		f.setLayout(new BorderLayout());
//		myArrangementView = new SM_RoomArrangementView();
//		f.add(myArrangementView);
//		File fl = _fm.getFilePathForRoom(myRoomName);
//
//		myArrangementView.init(f, new int[] { _size.width, _size.height }, fl, this, myViewAngles);
//		myArrangementView.setMenuExit();
////		f.setTitle(myRealName);
//		f.setUndecorated(true);
//		f.setVisible(true);
//		f.setSize(_size);
//		f.setLocation(_loc.width, _loc.height);
//		f.setResizable(true);
//		entered = true;
		
		SfFrame f = new SfFrame("fooframe");
		f.setLayout(new BorderLayout());
//		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.setUndecorated(true);
//		f.setAlwaysOnTop(true);
		myArrangementView = new SM_RoomArrangementView(_size.width, _size.height, base);
		
//		Panel p = new Panel();
//		p.add(myArrangementView);
//		f.add(p);
		
		f.add(myArrangementView);
		File fl = basw.fm.getFilePathForRoom(myRoomName);

		myArrangementView.frame = f;
		myArrangementView.resize(_size.width, _size.height);
		myArrangementView.setPreferredSize(_size);
		myArrangementView.setMinimumSize(_size);
		myArrangementView.frame.add(myArrangementView);
		myArrangementView.init(fl, this, myViewAngles);
		
		myArrangementView.setWallsGfx();
		
		// only wait for setup() in ArrViews if this code is NOT executed on the EDT - 
		// because if it is on the EDT, the setup() will never be called if we wait here
		
//		while( !myArrangementView.isSetupRun() ) {
//			try {
//				Thread.sleep(20);
//			} catch (InterruptedException e) {
//				System.err.println("wait on RoomArrangementView: setup was interrupted " + e.getMessage());
//			}
//		}
		
		myArrangementView.initSfpMenu();
		
		myArrangementView.setMenuExit();
		myArrangementView.frame.pack();
//		myArrangementView.frame.setIgnoreRepaint(true);
		myArrangementView.frame.setVisible(true);
		myArrangementView.frame.setResizable(false);
		myArrangementView.frame.setLocation(_loc.x, _loc.y);
		myArrangementView.frame.setTitle(myRealName);
//		myArrangementView.frame.setIgnoreRepaint(true);
		entered = true;
	}
	
	public LinkedHashMap<Character, SM_Wall> getWalls() {
		return myWalls;
	}

	public String[] getWallNames() {
		
		String[] s = new String[myWalls.size()];
		
		int i=0;
		for( Object c : myWalls.keySet() ) {
			SM_Wall w = (SM_Wall)myWalls.get(c);
			s[i] = w.getWallName();
		}
		
		return s;
	}

	public boolean hasArtworkOnWall(String _name, char _wallChar) {
		
		for( Object c : myWalls.keySet() ) {
			SM_Wall w = (SM_Wall)myWalls.get(c);
			
			if( (Integer)w.artwork(artworkActionType.HAS, _name, null) == 1 && w.getWallChar() == _wallChar ) return true;
			
		}
		return false;
	}
	
	public void addArtworkToWall(SM_FileManager fm, SM_Artwork _aw, char _wall) {
		SM_Wall w = (SM_Wall)myWalls.get((char)_wall);
		w.artwork(artworkActionType.ADD, _aw.getName(), _aw);
	}
	
	public void setRoomcolor( int _c) {
		myRoomColor = _c;
	}
	
	public int getRoomColor() {
		if (myRoomColor == 0) {
			return -1;
		}
		else return myRoomColor;
	}
	
	public String getName() {
		return myRoomName;
	}

	public String getRealName() {
		return myRealName;
	}
	
	public SM_Artwork getArtworkFromBase(String _invNr) {
		return base.artworks.get(_invNr);
	}

	public void fireUpdateRequest(WallUpdateRequestEvent e) {
		requestListener.updateRequested(e);
	}
	
	public void fireUpdateRequest(ArtworkUpdateRequestEvent e) {
		requestListener.updateRequested(e);
	}

	public void fireUpdateRequest(WallColorUpdateRequestEvent e) {
		requestListener.updateRequested(e);
	}
	
	public void requestRoomEnter() {
		base.wm.requestStateChange(progState.ROOM, myRoomName);
	}

	public void requestRoomExit() {

		base.wm.requestStateChange(progState.PROJECT, null);
	}
	
	public void endView() {

		if( myProjectView != null ) {

			myProjectView.dispose();
			myProjectView = null;
		} else if( myArrangementView != null) {

			myArrangementView.disposeVM(this);
			myArrangementView.dispose();
			myArrangementView = null;
			entered = false;
		}
	}

	public boolean isEntered() {
		return entered;
	}
	
	public String getDefaultView() {
		return myDefaultViewAngle;
	}

	public File getFilePath() {
		return myFilePath;
	}

	public PImage getShadowImage() {
		return base.fm.getShadowImage();
	}
	
	public File getThumbPath(String _artworkName) {
		return base.fm.getImageFilePathForArtwork(_artworkName, awFileSize.THUMB);
	}
	
	public SM_WindowManager getWindowManager() {
		return base.getWindowManager();
	}

	public void registerUpdateListener(UpdateListener _l){
		base.fm.registerUpdateListener(_l);
	}

	public void unregisterUpdateListener(UpdateListener _l) {
		base.fm.unregisterUpdateListener(_l);
	}

//	public void unregisterArtworkUpdateListeners() {
//		System.out.println("ROOM: unregistering listeners...");
//		if( myProjectView != null ) {
//
//
//
//		} else if( myArrangementView != null) {
//
//		}
//	}
	
	public void requestQuit() {
		base.fm.requestQuit();
	}
	
	public boolean getSaveDirty() {
		return base.fm.isSaveDirty();
	}
	
	public boolean requestSave() {
		return base.fm.requestSave();
	}

	public void exportMeasures(Character[] _forWalls) {
		
		
		SM_ExportWall[] exwls= new SM_ExportWall[_forWalls.length];
		
		// init export Walls and Artworks
		
		for( int i=0; i<_forWalls.length; i++) {
			
			
			SM_Wall wl 					= myWalls.get(_forWalls[i]);
			
			SM_Artwork[] aws 			= (SM_Artwork[]) wl.artwork(artworkActionType.GET_ARRAY, null,null);
			SM_ExportArtwork[] exaws	= new SM_ExportArtwork[aws.length];
			
			for( int ii =0; ii< aws.length; ii++) {
				exaws[ii] = new SM_ExportArtwork(aws[ii].getName(), aws[ii].getTotalWallPos(), aws[ii].getTotalWidth(), aws[ii].getTotalHeight());
			}
			
			exwls[i] = new SM_ExportWall(""+wl.getWallChar(), exaws, wl.getWidth(), wl.getHeight());
			
		}
		
		PShape grundriss = null;
		
		if( myProjectView != null ) {
			grundriss = myProjectView.getGreyWalls();
		}
		else if(myArrangementView != null ) {
			grundriss = myArrangementView.getGreyWalls();
		}
		
		String saveLoc = getExportPath().getAbsolutePath()+"/"+myRealName+".pdf";
		
		System.out.println("ROOM: The export will be saved to: "+saveLoc);
		
		SM_Exporter export = new SM_Exporter(exwls, saveLoc, myRealName, base.fm.getProjectName(), grundriss);
		
		export.init();
		
		while( !export.isExportDone ) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {

			}
		}
		export.stop();
		export.dispose();
		export = null;
		
		javax.swing.JOptionPane.showMessageDialog(null, Lang.exportSuccess_1 + ".../"+base.fm.getProjectName()+"/export/"+myRealName+".pdf", Lang.exportSuccess_title,  JOptionPane.INFORMATION_MESSAGE, base.getIcon());
	}

	public File getExportPath() {
		File exportLoc = new File(base.fm.getProjectFolderPath()+"/export");
		return exportLoc;
	}
	
	
	class starterThread extends Thread{
		
		private Dimension size;
		private Point point;
		private SmlFr base;
		private SM_Room room;
		
		public void set(Dimension _size, Point _loc, SmlFr base, SM_Room _room) {
			this.size = _size;
			this.point = _loc;
			this.base = base;
			this.room = _room;
		}
		
		@Override
		public void run() {
			
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			f.setLayout(new BorderLayout());
			f.setUndecorated(true);
			myProjectView = new SM_RoomProjectView(size.width, size.height, base);
//			f.setAlwaysOnTop(true);
			f.add(myProjectView);
			File fl = base.fm.getFilePathForRoom(myRoomName);

			myProjectView.frame = f;
			myProjectView.resize(size.width, size.height);
			myProjectView.setPreferredSize(size);
			myProjectView.setMinimumSize(size);
			myProjectView.frame.add(myProjectView);
			myProjectView.init(fl, room);
			
			while( !myProjectView.isSetupRun() ) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					System.err.println("wait on RoomProjectView: setup was interrupted " + e.getMessage());
				}
			}
			
			myProjectView.frame.pack();
			myProjectView.frame.setResizable(false);
			myProjectView.frame.setLocation(point.x, point.y);
			myProjectView.frame.setTitle(myRealName);
			myProjectView.frame.setVisible(true);
			
		}
		
	}
	
}








