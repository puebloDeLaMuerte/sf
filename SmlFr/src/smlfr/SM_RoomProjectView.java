package smlfr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
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
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;

import org.apache.poi.hssf.util.HSSFColor.BLACK;



import SMUtils.Lang;
import SMUtils.SM_DataFlavor;
import SMUtils.WallColorChooser;

import processing.core.PApplet;
import processing.core.PShape;
import processing.event.MouseEvent;
import updateModel.UpdateEvent;
import updateModel.ArtworkUpdateRequestEvent;
import updateModel.UpdateType;
import updateModel.WallColorUpdateRequestEvent;
import updateModel.WallUpdateRequestEvent;

public class SM_RoomProjectView extends PApplet implements DropTargetListener, DragGestureListener, Transferable, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -797918064517774209L;
	
	SmlFr										base;
	
	SM_Room 									myRoom;
	LinkedHashMap<Character, SM_Wall>			myWalls;
    private DragSource							ds;
    private JPopupMenu							pMenu;
    private JMenuItem							pMenuChangeColor, pMenuRemoveArtwork, pMenuEnterExitRoom, quitSF, savePr, export;

	
	// utils
//	private JFrame								myFrame;
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
	private SM_Artwork							menuArtOver;
	private boolean								drag;
	private float 								mx;
	private float 								my;
	
	private float xFact, yFact;
	
	private boolean setupRun = false;
	
	// drop feedback:
	private boolean dropAnim = false;
	private int bgr, bgg,bgb;
	private int dloX, dloY, druX, druY, dTargetMX, dTargetMY;
	private int mX, mY;
	private boolean moveWindow = false;
//	int count =0;
	
	public SM_RoomProjectView(int w, int h, SmlFr base) {
		super();
		mySize = new int[2];
		mySize[0] = w;
		mySize[1] = h;
		
		this.base = base;
	}
	
	public void setup() {
				

		
		ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);
		
		pMenu = new JPopupMenu();
		pMenuChangeColor = new JMenuItem(Lang.changeColor);
		pMenuChangeColor.addActionListener(this);
		pMenuRemoveArtwork = new JMenuItem(Lang.RemoveArtwork);
		pMenuRemoveArtwork.addActionListener(this);
		pMenuEnterExitRoom = new JMenuItem(Lang.enterRoom);
		pMenuEnterExitRoom.addActionListener(this);
		quitSF = new JMenuItem(Lang.quitSF);
		quitSF.addActionListener(this);
		savePr = new JMenuItem(Lang.saveProject);
		savePr.addActionListener(this);
		export = new JMenuItem(Lang.exportMenu);
		export.addActionListener(this);
		
		pMenu.add(pMenuRemoveArtwork);
		pMenu.add(pMenuChangeColor);
		pMenu.add(new JSeparator());
		pMenu.add(savePr);
		pMenu.add(export);
		pMenu.add(new JSeparator());
		pMenu.add(pMenuEnterExitRoom);
		pMenu.add(new JSeparator());
		pMenu.add(quitSF);
		
		bgr = 255;
		bgg = 255;
		bgb = 255;
		dt = new DropTarget(this,this);
		
		PShape gfxPack = loadShape(myFilePath.getAbsolutePath()+"/"+myRoom.getName()+".svg");
		
		wallsActiveGfx = new HashMap<Character, PShape>();
		wallsOverGfx = new HashMap<Character, PShape>();
		
		xFact = ((float)mySize[0] / 560f);
		yFact = ((float)mySize[1] / 350f);
		
		
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
			
			i++;
		}
		
		background(255);
		
		shape(greyRoom,0,0);

		fill(0);

