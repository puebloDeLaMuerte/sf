package smlfr;

import java.util.HashMap;

public class SM_ViewAngle {
	
	// from File
	private String						myName;
	private String						myRealName;
	private HashMap<String, Float[]>	wallSkew;  // <wall, float[]>
	private HashMap<String, Float[]>	wallCrop;
	
//	private int[]						myNavigatorBoundingBox;

	
	public SM_ViewAngle(String _name, HashMap<String, Float[]> _skewList, HashMap<String, Float[]> _cropList) {
		
		myName = _name;
		myRealName = myName.substring(myName.lastIndexOf('_')+1);
		wallSkew = _skewList;
		wallCrop = _cropList;
	}
	
	public void sayHi() {
		int count = 0;
		for( String s : wallSkew.keySet() ) {
			System.out.println("skews found for "+myName+": "+count+++": "+s);
		}
	}

	public String getRealName() {
		return myRealName;
	}
	
	public String getName() {
		return myName;
	}
}
