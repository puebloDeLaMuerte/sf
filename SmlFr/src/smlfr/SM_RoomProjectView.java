package smlfr;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import artworkUpdateModel.WallUpdateRequestEvent;

import com.sun.tools.jdi.LinkedHashMap;

import SMUtils.Lang;
import SMUtils.SM_DataFlavor;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

public class SM_RoomProjectView extends PApplet implements DropTargetListener, DragGestureListener, Transferable, ActionListener {

	SM_Room 									myRoom;
	LinkedHashMap								myWalls;
    private DragSource							ds;
    private JPopupMenu							pMenu;
    private JMenuItem							pMenuRemoveArtwork, pMenuEnterRoom;

	
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
	private SM_Artwork							artOver;
	private boolean								drag;
	private float 								mx;
	private float 								my;
	
	// drop feedback:
	private boolean dropAnim = false;
	private int bgr, bgg,bgb;
	private int dloX, dloY, druX, druY, dTargetMX, dTargetMY;
	private int mX, mY;
	private boolean moveWindow = false;
	int count =0;
	
	public void setup() {
		
		ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);
		
		pMenu = new JPopupMenu();
		pMenuRemoveArtwork = new JMenuItem(Lang.RemoveArtwork);
		pMenuRemoveArtwork.addActionListener(this);
		pMenuEnterRoom = new JMenuItem(Lang.enterRoom);
		pMenuEnterRoom.addActionListener(this);
		pMenu.add(pMenuRemoveArtwork);
		pMenu.add(new JSeparator());
		pMenu.add(pMenuEnterRoom);
		
		bgr = 255;
		bgg = 255;
		bgb = 255;
		dt = new DropTarget(this,this);
		
		PShape gfxPack = loadShape(myFilePath.getAbsolutePath()+"/"+myRoom.getName()+".svg");
		
		wallsActiveGfx = new HashMap<Character, PShape>();
		wallsOverGfx = new HashMap<Character, PShape>();
		
		System.out.println("width:  "+mySize[0]+" height:  "+mySize[1]);
		float xFact = ((float)mySize[0] / 560f);
		float yFact = ((float)mySize[1] / 350f);
		System.out.println("w-fact: "+xFact+" h-fact: "+yFact);
		
		
		greyRoom = gfxPack.getChild(0);
		greyRoom.scale(xFact, yFact);
		int i = 1;
		for( Object w : myWalls.keySet() ) {
			SM_Wall wl = (SM_Wall)myWalls.get(w);
			PShape wActive = gfxPack.getChild(i);
			wActive.scale(xFact, yFact);
			wallsActiveGfx.put(wl.getWallChar(), wActive);
			i++;
			PShape wOver = gfxPack.getChild(i);
			wOver.scale(xFact, yFact);
			wallsOverGfx.put(wl.getWallChar(), wOver);
			
			System.out.println(myRoom.getName()+": wallOverGraphics.put("+wl.getWallChar()+", gfxPack.getChild("+i+"))");
			i++;
		}
		
		background(255);
		
