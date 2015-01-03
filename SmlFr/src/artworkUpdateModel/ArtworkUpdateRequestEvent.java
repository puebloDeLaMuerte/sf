package artworkUpdateModel;

import java.util.EventObject;

import SMUtils.FrameStyle;

public class ArtworkUpdateRequestEvent extends EventObject {
	
	private String 				awName;
	private ArtworkUpdateType 	type;
	
	private int 				newPosX, newPosY;
	
	private boolean 			newLight;
	
	private FrameStyle			newFrameStyle;
	
	private int[]				newFrameSize;
	
	
	public ArtworkUpdateRequestEvent(Object source, String _awName, int _newPosX, int _newPosY) {
		super(source);
		awName = _awName;
		type = ArtworkUpdateType.POS_IN_WALL;
		newPosX = _newPosX;
		newPosY = _newPosY;
	}
	
	public ArtworkUpdateRequestEvent(Object source, String _awName, boolean _light) {
		super(source);
		awName = _awName;
		type = ArtworkUpdateType.LIGHT;
		newLight = _light;
	}
	
	public  ArtworkUpdateRequestEvent(Object source, String _awName, FrameStyle _newStyle) {
		super(source);
		awName = _awName;
		type = ArtworkUpdateType.FRAME_STYLE;
		newFrameStyle = _newStyle;
	}
	
	public  ArtworkUpdateRequestEvent(Object source, String _awName, int[] _newFrameSize) {
		super(source);
		awName = _awName;
		type = ArtworkUpdateType.FRAME_SIZE;
		newFrameSize = _newFrameSize;
	}
	
	public ArtworkUpdateType getType() {
		return type;
	}
	
	public String getName() {
		return awName;
	}
	
	public int getNewPosX() {
		if( type == ArtworkUpdateType.POS_IN_WALL ) return newPosX;
		else return -1;
	}
	
	public int getNewPosY() {
		if( type == ArtworkUpdateType.POS_IN_WALL ) return newPosY;
		else return -1;
	}
	
	public boolean getNewLight() {
		if( type == ArtworkUpdateType.LIGHT ) return newLight;
		else return false;
	}
	
	public FrameStyle getNewFrameStyle() {
		if( type == ArtworkUpdateType.FRAME_STYLE ) return newFrameStyle;
		else return null;
	}
	
	public int[] getNewFrameSize() {
		if( type == ArtworkUpdateType.FRAME_SIZE ) return newFrameSize;
		else return null;
	}
}
