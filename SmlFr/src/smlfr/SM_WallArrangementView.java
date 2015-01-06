package smlfr;

import java.awt.Dimension;

import artworkUpdateModel.ArtworkUpdateEvent;
import artworkUpdateModel.ArtworkUpdateListener;
import artworkUpdateModel.ArtworkUpdateRequestEvent;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class SM_WallArrangementView extends PApplet implements ArtworkUpdateListener {

/**
	 * 
	 */
	private static final long serialVersionUID = -8724642767602580803L;
	//	private JFrame			myFrame;
	private SM_Wall			myWall;
	private SM_ViewManager  vm;
	private Dimension		mySize;
	private PGraphics		wlGfx;
	private PImage			shadowGfx;
	private boolean			ready = false;
//	private PGraphics		bg;
	private float			scale;
	private float 			xOffsetPx, yOffsetPx;
	
	private SM_Artwork		awOver;
	private boolean 		awDrag = false;
	private PVector			awDragOfset;
	
	private boolean			artworkUpdatePending = true;
	private boolean			firstTime = true;
	
	int 					shadowAmount = 8;
	int 					shadowOfsetAmount = 5;
	
	int count = 0;
	
	public SM_WallArrangementView(SM_Wall _myW, Dimension _size, Dimension _location, SM_ViewManager _vm) {
		super();
		myWall = _myW;
		shadowGfx = myWall.getShadowImage();
		
		for( String a : myWall.getArtworks().keySet() ) {
			SM_Artwork aw = myWall.getArtworks().get(a);
			aw.setGfx( loadImage(aw.getFilePath().getAbsolutePath())  );
		}
		
		vm = _vm;
		vm.registerUpdateListener(this);
		
//		border = 100f;
		xOffsetPx = 0f;
		yOffsetPx = 0f;
		
		float aspect = (float)myWall.getHeight() / (float)myWall.getWidth();
		int resultheight = (int)(_size.width * aspect);
		mySize = new Dimension(_size.width, resultheight);
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
		line(0, wptos(0,myWall.getHeight()/2).y, wptos(myWall.getWidth(),0).x, wptos(0,myWall.getHeight()/2).y );
		popStyle();
		

		// DRAW wall
		
		image(drawWall( 0, 0 ),0,0);
		
		
		// DRAW mouseOver
		
		if( awOver != null) {
			PVector totalPos = wptos( new PVector(awOver.getTotalWallPos()[0], awOver.getTotalWallPos()[1]) );
			PVector totalSize = astos( new PVector(awOver.getTotalWidth(), awOver.getTotalHeight()));
			
			pushStyle();
			fill(200,100,100,80);
			noStroke();
			rect( totalPos.x -5, totalPos.y-5, totalSize.x+9, totalSize.y+9 );
			noFill();
			stroke(200,100,100,120);
			rect( totalPos.x -5, totalPos.y-5, totalSize.x+9, totalSize.y+9 );
			popStyle();
			
		}
		
		// DRAW drag
		
		if( awOver != null && awDrag ) {
			PVector wh = astos( new PVector(awOver.getTotalWidth(), awOver.getTotalHeight()));
			pushStyle();
			noFill();
			rect(mouseX+awDragOfset.x, mouseY+awDragOfset.y, wh.x, wh.y);
			popStyle();
		}
		
		// DRAW selected
		
		if(myWall.hasArtworks().length > 0 ) {
			for( SM_Artwork a : myWall.hasArtworks() ) {
				
				if(a.isSelected()) {
				
					PVector totalPos = wptos( new PVector(a.getTotalWallPos()[0], a.getTotalWallPos()[1]) );
					PVector totalSize = astos( new PVector(a.getTotalWidth(), a.getTotalHeight()));
				
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
//		wlGfxReady = false;
		loadMissingAWGraphics();
		
		PGraphics gfx;
		
		if( _mode == 0 ) gfx = wlGfx;
		else			 gfx = createGraphics(width, height);
		
		
		gfx.clear();
		gfx.beginDraw();
		
		
		// DRAW Schatten
		
		if( _mode == 1 ) {
			if(myWall.hasArtworks().length > 0 ) {

				if(!awDrag) awOver = null;

				for( SM_Artwork a : myWall.hasArtworks() ) {

					int[] tmpPos = a.getTotalWallPos();
					PVector totalPos = wptos( new PVector(tmpPos[0]-shadowAmount-(shadowOfsetAmount * shadowOfset), tmpPos[1]-shadowAmount-(shadowAmount*2)) );
					PVector totalSize = astos( new PVector(a.getTotalWidth()+(shadowAmount*2), a.getTotalHeight()+(shadowAmount*0.5f)));

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
				PVector totalPos = wptos( new PVector(tmpPos[0], tmpPos[1]) );

				PVector totalSize = astos( new PVector(a.getTotalWidth(), a.getTotalHeight()));
				
				
				int[] tmpPos2 = a.getPptWallPos();
				PVector pptPos = wptos( new PVector(tmpPos2[0], tmpPos2[1]) );
				
				int[] tmpPos3= a.getPptSize();
				PVector pptSize = astos(new PVector(tmpPos3[0], tmpPos3[1]));
				
				int[] tmpPos4 = a.getArtworkWallPos();
				PVector artworkPos = wptos( new PVector(tmpPos4[0], tmpPos4[1]) );
				
				int[] tmpPos5 = a.getArtworkSize();
				PVector artworkSize = astos(new PVector(tmpPos5[0], tmpPos5[1]));
				
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
				
				
			
				
			}
		}
		gfx.endDraw();
		ready = true;
		if(_mode == 1) artworkUpdatePending = false;
		return gfx;
	}
	
	
	// artwork Size to Screen
	private PVector astos(int _inX, int _inY) {
		return wptos(new PVector(_inX, _inY) );
	}
	private PVector astos(PVector _inpos) {

		_inpos.mult(scale);		
		return _inpos;
	}
	
	// wall Position to Screen
	private PVector wptos(int _inX, int _inY) {
		return wptos(new PVector(_inX, _inY) );
	}
	private PVector wptos(PVector _inpos) {
		
		
		_inpos.mult(scale);
		_inpos = invLogicScaled(_inpos);
		

		///  UNTESTED!!!!
//		_inpos.add(new PVector(xkOffsetPx, yOffsetPx));
				
		return _inpos;
	}
	
	// WallPsoitionLogic to ScreenLogic
	private PVector invLogicScaled(int _wX, int _wY){
		return invLogicScaled(new PVector(_wX, _wY));
	}
	private PVector invLogicScaled(PVector _inpos) {
		
		return new PVector(_inpos.x, myWall.getHeight()*scale-_inpos.y);
	}

	// ScreenLogic to WallPositionLogic
	private PVector ptowp(int _wX, int _wY){
		return ptowp(new PVector(_wX, _wY));
	}
	private PVector ptowp(PVector _inpos) {
		
		/// UNTESTED!!!!
//		_inpos.sub(new PVector(xOffsetPx, yOffsetPx));
		
		_inpos.div(scale);
		
		return new PVector(_inpos.x, myWall.getHeight()-_inpos.y);
	}

	private boolean checkMouseOver(SM_Artwork a) {
		
		PVector pos = wptos( new PVector(a.getTotalWallPos()[0], a.getTotalWallPos()[1]) );
		PVector sze = astos( new PVector(a.getTotalWidth(), a.getTotalHeight()));
		
		
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
		if( true ){
			for( String a : myWall.getArtworks().keySet() ) {
				SM_Artwork aw = myWall.getArtworks().get(a);
				if( ! aw.hasGfx() ) {
					aw.setGfx( loadImage(aw.getFilePath().getAbsolutePath())  );
				}
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

	@Override
	public void artworkUpdate(ArtworkUpdateEvent e) {
		artworkUpdatePending = true;
	}
	
	public void mousePressed() {
		if( awOver != null ) {
			awDrag = true;
			awDragOfset = wptos(awOver.getTotalWallPos()[0],awOver.getTotalWallPos()[1]);
			awDragOfset.sub(new PVector(mouseX,mouseY));
		}
	}
	
	public void mouseReleased() {
		if( awDrag ) {
//			awDragOfset.sub(new PVector(mouseX,mouseY));
			PVector npos = new PVector(mouseX,mouseY);
			npos.add(awDragOfset);
			PVector nPos = ptowp(npos);
			
			ArtworkUpdateRequestEvent e = new ArtworkUpdateRequestEvent(this, awOver.getName(), (int)nPos.x, (int)nPos.y);
			myWall.myRoom.fireUpdateRequest(e);
			awDrag = false;
		}
	}
	
	public void mouseDraged() {
		if( awDrag ) {
			awDrag = false;
		}
	}
	
	public void mouseClicked() {
		
		if( awOver != null ) {			
			awOver.toggleSelected();
			
//			System.out.println("aw pos in wall: " +awOver.getPosInWall()[0] +" x "+awOver.getPosInWall()[1] );
//			System.out.println("mousepos  wall: "+  ptowp(mouseX, mouseY).x+" x "+ ptowp(mouseX, mouseY).y  );
//			System.out.println("mousepos  scrn: "+mouseX+" x "+mouseY);
		} else {
			deselectAll();
		}
	}
}