		shape(greyRoom,0,0);

		
		fill(0);
		

	}
	
	public void draw() {

//		System.out.println(myRoom.getName()+" "+count++);
		
		if (!drag) {
			mx = (float) mouseX / (float) mySize[0];
			my = (float) mouseY / (float) mySize[1];
			wallOver = ' ';
			for( Object w : myWalls.keySet() ) {
				SM_Wall wl = (SM_Wall)myWalls.get(w);
				nbnd = wl.getNavBounds();

				if (mx > nbnd[0] && mx < nbnd[2] && my > nbnd[1] && my < nbnd[3]) {

					wallOver = wl.getWallChar();
//					break;
				}

			}
		}
		
		// drop visual feedback
		bgr = bgr + ((255-bgr)/3);
		bgg = bgg + ((255-bgg)/3);
		bgb = bgb + ((255-bgb)/3);
		background(bgr,bgg,bgb);
		if (dropAnim) dropAnim();
		
		
		// draw Artworks
		drawArtworks();
		
		// draw walls / ARtworks + mouseover
		shape(greyRoom,0,0);
		
		// draw MouseOver for Walls
		if(wallOver != ' ') {
			shape(wallsOverGfx.get(wallOver), 0, 0);
		}
		
		if(artOver != null) {
			pushStyle();
			fill(160);
			text(artOver.getTitle(), mouseX, mouseY-10);
			popStyle();
		}
		
//		text( "wallOver: "+wallOver, 20,20  );
//		text( mx+" x "+my, 20,40);

	}
	
	private void drawArtworks() {
		
		boolean lock = false;
		artOver = null;
		
		for( Object w : myWalls.keySet() ) {
			SM_Wall wl = (SM_Wall)myWalls.get(w);
			
			// debug: draw wall Pos
//			line( wl.getNavPos()[0] * width, wl.getNavPos()[1] * height, wl.getNavPos()[2] * width, wl.getNavPos()[3] * height );
			
			
			if( wl.hasArtworks().length > 0 ) {

				//PShape s = wallsActiveGfx.get(wl.getWallChar());

				pushStyle();
				rectMode(CORNERS);
				noStroke();
				for(SM_Artwork aw : wl.hasArtworks()) {
					
					float[] icnBounds = getArtworkIconBounds(aw, wl);
					
					
					
//					System.out.println( "for wall: "+wl.getWallName()+"  -  aw: "+aw.getName()+" orient: "+wl.getOrientation());
					boolean awMouseOver = false;
					switch (wl.getOrientation()) {
					case 0:
						if (!lock && mouseX < icnBounds[0]+15 && mouseX > icnBounds[2]-15 && mouseY < icnBounds[1]+15 && mouseY > icnBounds[3]-15) {
							icnBounds[0] += 5;
							icnBounds[1] += 5;
							icnBounds[2] -= 5;
							icnBounds[3] -= 5;
							awMouseOver = true;
							artOver = aw;
							lock = true;
						}
						break;
					case 1:
						if (!lock && mouseX < icnBounds[0]+15 && mouseX > icnBounds[2]-15 && mouseY < icnBounds[1]+15 && mouseY > icnBounds[3]-15) {
							icnBounds[0] += 5;
							icnBounds[1] += 5;
							icnBounds[2] -= 5;
							icnBounds[3] -= 5;
							awMouseOver = true;
							artOver = aw;
							lock = true;
						}
						break;
					case 2:
						if (!lock && mouseX > icnBounds[0]-15 && mouseX < icnBounds[2]+15 && mouseY > icnBounds[1]-15 && mouseY < icnBounds[3]+15) {
							icnBounds[0] -= 5;
							icnBounds[1] -= 5;
							icnBounds[2] += 5;
							icnBounds[3] += 5;
							awMouseOver = true;
							artOver = aw;
							lock = true;
						}
						break;
					case 3:
						if (!lock && mouseX > icnBounds[0]-15 && mouseX < icnBounds[2]+15 && mouseY > icnBounds[1]-15 && mouseY < icnBounds[3]+15) {
							icnBounds[0] -= 5;
							icnBounds[1] -= 5;
							icnBounds[2] += 5;
							icnBounds[3] += 5;
							awMouseOver = true;
							artOver = aw;
							lock = true;
						}
						break;
					}
					
					if(awMouseOver){
						fill(250, 10, 30, 80);
//						rect( icnBounds[0]-3f, icnBounds[1]+3f, icnBounds[2]+3f, icnBounds[3]-3f );
						rect( icnBounds[0], icnBounds[1], icnBounds[2], icnBounds[3]);
//						stroke(0);
//						fill(0);
//						text("bounds "+icnBounds[0]+" / "+icnBounds[1]+" / "+icnBounds[2]+" / "+icnBounds[3], 20,20);


					} else {
						fill(250, 10, 20, 160);
						rect( icnBounds[0], icnBounds[1], icnBounds[2], icnBounds[3] );
					}
					
					
				}
				popStyle();
				
				//shape(s,0,0);
			}
			
			
		}
	}
	
	private float[] getArtworkIconBounds(SM_Artwork _aw, SM_Wall _wl) {
		
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
			normalizedPosY2 = _wl.getNavPos()[1] - normalOffsetY*2;
			normalizedPosY1 = _wl.getNavPos()[1] - normalOffsetY*1;
			break;
		case 1:
			normalizedPosX2 = _wl.getNavPos()[0] + normalOffsetX*1;
			normalizedPosX1 = _wl.getNavPos()[0] + normalOffsetX*2;
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
		
		
		return new float[] { normalizedPosX1 * (float)width , normalizedPosY1 * (float)height, normalizedPosX2 * (float)width, normalizedPosY2 * (float)height };
	
		
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
				String originRoom = arr[1];
				char originWall = arr[2].charAt(arr[2].length()-1);

				// Artwork already on this wall?
				if( myRoom.hasArtworkOnWall(name, wallOver) ){
					System.out.println("rejected by room");
					bgg = 0;
					bgb = 0;
					dtde.rejectDrop();
					wallOver = ' ';
					return;
				}
				
				// Artwork too big for this wall?
				SM_Wall w = (SM_Wall)myWalls.get(wallOver); 
				if( w.getWidth() < myRoom.getArtworkFromBase(name).getWidth()  ) {
					
//					int i = javax.swing.JOptionPane.showConfirmDialog(this, Lang.artworkTooBigForWall_1 + myRoom.getArtworkFromBase(name).getWidth() + Lang.artworkTooBigForWall_2 + w.getWidth() + Lang.artworkTooBigForWall_3);
					javax.swing.JOptionPane.showMessageDialog(this, Lang.artworkTooBigForWall_1 + myRoom.getArtworkFromBase(name).getWidth() + Lang.artworkTooBigForWall_2 + w.getWidth() + Lang.artworkTooBigForWall_3, "Artwork doesn't fit!", javax.swing.JOptionPane.OK_OPTION);
					bgg = 0;
					bgb = 0;
					dtde.rejectDrop();
					wallOver = ' ';
					return;
				}
				
				System.out.println("passed by room");
				
//				WallUpdateRequestEvent e = new WallUpdateRequestEvent(this, name, wallOver, myRoom.getName());
				
				System.out.println("firing this update Request event: "+name+" "+ wallOver+" "+ myRoom.getName()+" "+ originRoom+" "+ originWall);
				
				WallUpdateRequestEvent e = new WallUpdateRequestEvent(this, name, wallOver, myRoom.getName(), originRoom, originWall);

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

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
			String[] t = new String[] { artOver.getName(), myRoom.getName(), artOver.getWall() ,"SM_Artwork" };
		return t;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] dfs = new DataFlavor[1];
		dfs[0] = SM_DataFlavor.SM_AW_Flavor;
		return dfs;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		if (artOver != null) {
			Cursor cursor = null;
			if (dge.getDragAction() == DnDConstants.ACTION_COPY) {
				cursor = DragSource.DefaultCopyDrop;
				//            cursor = DragSource.DefaultLinkDrop;
			}
			dge.startDrag(cursor, this);
			
		}
		else {
			
		}
		
	}

	public void mousePressed() {
		
		if( artOver != null ) pMenuRemoveArtwork.setEnabled(true);
		else pMenuRemoveArtwork.setEnabled(false);
		
		if( mouseButton == RIGHT ) pMenu.show(this, mouseX, mouseY);
		mX = MouseInfo.getPointerInfo().getLocation().x;
		mY = MouseInfo.getPointerInfo().getLocation().y;
	}

	public void mouseDragged() {
		
		if(moveWindow) {
			int deltaX = myFrame.getLocation().x+MouseInfo.getPointerInfo().getLocation().x-mX;
			int deltaY = myFrame.getLocation().y+MouseInfo.getPointerInfo().getLocation().y-mY;
			
			myFrame.setLocation(deltaX, deltaY);
			mX = MouseInfo.getPointerInfo().getLocation().x;
			mY = MouseInfo.getPointerInfo().getLocation().y;
		}
	}
	
	public void keyPressed() {
		if( keyCode == SHIFT) {
			moveWindow = true;
		}
	}
	
	public void keyReleased() {
		if( keyCode == SHIFT ) {
			moveWindow = false;
		}
	}
	
	public void mouseExited() {
		wallOver = ' ';
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if( artOver != null && e.getActionCommand().equalsIgnoreCase(Lang.RemoveArtwork)) {
			System.out.println("now removing this artwork: "+ artOver.getName());
			
			WallUpdateRequestEvent r = new WallUpdateRequestEvent(this, artOver.getName(), ' ', "Library", myRoom.getName(), artOver.getWallChar());

			myRoom.fireUpdateRequest(r);
		} else if( e.getActionCommand().equalsIgnoreCase(Lang.enterRoom)) {
			
			myRoom.requestRoomEnter();
			
		}
		
	}

	public void dispose() {
		
		myFrame.dispose();
		super.dispose();
	}
}
