package smlfr;

import java.util.HashMap;
import java.util.Set;

import SMUtils.artworkActionType;
import SMupdateModel.UpdateEvent;
import SMupdateModel.UpdateListener;


import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class SM_Wall implements UpdateListener {
	
	// from file:
	private String							myWallName;
	private char							myWallChar;
	private float[]							myNavigatorBoundingBox;
	private float[]							myNavigatorPos;
	private int								myNavigatorOrientation;
	private int[]							mySize;
	private int								myMidHeight;
	
	
	// from Project:
	private HashMap<String, SM_Artwork>		myArtworks;
//	public enum								artworkActionType { ADD, REMOVE, HAS, GET_AW, GET_KEYS, GET_ARRAY};
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
		
		myMidHeight = _fm.getMidHeight();
		myMidHeight -= _w.getInt("bottomOffset", 0);
		
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
			myArtworks.get(id).initProjectData(myWallName, myRoom.getRealName(), posX, posY, light, shadow);
		}
	}
		
	public synchronized Object artwork( artworkActionType type, String _name, SM_Artwork _aw) {
		
		switch (type) {
		
		case ADD:
			
			if( _name == null || _aw == null) break;
			addArtwork(_aw, _name);
			break;

		case REMOVE:
			
			if( _name == null) break;
			removeArtwork(_name);
			break;
			
		case HAS:
			
			if( _name == null) break;
			if( hasArtwork(_name) ) {
				return (Integer)1;
			}else{
				return (Integer)0;
			}
			
		case HOWMANY:
			return (Integer)myArtworks.size();
			
		case GET_AW:
			
			if( _name == null) break;
			return getArtwork(_name);
			
		case GET_KEYS:
			
			return getArtworksKeys();
			
		case GET_ARRAY:
			
			return getArtworksArray();
			
		default:
			return null;
		}
		
		
		return null;
	}
	
	private void addArtwork(SM_Artwork _aw, String _awName) {
		
		
		int adOfset = 0;
		
		for( String s : myArtworks.keySet() ) {
			SM_Artwork a = myArtworks.get(s);
			if( (a.getTotalHeight() - a.getTotalWallPos()[1]) < 5  ) adOfset += a.getTotalWidth() + 100;
		}
		
		myArtworks.put(_awName, _aw);
		
		_aw.setTotalWallPos(adOfset % mySize[0], _aw.getTotalHeight());
//		_aw.setTotalWallPos((mySize[0]/2)-(_aw.getTotalWidth()/2), (mySize[1]/2)+(_aw.getTotalHeight()/2));
		System.out.println("WALL: added Artwork "+_aw.getTitle()+"\n  pos: "+((mySize[0]/2)-(_aw.getTotalWidth()/2))+" x "+((mySize[1]/2)+(_aw.getTotalHeight()/2)));
	}
	
	private void removeArtwork(String _name) {
		System.out.println("WALL: me, the Wall "+ myWallName+ " REMOVED: "+_name);
		myArtworks.remove(_name);
	}
	
	private boolean hasArtwork(String _name) {
		if(myArtworks.containsKey(_name)) return true;
		else return false;
	}
	
	private SM_Artwork getArtwork(String _name) {
		if(myArtworks.containsKey(_name)) {
			return myArtworks.get(_name);
		}
		else return null;
	}
	
	private Set<String> getArtworksKeys(){
		return myArtworks.keySet();
	}

	private SM_Artwork[] getArtworksArray() {
		
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
