package SMupdateModel;

import java.util.EventObject;

public class WallUpdateRequestEvent extends EventObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7297688609009917058L;
	
	private String		name;
	private String		targetRoom;
	private char		targetWall;
	private String 		originRoom;
	private char		originWall;

	public WallUpdateRequestEvent( Object source , String _awName, char _targetWall, String _targetRoom, String _originRoom, char _originWall) {
		super(source);
		originRoom = _originRoom;
		originWall = _originWall;
		name = _awName;
		targetWall = _targetWall;
		targetRoom = _targetRoom;
	}
	
	

	public char getTargetWall() {
		return targetWall;
	}
	
	public String getTargetRoom() {
		return targetRoom;
	}
	
	public String getOriginRoom() {
		return originRoom;
	}
	
	public char getOriginWall() {
		return originWall;
	}
	
	public String getName() {
		return name;
	}

}
