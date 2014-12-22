package smlfr;

import processing.data.JSONObject;

public class SM_Wall {
	
	// from file:
	private String			myWallName;
	private char			myWallChar;
	private float[]			myNavigatorBoundingBox;
	
	// from Project:
	private SM_Artwork[]	myArtworks;
	
	public SM_Wall(String _name, JSONObject _w) {
		
		System.out.println("the wall says Hi!");
		
		myWallName = _name;
		myWallChar = myWallName.charAt(myWallName.length()-1);
		myNavigatorBoundingBox = _w.getJSONArray("navigatorBoundingBox").getFloatArray();
		
		System.out.println("the wall says this about it's navbounds: "+myNavigatorBoundingBox[0]);
		
		
		
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
