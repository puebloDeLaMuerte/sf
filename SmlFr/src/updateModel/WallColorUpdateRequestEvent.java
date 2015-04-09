package updateModel;

import java.util.EventObject;

public class WallColorUpdateRequestEvent extends EventObject {
	
	private String 		roomName;
	private int 		color;
	private boolean 	isPreview;

	public WallColorUpdateRequestEvent(Object source, String _roomName, int _color, boolean _preview) {
		super(source);
		
		roomName = _roomName;
		color = _color;
		isPreview = _preview;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public int getColor() {
		return color;
	}
	
	public boolean isPreview() {
		return isPreview;
	}
	
}
