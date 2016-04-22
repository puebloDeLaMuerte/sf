package SMupdateModel;

import java.util.EventObject;
import java.util.LinkedHashMap;

import SMUtils.FrameStyle;

public class ArtworkUpdateRequestEvent extends EventObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8434037640501633009L;
	
	private String 					awName;
	private UpdateType 				type;
	private int						multiple;
	private boolean 				isMultiple;
	
	private int 					newPosX, newPosY;
	
	private boolean 				newLight;
	
	private FrameStyle				newFrameStyle;
	
	private int[]					newFrameSize;
	
	LinkedHashMap<String, Object> 	newData;
	
	
	public ArtworkUpdateRequestEvent(Object source, boolean _isMultiple, int _multiple ,String _awName, int _newPosX, int _newPosY) {
		super(source);
		awName = _awName;
		type = UpdateType.POS_IN_WALL;
		isMultiple = _isMultiple;
		multiple = _multiple;
		
		newPosX = _newPosX;
		newPosY = _newPosY;
	}
	
	public ArtworkUpdateRequestEvent(Object source, boolean _isMultiple, int _multiple, String _awName, boolean _light) {
		super(source);
		awName = _awName;
		type = UpdateType.LIGHT;
		isMultiple = _isMultiple;
		multiple = _multiple;
		
		newLight = _light;
	}
	
	public  ArtworkUpdateRequestEvent(Object source, boolean _isMultiple, int _multiple, String _awName, FrameStyle _newStyle) {
		super(source);
		awName = _awName;
		type = UpdateType.FRAME_STYLE;
		isMultiple = _isMultiple;
		multiple = _multiple;
		newFrameStyle = _newStyle;
	}
	
	public  ArtworkUpdateRequestEvent(Object source, boolean _isMultiple, int _multiple, String _awName, int[] _newFrameSize) {
		super(source);
		awName = _awName;
		type = UpdateType.FRAME_SIZE;
		isMultiple = _isMultiple;
		multiple = _multiple;
		
		newFrameSize = _newFrameSize;
	}
	
	public ArtworkUpdateRequestEvent(Object source, boolean _isMultiple, int _multiple, LinkedHashMap<String, Object> _data) {
		super(source);
		awName = (String)_data.get("Name");
		type = UpdateType.GENERAL_AW_DATA;
		isMultiple = _isMultiple;
		multiple = _multiple;
		
		newData = _data;
	}
	
	public boolean isMultiple() {
		return isMultiple;
	}
	
	public int getMultiple() {
		return multiple;
	}
	
	public UpdateType getType() {
		return type;
	}
	
	public String getName() {
		return awName;
	}
	
	public int getNewPosX() {
		if( type == UpdateType.POS_IN_WALL ) return newPosX;
		else return -1;
	}
	
	public int getNewPosY() {
		if( type == UpdateType.POS_IN_WALL ) return newPosY;
		else return -1;
	}
	
	public boolean getNewLight() {
		if( type == UpdateType.LIGHT ) return newLight;
		else return false;
	}
	
	public FrameStyle getNewFrameStyle() {
		if( type == UpdateType.FRAME_STYLE ) return newFrameStyle;
		else return null;
	}
	
	public int[] getNewFrameSize() {
		if( type == UpdateType.FRAME_SIZE ) return newFrameSize;
		else return null;
	}

	
	public LinkedHashMap<String, Object> getAwData() {

		if( type == UpdateType.GENERAL_AW_DATA) return newData;
		return null;
	}
}
