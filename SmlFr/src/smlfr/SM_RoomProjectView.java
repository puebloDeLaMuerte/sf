package smlfr;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.core.PShape;

public class SM_RoomProjectView extends PApplet implements DropTargetListener {

	SM_Room 			myRoom;
	
//	SM_Wall[]			myWalls;
	
	// utils
	private JFrame		myFrame;
//	private int[]		mySize;
	private File		myFilePath;
	private PShape		myGraphics;

	
	public void setup() {
		myGraphics = loadShape(myFilePath.getAbsolutePath()+"/"+myRoom.getName()+".svg");

		background(255);
		
		shape(myGraphics,0,0);
		

		
	}
	
	public void draw() {

		
		background(255);
		shape(myGraphics,0,0);

	}
	
	public void init(JFrame _frame, File _filePath, SM_Room _room) {
		
//		mySize = _size;
		myFrame = _frame;
		myFilePath = _filePath;
		myRoom = _room;

		super.init();
	}
	
	
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}
	
}
