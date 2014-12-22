package smlfr;

import java.awt.dnd.DropTargetListener;
import java.io.File;

import javax.swing.JFrame;

public interface SM_RoomView {

	
	public void init(JFrame _frame, int[] _size, File _filePath, SM_Room _room);
}
