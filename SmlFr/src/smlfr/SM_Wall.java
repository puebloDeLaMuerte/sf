package smlfr;

import processing.data.JSONObject;

public class SM_Wall {
	
	// from file:
	private String			myWallName;
	private float[]			myNavigatorBoundingBox;
	
	// from Project:
	private SM_Artwork[]	myArtworks;
	
	public SM_Wall(String _name, JSONObject _w) {
		
		myWallName = _name;
		myNavigatorBoundingBox = _w.getJSONArray("navigatorBoundingBox").getFloatArray();
		
		
		
	}
}
