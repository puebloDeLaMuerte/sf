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

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.core.PShape;
import processing.data.JSONArray;
import processing.data.JSONObject;



public class SM_Room {

	// TODO	implement ArtworkUpdateListener
	// TODO implement DropTarget dt

	// From Museum-File
	private String				myRoomName;
	private String				myRealName;
	private SM_ViewAngle[] 		myViewAngles;
	private SM_Wall[] 			myWalls;

	// upon init
	private SM_RoomProjectView	myView;
	private SmlFr				base;
	private boolean				saveDirty;
	private boolean				entered;




	public SM_Room(SmlFr _base, String _name, JSONObject _jRoom) {

		base = _base;
		myRoomName = _name;
		myRealName = _jRoom.getString("roomRealName");


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
		
		myWalls = new SM_Wall[count];
		count = 0;
		it = _jRoom.keyIterator();
		while( it.hasNext() ) {
			String str = (String)it.next();
			if(str.startsWith("w_")) {
				System.out.println("...making wall nr "+count);
				myWalls[count] = new SM_Wall(str, _jRoom.getJSONObject(str));
				count++;
			}
		}

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
		return "Hi, this is room "+myRealName+" ("+myRoomName+") \nI have "+myViewAngles.length+" ViewAngles.\nI also have as many as "+myWalls.length+" Walls.\n\n";
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
		
		f.setTitle(myRealName);
		f.setVisible(true);
		f.setSize(_size);
		f.setLocation(_loc.width, _loc.height);
		f.setResizable(true);
		
		
		
	}
	
	public SM_Wall[] getWalls() {
		if( myWalls == null ) System.out.println("THEY ARE FUCKING NULL IN THE ROOM");
		else System.out.println("the null seems to be somhere esle");
		return myWalls;
	}

	public String getName() {
		return myRoomName;
	}
}
