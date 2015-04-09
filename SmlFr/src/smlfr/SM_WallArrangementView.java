package smlfr;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import SMUtils.FrameStyle;
import SMUtils.Lang;
import SMUtils.SM_DataFlavor;
import SMUtils.SM_Frames;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import updateModel.UpdateEvent;
import updateModel.UpdateListener;
import updateModel.ArtworkUpdateRequestEvent;
import updateModel.WallUpdateRequestEvent;

public class SM_WallArrangementView extends PApplet implements DropTargetListener, UpdateListener, ActionListener {

/**
	 * 
	 */
	private static final long serialVersionUID = -8724642767602580803L;

	private SM_Wall			myWall; 
	private SM_ViewManager  vm;
	private Dimension		mySize;
	private PGraphics		wlGfx;

	private boolean			ready = false;

	private float			scale;
	private float 			xOffsetPx, yOffsetPx;
	private int				myMidHeight;
	
	private SM_Artwork		awOver, menuAW;
	private boolean 		awDrag = false;
	private boolean			horizontalLoc = false;
	private PVector			awDragOfset;
	private PVector			awDragStart;
	
	private JPopupMenu		pMenu;
	private JMenuItem		putBack, snapToMidHeight, close;
	private JMenuItem[]		frameStyles;
	private JMenu			editArtwork;
	private JMenuItem		editMeasurements;
	
	private DropTarget		dt;
	
	private boolean			artworkUpdatePending = true;
	private boolean			firstTime = true;
	
	int 					shadowAmount = 8;
	int 					shadowOfsetAmount = 5;
	
	int count = 0;
	
	public SM_WallArrangementView(SM_Wall _myW, Dimension _size, SM_ViewManager _vm) {
		super();
		myWall = _myW;
		dt = new DropTarget(this,this);
		myMidHeight = myWall.getMidHeight();
		
		initMenu();
		
		
		for( String a : myWall.getArtworks().keySet() ) {
			SM_Artwork aw = myWall.getArtworks().get(a);
			PImage awimg = loadImage(aw.getFilePath().getAbsolutePath());
			aw.setGfx( awimg  );
		}
		
		vm = _vm;
		vm.registerUpdateListener(this);
		
//		border = 100f;
		xOffsetPx = 0f;
		yOffsetPx = 0f;
		
		// Calculate Window Size from Available Space:
		
		float aspect = (float)myWall.getHeight() / (float)myWall.getWidth();
		int resultHeight;
		int resultWidth;

		System.out.println("\n\navailable space: "+_size.width+" x "+_size.height);
		
		// Querformat
		if( aspect <= 1) {   
			
			resultHeight = (int)(_size.width * aspect);
			resultWidth = _size.width;
			
			if( resultHeight > _size.height) {
				resultWidth = (int)(_size.height / aspect);
				resultHeight = _size.height;
			}
		} else
		
		// Hochformat
		{
			resultWidth = (int)(_size.height / aspect);
			resultHeight = _size.height;
		}

		
		mySize = new Dimension(resultWidth, resultHeight);
	}
	
	private void initMenu() {
		pMenu = new JPopupMenu();
		
		putBack = new JMenuItem(Lang.RemoveArtwork);
		putBack.addActionListener(this);
		putBack.setEnabled(false);
		
		editArtwork = new JMenu(Lang.editArtwork);
		editArtwork.setEnabled(true);
		int i=0;
		frameStyles = new JMenuItem[ FrameStyle.values().length ];
		for( FrameStyle fst : SMUtils.FrameStyle.values() ) {
			
			String style = fst.toString(); 

			frameStyles[i] = new JMenuItem(style);
			frameStyles[i].addActionListener(this);
			frameStyles[i].setEnabled(true);
			editArtwork.add(frameStyles[i]);
			i++;
		}

		editMeasurements = new JMenuItem(Lang.editMeasurements);
		editMeasurements.addActionListener(this);
											editMeasurements.setEnabled(false);
		editArtwork.add(new JSeparator());
		editArtwork.add(editMeasurements);
		
		snapToMidHeight = new JMenuItem(Lang.snapToMidHeight);
		snapToMidHeight.addActionListener(this);
		snapToMidHeight.setEnabled(false);
		
		close = new JMenuItem(Lang.closeWall);
		close.addActionListener(this);
		close.setEnabled(true);
		
		pMenu.add(snapToMidHeight);
		pMenu.add(putBack);
		pMenu.add(editArtwork);
		pMenu.add(new JSeparator());
		pMenu.add(close);
		
	}
	
