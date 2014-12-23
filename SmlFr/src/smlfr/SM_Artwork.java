package smlfr;

import processing.data.JSONArray;
import processing.data.JSONObject;
import SMUtils.FrameStyle;

public class SM_Artwork {
	
	
	// init from file or import
	private String 			artist;
	private String 			title;
	private String 			invNr;
	private int[] 			size;
	private FrameStyle		frameStyle;
	private int[]			frameSize;
	private int[]			passepartoutSize;
	
	
	// init from project
	private String			isInWall;
	private int[]			posInRoom;
	private boolean			light;

	private boolean			selected = false;

	SM_Artwork(JSONObject _j) {
		
		artist = _j.getString("artist");
		title = _j.getString("title");
		invNr = _j.getString("invNr");
		size = _j.getJSONArray("size").getIntArray();
		frameStyle = FrameStyle.valueOf(FrameStyle.class, _j.getString("frameStyle"));
		frameSize = _j.getJSONArray("frameSize").getIntArray();
		passepartoutSize = _j.getJSONArray("pasSize").getIntArray();
		
		isInWall = null;
		posInRoom = new int[2];
		light = false;
		
		
		
		
		
	}
	
	SM_Artwork(String _artist, String _title, String _invNr, int _sizeX, int _sizeY) {
		artist = _artist;
		title = _title;
		invNr = _invNr;
		size = new int[2];
		size[0] = _sizeX;
		size[1] = _sizeY;
		frameStyle = FrameStyle.NONE;
		frameSize = null;
		passepartoutSize = null;
	}
	
	SM_Artwork(String _artist, String _title, String _invNr, int _sizeX, int _sizeY, FrameStyle _frame, int _frameTop, int _frameBottom, int _frameLeft, int _frameRight){
		artist = _artist;
		title = _title;
		invNr = _invNr;
		size = new int[2];
		size[0] = _sizeX;
		size[1] = _sizeY;
		frameStyle = _frame;
		frameSize = new int[4];
		frameSize[0] = _frameTop;
		frameSize[1] = _frameBottom;
		frameSize[2] = _frameLeft;
		frameSize[3] = _frameRight;
		passepartoutSize = null;
	}
	
	SM_Artwork(String _artist, String _title, String _invNr, int _sizeX, int _sizeY, FrameStyle _frame, int _frameTop, int _frameBottom, int _frameLeft, int _frameRight, int _pTop, int _pBottom, int _pLeft, int _pRight) {
		artist = _artist;
		title = _title;
		invNr = _invNr;
		size = new int[2];
		size[0] = _sizeX;
		size[1] = _sizeY;
		frameStyle = _frame;
		frameSize = new int[4];
		frameSize[0] = _frameTop;
		frameSize[1] = _frameBottom;
		frameSize[2] = _frameLeft;
		frameSize[3] = _frameRight;
		passepartoutSize = new int[4];
		passepartoutSize[0] = _pTop;
		passepartoutSize[1] = _pBottom;
		passepartoutSize[2] = _pLeft;
		passepartoutSize[3] = _pRight;
				
	}
	
	public void initProjectData( String _isInWall, int _posX, int _posY, boolean _hasLight) {
		isInWall = _isInWall;
		posInRoom = new int[2];
		posInRoom[0] = _posX;
		posInRoom[1] = _posY;
		light = _hasLight;
	}

	
	// getters setters
	
	public boolean isSelected() {
		return selected;
	}
	
	public void selectAction() {
		selected = !selected;
	}
	
	public boolean hasLight() {
		return light;
	}
	
	public void setLight(boolean _onOff) {
		light = _onOff;
	}
	
	public void setPos(int x, int y) {
		posInRoom[0] = x;
		posInRoom[0] = y;
	}
	
	/// Passepartout
	
	public boolean hasPassepartout() {
		if(passepartoutSize != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public int[] getPassepartoutMeasure() {
		return passepartoutSize;
	}
	/// Frame
	
	public boolean hasFrame() {
		if(frameStyle != FrameStyle.NONE) {
			return true;
		} else {
			return false;
		}
	}
	
	public FrameStyle getFrameStyle() {
		return frameStyle;
	}
	
	public void setFrameStyle(FrameStyle _fStyle) {
		frameStyle = _fStyle;
	}
	
	public int[] getFrameMeasure() {
		return frameSize;
	}
	
	public String getWall() {
		return isInWall;
	}
	
	public void sayHi() {
		
		System.out.println("\ninvNR: "+invNr);
		System.out.println("  "+title);
		System.out.println("  "+artist);
		System.out.println("  "+size[0]+" x "+size[1]);
		
		
	}

	public void setWall(SM_FileManager sm_FileManager, String _wall) {
		
	}

	public String getName() {
		return invNr;
	}

	public JSONObject getAsJsonObject(SM_FileManager fm) {
		JSONObject o = new JSONObject();
		o.setString("invNr", invNr);
		o.setBoolean("light", light);
		JSONArray pos = new JSONArray();
		pos.append(posInRoom[0]);
		pos.append(posInRoom[1]);
		o.setJSONArray("pos", pos);
		return o;
	}
	
	
}
