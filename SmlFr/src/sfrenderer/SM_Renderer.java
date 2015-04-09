package sfrenderer;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;

import java.io.File;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

//import org.multiply.processing.TimedEventGenerator;



import SMUtils.Lang;
import SMUtils.Skewmator;
import SMUtils.ViewMenuItem;
import SMUtils.pTimedEventGenerator;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;
import smlfr.SM_ViewAngle;
import smlfr.SM_ViewManager;


public class SM_Renderer extends PApplet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5902287327131409732L;
	private SM_ViewManager			vm;
//	private JFrame					myFrame;
	
	private SM_ViewAngle			currentView;
	private String					currentViewString;
	private File					generalPath;
	private File					currentPath;
	private String					currentFileStub;
	
	private JPopupMenu				pMenu;
	private ViewMenuItem[]			pMenuViews;				
	
	private PImage[] layers;
	private float cr, cg, cb;
	private boolean b1 = true;
	private boolean b2 = true;
	private boolean b3 = false;
	private boolean b4 = true;
	private boolean b5 = true;

//	private PGraphics 				texture;
//	private PImage 					textureIMG;
//	private PImage 					bild ;
	
	private PGraphics				cropMask;
	private PGraphics[]				wallGfxs;
	private char[]					wallGfxsId;

	private Skewmator				skewmator;

	private double 					aspect;
	private float 					scale;
	private int 					ySize = 600;
	private int						photoX = 1200;
	private int						photoY = 800;
	private float					zoomFact;
	private int						xOffset, yOffset;
	
	public boolean 					setupRun = false;
	
