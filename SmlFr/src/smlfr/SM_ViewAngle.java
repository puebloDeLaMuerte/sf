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
			for( Float fl : wallSkew.get(s) ) System.out.print(fl+", ");
		}
	}

	public String getRealName() {
		return myRealName;
	}
	
	public Float[] getWallSkew(Character _w) {
		String querystrng = "w"+myName.substring(1, myName.lastIndexOf('_')+1)+_w;
		System.out.println("wallSkew Query:\n  the char: "+_w+"\n  the generated query string: "+querystrng);
		Float[] ar = wallSkew.get(querystrng);
		if( ar != null && ar.length == 10) return ar;
		else return null;
	}
	
	public String getName() {
		return myName;
	}
	
	public String[] getFullWallNames() {
		String[] names = new String[wallSkew.keySet().size()];
		
		int i =0;
		for(String n : wallSkew.keySet() ) {
			names[i] = n;
			i++;
		}
		
		return names;
		
	}
	
	public Character[] getWallChars() {
		
		Character[] chars = new Character[wallSkew.keySet().size()];
		
		int i =0;
		for(String n : wallSkew.keySet() ) {
			chars[i] = n.charAt(n.length()-1);
			i++;
		}
		
		return chars;
		
	}
	
	public String getWallCharsAsString() {
		String s = "";
		
		for(String n : wallSkew.keySet() ) {
			s += n.charAt(n.length()-1);
		}
		
//		return s;
		return myRealName;
	}
}
