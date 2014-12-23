package smlfr;

import java.awt.Color;
import java.util.HashMap;

import processing.data.JSONArray;
import processing.data.JSONObject;

public class SM_Wall {
	
	// from file:
	private String							myWallName;
	private char							myWallChar;
	private float[]							myNavigatorBoundingBox;
	private int[]							mySize;
	
	// from Project:
	private HashMap<String, SM_Artwork>		myArtworks;
	private Integer							myColor;
	
	// upon init
	private SM_Room							myRoom;
	
	public SM_Wall(String _name, JSONObject _w, SM_Room _room) {
		
		System.out.println("the wall says Hi!");
		myRoom = _room;
		myWallName = _name;
		myWallChar = myWallName.charAt(myWallName.length()-1);
		myNavigatorBoundingBox = _w.getJSONArray("navigatorBoundingBox").getFloatArray();
		mySize = _w.getJSONArray("wallSize").getIntArray();
		System.out.println("the wall says this about it's navbounds: "+myNavigatorBoundingBox[0]);
		
		
		
	}
	
	public void setColor( Integer _cInt, String _colorBrillux ) {
		myColor = _cInt;   // TODO   BRILLUX NOT IMPLEMENTED
	}
	
	public void setArtworks(JSONArray _awks) {

		myArtworks = new HashMap<String, SM_Artwork>();
		
		for( int i=0; i< _awks.size(); i++) {
			
			JSONObject aw = _awks.getJSONObject(i);
			
			String id = aw.getString("invNr");
			
			int posX = aw.getJSONArray("pos").getInt(0);
			int posY = aw.getJSONArray("pos").getInt(1);
			boolean light = aw.getBoolean("light");		
			
			myArtworks.put(id, myRoom.getArtworkFromBase(id));
			myArtworks.get(id).initProjectData(myWallName, posX, posY, light);
			
		}
	}
	
	public void addArtwork(SM_Artwork _aw, String _awName) {
		myArtworks.put(_awName, _aw);
		_aw.setPos(mySize[0/2], mySize[1]/2);
	}
	
	public String getWallName() {
		return myWallName;
	}
	
	public char getWallChar() {
		return myWallChar;
	}
	
	public float[] getNavBounds() {
		if(myNavigatorBoundingBox != null) {
			return myNavigatorBoundingBox;
		}
		else {
			return null;
		}
	}
}