//	private pTimedEventGenerator	tGen;
	private int 					tCount = 0;		
	private boolean 				tStop = false;

	
	public SM_Renderer(SM_ViewManager _vm, SM_ViewAngle _defaultView, File _filePath, int _YSize) {
//		super();
		vm = _vm;
		skewmator = new Skewmator(photoX, photoY);
		skewmator.init();
//		tGen = new pTimedEventGenerator(this);
		
		initMenu();

		int c = color(123);
		
		System.err.println(c);
		
		generalPath = _filePath;
		currentView = _defaultView;
		currentViewString = currentView.getName();
		setCurrentPath(currentViewString);
		
		ySize = _YSize;
		if( ySize > 600 ) ySize = 600;
		aspect = (double)((double)photoX / (double)photoY);

//		this.frame = _frame;
//		myFrame = _frame;
	}
	
	public void changeView( SM_ViewAngle _view ) {
		
		System.out.println("\n\n\nVIEW CHANGED in rendddd   "+_view.getName());
		
//		layers = null;
//		System.gc();
		
		currentView = _view;
		currentViewString = currentView.getName();
		setCurrentPath(currentViewString);
		setup();
		
		while( !setupRun) {
			System.out.println("SETUP IS NOT RUNNING");
		}
		
		
		
		for( Character wc : _view.getWallChars() ) {
			
			vm.requestRendererUpdate(wc);
			
		}
		System.gc();
		redraw();
	}
	
	
	private void setCurrentPath(String _view) {
		
		currentFileStub = _view.substring(2);
		currentFileStub = "/"+currentFileStub;
		currentPath = new File(  generalPath.getAbsolutePath()+currentFileStub   );
	}
	
	public void init() {
		super.init();
	}
	
	public void initMenu() {
		pMenu = new JPopupMenu();
		JMenuItem title = new JMenuItem(Lang.selectView);
		title.setFont(title.getFont().deriveFont(Font.ITALIC));
		title.setEnabled(false);
		pMenu.add(title);
		pMenu.add(new JSeparator());
		pMenuViews = new ViewMenuItem[vm.getNumberOfViewAngles()];
		int i=0;
		for( String n : vm.getViewAngleRealNames()) {
			pMenuViews[i] = new ViewMenuItem(n, vm.getViewAngle(i).getName());
			pMenuViews[i].addActionListener(vm);
			pMenuViews[i].addMouseListener(vm.getView());
			pMenu.add(pMenuViews[i]);
			i++;
		}
	}
	
	public void setup() {
		
		setupRun = false;
		
		frame.setTitle(currentViewString.substring(2));
		layers = new PImage[7];
		layers[0] = loadImage(currentPath.getAbsolutePath()+currentFileStub+"_Hintergrund.png");
		
		scale = (float)ySize / (float)layers[0].width;
		
		zoomFact = 1.0f;
		xOffset = 0;
		yOffset = 0;
		
		String w = currentView.getWallCharsAsString();
		wallGfxs = new PGraphics[w.length()];
		wallGfxsId = new char[w.length()];
		
		for( int i=0; i< w.length(); i++ ) {
			wallGfxs[i]   = createGraphics(layers[0].width, layers[0].height);
			wallGfxs[i].beginDraw();
			wallGfxs[i].background(0,0);
			wallGfxs[i].endDraw();
			wallGfxsId[i] =  w.charAt(i);
		}
		



		/* 
		 * [0] Background	FILE
		 * [1] Color		dynamic
		 * [2] light		VIEWMANAGER
		 * [3] Artworks		VIEWMANAGER
		 * [4] Shadow		dynamic
		 * [5] #colorMask	FILE
		 * [6] #shadowMask	FILE
		 */
		layers[1] = new PImage(layers[0].width,layers[0].height);
		layers[2] = new PImage(layers[0].width,layers[0].height); // VIEWMANAGER
		layers[3] = new PImage(layers[0].width,layers[0].height); // VIEWMANAGER
		layers[4] = new PImage(layers[0].width,layers[0].height);
		layers[5] = loadImage(currentPath.getAbsolutePath()+currentFileStub+"_Farbe.png");
		layers[6] = loadImage(currentPath.getAbsolutePath()+currentFileStub+"_Schatten.png");

		

//		aspect = (double)((double)layers[0].width / (double)layers[0].height);


		
//		for( PImage l : layers) {
//			l.resize( (int)(ySize * aspect), ySize);
//		}
//		for( PGraphics wg : wallGfxs ) {
//			wg.resize( (int)(ySize * aspect), ySize);
//		}
	

		// schatten Maske:

		PGraphics s = createGraphics(layers[6].width,layers[6].height);
		s.beginDraw();
		s.fill(0);
		s.endDraw();
		layers[4]  = s.get();
		layers[6].filter(INVERT);
		layers[4].mask(layers[6]);
		layers[4].filter(ERODE);
//		layers[4].filter(DILATE);
		

		// Farbe:

		updateRoomColorLayer(null);
		

		// clear memory:
		layers[6] = null;
//		layers[5] = null;
		
		
		noLoop();
//		frameRate(5);
//		smooth();
		setupRun = true;
		
		
	}

	public void updateRoomColorLayer( Integer _previewColor ) {
		
		PGraphics t = createGraphics(layers[5].width,layers[5].height);
		
		int c;
		
		if( _previewColor == null ) c = vm.getRoomColor();
		else c = _previewColor;
		
//		cr= red(c);
//		cg= blue(c);
//		cb= green(c);

		Color clr = new Color(c);
		
		cr= clr.getRed();
		cg = clr.getGreen();
		cb = clr.getBlue();
		
		t.beginDraw();
		t.tint(cr,cg,cb);
		t.image(layers[0],0,0,layers[5].width,layers[5].height);
		g.removeCache(t);
		t.endDraw();
		layers[1] = t.get();
//		layers[1].filter(DILATE);
//		layers[5].filter(DILATE);
		layers[1].mask(layers[5]);
		
		if(setupRun) redraw();
	}
	
	public void updateArtworksLayer( char _wallChar) {
		
		System.out.println("RENDERER: updating... "+_wallChar);
		
//		artworksLayer.clear();
		
//		for( Character wc : currentView.getWallChars()) {
//			System.out.println("  Asking Wall "+wc+"...");
			
			if( currentView.getWallCharsAsString().contains(""+_wallChar) ) {
				
				
				Float[] skewValues = currentView.getWallSkew(_wallChar);

				int shadowOfset = 0;
				
				// determine shadow ofset
				float left = ( skewValues[9]-skewValues[3] );
				float right =( skewValues[7]-skewValues[5] );
					shadowOfset = 0;
				if( left > right ) {
					shadowOfset = -1;
				}else{
					shadowOfset =  1;
				}
				
				PImage wallGfx = null;

				
				wallGfx = vm.getWallGfx(_wallChar, shadowOfset);
				
				if( /*artworksLayer != null && */ wallGfx != null) {
					
					
					for(int i=0; i<wallGfxs.length; i++) {
						if( wallGfxsId[i] == _wallChar ) {
							
							
							System.out.println("RENDERER: painting on wall ["+i+"] : "+_wallChar);
							
							int wgWidth = wallGfxs[i].height;
							
							wallGfxs[i].beginDraw();
							wallGfxs[i].clear();
//							wallGfxs[i].image(wallGfx,0,0);
							wallGfxs[i].image(skewmator.skewToWall(wallGfx, skewValues, 0, wgWidth), 0,0);
							g.removeCache(wallGfxs[i]);
							wallGfxs[i].endDraw();

							if( currentView.isWallCrop(_wallChar) ) {
								
								Float[] cropValues = currentView.getWallCrop(_wallChar);
								float f = cropValues[0] / photoX;
								
								cropMask = skewmator.drawCropImage(cropValues);
								cropMask.resize(wallGfxs[i].width, wallGfxs[i].height);
								g.removeCache(cropMask);
								manualMask(wallGfxs[i], cropMask);
								
//								PImage maskedImg = wallGfxs[i].get();
//								maskedImg.mask(cropMask);
								
								
//								wallGfxs[i].beginDraw();
//								wallGfxs[i].clear();
//								wallGfxs[i].image(maskedImg,0,0);
//								wallGfxs[i].endDraw();

							}
						}
					}
					
					
//					artworksLayer.image(skewmator.skewToWall(wallGfx, values, 0, ySize), 0,0);

					
					System.out.println("  painted.");
				}
//				artworksLayer.beginDraw();
//				artworksLayer.clear();
//				for( PGraphics wg : wallGfxs ) {
//					artworksLayer.image(wg,0,0);
//				}
//				artworksLayer.endDraw();
//				layers[3] = artworksLayer;
//				
				
				
			}
//		}
		System.out.println("end update.\n");
		redraw();
		
	}
	
	private void manualMask(PGraphics display, PImage mask) {
		
		
		  mask.loadPixels();
		  display.beginDraw();
		  display.loadPixels();
		  for (int i = 0; i < display.pixels.length; i++) {
		 
		    int d = display.pixels[i];
		 
		    // mask alpha
		    int m_a = mask.pixels[i] & 0xFF;
		    // display alpha
		    int d_a = (d >> 24) & 0xFF;
		    // output alpha (do not change alpha if already transparent)

		    int o_a;
		    if( m_a < d_a ) {
		    	o_a = m_a;
		    	display.pixels[i] = (o_a << 24) | (0x00FFFFFF & d);
		    }
		    
		  }
		  display.updatePixels();
		  display.endDraw();
		}
	
	public void draw(){

		
		
		background(255);

		int displW = (int)(width  * zoomFact);
		int displH = (int)(height * zoomFact);
		
		int xOff = xOffset;
		int yOff = yOffset;
		
		// draw Base

		if(b1) {
			blendMode(BLEND);
			image(layers[0], xOff, yOff,displW,displH);
			g.removeCache(g);
			
		}


		// draw Farbe

		if(b2){
			pushStyle();
			blendMode(BLEND);
//			tint(vm.getRoomColor());
			image(layers[1], xOff, yOff,displW,displH);
			g.removeCache(g);
			//blend(layers[1], 0, 0, width, height, 0, 0, width, height, MULTIPLY);
			popStyle();
		}


		// draw Licht

		if(b3) {
			pushStyle();
			//blendMode(ADD);
			tint(255, 70);
			image(layers[2], xOff, yOff,displW,displH);
			g.removeCache(g);
			popStyle();
		}  


		// draw Bild

		if(b4) {
			pushStyle();
			blendMode(BLEND);
			for( PGraphics wg : wallGfxs) {
				image(wg, xOff, yOff,displW,displH);
				g.removeCache(g);
			}

			popStyle();
		}

		// draw Schatten

		if(b5) {
			pushStyle();
			blendMode(BLEND);
			tint(255, 65);
			image(layers[4], xOff, yOff,displW,displH);
			g.removeCache(g);
			popStyle();
		}

//		drawGUI();
		
	}

	void drawGUI() {
		pushStyle();
		stroke(0);
		blendMode(BLEND);
		if( b1) fill(20,180,20); else fill(180,20,20); 
		//rect(10,10,15,15);
		text("1: basis", 10,20);

		if( b2) fill(20,180,20); else fill(180,20,20);
		//rect(30,10,15,15);
		text("2: farbe",10,40);

		if( b3) fill(20,180,20); else fill(180,20,20);
		//rect(50,10,15,15);
		text("3: licht", 10,60);

		if( b4) fill(20,180,20); else fill(180,20,20);
		//rect(70, 10, 15,15);
		text("4: bild", 10,80);

		if( b5) fill(20,180,20); else fill(180,20,20);
		//rect(90, 10, 15,15);
		text("5: schatten", 10,100);
		popStyle();
	}

	public void mousePressed() {
		if( mouseButton == RIGHT ) {
			for( JMenuItem m : pMenuViews) {
				if( m.getActionCommand().equalsIgnoreCase(currentViewString.substring(currentViewString.lastIndexOf('_')+1))) {
					m.setFont(m.getFont().deriveFont(Font.ITALIC));
					m.setEnabled(false);
				} else {
					m.setFont(m.getFont().deriveFont(Font.PLAIN));
					m.setEnabled(true);
				}
			}
			pMenu.show(this, mouseX, mouseY);
		}
	}

	public void mouseDragged() {
		xOffset += mouseX-pmouseX;
		yOffset += mouseY-pmouseY;
		System.out.println("da");
		offsetBounds();
		redraw();
	}
	
	public void mouseWheel(MouseEvent event) {
		
		float e = event.getCount();
		
		float f = 0;
		
		if( e < 0) {
			f = -0.07f;
			xOffset -= (int)(0.5f * ((float)width*f));
			yOffset -= (int)(0.5f * ((float)height*f));

		}
		else if( e > 0 ) { 
			f =  0.04f;
			xOffset -= (int)(((float)mouseX / (float)width) * ((float)width*f));
			yOffset -= (int)(((float)mouseY / (float)height) * ((float)height*f));

			
		}
		zoomFact += f;
//		xOffset -= (int)(((float)mouseX / (float)width) * ((float)width*f));
//		yOffset -= (int)(((float)mouseY / (float)height) * ((float)height*f));
		
		if(zoomFact < 1.0f) {
			zoomFact = 1.0f;
			xOffset = 0;
			yOffset = 0;
		}
		
		
		offsetBounds();

		System.out.println(xOffset);
		redraw();
	}
	
	private void offsetBounds() {
		if((xOffset+(width*zoomFact)) < width ) xOffset += width - (xOffset+(width*zoomFact));
		if( xOffset > 0 ) xOffset = 0;
		
		if((yOffset+(height*zoomFact)) < height ) yOffset += height - (yOffset+(height*zoomFact));
		if( yOffset > 0 ) yOffset = 0;
	}
	
 	public boolean isMenuOpen() {
		return pMenu.isVisible();
	}

	public void keyPressed() {
		if( keyCode == ESC) {
			key = 0;
		} else {
			if( key == '1') b1 = !b1;
			if( key == '2') b2 = !b2;
			if( key == '3') b3 = !b3;
			if( key == '4') b4 = !b4;
			if( key == '5') b5 = !b5;
	
			if( key == 'u') {
				for( char w : wallGfxsId )
					updateArtworksLayer(w);
			}
			redraw();
		}
	}

	@Deprecated
	private PImage skewImage(PImage inputImage, PVector lo, PVector ro, PVector ru, PVector lu) {
		
		
		PImage wall, mask;
		XTImage  img;
	
		System.out.println("in: " + inputImage.width+" x "+inputImage.height);
		
		// Use Buffered Image to convert from PImage to javaxt.io.image
		
//		BufferedImage bi = new BufferedImage(inputImage.width, inputImage.height, BufferedImage.TYPE_INT_ARGB);
//		bi.getGraphics().drawImage((java.awt.Image) inputImage.getNative(), 0, 0 , null);
//		img = new javaxt.io.Image(inputImage.width, inputImage.height);
//		img.addImage(bi, 0, 0, true);
//		
		
		// Works as well
		
//		javaxt.io.Image source = new javaxt.io.Image( new File("data/2_Bild_Crop.png") );
//		img = new javaxt.io.Image(inputImage.width, inputImage.height);
//		img.addImage(source, 100, 100, true);
		
		// This is the shortes working way:
		// 	(They all have the transparency right!)
		
		img = new XTImage((BufferedImage)inputImage.getNative());
		
//		File sf = new File("temp/secondtest.png");
//		img.saveAs(sf);
//		System.out.println("xt: "+img.getWidth()+" x "+img.getHeight());


		
		// skew the image
		img.setCorners((int)lo.x , (int)lo.y, (int)ro.x, (int)ro.y, (int)ru.x, (int)ru.y, (int)lu.x, (int)lu.y);
		
		System.out.println("sk: "+img.getWidth()+" x "+img.getHeight());
		
		// set up the mask
//		PGraphics m = createGraphics(img.getWidth(), img.getHeight());
//		m.beginDraw();
//		m.noStroke();
//		m.background(0);
//		m.fill(255);
//		m.beginShape();
//		m.vertex(lo.x, lo.y);
//		m.vertex(ro.x, ro.y);
//		m.vertex(ru.x, ru.y);
//		m.vertex(lu.x, lu.y);
//		m.vertex(lo.x, lo.y);
//		m.endShape();
//		m.endDraw();
//		mask = m.get();
		

		// convert the skewed image back to PImage by manually drawing
		
		PGraphics pg = createGraphics(img.getWidth(), img.getHeight());
		pg.beginDraw();
		for(int x = 0; x < pg.width; x++) {
			for(int y = 0; y < pg.height; y++) {
				
				pg.set(x, y, img.getColor(x, y).getRGB() );
				
			}
		}
		pg.endDraw();
		wall = pg.get();
				
		// mask the image		
		//wall.mask(mask);
		
		System.out.println("nd: "+wall.width+" x "+wall.height);
		
		return wall;
		
	}

	public Dimension getSize() {
		return new Dimension((int)(ySize * aspect), ySize);
	}

	@Deprecated
	private void checkForWallGfx() {
		System.out.println("RENDERER CHECK:");
		
		char wc = currentView.getWallChars()[0];
		
		
		boolean check = false;
		while(!check) {
			check = vm.isWallGfxReady( wc );
			
			if( !check ){
				System.out.println("waiting for "+wc+"to come up with graphics");
			}
			else {
				System.out.println("jayyyyyyy");
			}
		}
		System.out.println("RENDERER CHECK FINISHED");
	}
	
	@Deprecated
	public void onTimerEvent() {

//		tGen.setEnabled(true);
//		tGen.setEnabled(false);
		
		for(char w : currentView.getWallChars() ) {
			updateArtworksLayer(w);
		}	
	}
	
	public void setTimer() {
		
		System.out.println("SET TIMER CALLED");
		
//		tGen.setEnabled(true);
//		tGen.setIntervalMs(400);
		tCount = 0;
	}
	
	public char[] getCurrentWallChars() {
		System.out.println(currentFileStub);
		return currentFileStub.substring(currentFileStub.lastIndexOf('_')+1).toCharArray();
	}
	
	public void prepareFrameForClosing() {
		
		tStop = true;
//		tGen.setEnabled(false);
//		tGen.setEnabled(false);
		
//		frame.setVisible(false);
		
		
	}
	
	public void dispose() {
		System.err.println("Renderer goodbye...1");
		
//		tGen.dispose();
		
		frame.dispose();

		super.dispose();

	}



}
