package smlfr;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import artworkUpdateModel.ArtworkUpdateEvent;
import artworkUpdateModel.ArtworkUpdateListener;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class SM_WallArrangementView extends PApplet implements ArtworkUpdateListener {

	private JFrame			myFrame;
	private SM_ViewManager  vm;
	private Dimension		mySize;
//	private PGraphics		bg;
	private PGraphics		wlGfx;
	private boolean			wlGfxReady = false;
	private SM_Wall			myWall;
//	private float			border;
	private float			scale;
	private float 			xOffsetPx, yOffsetPx;
	
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
	
	public void init(JFrame _frame ) {
//		
//		myFrame = _frame;
//		mySize = _size;
//		
//		
//		System.out.println(" The Size i got: "+ _size.width+" x "+ _size.height);
//		System.out.println(" the Wall Size:  "+ myWall.getWidth() +" x "+myWall.getHeight());
//		
//		float aspect = (float)myWall.getHeight() / (float)myWall.getWidth();
//		
//		System.out.println(" the aspect is:  " + aspect);
//		
//		int resultheight = (int)(_size.width * aspect);
//		
//		System.out.println(" the resutlt is: " + resultheight);
//		
//		
//		myFrame.setSize(_size.width, resultheight);
//		
//		System.out.println(" control aspect: "+ ((float)resultheight / (float)_size.width));
//		
//		myFrame.setLocation(_location.width, _location.height);
//		
//		myFrame.setVisible(true);
		
		super.init();
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
		
		frameRate(10);
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
		
		
//		System.out.println("stillhere "+count++);
		
		background(230);
//		image(bg,0,0);
		
//		rect(0,0, wptos(myWall.getWidth(), myWall.getHeight()).x, wptos(myWall.getWidth(), myWall.getHeight()).y );
		
		// Mittelhšhe
		pushStyle();
		stroke(150);
		line(0, wptos(0,myWall.getHeight()/2).y, wptos(myWall.getWidth(),0).x, wptos(0,myWall.getHeight()/2).y );
		popStyle();
		
		wlGfx.clear();
		wlGfx.beginDraw();
//		wlGfx.background(50,90,10,100);
		if(myWall.hasArtworks().length > 0 ) {
			
			for( SM_Artwork a : myWall.hasArtworks() ) {
			
				PVector awDrawPos = wptos( new PVector(a.getPosInWall()[0], a.getPosInWall()[1]) );
				PVector awDrawSize = astos( new PVector(a.getWidth(), a.getHeight()));
				
				wlGfx.fill(230,230,230);
				wlGfx.rect( awDrawPos.x-1, awDrawPos.y-1, awDrawSize.x+1, awDrawSize.y+1 );
				wlGfx.fill(230,230,230, 40);
				wlGfx.rect( awDrawPos.x-2, awDrawPos.y-2, awDrawSize.x+2, awDrawSize.y+2 );

				wlGfx.image(a.getGfx(), awDrawPos.x, awDrawPos.y, awDrawSize.x, awDrawSize.y);
				wlGfx.fill(200,200,200);
//				wlGfx.rect( awDrawPos.x+5, awDrawPos.y+5, awDrawSize.x, awDrawSize.y );
				
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
		
		if( artworkUpdate ) {
			artworkUpdate = false;
			vm.setRendererUpdate();
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
		return wptop(new PVector(_wX, _wY));
	}
	private PVector ptowp(PVector _inpos) {
		
		return new PVector(_inpos.x, myWall.getHeight()-_inpos.y);
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
	
//	public PImage getWallGraphics() {
//		if( wlGfx != null ) {
//			
//			return wlGfx;
//		}
//		else return null;
//	}
	
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
	
	public String getWallName() {
		return myWall.getWallName();
	}

	@Override
	public void artworkUpdate(ArtworkUpdateEvent e) {
		artworkUpdate = true;
		wlGfxReady = false;
	}
}
