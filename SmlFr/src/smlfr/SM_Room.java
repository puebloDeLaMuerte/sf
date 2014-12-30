package smlfr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;

import SMUtils.progState;
import artworkUpdateModel.WallUpdateRequestEvent;
import artworkUpdateModel.ArtworkUpdateRequestListener;

import com.sun.tools.jdi.LinkedHashMap;

import processing.core.PApplet;
import processing.core.PShape;
import processing.data.JSONArray;
import processing.data.JSONObject;



public class SM_Room {

	// TODO	implement ArtworkUpdateListener

	// From Museum-File
	private String							myRoomName;
	private String							myRealName;
	private SM_ViewAngle[] 					myViewAngles;
	private LinkedHashMap 					myWalls;

	// upon init
	private SM_RoomProjectView				myView;
	private SmlFr							base;
	private boolean							entered;

	private ArtworkUpdateRequestListener	requestListener;


	public SM_Room(SmlFr _base, String _name, JSONObject _jRoom) {

		base = _base;
		myRoomName = _name;
		myRealName = _jRoom.getString("roomRealName");

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
		
		myWalls = new LinkedHashMap();
		count = 0;
		it = _jRoom.keyIterator();
		while( it.hasNext() ) {
			String str = (String)it.next();
			if(str.startsWith("w_")) {
				char key = str.charAt(str.length()-1);
				System.out.println("...making wall nr "+count+" the char is: ");
				myWalls.put(key, new SM_Wall(str, _jRoom.getJSONObject(str), this, base.fm));
				count++;
			}
		}
		Map<Character, SM_Wall> tMap = new TreeMap<Character, SM_Wall>(myWalls);
		myWalls = new LinkedHashMap(tMap);

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
				
				Float[] skews = new Float[4];
				Float[] crops = new Float[4];
				
				
				JSONArray relViews = _jRoom.getJSONObject(wallName).getJSONArray("relatedViews");
				for( int v=0; v<relViews.size(); v++) {
					if( relViews.getJSONObject(v).getString("viewName").equalsIgnoreCase(thisAngle) ){
						for(int f=0; f<4; f++) {
							skews[f] = relViews.getJSONObject(v).getJSONArray("viewSkew").getFloat(f);
						}
						for(int f=0; f<4; f++) {
							crops[f] = relViews.getJSONObject(v).getJSONArray("viewCrop").getFloat(f);
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
	
	public void enterRoom() {
		// TODO Change RoomView to EneteredView
		// TODO initiate ViewAngleMAnager
		// TODO initiate PreviewManager
		// TODO open Tools Window (child of Room, same Thread)
		entered = true;
	}

	public void leaveRoom() {

		entered = false;
		// TODO Save Room State

		// TODO change Roomview to projectView
		// TODO dispose ViewAngleManager
		// TODO dispose PreviewManager
		// TODO dispose Tools


	}

	public void initProjectView(Dimension _size, Dimension _loc, SM_FileManager _fm) {
		
		
		
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		myView = new SM_RoomProjectView();
		f.add(myView);
		File fl = _fm.getFilePathForRoom(myRoomName);
		System.out.println("theRoom passes:\n"+fl.getAbsolutePath());
		myView.init(f, new int[] { _size.width, _size.height }, fl, this);

//		f.setTitle(myRealName);
		f.setUndecorated(true);
		f.setVisible(true);
		f.setSize(_size);
		f.setLocation(_loc.width, _loc.height);
		f.setResizable(true);
		

		
		
	}
	
	public void initArrangementView() {
		System.out.println("soweit so gut");
	}
	
	public LinkedHashMap getWalls() {
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
		
		int i=0;
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

	public void requestRoomEnter() {
		base.wm.requestStateChange(progState.ROOM, myRoomName);
	}

	public void endView() {
		if( myView != null ) {
			System.out.println("exiting "+myRoomName);
			myView.dispose();
			myView = null;
		}
	}
}
