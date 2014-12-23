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
import java.util.HashMap;

import javax.swing.JFrame;

import artworkUpdateModel.WallUpdateRequestEvent;

import com.sun.tools.jdi.LinkedHashMap;

import SMUtils.SM_DataFlavor;

import processing.core.PApplet;
import processing.core.PShape;

public class SM_RoomProjectView extends PApplet implements DropTargetListener {

	SM_Room 									myRoom;
	LinkedHashMap								myWalls;
	
	// utils
	private JFrame								myFrame;
	private int[]								mySize;
	private File								myFilePath;
	private HashMap<Character, PShape>			wallsActiveGfx;
	private HashMap<Character, PShape>			wallsOverGfx;
	private PShape								greyRoom;
	private DropTarget							dt;
	private float[]								nb;
	private char								wallOver;
	private boolean								drag;
	private float 								mx;
	private float 								my;
	// drop feedback:
	private boolean dropAnim = false;
	private int bgr, bgg,bgb;
	private int dloX, dloY, druX, druY, dTargetMX, dTargetMY;
	
	public void setup() {
		bgr = 255;
		bgg = 255;
		bgb = 255;
		dt = new DropTarget(this,this);
		
		PShape gfxPack = loadShape(myFilePath.getAbsolutePath()+"/"+myRoom.getName()+".svg");
		
		wallsActiveGfx = new HashMap<Character, PShape>();
		wallsOverGfx = new HashMap<Character, PShape>();
		
		
		
		greyRoom = gfxPack.getChild(0);
		int i = 1;
		for( Object w : myWalls.keySet() ) {
			SM_Wall wl = (SM_Wall)myWalls.get(w);
			wallsActiveGfx.put(wl.getWallChar(), gfxPack.getChild(i));
			i++;
			wallsOverGfx.put(wl.getWallChar(), gfxPack.getChild(i));
			
			System.out.println("wallOverGraphics.put("+wl.getWallChar()+", gfxPack.getChild("+i+"))");
			i++;
		}
		
		background(255);
		
		shape(greyRoom,0,0);

		
		fill(0);
		
	}
	
	public void draw() {

		
		if (!drag) {
			mx = (float) mouseX / (float) mySize[0];
			my = (float) mouseY / (float) mySize[1];
			wallOver = ' ';
			for( Object w : myWalls.keySet() ) {
				SM_Wall wl = (SM_Wall)myWalls.get(w);
				nb = wl.getNavBounds();

//				if(w == 'M') {
//					rectMode(CORNERS);
//					stroke(0);
//					rect(nb[0]*mySize[0], nb[1]*mySize[1], nb[2]*mySize[0], nb[3]*mySize[1]);
//				}
				
				if (mx > nb[0] && mx < nb[2] && my > nb[1] && my < nb[3]) {

					wallOver = wl.getWallChar();
					break;
				}

			}
		}
		bgr = bgr + ((255-bgr)/3);
		bgg = bgg + ((255-bgg)/3);
		bgb = bgb + ((255-bgb)/3);
		background(bgr,bgg,bgb);
		
		if (dropAnim) dropAnim();
		
		shape(greyRoom,0,0);
		if(wallOver != ' ') {
			shape(wallsOverGfx.get(wallOver), 0, 0);
		}
		
		text( wallOver, 20,20  );
//		text( mx+" x "+my, 20,40);

	}
	
	public void init(JFrame _frame, int[] _size, File _filePath, SM_Room _room) {
		
		mySize = _size;
		myFrame = _frame;
		myFilePath = _filePath;
		myRoom = _room;
		myWalls = myRoom.getWalls();
		
		super.init();
	}
	
	private void dropAnim() {
		dloX = dloX + ((dTargetMX - dloX) / 5);
		druX = druX + ((dTargetMX - druX) / 5);
		dloY = dloY + ((dTargetMY - dloY) / 5);
		druY = druY + ((dTargetMY - druY) / 5);
		rectMode(CORNERS);
		noFill();
		rect(dloX, dloY, druX, druY);
		
		if( abs(dloX-dTargetMX) < 10 ) dropAnim = false;
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
				
		for( Object w : myWalls.keySet() ) {
			SM_Wall wl = (SM_Wall)myWalls.get(w);
			
			nb = wl.getNavBounds();
			
			if( mx > nb[0] && mx < nb[2] && my > nb[1] && my < nb[3] ) {
				
				wallOver = wl.getWallChar();
				break;
			}
			
		}
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		
		if(dtde.getCurrentDataFlavors()[0] == SM_DataFlavor.SM_AW_Flavor && wallOver != ' ' ) {
			
			setDropAnim( dtde.getLocation().x, dtde.getLocation().y );
			
			try {
				
				String[] arr = (String[])dtde.getTransferable().getTransferData(SM_DataFlavor.SM_AW_Flavor);
				String name = arr[0];
				
				System.out.println("TRYING TO FIRE");
				
						
				WallUpdateRequestEvent e = new WallUpdateRequestEvent(this, name, wallOver, myRoom.getName());
				myRoom.fireUpdateRequest(e);
				
				
				
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
