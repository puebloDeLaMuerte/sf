package smlfr;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import SMUtils.SM_DataFlavor;

import processing.core.PApplet;
import processing.core.PShape;

public class SM_RoomProjectView extends PApplet implements DropTargetListener {

	SM_Room 			myRoom;
	SM_Wall[]			myWalls;
	
	// utils
	private JFrame		myFrame;
	private int[]		mySize;
	private File		myFilePath;
	private PShape		myGraphics;
	private DropTarget	dt;
	private float[]		nb;
	private char		wallOver;
	private boolean		drag;
	private float mx;
	private float my;
	// drop feedback:
	private boolean dropAnim = false;
	private int bgr, bgg,bgb;
	private int dloX, dloY, druX, druY, dTargetMX, dTargetMY;
	
	public void setup() {
		bgr = 255;
		bgg = 255;
		bgb = 255;
		dt = new DropTarget(this,this);
		myGraphics = loadShape(myFilePath.getAbsolutePath()+"/"+myRoom.getName()+".svg");
		background(255);
		
		shape(myGraphics,0,0);
		
		fill(0);
		
	}
	
	public void draw() {

		
		if (!drag) {
			mx = (float) mouseX / (float) mySize[0];
			my = (float) mouseY / (float) mySize[1];
			wallOver = ' ';
			for (SM_Wall w : myWalls) {

				nb = w.getNavBounds();

				if (mx > nb[0] && mx < nb[2] && my > nb[1] && my < nb[3]) {

					wallOver = w.getWallChar();
					break;
				}

			}
		}
		bgr = bgr + ((255-bgr)/6);
		bgg = bgg + ((255-bgg)/6);
		bgb = bgb + ((255-bgb)/6);
		background(bgr,bgg,bgb);
		
		if (dropAnim) {
			dloX = dloX + ((dTargetMX - dloX) / 5);
			druX = druX + ((dTargetMX - druX) / 5);
			dloY = dloY + ((dTargetMY - dloY) / 5);
			druY = druY + ((dTargetMY - druY) / 5);
			rectMode(CORNERS);
			noFill();
			rect(dloX, dloY, druX, druY);
			
			if( abs(dloX-dTargetMX) < 10 ) dropAnim = false;
		}
		shape(myGraphics,0,0);
		
		text( wallOver, 20,20  );
		text( mx+" x "+my, 20,40);

	}
	
	public void init(JFrame _frame, int[] _size, File _filePath, SM_Room _room) {
		
		mySize = _size;
		myFrame = _frame;
		myFilePath = _filePath;
		myRoom = _room;
		myWalls = myRoom.getWalls();
		
		super.init();
	}
	
	
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		drag = true;
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		drag = false;
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		mx = (float)dtde.getLocation().x / (float)mySize[0];
		my = (float)dtde.getLocation().y / (float)mySize[1];
		wallOver = ' ';
		
		System.out.println(mx+" x "+my);
		
		for(SM_Wall w : myWalls){
			
			
			nb = w.getNavBounds();
			
			if( mx > nb[0] && mx < nb[2] && my > nb[1] && my < nb[3] ) {
				
				wallOver = w.getWallChar();
				break;
			}
			
		}
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		
		if(dtde.getCurrentDataFlavors()[0] == SM_DataFlavor.SM_AW_Flavor && wallOver != ' ' ) {
			
			setDropAnim( dtde.getLocation().x, dtde.getLocation().y );
			
			System.out.println("The Flavor is just right!");
			try {
				System.out.println("WE JUST DROPED "+dtde.getTransferable().getClass()+" ON THE PAPPLET");

				System.out.println("WE JUST DROPED "+dtde.getTransferable().getTransferData(DataFlavor.stringFlavor).toString()+" ON THE PAPPLET");
				dtde.dropComplete(true);
				dtde.acceptDrop(dtde.getDropAction());
				bgr = 50;
				bgb = 0;
			} catch (Exception e) {
				bgg = 0;
				bgb = 0;
				dtde.rejectDrop();
				e.printStackTrace();
			}
		} else {
			bgg = 0;
			bgb = 0;
			dtde.rejectDrop();
		}
		
		
		drag = false;
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	private void setDropAnim(int x, int y){
		dropAnim = true;
		dTargetMX = x;
		dTargetMY = y;
		dloX = 0;
		dloY = 0;
		druX = mySize[0];
		druY = mySize[1];
	}
}
