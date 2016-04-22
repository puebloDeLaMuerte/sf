package updateModel;

import java.util.EventObject;

public class WallColorUpdateRequestEvent extends EventObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3813343036901043314L;
	private String 		roomName;
	private int 		color;
	private Character	wallCharacter;
	private boolean 	isPreview;
	private boolean		isSingleWall;
	private boolean 	isOriginalColorRequested = false;
	private boolean		deleteWallColor = false;

	public WallColorUpdateRequestEvent(Object source, String _roomName, int _color, boolean _preview) {
		super(source);
		
		roomName = _roomName;
		color = _color;
		isPreview = _preview;
		isSingleWall = false;
	}
	
	public WallColorUpdateRequestEvent(Object source, String _roomName, Character _wallCharacter, int _color, boolean _preview) {
		super(source);
		
		roomName = _roomName;
		wallCharacter = _wallCharacter;
		color = _color;
		isPreview = _preview;
		isSingleWall = true;
	}
	
	public WallColorUpdateRequestEvent(Object _source, String _roomName, Character _wallCharacter) {
		super(_source);
		roomName = _roomName;
		wallCharacter = _wallCharacter;
		deleteWallColor = true;
		isSingleWall = true;
	}
	
	public WallColorUpdateRequestEvent(Object _source, String _roomName) {
		super(_source);
		roomName = _roomName;
		isOriginalColorRequested = true;
		isSingleWall = false;
	}
	
	
	public boolean isSingleWall() {
//		if( wallCharacter == null ) return false;
//		else return true;
		return isSingleWall;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public Character getWallCharacter() {
		return wallCharacter;
	}
	
	public int getColor() {
		return color;
	}
	
	public boolean isPreview() {
		return isPreview;
	}
	
	public boolean isOriginalColorRequested() {
		return isOriginalColorRequested;
	}
	
	public boolean isDeleteWallColor() {
		return deleteWallColor;
	}
}
