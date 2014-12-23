package artworkUpdateModel;

import java.util.EventObject;

public class WallUpdateRequestEvent extends EventObject{

	private String		name;
	private char		wall;
	private String 		room;

	public WallUpdateRequestEvent( Object source , String _name, char _wall, String _room ) {
		super(source);

		name = _name;
		wall = _wall;
		room = _room;
	}

	public char getWall() {
		return wall;
	}
	
	public String getRoom() {
		return room;
	}
	
	public String getName() {
		return name;
	}

}