//		redraw();
		setupRun = true;
	}
	
	public boolean isSetupRun() {
		return setupRun;
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
		
		if(artOver != null ) {
			
			if( ! artOver.hasThumb() ) {
				artOver.setThumb( loadImage(myRoom.getThumbPath(artOver.getName()).getAbsolutePath() ));
			}
			
			int thumbwidth = artOver.getThumb().width;

			pushStyle();
			fill(100);
			text( artOver.getName(), 5 + (( width + thumbwidth)/2) - 50, 18);
			text(artOver.getTitle(), 5 + (( width + thumbwidth)/2) - 50, 34);
			image(artOver.getThumb(), ((width-thumbwidth)/2) - 50 , 5);
			popStyle();
		}
		

		
		pushStyle();
		fill(180);
		textSize(15);
		text(myRoom.getRealName(), 10, 20);
		textSize(12);
		noFill();
		stroke(210);
		rect(0,0,width, height);
		popStyle();
		

	}
	
	private void drawArtworks() {
		
		boolean lock = false;
		artOver = null;
		
		for( Object w : myWalls.keySet() ) {
			SM_Wall wl = (SM_Wall)myWalls.get(w);
			
			// debug: draw wall Pos
//			line( wl.getNavPos()[0] * width, wl.getNavPos()[1] * height, wl.getNavPos()[2] * width, wl.getNavPos()[3] * height );
			
			
			if( wl.getArtworksArray().length > 0 ) {

				//PShape s = wallsActiveGfx.get(wl.getWallChar());

				pushStyle();
				rectMode(CORNERS);
				noStroke();
				for(SM_Artwork aw : wl.getArtworksArray()) {
					
					
					
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
						rect( icnBounds[0], icnBounds[1], icnBounds[2], icnBounds[3]);

						
						
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
			
			normalizedSize   = map(_aw.getTotalWidth(),        0, _wl.getWidth(), 0, _wl.getNavPos()[2] - _wl.getNavPos()[0]);
			normalizedPosX1  = map(_aw.getTotalWallPos()[0], 0, _wl.getWidth(), _wl.getNavPos()[0], _wl.getNavPos()[2]); 
			normalizedPosX2  = normalizedPosX1+normalizedSize;
			
		}else if( orientation == 1 || orientation == 3) {
			
			normalizedSize   = map(_aw.getTotalWidth(),        0, _wl.getWidth(), 0, _wl.getNavPos()[3] -  _wl.getNavPos()[1]);
			normalizedPosY1  = map(_aw.getTotalWallPos()[0], 0, _wl.getWidth(), _wl.getNavPos()[1], _wl.getNavPos()[3]);
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
	
	public void init(File _filePath, SM_Room _room) {
		
		
		myFilePath = _filePath;
		myRoom = _room;
		myWalls = myRoom.getWalls();
		
		super.init();

	}
	
//	public void init(JFrame _frame, int[] _size, File _filePath, SM_Room _room) {
//		
//		mySize = _size;
//		myFrame = _frame;
//		myFilePath = _filePath;
//		myRoom = _room;
//		myWalls = myRoom.getWalls();
//		
//		super.init();
//	}
	
	public void originalColorCallback() {

		WallColorUpdateRequestEvent e = null;

		e = new WallColorUpdateRequestEvent(this, myRoom.getName());

		myRoom.fireUpdateRequest(e);
	}
	
	public void roomColorCallback(int _color, boolean _preview) {
		
		WallColorUpdateRequestEvent e = null;

		e = new WallColorUpdateRequestEvent(this, myRoom.getName(), _color, _preview);

		myRoom.fireUpdateRequest(e);
	}
	
	public void wallColorCallback(Integer _color, char wallChar, boolean _preview) {
		
		WallColorUpdateRequestEvent e = null;
	
		e = new WallColorUpdateRequestEvent(this, myRoom.getName(), (Character) wallChar, _color, _preview);
		
		myRoom.fireUpdateRequest(e);
	}
	
	public void deleteWallColorCallback( char wallChar) {
		
		WallColorUpdateRequestEvent e = null;
		
		e = new WallColorUpdateRequestEvent(this, myRoom.getName(), (Character) wallChar );
		
		myRoom.fireUpdateRequest(e);
	}
	
	public HashMap<Character, PShape> getWallsOverGfx() {
		return wallsOverGfx;
	}
	
	
	public HashMap<Character, PShape> getWallsActiveGfx() {
		return wallsActiveGfx;
	}
	
	public PShape getGreyWalls() {
		if( greyRoom != null ) return greyRoom;
		else return null;
	}
	
	public char getWallOverChar() {
		return wallOver;
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
					bgg = 0;
					bgb = 0;
					dtde.rejectDrop();
					wallOver = ' ';
					return;
				}
				
				// Artwork too big for this wall?
				SM_Wall w = (SM_Wall)myWalls.get(wallOver); 
				if( w.getWidth() < myRoom.getArtworkFromBase(name).getTotalWidth()  ) {
					
//					int i = javax.swing.JOptionPane.showConfirmDialog(this, Lang.artworkTooBigForWall_1 + myRoom.getArtworkFromBase(name).getWidth() + Lang.artworkTooBigForWall_2 + w.getWidth() + Lang.artworkTooBigForWall_3);
					javax.swing.JOptionPane.showMessageDialog(this, Lang.artworkTooBigForWall_1 + myRoom.getArtworkFromBase(name).getTotalWidth() + Lang.artworkTooBigForWall_2 + w.getWidth() + Lang.artworkTooBigForWall_3, "Artwork doesn't fit!", javax.swing.JOptionPane.OK_OPTION);
					bgg = 0;
					bgb = 0;
					dtde.rejectDrop();
					wallOver = ' ';
					return;
				}
				
				
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

	public void setMenuExit() {
		pMenuEnterExitRoom.setText(Lang.exitRoom);
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
		
		if( artOver != null ) {
			pMenuRemoveArtwork.setEnabled(true);
			menuArtOver = artOver;
		}
		else {
			pMenuRemoveArtwork.setEnabled(false);
			menuArtOver = null;
		}
		
		if( mouseButton == RIGHT ) {
			if( myRoom.getSaveDirty() ) {
				savePr.setEnabled(true);
			} else {
				savePr.setEnabled(false);
			}
			pMenu.show(this, mouseX, mouseY);
		}
		mX = MouseInfo.getPointerInfo().getLocation().x;
		mY = MouseInfo.getPointerInfo().getLocation().y;
	}

	public void mouseDragged() {
		
//		if(moveWindow) {
//			int deltaX = myFrame.getLocation().x+MouseInfo.getPointerInfo().getLocation().x-mX;
//			int deltaY = myFrame.getLocation().y+MouseInfo.getPointerInfo().getLocation().y-mY;
//			
//			myFrame.setLocation(deltaX, deltaY);
//			mX = MouseInfo.getPointerInfo().getLocation().x;
//			mY = MouseInfo.getPointerInfo().getLocation().y;
//		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if(e.getCount() > 1) {
			if ( this.getClass() == smlfr.SM_RoomProjectView.class) {
				myRoom.requestRoomEnter();
			}
		}
	}
	
	public void mouseExited() {
		mouseX = 0;
		mouseY = 0;	
	}
	
	public void keyPressed() {
//		if( keyCode == SHIFT) {
//			moveWindow = true;
//		}
		if( keyCode == ESC ) {
			key = 0;
		}
	}
	
	public void keyReleased() {
//		if( keyCode == SHIFT ) {
//			moveWindow = false;
//		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if( e.getActionCommand().equalsIgnoreCase(Lang.changeColor)) {
		
			int wallColor = 0;
			if( wallOver != ' ' &&  myWalls.get((Character)wallOver).hasColor() ) {
				wallColor = myWalls.get((Character)wallOver).getColor();
			}
			
			new WallColorChooser(this, wallOver, myRoom.getRoomColor(), wallColor);
			
			
		} else if( menuArtOver != null && e.getActionCommand().equalsIgnoreCase(Lang.RemoveArtwork)) {
			
			WallUpdateRequestEvent r = new WallUpdateRequestEvent(this, menuArtOver.getName(), ' ', "Library", myRoom.getName(), menuArtOver.getWallChar());

			myRoom.fireUpdateRequest(r);
		} else if( e.getActionCommand().equalsIgnoreCase(Lang.enterRoom)) {
			
			myRoom.requestRoomEnter();
			
		} else if( e.getActionCommand().equalsIgnoreCase(Lang.exitRoom)) {
			
			myRoom.requestRoomExit();
			
		} else if( e.getActionCommand().equalsIgnoreCase(Lang.quitSF)) {
			myRoom.requestQuit();
		} else if( e.getActionCommand().equalsIgnoreCase(Lang.saveProject)){
			boolean saved = myRoom.requestSave();
			
			if(saved) javax.swing.JOptionPane.showMessageDialog(null, Lang.saved);
			
		} else if( e.getActionCommand().equalsIgnoreCase(Lang.exportMenu)){
			
			myRoom.exportMeasures(myWalls.keySet().toArray(new Character[myWalls.keySet().size()]));
			
		}
		
	}

	
	

	public void dispose() {
		frame.dispose();
//		myFrame.dispose();
		super.dispose();
	}

	

}
