package smlfr;

import java.io.File;


import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import SMUtils.FrameStyle;
import SMUtils.SM_Frames;

public class SM_Artwork {
	
	
	// init from file or import
	private String 			artist;
	private String 			title;
	private String 			invNr;
	private int[] 			mySize;
	private FrameStyle		frameStyle;
	private int[]			frameSize;
	private int[]			passepartoutSize;
	
	private File			myPath;
	
	// init from project
	private String			isInWall;
	private int[]			artworkPosInWall;
	private boolean			light;
	
	private PImage			myGfx;
	private PImage			myThumb;
	private PImage			myFrameGfx;
	private boolean			hasFrameGfx;

	private boolean			selected = false;

	SM_Artwork(JSONObject _j, File _path, SM_Frames _frames) {
		myPath = _path;
		
		artist = _j.getString("artist");
		title = _j.getString("title");
		invNr = _j.getString("invNr");
		mySize = _j.getJSONArray("size").getIntArray();
		frameStyle = FrameStyle.valueOf(FrameStyle.class, _j.getString("frameStyle"));
		frameSize = _j.getJSONArray("frameSize").getIntArray();
		passepartoutSize = _j.getJSONArray("pasSize").getIntArray();
		
		if( frameStyle != FrameStyle.NONE ) {
			myFrameGfx = _frames.getFrameImg(frameStyle);
		}
		
		isInWall = null;
		artworkPosInWall = new int[2];
		light = false;
		
	}

	// alternative constructors
	{
//	SM_Artwork(String _artist, String _title, String _invNr, int _sizeX, int _sizeY) {
//		artist = _artist;
//		title = _title;
//		invNr = _invNr;
//		mySize = new int[2];
//		mySize[0] = _sizeX;
//		mySize[1] = _sizeY;
//		frameStyle = FrameStyle.NONE;
//		frameSize = null;
//		passepartoutSize = null;
//	}
//	
//	SM_Artwork(String _artist, String _title, String _invNr, int _sizeX, int _sizeY, FrameStyle _frame, int _frameTop, int _frameBottom, int _frameLeft, int _frameRight){
//		artist = _artist;
//		title = _title;
//		invNr = _invNr;
//		mySize = new int[2];
//		mySize[0] = _sizeX;
//		mySize[1] = _sizeY;
//		frameStyle = _frame;
//		frameSize = new int[4];
//		frameSize[0] = _frameTop;
//		frameSize[1] = _frameBottom;
//		frameSize[2] = _frameLeft;
//		frameSize[3] = _frameRight;
//		passepartoutSize = null;
//	}
//	
//	SM_Artwork(String _artist, String _title, String _invNr, int _sizeX, int _sizeY, FrameStyle _frame, int _frameTop, int _frameBottom, int _frameLeft, int _frameRight, int _pTop, int _pBottom, int _pLeft, int _pRight) {
//		artist = _artist;
//		title = _title;
//		invNr = _invNr;
//		mySize = new int[2];
//		mySize[0] = _sizeX;
//		mySize[1] = _sizeY;
//		frameStyle = _frame;
//		frameSize = new int[4];
//		frameSize[0] = _frameTop;
//		frameSize[1] = _frameBottom;
//		frameSize[2] = _frameLeft;
//		frameSize[3] = _frameRight;
//		passepartoutSize = new int[4];
//		passepartoutSize[0] = _pTop;
//		passepartoutSize[1] = _pBottom;
//		passepartoutSize[2] = _pLeft;
//		passepartoutSize[3] = _pRight;
//				
//	}
	}
	
public void initProjectData( String _isInWall, int _posX, int _posY, boolean _hasLight) {
		isInWall = _isInWall;
		artworkPosInWall = new int[2];
		setTotalWallPos(_posX, _posY);
//		artworkPosInWall[0] = _posX;
//		artworkPosInWall[1] = _posY;
		light = _hasLight;
	}

	
	// getters setters
	
	public boolean isSelected() {
		return selected;
	}
	
	public void toggleSelected() {
		selected = !selected;
	}
	
	public void setSelected(boolean s) {
		selected = s;
	}
	
	public boolean hasLight() {
		return light;
	}
	
	public void setLight(boolean _onOff) {
		light = _onOff;
	}
	
	
	/// Passepartout
	