	public Dimension getSize() {
		return mySize;
	}
	
	
	public void setup() {
		scale = ((float)mySize.width ) / ((float)myWall.getWidth());
		wlGfx = createGraphics(width, height);
//		bg = createGraphics(mySize.width, mySize.height);
//		bg.beginDraw();
//		bg.background(255);
//		for(int intx=0; intx<mySize.width; intx++) {
//			for(int inty=0; inty<mySize.height; inty++) {
//				bg.stroke(random(248,255));
//				bg.point(intx,inty);
//			}
//		}
//		bg.endDraw();

//		wlGfx = createGraphics(mySize.width, mySize.height);
		
//		rectMode(CORNER);
		frameRate(15);
	}
	
	public void draw() {
		
		loadMissingAWGraphics();
		
		
		background(230);
	
		// Mittelhšhe
		pushStyle();
		stroke(150);
		line(0, wptos(0,myMidHeight, scale).y, wptos(myWall.getWidth(),0,scale).x, wptos(0,myMidHeight, scale).y );
		popStyle();
		

		// DRAW wall
		
		image(drawWall( 0, 0 ),0,0);
		
		
		// DRAW mouseOver
		
		if( awOver != null) {
			PVector totalPos = wptos( new PVector(awOver.getTotalWallPos()[0], awOver.getTotalWallPos()[1]), scale );
			PVector totalSize = astos( new PVector(awOver.getTotalWidth(), awOver.getTotalHeight()), scale);
			
			pushStyle();
			fill(200,100,100,80);
			noStroke();
			rect( totalPos.x , totalPos.y, totalSize.x, totalSize.y );
			popStyle();
			
		}
		
		// DRAW drag
		
		if( awOver != null && awDrag ) {
			PVector wh = astos( new PVector(awOver.getTotalWidth(), awOver.getTotalHeight()), scale);
			
			float y;
			if( horizontalLoc ) {
				y = wptos(0,awOver.getTotalWallPos()[1], scale).y;
			} else {
				y = mouseY+awDragOfset.y;
			}
			
			pushStyle();
			noFill();
			rect(mouseX+awDragOfset.x, y, wh.x, wh.y);
			popStyle();
		}
		
		// DRAW selected
		
		if(myWall.hasArtworks().length > 0 ) {
			for( SM_Artwork a : myWall.hasArtworks() ) {
				
				if(a != null && a.isSelected()) {
				
					PVector totalPos = wptos( new PVector(a.getTotalWallPos()[0], a.getTotalWallPos()[1]), scale );
					PVector totalSize = astos( new PVector(a.getTotalWidth(), a.getTotalHeight()), scale);
				
					pushStyle();
					strokeWeight(3);
					stroke(20,50,200,80);
					noFill();
					rect( totalPos.x -5, totalPos.y-5, totalSize.x+9, totalSize.y+9 );
					popStyle();
				
				}
			}
		}
		ready = true;
		if( firstTime ) {
			vm.requestRendererUpdate(myWall.getWallChar());
			firstTime = false;
		}
	}
	
