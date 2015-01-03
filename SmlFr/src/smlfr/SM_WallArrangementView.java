package smlfr;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import SMUtils.ViewMenuItem;
import artworkUpdateModel.ArtworkUpdateEvent;
import artworkUpdateModel.ArtworkUpdateListener;
import artworkUpdateModel.ArtworkUpdateRequestEvent;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class SM_WallArrangementView extends PApplet implements ArtworkUpdateListener {

//	private JFrame			myFrame;
	private SM_ViewManager  vm;
	private Dimension		mySize;
//	private PGraphics		bg;
	private PGraphics		wlGfx;
	private boolean			wlGfxReady = false;
	private SM_Wall			myWall;
//	private float			border;
	private float			scale;
	private float 			xOffsetPx, yOffsetPx;
	
	private SM_Artwork		awOver;
	private boolean 		awDrag = false;
	private PVector			awDragOfset;
	
	private boolean			artworkUpdate = true;
	
	int count = 0;
	
	public SM_WallArrangementView(SM_Wall _myW, Dimension _size, Dimension _location, SM_ViewManager _vm) {
		super();
		myWall = _myW;
		
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

		wlGfx = createGraphics(mySize.width, mySize.height);
		
//		rectMode(CORNER);
		frameRate(15);
	}
	
	public void draw() {
		
		if( true ){
			for( String a : myWall.getArtworks().keySet() ) {
				SM_Artwork aw = myWall.getArtworks().get(a);
				if( ! aw.hasGfx() ) {
					aw.setGfx( loadImage(aw.getFilePath().getAbsolutePath())  );
				}
			}
		}
		
		
		
		background(230);
	
		// Mittelhšhe
		pushStyle();
		stroke(150);
		line(0, wptos(0,myWall.getHeight()/2).y, wptos(myWall.getWidth(),0).x, wptos(0,myWall.getHeight()/2).y );
		popStyle();
		
		
		
		wlGfxReady = false;
		wlGfx.clear();
		wlGfx.beginDraw();
//		wlGfx.background(50,90,10,100);
		if(myWall.hasArtworks().length > 0 ) {
			
			if(!awDrag) awOver = null;
			
			for( SM_Artwork a : myWall.hasArtworks() ) {
			
				PVector awDrawPos = wptos( new PVector(a.getPosInWall()[0], a.getPosInWall()[1]) );
				PVector awDrawSize = astos( new PVector(a.getWidth(), a.getHeight()));
				
				if( !awDrag ) {
					if( mouseX > awDrawPos.x && mouseX < (awDrawPos.x + awDrawSize.x) ) {
						if( mouseY > awDrawPos.y && mouseY < (awDrawPos.y + awDrawSize.y) ) {
							awOver = a;
						}
					}
				}
				
				
				wlGfx.fill(230,230,230,100);
				wlGfx.rect( awDrawPos.x-1, awDrawPos.y-1, awDrawSize.x+1, awDrawSize.y+1 );
//				wlGfx.fill(230,230,230, 40);
//				wlGfx.rect( awDrawPos.x-2, awDrawPos.y-2, awDrawSize.x+4, awDrawSize.y+4 );

				wlGfx.image(a.getGfx(), awDrawPos.x, awDrawPos.y, awDrawSize.x, awDrawSize.y);
				
				
//				System.out.println(a.getTitle()+":");
//				System.out.println("awSiz: "+a.getWidth()+" x "+a.getHeight());
//				System.out.println("awPos: "+a.getPosInWall()[0]+" x "+a.getPosInWall()[1]);
//				System.out.println("rmSiz: "+myWall.getWidth()+" x "+myWall.getHeight());
//				System.out.println(".");
				
			
				
			}
		}
		wlGfx.endDraw();
		wlGfxReady = true;
		
		image(wlGfx,0,0);
		
		
		// DRAW mouseOver
		
		if( awOver != null) {
			PVector awDrawPos = wptos( new PVector(awOver.getPosInWall()[0], awOver.getPosInWall()[1]) );
			PVector awDrawSize = astos( new PVector(awOver.getWidth(), awOver.getHeight()));
			
			pushStyle();
			fill(200,100,100,80);
			noStroke();
			rect( awDrawPos.x -5, awDrawPos.y-5, awDrawSize.x+9, awDrawSize.y+9 );
			noFill();
			stroke(200,100,100,120);
			rect( awDrawPos.x -5, awDrawPos.y-5, awDrawSize.x+9, awDrawSize.y+9 );
			popStyle();
			
		}
		
		// DRAW drag
		
		if( awOver != null && awDrag ) {
			PVector wh = astos( new PVector(awOver.getWidth(), awOver.getHeight()));
			pushStyle();
			noFill();
			rect(mouseX+awDragOfset.x, mouseY+awDragOfset.y, wh.x, wh.y);
			popStyle();
		}
		
		// DRAW selected
		
		if(myWall.hasArtworks().length > 0 ) {
			for( SM_Artwork a : myWall.hasArtworks() ) {
				
				if(a.isSelected()) {
				
					PVector awDrawPos = wptos( new PVector(a.getPosInWall()[0], a.getPosInWall()[1]) );
					PVector awDrawSize = astos( new PVector(a.getWidth(), a.getHeight()));
				
					pushStyle();
					strokeWeight(3);
					stroke(20,50,200,80);
					noFill();
					rect( awDrawPos.x -5, awDrawPos.y-5, awDrawSize.x+9, awDrawSize.y+9 );
					popStyle();
				
				}
			}
		}
		
		if( artworkUpdate ) {
			artworkUpdate = false;
			vm.setRendererUpdate();
		}
	}
	
	public void mousePressed() {
		if( awOver != null ) {
			awDrag = true;
			awDragOfset = wptos(awOver.getPosInWall()[0],awOver.getPosInWall()[1]);
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
	
	public void mouseClicked() {
		
		if( awOver != null ) {			
			awOver.toggleSelected();
			
//			System.out.println("aw pos in wall: " +awOver.getPosInWall()[0] +" x "+awOver.getPosInWall()[1] );
//			System.out.println("mousepos  wall: "+  ptowp(mouseX, mouseY).x+" x "+ ptowp(mouseX, mouseY).y  );
//			System.out.println("mousepos  scrn: "+mouseX+" x "+mouseY);
		}
	}
	
	private PVector astos(int _inX, int _inY) {
		return wptos(new PVector(_inX, _inY) );
	}
	private PVector astos(PVector _inpos) {

		_inpos.mult(scale);		
		return _inpos;
	}
	
	
	private PVector wptos(int _inX, int _inY) {
		return wptos(new PVector(_inX, _inY) );
	}
	private PVector wptos(PVector _inpos) {
		
		_inpos = wptop(_inpos);
		
		_inpos.mult(scale);
		
//		_inpos.add(new PVector(border/2, border/2));

		_inpos.add(new PVector(xOffsetPx, yOffsetPx));
				
		return _inpos;
	}
	
	private PVector wptop(int _wX, int _wY){
		return wptop(new PVector(_wX, _wY));
	}
	private PVector wptop(PVector _inpos) {
		
		return new PVector(_inpos.x, myWall.getHeight()-_inpos.y);
	}

	private PVector ptowp(int _wX, int _wY){
		return ptowp(new PVector(_wX, _wY));
	}
	private PVector ptowp(PVector _inpos) {
		
		_inpos.div(scale);
		
		return new PVector(_inpos.x, myWall.getHeight()-_inpos.y);
	}

	private boolean checkMouseOver(SM_Artwork a) {
		
		PVector pos = wptos( new PVector(a.getPosInWall()[0], a.getPosInWall()[1]) );
		PVector sze = astos( new PVector(a.getWidth(), a.getHeight()));
		
		
		if( mouseX > pos.x && mouseX < (pos.x + sze.x) ) {
			if( mouseY > pos.y && mouseY < (pos.y + sze.y) ) {
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	
	public boolean isWallGfxReady() {
		return wlGfxReady;
	}
	
	public PImage getWallGfx() {
		if( wlGfxReady ) {

			try {
				return (PImage)wlGfx.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
//			return wlGfx;
		}
		else {
			return null;
		}
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
		artworkUpdate = true;
		wlGfxReady = false;
	}
}
