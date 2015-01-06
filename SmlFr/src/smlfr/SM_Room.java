package smlfr;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import SMUtils.progState;
import artworkUpdateModel.ArtworkUpdateListener;
import artworkUpdateModel.ArtworkUpdateRequestEvent;
import artworkUpdateModel.WallUpdateRequestEvent;
import artworkUpdateModel.ArtworkUpdateRequestListener;

//import com.sun.tools.jdi.LinkedHashMap;

import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;



public class SM_Room {


	// From Museum-File
	private String							myRoomName;
	private String							myRealName;
	private SM_ViewAngle[] 					myViewAngles;
	private String							myDefaultViewAngle;
	private LinkedHashMap<Character, SM_Wall> 					myWalls;

	// upon init
	private SM_RoomProjectView				myProjectView;
	private SM_RoomArrangementView			myArrangementView;
	private SmlFr							base;
	private boolean							entered;
	private File							myFilePath;

	private ArtworkUpdateRequestListener	requestListener;


	public SM_Room(SmlFr _base, String _name, JSONObject _jRoom, File _filePath) {

		base = _base;
		myFilePath = _filePath;
		myRoomName = _name;
		myRealName = _jRoom.getString("roomRealName");
		myDefaultViewAngle = _jRoom.getString("defaultViewAngle");

		requestListener = base.getFileManager();

		/// Walls:
		
		Iterator<?> it = _jRoom.keyIterator();
		int count = 0;
		while( it.hasNext() ) {
			String str = (String)it.next();
			if(str.startsWith("w_")) {
				count++;
			}
		}
		
		System.out.println("the room wants to init "+count+" walls!");
		
		myWalls = new LinkedHashMap<Character, SM_Wall>();
		count = 0;
		it = _jRoom.keyIterator();
		while( it.hasNext() ) {
			String str = (String)it.next();
			if(str.startsWith("w_")) {
				char key = str.charAt(str.length()-1);
				System.out.println("...making wall nr "+count+" the char is: "+key);
				myWalls.put(key, new SM_Wall(str, _jRoom.getJSONObject(str), this, base.fm));
				count++;
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
						System.out.println(""+thisAngle);
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
	
	


	public void initProjectView(Dimension _size, Dimension _loc, SM_FileManager _fm) {
		
		
		
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
		
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		myProjectView = new SM_RoomProjectView(_size.width, _size.height);
//		f.setAlwaysOnTop(true);
		f.add(myProjectView);
		File fl = _fm.getFilePathForRoom(myRoomName);

		myProjectView.frame = f;
		myProjectView.resize(_size.width, _size.height);
		myProjectView.setPreferredSize(_size);
		myProjectView.setMinimumSize(_size);
		myProjectView.frame.add(myProjectView);
		myProjectView.init(fl, this);
		myProjectView.frame.pack();
		myProjectView.frame.setVisible(true);
		myProjectView.frame.setLocation(_loc.width, _loc.height);
		myProjectView.frame.setTitle(myRealName);
		
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
	
	public void initArrangementView(Dimension _size, Dimension _loc, SM_FileManager _fm) {
		
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
		
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
//		f.setAlwaysOnTop(true);
		myArrangementView = new SM_RoomArrangementView(_size.width, _size.height);
		f.add(myArrangementView);
		File fl = _fm.getFilePathForRoom(myRoomName);

		myArrangementView.frame = f;
		myArrangementView.resize(_size.width, _size.height);
		myArrangementView.setPreferredSize(_size);
		myArrangementView.setMinimumSize(_size);
		myArrangementView.frame.add(myArrangementView);
		myArrangementView.init(fl, this, myViewAngles);
		myArrangementView.setMenuExit();
		myArrangementView.frame.pack();
		myArrangementView.frame.setVisible(true);
		myArrangementView.frame.setLocation(_loc.width, _loc.height);
		myArrangementView.frame.setTitle(myRealName);
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
			
			if( w.hasArtwork(_name) && w.getWallChar() == _wallChar ) return true;
			
		}
		return false;
	}
	
	public void addArtworkToWall(SM_FileManager fm, SM_Artwork _aw, char _wall) {
		SM_Wall w = (SM_Wall)myWalls.get((char)_wall);
		w.addArtwork(_aw, _aw.getName());
	}
	
	public String getName() {
		return myRoomName;
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

	public void requestRoomEnter() {
		base.wm.requestStateChange(progState.ROOM, myRoomName);
	}

	public void requestRoomExit() {
		// TODO Auto-generated method stub
		base.wm.requestStateChange(progState.PROJECT, null);
	}
	
	public void endView() {
		System.out.println("i, the room, should now end my view...");
		if( myProjectView != null ) {
			System.out.println("exiting ProjView"+myRoomName);
			myProjectView.dispose();
			myProjectView = null;
		} else if( myArrangementView != null) {
			System.out.println("exiting ArrView"+myRoomName);
			myArrangementView.disposeVM();
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
	
	public SM_WindowManager getWindowManager() {
		return base.getWindowManager();
	}

	public void registerUpdateListener(ArtworkUpdateListener _l){
		base.fm.registerUpdateListener(_l);
	}

	public void unregisterUpdateListener(ArtworkUpdateListener _l) {
		base.fm.registerUpdateListener(_l);
	}
}