	public boolean hasPassepartout() {
		if(passepartoutSize.length > 0) {
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
	
	public void setFrameGfx(PImage _fgfx) {
		if( _fgfx != null ) {
			myFrameGfx = _fgfx;
			hasFrameGfx = true;
		}
	}
	
	public boolean hasFrameGfx() {
		return hasFrameGfx;
	}
	
	public PImage getFrameGfx() {
		if( hasFrame() ) return myFrameGfx;
		else return null;
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
	
	///
	
	public String getWall() {
		return isInWall;
	}
	
	public char getWallChar() {
		return isInWall.charAt(isInWall.length()-1);
	}
	
	public void sayHi() {
		
		System.out.println("\ninvNR: "+invNr);
		System.out.println("  "+title);
		System.out.println("  "+artist);
		System.out.println("  "+mySize[0]+" x "+mySize[1]);
		
		
	}

	public void setWall(SM_FileManager sm_FileManager, String _wall) {
		// TODO not implemented yet
		System.out.println("I GET CALLED, FIX ME!!!");
		isInWall = _wall;
	}
	
	// Position
	
	public void setTotalWallPos(int x, int y) {		
		
		int pX = x;
		int pY = y;
		
		if( frameSize.length > 0 ) {
			pX += frameSize[2];
			pY -= frameSize[0];
		}
		if( passepartoutSize.length > 0 ) {
			pX += passepartoutSize[2];
			pY -= passepartoutSize[0];
		}
		
		
		artworkPosInWall[0] = pX;
		artworkPosInWall[1] = pY;
		
	}
	
	public int[] getTotalWallPos() {
		if( artworkPosInWall != null ) {
			
			int[] p = new int[2];
			
			p[0] = artworkPosInWall[0];
			p[1] = artworkPosInWall[1];
			
			if( frameSize.length > 0 ) {
				p[0] -= frameSize[2];
				p[1] +=	frameSize[0];
			}
			if( passepartoutSize.length > 0 ) {
				p[0] -= passepartoutSize[2];
				p[1] += passepartoutSize[0];
			}
			
			return p;
		}
		return null;
	}
	
	public int[] getPptWallPos() {
		
		int[] ppos = new int[] {0,0};
		
		if( hasPassepartout() ) {
			ppos[0] = artworkPosInWall[0];
			ppos[1] = artworkPosInWall[1];
		
			ppos[0] -= passepartoutSize[2];
			ppos[1] += passepartoutSize[0];
		}
		
		return ppos;
	}
	
	public int[] getPptSize() {
		int[] w = mySize.clone();
		
		if( passepartoutSize.length > 0 ) {
			w[0] += (passepartoutSize[2] + passepartoutSize[3]);
			w[1] += (passepartoutSize[0] + passepartoutSize[1]);
		}
		
		return w;
	}
	
	public int[] getArtworkWallPos() {
		
		return artworkPosInWall;
	}
	
	public int[] getArtworkSize() {
		return mySize;
	}
	
	public int getTotalWidth(){
		
		int w = mySize[0];
		
		if( frameSize.length > 0 ) {
			w += frameSize[2];
			w += frameSize[3];
		}
		if( passepartoutSize.length > 0 ) {
			w += passepartoutSize[2];
			w += passepartoutSize[3];
		}
		
		return w;
	}
	
	public int getTotalHeight(){
		
		int h = mySize[1];

		if( frameSize.length > 0 ) {
			h += frameSize[0];
			h += frameSize[1];
		}
		if( passepartoutSize.length > 0 ) {
			h += passepartoutSize[0];
			h += passepartoutSize[1];
		}

		return h;
	}

	// trivia
	
	public String getName() {
		return invNr;
	}

	public String getArtis() {
		return artist;
	}
	
	public String getTitle() {
		return title;
	}
	
	public File getFilePath() {
		return myPath;
	}
	
	public void setGfx( PImage _g) {
		myGfx = _g;
	}
	
	public PImage getGfx() {
		return myGfx;
	}
	
	public boolean hasGfx() {
		if( myGfx == null ) return false;
		else return true;
	}
	
	public void unloadGraphics() {
		myGfx = null;
	}
	
	public boolean hasThumb() {
		if( myThumb == null ) return false;
		else return true;
	}
	
	public void setThumb(PImage _t) {
		myThumb = _t;
	}
	
	public PImage getThumb() {
		return myThumb;
	}
	
	public JSONObject getAsJsonObjectForProject(SM_FileManager fm) {
		JSONObject o = new JSONObject();
		o.setString("invNr", invNr);
		o.setBoolean("light", light);
		JSONArray pos = new JSONArray();
		pos.append(artworkPosInWall[0]);
		pos.append(artworkPosInWall[1]);
		o.setJSONArray("pos", pos);
		return o;
	}

	
	
}
