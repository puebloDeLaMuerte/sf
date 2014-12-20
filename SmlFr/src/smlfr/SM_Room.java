package smlfr;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.PShape;
import processing.data.JSONArray;
import processing.data.JSONObject;



public class SM_Room {

	// TODO	implement ArtworkUpdateListener
	// TODO implement DropTarget dt

	// From Museum-File
	private String			myRoomName;
	private String			myRealName;
	private SM_ViewAngle[] 	myViewAngles;
	private SM_Wall[] 		myWalls;

	// upon init
	private SM_RoomView		myView;
	private PShape			myGraphics;
	private SmlFr			base;
	private boolean			saveDirty;
	private boolean			entered;




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
		myWalls = new SM_Wall[count];
		count = 0;
		while( it.hasNext() ) {
			String str = (String)it.next();
			if(str.startsWith("w_")) {
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
}
