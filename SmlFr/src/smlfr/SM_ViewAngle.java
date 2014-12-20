package smlfr;

import java.util.HashMap;

public class SM_ViewAngle {
	
	// from File
	private String						myName;
	private HashMap<String, Float[]>	wallSkew;
	private HashMap<String, Float[]>	wallCrop;
	
	private int[]			myNavigatorBoundingBox;
	
	// upon init
	private boolean			activeInView;
	
	public SM_ViewAngle(String _name, HashMap<String, Float[]> _skewList, HashMap<String, Float[]> _cropList) {
		
		myName = _name;
		wallSkew = _skewList;
		wallCrop = _cropList;
		activeInView = false;
	}
}
