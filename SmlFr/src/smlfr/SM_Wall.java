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
	private float[]							myNavigatorPos;
	private int								myNavigatorOrientation;
	private int[]							mySize;
	
	// from Project:
	private HashMap<String, SM_Artwork>		myArtworks;
	private Integer							myColor;
	
	// upon init
	private SM_Room							myRoom;
	
	public SM_Wall(String _name, JSONObject _w, SM_Room _room) {
		
		System.out.println("the wall says Hi! ..my name? Shure, here it is: "+_name);
		myRoom = _room;
		myWallName = _name;
		myWallChar = myWallName.charAt(myWallName.length()-1);
		myNavigatorBoundingBox = _w.getJSONArray("navigatorBoundingBox").getFloatArray();
		myNavigatorPos = _w.getJSONArray("navigatorPos").getFloatArray();
		myNavigatorOrientation = _w.getInt("navigatorOrientation");
		mySize = _w.getJSONArray("wallSize").getIntArray();
		System.out.println("the wall says this about it's navbounds: "+myNavigatorBoundingBox[0]);
		
		
		
	}
	
	public void setColor( Integer _cInt, String _colorBrillux ) {
		myColor = _cInt;   
		// TODO   BRILLUX NOT IMPLEMENTED
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
		_aw.setPos(mySize[0]/2, mySize[1]/2);
	}
	
	public boolean hasArtwork(String _name) {
		if(myArtworks.containsKey(_name)) return true;
		else return false;
	}
	
	public SM_Artwork[] hasArtworks() {
		
		SM_Artwork[] aws = new SM_Artwork[myArtworks.keySet().size()];
		int i=0;
		for( String s : myArtworks.keySet() ) {
			aws[i] = myArtworks.get(s);
			i++;
		}
		return aws;
	}
	
	public int getWidth() {
		return mySize[0];
	}
	
	public int getHeight() {
		return mySize[1];
	}
	
	public String getWallName() {
		return myWallName;
	}
	
	public char getWallChar() {
		return myWallChar;
	}
	
	public int getOrientation() {
		if( myNavigatorOrientation >= 0 && myNavigatorOrientation <= 3) return myNavigatorOrientation;
		else return -1;
	}
	
	public float[] getNavPos() {
		if( myNavigatorPos != null ) {
			return myNavigatorPos;
		} else {
			return null;
		}
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
