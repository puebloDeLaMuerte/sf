package smlfr;

import java.util.HashMap;


import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import updateModel.UpdateEvent;
import updateModel.UpdateListener;

public class SM_Wall implements UpdateListener {
	
	// from file:
	private String							myWallName;
	private char							myWallChar;
	private float[]							myNavigatorBoundingBox;
	private float[]							myNavigatorPos;
	private int								myNavigatorOrientation;
	private int[]							mySize;
	private int								myMidHeight = 1500;
	
	
	// from Project:
	private HashMap<String, SM_Artwork>		myArtworks;
	private Integer							myWallColor;
	private boolean							hasColor;
	
	
	// upon init
	public SM_Room							myRoom;
	
	public SM_Wall(String _name, JSONObject _w, SM_Room _room, SM_FileManager _fm) {
		
		myRoom = _room;
		myWallName = _name;
		myWallChar = myWallName.charAt(myWallName.length()-1);
		hasColor = false;
		myNavigatorBoundingBox = _w.getJSONArray("navigatorBoundingBox").getFloatArray();
		myNavigatorPos = _w.getJSONArray("navigatorPos").getFloatArray();
		myNavigatorOrientation = _w.getInt("navigatorOrientation");
		mySize = _w.getJSONArray("wallSize").getIntArray();
		_fm.registerUpdateListener(this);
		
		
	}
	
	public void setColor( int _cInt ) {
		myWallColor = _cInt;
		hasColor = true;
	}
	
	public void removeColor() {
		hasColor = false;
	}
	
	public int getColor() {
		return myWallColor;
	}

	public boolean hasColor() {
		return hasColor;
	}
	
	public void setArtworks(JSONArray _awks) {

		myArtworks = new HashMap<String, SM_Artwork>();
		
		for( int i=0; i< _awks.size(); i++) {
			
			JSONObject aw = _awks.getJSONObject(i);
			
			String id = aw.getString("invNr");
			
			int posX = aw.getJSONArray("pos").getInt(0);
			int posY = aw.getJSONArray("pos").getInt(1);
			boolean light = aw.getBoolean("light");
			
			// things that were not there in the first published version should be handled with care:
			
			boolean shadow;
			
			try {
				shadow = aw.getBoolean("shadow");
			} catch (Exception e) {
				shadow = true;
			}
			
			myArtworks.put(id, myRoom.getArtworkFromBase(id));
			myArtworks.get(id).initProjectData(myWallName, posX, posY, light, shadow);
		}
	}
	
	public void addArtwork(SM_Artwork _aw, String _awName) {
		myArtworks.put(_awName, _aw);
		
		int adOfset = 0;
		
		for( String s : myArtworks.keySet() ) {
			SM_Artwork a = myArtworks.get(s);
			if( (a.getTotalHeight() - a.getTotalWallPos()[1]) < 5  ) adOfset += a.getTotalWidth() + 100; 
		}
		
		_aw.setTotalWallPos(adOfset % mySize[0], _aw.getTotalHeight());
//		_aw.setTotalWallPos((mySize[0]/2)-(_aw.getTotalWidth()/2), (mySize[1]/2)+(_aw.getTotalHeight()/2));
		System.out.println("added Artwork "+_aw.getTitle()+"\n  pos: "+((mySize[0]/2)-(_aw.getTotalWidth()/2))+" x "+((mySize[1]/2)+(_aw.getTotalHeight()/2)));
	}
	
	public void removeArtwork(String _name) {
		System.out.println("Wall "+ myWallName+ " REMOVED: "+_name);
		myArtworks.remove(_name);
	}
	
	public boolean hasArtwork(String _name) {
		if(myArtworks.containsKey(_name)) return true;
		else return false;
	}
	
	public SM_Artwork getArtwork(String _name) {
		if(myArtworks.containsKey(_name)) {
			return myArtworks.get(_name);
		}
		else return null;
	}
	
	public SM_Artwork[] getArtworksArray() {
		
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
	
	public int getMidHeight() {
		return myMidHeight;
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

	public HashMap<String, SM_Artwork> getArtworks() {
		return myArtworks;
	}
	
	@Override
	public void doUpdate(UpdateEvent e) {
		switch (e.getType()) {
		case WALL:
			
			break;

		default:
			break;
		}
	}
	
	public PImage getShadowImage() {
		return myRoom.getShadowImage();
	}
}