	public synchronized PGraphics drawWall( int _mode, int shadowOfset) {
		
		float drawScale;
		
//		wlGfxReady = false;
		loadMissingAWGraphics();
		
		PGraphics gfx;
		
		if( _mode == 0 ) {
			gfx = wlGfx;
			drawScale = scale;
		}
		else{
			gfx = createGraphics(width, height);
			drawScale = ((float)gfx.width ) / ((float)myWall.getWidth());
		}
		
		
		gfx.clear();
		gfx.beginDraw();
		
		
		// DRAW Schatten
		
		if( _mode == 1 ) {
			if(myWall.hasArtworks().length > 0 ) {

				if(!awDrag) awOver = null;

				for( SM_Artwork a : myWall.hasArtworks() ) {

					int[] tmpPos = a.getTotalWallPos();
					PVector totalPos = wptos( new PVector(tmpPos[0]-shadowAmount-(shadowOfsetAmount * shadowOfset), tmpPos[1]-shadowAmount-(shadowAmount*2)), drawScale );
					PVector totalSize = astos( new PVector(a.getTotalWidth()+(shadowAmount*2), a.getTotalHeight()+(shadowAmount*0.5f)), drawScale);

					gfx.pushStyle();
					gfx.fill(50);
					gfx.rect( totalPos.x, totalPos.y, totalSize.x, totalSize.y );
					gfx.popStyle();

				}
			}

			gfx.filter(BLUR, 3);
		}
		
//		gfx.background(50,90,10,100);
		if(myWall.hasArtworks().length > 0 ) {
			
			if(!awDrag) awOver = null;
			
			for( SM_Artwork a : myWall.hasArtworks() ) {

				
				int[] tmpPos = a.getTotalWallPos();
				PVector totalPos = wptos( new PVector(tmpPos[0], tmpPos[1]), drawScale );

				PVector totalSize = astos( new PVector(a.getTotalWidth(), a.getTotalHeight()), drawScale);
				
				
				int[] tmpPos2 = a.getPptWallPos();
				PVector pptPos = wptos( new PVector(tmpPos2[0], tmpPos2[1]), drawScale );
				
				int[] tmpPos3= a.getPptSize();
				PVector pptSize = astos(new PVector(tmpPos3[0], tmpPos3[1]), drawScale);
				
				int[] tmpPos4 = a.getArtworkWallPos();
				PVector artworkPos = wptos( new PVector(tmpPos4[0], tmpPos4[1]), drawScale );
				
				int[] tmpPos5 = a.getArtworkSize();
				PVector artworkSize = astos(new PVector(tmpPos5[0], tmpPos5[1]), drawScale);
				
				if( !awDrag ) {
					if( mouseX > totalPos.x && mouseX < (totalPos.x + totalSize.x) ) {
						if( mouseY > totalPos.y && mouseY < (totalPos.y + totalSize.y) ) {
							awOver = a;
						}
					}
				}
				
				
				gfx.fill(230,230,230,100);
				
				int shadowFact = 5;
				
				// draw total (frame)
//				gfx.rect( totalPos.x-1, totalPos.y-1, totalSize.x+1, totalSize.y+1 );
//				gfx.image(shadowGfx, totalPos.x-(artworkSize.y/shadowFact), totalPos.y-(artworkSize.x/shadowFact), totalSize.x+(artworkSize.y/shadowFact), totalSize.y+(artworkSize.x/shadowFact));

				if(a.hasFrame()) {
					gfx.image(a.getFrameGfx(), totalPos.x, totalPos.y, totalSize.x, totalSize.y);
				}
				
				// draw ppt
				if(a.hasPassepartout()) {
					gfx.noStroke();
					gfx.pushStyle();
					gfx.fill(200,190,170,255);
					gfx.rect(pptPos.x, pptPos.y, pptSize.x, pptSize.y);
					gfx.popStyle();
				}
				// draw artwork
				gfx.image(a.getGfx(), artworkPos.x, artworkPos.y, artworkSize.x, artworkSize.y);
				g.removeCache(gfx);

				
				
			
				
			}
		}
		gfx.endDraw();
		ready = true;
		if(_mode == 1) artworkUpdatePending = false;
		return gfx;
	}
	
	
	// artwork Size to Screen
	private PVector astos(int _inX, int _inY, float scl) {
		return wptos(new PVector(_inX, _inY), scl );
	}
	private PVector astos(PVector _inpos, float scl) {

		_inpos.mult(scl);		
		return _inpos;
	}
	
	// wall Position to Screen
	private PVector wptos(int _inX, int _inY, float scl) {
		return wptos(new PVector(_inX, _inY), scl );
	}
	private PVector wptos(PVector _inpos, float scl) {
			
		_inpos.mult(scl);
		_inpos = invLogicScaled(_inpos, scl);
				
		return _inpos;
	}
	
	// WallPsoitionLogic to ScreenLogic
	private PVector invLogicScaled(int _wX, int _wY, float scl){
		return invLogicScaled(new PVector(_wX, _wY), scl);
	}
	private PVector invLogicScaled(PVector _inpos, float scl) {
		
		return new PVector(_inpos.x, myWall.getHeight()*scl-_inpos.y);
	}

