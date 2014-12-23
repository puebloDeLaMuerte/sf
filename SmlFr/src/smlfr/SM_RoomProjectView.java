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
import processing.core.PVector;

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
	private float[]								nbnd;
	private float[]								npos;
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
				nbnd = wl.getNavBounds();

				// debug: draw navBounds
//				if(w == 'M') {
//					rectMode(CORNERS);
//					stroke(0);
//					rect(nb[0]*mySize[0], nb[1]*mySize[1], nb[2]*mySize[0], nb[3]*mySize[1]);
//				}
				
				if (mx > nbnd[0] && mx < nbnd[2] && my > nbnd[1] && my < nbnd[3]) {

					wallOver = wl.getWallChar();
					break;
				}

			}
		}
		
		// drop visual feedback
		bgr = bgr + ((255-bgr)/3);
		bgg = bgg + ((255-bgg)/3);
		bgb = bgb + ((255-bgb)/3);
		background(bgr,bgg,bgb);
		if (dropAnim) dropAnim();
		
		// draw walls
		shape(greyRoom,0,0);
		
		// draw Artworks
		for( Object w : myWalls.keySet() ) {
			SM_Wall wl = (SM_Wall)myWalls.get(w);
			
			// debug: draw wall Pos
//			line( wl.getNavPos()[0] * width, wl.getNavPos()[1] * height, wl.getNavPos()[2] * width, wl.getNavPos()[3] * height );
			
			if( wl.hasArtworks().length > 0 ) {

				//PShape s = wallsActiveGfx.get(wl.getWallChar());

				pushStyle();
				fill(250, 10, 30);
				noStroke();
				for(SM_Artwork aw : wl.hasArtworks()) {
					
					drawArtworkIcon(aw, wl);
					
				}
				popStyle();
				
				//shape(s,0,0);
			}
			
			
		}
		
		
		
		if(wallOver != ' ') {
			shape(wallsOverGfx.get(wallOver), 0, 0);
		}
		
//		text( wallOver, 20,20  );
//		text( mx+" x "+my, 20,40);

	}
	
	private void drawArtworkIcon(SM_Artwork _aw, SM_Wall _wl) {
		
		int orientation = _wl.getOrientation();
		float normalOffsetY = 0.008f;
		float normalOffsetX = ((float)height/(float)width) * normalOffsetY;
		float normalizedSize =0;
		
		float normalizedPosX1=0;
		float normalizedPosY1=0;
		float normalizedPosX2=0;
		float normalizedPosY2=0;
		
		if( orientation == 0 || orientation == 2 ) {
			
			normalizedSize   = map(_aw.getWidth(),        0, _wl.getWidth(), 0, _wl.getNavPos()[2] - _wl.getNavPos()[0]);
			normalizedPosX1  = map(_aw.getPosInWall()[0], 0, _wl.getWidth(), _wl.getNavPos()[0], _wl.getNavPos()[2]); 
			normalizedPosX2  = normalizedPosX1+normalizedSize;
			
		}else if( orientation == 1 || orientation == 3) {
			
			normalizedSize   = map(_aw.getWidth(),        0, _wl.getWidth(), 0, _wl.getNavPos()[3] -  _wl.getNavPos()[1]);
			normalizedPosY1  = map(_aw.getPosInWall()[0], 0, _wl.getWidth(), _wl.getNavPos()[1], _wl.getNavPos()[3]);
			normalizedPosY2  = normalizedPosY1+normalizedSize;
		}
		
		switch (orientation) {
		case 0:
			normalizedPosY1 = _wl.getNavPos()[1] - normalOffsetY*2;
			normalizedPosY2 = _wl.getNavPos()[1] - normalOffsetY*1;
			break;
		case 1:
			normalizedPosX1 = _wl.getNavPos()[0] + normalOffsetX*1;
			normalizedPosX2 = _wl.getNavPos()[0] + normalOffsetX*2;
			break;
		case 2:
			normalizedPosY1 = _wl.getNavPos()[1] + normalOffsetY*2;
			normalizedPosY2 = _wl.getNavPos()[1] + normalOffsetY*1;
			break;
		case 3:
			normalizedPosX1 = _wl.getNavPos()[0] - normalOffsetX*1;
			normalizedPosX2 = _wl.getNavPos()[0] - normalOffsetX*2;
			break;
			
		default:
			break;
		}
		
		rectMode(CORNERS);
		rect( normalizedPosX1 * width , normalizedPosY1 * height, normalizedPosX2 * width, normalizedPosY2 * height );
		
	
		
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
			
			nbnd = wl.getNavBounds();
			
			if( mx > nbnd[0] && mx < nbnd[2] && my > nbnd[1] && my < nbnd[3] ) {
				
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

				if( myRoom.hasArtwork(name, wallOver) ){
					System.out.println("rejected by room");
					bgg = 0;
					bgb = 0;
					dtde.rejectDrop();
					wallOver = ' ';
					return;
				}
				
				System.out.println("passed by room");
				
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
