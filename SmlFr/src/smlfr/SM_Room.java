package smlfr;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;



public class SM_Room {

	// TODO	implement ArtworkUpdateListener
	
	// From Museum-File
	private String			myRoomName;
	private String[] 		myViewAngles;
	private SM_Wall[] 		myWalls;
	
	// upon init
	private SM_RoomView		myView;
	private SmlFr			base;
	private boolean			saveDirty;
	private boolean			entered;
	
	
	public SM_Room( SmlFr _base, String[] _viewAngles, SM_Wall[] _walls) {
		base = _base;
		myViewAngles = _viewAngles;
		myWalls = _walls;
		saveDirty = false;
		entered = false;
	}

	public void enterRoom() {
		// TODO Change RoomView to EneteredView
		// TODO initiate ViewAngleMAnager
		// TODO initiate PreviewManager
		// TODO open Tools Window (child of Room, same Thread)
		entered = true;
	}
	
	public void leaveRoom() {
		
		entered = false;
		// TODO Save Room State
		
		// TODO change Roomview to projectView
		// TODO dispose ViewAngleManager
		// TODO dispose PreviewManager
		// TODO dispose Tools
		
		
	}
}