	// ScreenLogic to WallPositionLogic
	private PVector ptowp(int _wX, int _wY, float scl){
		return ptowp(new PVector(_wX, _wY), scl);
	}
	private PVector ptowp(PVector _inpos, float scl) {
		
		/// UNTESTED!!!!
//		_inpos.sub(new PVector(xOffsetPx, yOffsetPx));
		
		_inpos.div(scl);
		
		return new PVector(_inpos.x, myWall.getHeight()-_inpos.y);
	}

	private boolean checkMouseOver(SM_Artwork a) {
		
		PVector pos = wptos( new PVector(a.getTotalWallPos()[0], a.getTotalWallPos()[1]), scale );
		PVector sze = astos( new PVector(a.getTotalWidth(), a.getTotalHeight()), scale);
		
		
		if( mouseX > pos.x && mouseX < (pos.x + sze.x) ) {
			if( mouseY > pos.y && mouseY < (pos.y + sze.y) ) {
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	private void deselectAll() {
		
		for( SM_Artwork a : myWall.hasArtworks() ) a.setSelected(false);
	}
	
	public void loadMissingAWGraphics() {
		
		for( String a : myWall.getArtworks().keySet() ) {
			SM_Artwork aw = myWall.getArtworks().get(a);
			if( ! aw.hasGfx() ) {
				aw.setGfx( loadImage(aw.getFilePath().getAbsolutePath())  );
			}
		}
		
	}
	
	public boolean isWallGfxReady() {
		return ready;
	}
	
	public PImage getWallGfx() {
		if( ready ) {
//
//			try {
//				return (PImage)wlGfx.clone();
//			} catch (CloneNotSupportedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return null;
//			}
		}
		else {
			return null;
		}
		return null;
	}
	
	
	public void dispose() {
		
		
		vm.unregisterUpdateListener(this);
		
//		frame.setVisible(false);
		super.dispose();
//		if( frame == null ) System.out.println("frame is null, again... ");
//		
//		frame.dispose();
//		frame.setVisible(false);
		frame = null;
	}
	
	public boolean isSleeping(){
		if(frame.isVisible() ) return false;
		else return true;
	}
	
	public String getWallName() {
		return myWall.getWallName();
	}

	public SM_Wall getWall() {
		return myWall;
	}
	
	@Override
 	public void doUpdate(UpdateEvent e) {
		artworkUpdatePending = true;
		awOver = null;
	}

	
	public void mousePressed() {
		if( awOver != null ) {
			awDrag = true;
			awDragStart = new PVector(mouseX,mouseY);
			awDragOfset = wptos(awOver.getTotalWallPos()[0],awOver.getTotalWallPos()[1], scale);
			awDragOfset.sub(awDragStart);
		}
	}
	
	public void mouseReleased() {
		if( awDrag ) {

			awDrag = false;

			if (mouseX != awDragStart.x || mouseY != awDragStart.y) {
				int nposX;
				int nposY;
				PVector npos;
				ArtworkUpdateRequestEvent e;
				if (horizontalLoc) {
					npos = new PVector(mouseX, 0);

					npos.add(awDragOfset);
					PVector nPos = ptowp(npos, scale);
					e = new ArtworkUpdateRequestEvent(this, awOver.getName(),
							(int) nPos.x, awOver.getTotalWallPos()[1]);

				} else {
					npos = new PVector(mouseX, mouseY);

					npos.add(awDragOfset);
					PVector nPos = ptowp(npos, scale);
					e = new ArtworkUpdateRequestEvent(this, awOver.getName(),
							(int) nPos.x, (int) nPos.y);
				}
				myWall.myRoom.fireUpdateRequest(e);
			}
		}
	}
		
	public void mouseClicked() {
		
		if( awOver != null && mouseButton != RIGHT) {			
			awOver.toggleSelected();
			
//			System.out.println("aw pos in wall: " +awOver.getPosInWall()[0] +" x "+awOver.getPosInWall()[1] );
//			System.out.println("mousepos  wall: "+  ptowp(mouseX, mouseY).x+" x "+ ptowp(mouseX, mouseY).y  );
//			System.out.println("mousepos  scrn: "+mouseX+" x "+mouseY);
		} else if(mouseButton != RIGHT){
			deselectAll();
		}
		
		if( mouseButton == RIGHT ) {
			
			if( awOver != null ) {
				menuAW = awOver;
				putBack.setEnabled(true);
				editArtwork.setEnabled(true);
				snapToMidHeight.setEnabled(true);
			} else {
				putBack.setEnabled(false);
				editArtwork.setEnabled(false);
				snapToMidHeight.setEnabled(false);
			}
			
			
			pMenu.show(this, mouseX, mouseY);
		}
	}

	public void keyPressed() {
		if( keyCode == ESC ) {
			key =0;
		} else {
			if(keyCode == SHIFT) {
				horizontalLoc = true;
			}
		}
	}
	
	public void keyReleased() {
		if(keyCode == SHIFT) {
			horizontalLoc = false;
		}
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String action = e.getActionCommand();
		
		if(action.equalsIgnoreCase(Lang.closeWall)) {
//			this.frame.setVisible(false);
			vm.sleepWallArr(""+myWall.getWallChar());
		} else
		if(action.equalsIgnoreCase(Lang.snapToMidHeight)) {
			if( awOver != null ) {
				ArtworkUpdateRequestEvent rq = new ArtworkUpdateRequestEvent(this, awOver.getName(), awOver.getTotalWallPos()[0], (myMidHeight)+(awOver.getTotalHeight()/2)  );
				myWall.myRoom.fireUpdateRequest(rq);
			}
		} else 
		if( action.equalsIgnoreCase(Lang.RemoveArtwork) && awOver != null) {
			WallUpdateRequestEvent r = new WallUpdateRequestEvent(this, awOver.getName(), ' ', "Library", myWall.myRoom.getName(), awOver.getWallChar());
			awOver = null;
			myWall.myRoom.fireUpdateRequest(r);
		}
		else
		if( action.equalsIgnoreCase(Lang.editMeasurements) ) {
			
			SMUtils.ArtworkMeasurementChooser chooser = new SMUtils.ArtworkMeasurementChooser(this, menuAW );
			chooser.setSize(400, 200);
			chooser.setVisible(true);
		}
	
		// if it's none of the above it must have been a frameStyle selected. Let's find out which one!
		else {


			for (JMenuItem item : frameStyles) {
				if( e.getActionCommand().equalsIgnoreCase(item.getText())) {
					
					FrameStyle style = FrameStyle.valueOf(item.getText());
					
					ArtworkUpdateRequestEvent request = new ArtworkUpdateRequestEvent(this, menuAW.getName(), style);
					System.out.println(menuAW.getName());
					
					myWall.myRoom.fireUpdateRequest(request);
					
					break;
				}
			}
		}
	}
	
	public void artworkMeasurementCallback() {
		
	}

	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		
		if(dtde.getCurrentDataFlavors()[0] == SM_DataFlavor.SM_AW_Flavor) {

			
			try {

				String[] arr = (String[])dtde.getTransferable().getTransferData(SM_DataFlavor.SM_AW_Flavor);
				String name = arr[0];
				String originRoom = arr[1];
				char originWall = arr[2].charAt(arr[2].length()-1);

				// Artwork already on this wall?
				if( myWall.hasArtwork(name) ) {
					dtde.rejectDrop();
					return;
				}

				// Artwork too big for this wall?
				if( myWall.getWidth() < myWall.myRoom.getArtworkFromBase(name).getTotalWidth()  ) {

					javax.swing.JOptionPane.showMessageDialog(this, Lang.artworkTooBigForWall_1 + myWall.myRoom.getArtworkFromBase(name).getTotalWidth() + Lang.artworkTooBigForWall_2 + myWall.getWidth() + Lang.artworkTooBigForWall_3, "Artwork doesn't fit!", javax.swing.JOptionPane.OK_OPTION);

					dtde.rejectDrop();
					return;
				}

				System.out.println("firing this update Request event: "+name+" "+ myWall.getWallChar()+" "+ myWall.myRoom.getName() +" "+ originRoom+" "+ originWall);

				WallUpdateRequestEvent e = new WallUpdateRequestEvent(this, name, myWall.getWallChar(), myWall.myRoom.getName(), originRoom, originWall);

				myWall.myRoom.fireUpdateRequest(e);

				dtde.dropComplete(true);
				dtde.acceptDrop(dtde.getDropAction());

			} catch (Exception e) {

				dtde.rejectDrop();
				e.printStackTrace();
			}
		} else {

			dtde.rejectDrop();
		}
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
