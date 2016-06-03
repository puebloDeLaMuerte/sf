package sfrenderer;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

//import org.multiply.processing.TimedEventGenerator;



import SMUtils.Lang;
import SMUtils.SfPApplet;
import SMUtils.SkewMode;
import SMUtils.Skewmator;
import SMUtils.SysInfo;
import SMUtils.ViewMenuItem;
import SMUtils.pTimedEventGenerator;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.MouseEvent;
import sfpMenu.SfpComponent;
import sfpMenu.SfpMenu;
import sfpMenu.SfpSeparator;
import sfpMenu.SfpViewMenuItem;
import smlfr.SM_ViewAngle;
import smlfr.SM_ViewManager;


public class SM_Renderer extends SfPApplet{

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
	
	// the NEW menu:
	
	private SfpMenu 			menu;
	private SfpViewMenuItem[] 	menuViews;
	private SfpComponent		savePreview;
	
	// the old menu:
	private JPopupMenu				pMenu;
	private JMenuItem				pSavePreview;
	private ViewMenuItem[]			pMenuViews;
	
	
	public RendererUpdateThreadManager		update;
	private pTimedEventGenerator			tGen;
	
	private PImage[] layers;
	private float cr, cg, cb;
	
	private boolean b1 = true;
	private boolean b2 = true;
	private boolean b3 = true;
	private boolean b4 = true;
	private boolean b5 = true;
	
	
	private PGraphics				cropMask;
	private volatile PGraphics[]	wallGfxsAW;
	private	volatile PGraphics[]	wallGfxsLG;
	private volatile char[]			wallGfxsId;
	private boolean					colorPreview = false;

	private Skewmator				skewmator;

	private double 					aspect;
//	private float 					scale;
	private int 					ySize = 600;
	private int						photoX = 1200;
	private int						photoY = 800;
	private float					zoomFact;
	private int						xOffset, yOffset;
	
	public boolean 					setupRun = false;
	
	private boolean 				devGUI = false;
	private String					devBuff = "       ";
	
	private boolean					isBusy = false;
	private String					busyMessage = "";
	private int						busyQueueMax = 0;
	private int						busyQueueProgress = 0;
	private int						busyclock = 0;

	private boolean					isDrawingPreview = false;
	private String					previewStatus;
	private int[]					previewAdvance;
	
	private boolean					savetyDraw = false;
	private double					lastUpdate = 0;
	private double					lastUpdateColor = 0;
	private double					lastUpdateLights = 0;
	private double					lastUpdateArtworks = 0;

	double			Ac = 0, Lc =0;
	double			dA, dL;
	
	public SM_Renderer(SM_ViewManager _vm, SM_ViewAngle _defaultView, File _filePath, int _YSize) {
		super();
		vm = _vm;
		skewmator = new Skewmator(photoX, photoY);
		
		update = new RendererUpdateThreadManager(this);
		
//		SETUP THE MENU IF ITS A JMENU
//		initJPopupMenu();

		int c = color(123);
		
//		System.err.println(c);
		
		generalPath = _filePath;
		currentView = _defaultView;
		currentViewString = currentView.getName();
		setCurrentPath(currentViewString);
		
		ySize = _YSize;
		if( ySize > 600 ) ySize = 600;
		aspect = (double)((double)photoX / (double)photoY);

		this.registerMethod("post", this);
		
		skewmator.init();
		tGen = new pTimedEventGenerator(this, "onTimerEvent", false);
		tGen.setIntervalMs(1500);
		tGen.setEnabled(true);

	}
	
	public synchronized void changeView( SM_ViewAngle _view ) {
		
		
//		layers = null;
//		System.gc();
		
		currentView = _view;
		vm.setCurrentViewAngle( currentView.getName() );
		
		currentViewString = currentView.getName();
		setCurrentPath(currentViewString);
		setup();
		
		while( !setupRun) {
			System.out.println("RENDERER: setup is not running");
		}
		
		
		
		for( Character wc : _view.getWallChars() ) {
			
			vm.requestRendererUpdate(wc);
			
		}
		
//		SysInfo.printHeapStats();
		System.gc();
		
//		this.frame.repaint();
//		redraw();
	}
	
	
	private void setCurrentPath(String _view) {
		
		currentFileStub = _view.substring(2);
		currentFileStub = "/"+currentFileStub;
		currentPath = new File(  generalPath.getAbsolutePath()+currentFileStub   );
	}
	
//	@Override
//	public void init() {
//		super.init();
//	}
	
	public void initSpfMenu() {
		
		
		menu = new SfpMenu(this, "");

		SfpComponent title = new SfpComponent(Lang.selectView);
		title.setEnabled(false);
		menu.addSfpComponent(title);
		
		menu.addSfpComponent(new SfpSeparator());
		
		// SUB-MENU TEST CODE:::
		
//		SfpMenu sub = new SfpMenu(this, "subtles menu...");
//		
//		SfpComponent s1 = new SfpComponent("s1 - test");
//		SfpComponent s2 = new SfpComponent("s2 - test");
//		SfpComponent s3 = new SfpComponent("s3 - test");
//		SfpComponent s4 = new SfpComponent("s4 - test");
//		
//		sub.addSfpComponent(s1);
//		sub.addSeparator();
//		sub.addSfpComponent(s2);
//		
//		sub.addSfpComponent(s3);
//		sub.addSfpComponent(s4);
//
//		SfpMenu sub2 = new SfpMenu(this, "subtles menu...");
//		
//		SfpComponent s21 = new SfpComponent("s21 - test");
//		SfpComponent s22 = new SfpComponent("s22 - test");
//		
//		sub2.addSfpComponent(s21);
//		sub2.addSfpComponent(s22);
//		
//		sub.addSfpComponent(sub2);
//		
//		menu.addSfpComponent(sub);
		
		// END TEST
		
		menuViews = new SfpViewMenuItem[vm.getNumberOfViewAngles()];
		int i=0;
		for( String n : vm.getViewAngleRealNames()) {
			menuViews[i] = new SfpViewMenuItem(n, vm.getViewAngle(i).getName());
			menuViews[i].addEventListener(vm);
			menuViews[i].addMouseListener(vm.getRoomArrView());
			menu.addSfpComponent(menuViews[i]);
			i++;
		}

		menu.addSfpComponent(new SfpSeparator());

		savePreview = new SfpComponent(Lang.savePreviewImage);
		savePreview.addEventListener(vm);
		menu.addSfpComponent(savePreview);
		
		menu.pack();
				
				
				
	}
	
	public void initJPopupMenu() {
		
		

		// old menu code here:
		
		pMenu = new JPopupMenu();
		JMenuItem title = new JMenuItem(Lang.selectView);
		title.setFont(title.getFont().deriveFont(Font.ITALIC));
		title.setEnabled(false);
		pMenu.add(title);
		pMenu.add(new JSeparator());
		pMenuViews = new ViewMenuItem[vm.getNumberOfViewAngles()];
		int i = 0;
		for( String n : vm.getViewAngleRealNames()) {
			pMenuViews[i] = new ViewMenuItem(n, vm.getViewAngle(i).getName());
			pMenuViews[i].addActionListener(vm);
			pMenuViews[i].addMouseListener(vm.getRoomArrView());
			pMenu.add(pMenuViews[i]);
			i++;
		}
		
		pMenu.add(new JSeparator());
		pSavePreview = new JMenuItem(Lang.savePreviewImage);
		pSavePreview.addActionListener(vm);
		pMenu.add(pSavePreview);
	}
	
	@Override
	public void setup() {
		
		setupRun = false;
		
//		size(800,600);
		
		frame.setTitle(currentViewString.substring(2));
		
		
		layers = new PImage[7];
		layers[0] = loadImage(currentPath.getAbsolutePath()+currentFileStub+"_Hintergrund.png");
		
//		scale = (float)ySize / (float)layers[0].width;
		
		zoomFact = 1.0f;
		xOffset = 0;
		yOffset = 0;
		
		String w = currentView.getWallCharsAsString();
		
		wallGfxsAW = new PGraphics[w.length()];
		wallGfxsLG = new PGraphics[w.length()];
		wallGfxsId = new char[w.length()];
		
		for( int i=0; i< w.length(); i++ ) {
			
			wallGfxsAW[i]   = createGraphics(layers[0].width, layers[0].height);
			wallGfxsAW[i].beginDraw();
			wallGfxsAW[i].background(0,0);
			wallGfxsAW[i].endDraw();
			
			wallGfxsLG[i]   = createGraphics(layers[0].width, layers[0].height);
			wallGfxsLG[i].beginDraw();
			wallGfxsLG[i].background(0,0);
			wallGfxsLG[i].endDraw();
			
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
//		layers[2] = new PImage(layers[0].width,layers[0].height); // VIEWMANAGER
//		layers[3] = new PImage(layers[0].width,layers[0].height); // VIEWMANAGER
		layers[4] = new PImage(layers[0].width,layers[0].height);
		layers[5] = loadImage(currentPath.getAbsolutePath()+currentFileStub+"_Farbe.png");
		layers[6] = loadImage(currentPath.getAbsolutePath()+currentFileStub+"_Schatten.png");

		

//		aspect = (double)((double)layers[0].width / (double)layers[0].height);


		
//		for( PImage l : layers) {
//			l.resize( (int)(ySize * aspect), ySize);
//		}
//		for( PGraphics wg : wallGfxsAW ) {
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

		updateRoomColorLayer(null, null, null);
		

		// clear memory:
		layers[6] = null;
//		layers[5] = null;
		
		

		noLoop();
//		smooth();
		setupRun = true;
		
		
	}
	
	public void setBusy( boolean b, String msg) {
		isBusy = b;
		busyMessage = msg;
	}
	
	/**
	 * 
	 * 
	 * @param _previewColor if null no roomColor<b>Preview</b> will be drawn
	 * @param _previewWall if null no WallColor<b>Preview</b> will be drawn
	 * @param _previewWallColor
	 */
	protected synchronized void updateRoomColorLayer( Integer _previewColor, Character _previewWall, Integer _previewWallColor ) {
		
		
		
		int w = layers[5].width;
		int h = layers[5].height;
		
		colorPreview = false;
		int c;
		
		if( _previewColor == null ) {
			c = vm.getRoomColor();
		}
		else {
			c = _previewColor;
			colorPreview = true;
		}


		Color clr = new Color(c);
		
		cr= clr.getRed();
		cg = clr.getGreen();
		cb = clr.getBlue();
		
		PGraphics t = createGraphics(w,h);
		t.beginDraw();
		
		t.pushStyle();
		t.tint(cr,cg,cb);
		t.image(layers[0],0,0,w,h);
		t.popStyle();
		
		// Draw Single Wall Colors:
		
		for(char wc : currentView.getWallChars() ) {
						
			boolean hasWallColor  = vm.hasWallColor(wc);
			boolean isPreviewWall = ( (Character)wc == _previewWall);
			
//			if( isPreviewWall ) colorPreview = true;
			
			
//			System.out.println("isPrreviewWall calculated for \""+wc+"\" and previewWall \""+_previewWall+"\" ---> "+isPreviewWall);
			
			if( hasWallColor || isPreviewWall ) {

				Color col ;
				
				if( isPreviewWall ) {
					col = new Color(_previewWallColor);
					colorPreview = true;
				} else {
					col = new Color(vm.getWallColor(wc));
				}
				
				 
				
				
				// offset skew values to display values
				
				Float[] skewValues = new Float[10];
				
				boolean useCrop = false;
				if( currentView.getWallCrop((Character)wc)[0] != -1 ) useCrop = true;
				
				for (int i = 0; i < skewValues.length; i++) {
					
					if( !useCrop ) {
						skewValues[i] = currentView.getWallSkew((Character)wc)[i];
					} else {
						skewValues[i] = currentView.getWallCrop((Character)wc)[i];
					}
				}
				
				Float xOffset = ( (skewValues[0] - photoX) / 2 );
				Float yOffset = ( (skewValues[1] - photoY) / 2 );

				
				for (int i = 0; i < skewValues.length -1; i+=2) {
					
					skewValues[i] 	-= xOffset;
					skewValues[i+1]	-= yOffset;
				}

				// zeichnen der Farbe f�r eine Wand::
				
				PGraphics wcGfx = createGraphics(w,h);
				
				wcGfx.beginDraw();
				wcGfx.tint(col.getRed(), col.getGreen(), col.getBlue());
				wcGfx.image(layers[0], 0, 0, w, h);
				wcGfx.endDraw();
				
				PGraphics shape = createGraphics(w, h);
				shape.beginDraw();
				shape.fill(0);
				shape.stroke(0);
				shape.strokeWeight(1);
				shape.beginShape();
				for (int i = 2; i < skewValues.length -1; i+=2) {
					shape.vertex(skewValues[i], skewValues[i+1]);
				}
				shape.vertex(skewValues[2], skewValues[3]);
				shape.endShape();
				shape.endDraw();
				
				manualWallColorMask(shape, layers[4]);
				
				manualWallColorMask(wcGfx, shape.get());
				
				
				// gfx auf "t" zeichnen

				t.image(wcGfx,0,0,w,h);

				
			}
		}
		
		g.removeCache(t);
		t.endDraw();
		
		layers[1] = t.get();
		
//		layers[1].filter(DILATE);
//		layers[5].filter(DILATE);
		
		layers[1].mask(layers[5]);
		
		if(setupRun) ;//redraw();
		
		lastUpdate = millis();
		
	}
	
	protected synchronized void updateLightsLayer( char _wallChar) {
		
		double lmili = millis();
		
		System.out.println("RENDERER: LIGHTS UPDATE: "+ _wallChar +" start on Thread " + Thread.currentThread().getName());
		
		if( currentView.getWallCharsAsString().contains(""+_wallChar) ) {
			
			
			Float[] skewValues = currentView.getWallSkew(_wallChar);
			
			PImage wallGfx = null;

			
			wallGfx = vm.getLightsGfx(_wallChar);
			
			if( wallGfx != null) {
				
				
				for(int i=0; i<wallGfxsLG.length; i++) {
					if( wallGfxsId[i] == _wallChar ) {
						
												
						int wgWidth = wallGfxsLG[i].height;
						
						wallGfxsLG[i].beginDraw();
						wallGfxsLG[i].clear();
						wallGfxsLG[i].image(skewmator.skewToWall(wallGfx, skewValues, 0, wgWidth, SkewMode.LIGHTS), 0,0);
						g.removeCache(wallGfxsLG[i]);
						wallGfxsLG[i].endDraw();

						if( currentView.isWallCrop(_wallChar) ) {
							
							Float[] cropValues = currentView.getWallCrop(_wallChar);
							
							cropMask = skewmator.drawCropImage(cropValues);
							cropMask.resize(wallGfxsLG[i].width, wallGfxsLG[i].height);
							g.removeCache(cropMask);
							manualMask(wallGfxsLG[i], cropMask);

						}
						
						// mask the wallGfx with shadowmask to make it hide behind obstacles

						manualWallColorMask(wallGfxsLG[i], layers[4]);
					}
				}
			}
			
			
		}

		
		
		double endmili =  (millis() - lmili);
		
	System.out.println("RENDERER: LIGHTS UPDATE: "+ _wallChar +" end. time:" + endmili);
	
	Lc++;
	
	dL = dL * ( (Lc-1) / Lc );
	
	dL += endmili * ( 1 / Lc );
	
	
	
//	redraw();
	lastUpdate = millis();
	}
	
	protected synchronized void updateArtworksLayer( char _wallChar ) {
		updateArtworksLayer(_wallChar, SkewMode.STANDART);
	}
	
	protected synchronized void updateArtworksLayer( char _wallChar, SkewMode skewMode) {
		
		double amili = millis();
		
		System.out.println("RENDERER: ARTWORK UPDATE: start "+_wallChar);
		
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

				if( skewMode == SkewMode.FORCE_HIGH ) {
					wallGfx = vm.getWallGfxHiRes(_wallChar, shadowOfset);
				} else {
					
					wallGfx = vm.getWallGfx(_wallChar, shadowOfset);
				}
				

				
				if( /*artworksLayer != null && */ wallGfx != null) {
					
					
					for(int i=0; i<wallGfxsAW.length; i++) {
						if( wallGfxsId[i] == _wallChar ) {
							
														
							int wgWidth = wallGfxsAW[i].height;
							
							wallGfxsAW[i].beginDraw();
							wallGfxsAW[i].clear();
//							wallGfxsAW[i].image(wallGfx,0,0);
							wallGfxsAW[i].image(skewmator.skewToWall(wallGfx, skewValues, 0, wgWidth, skewMode), 0,0);
							g.removeCache(wallGfxsAW[i]);
							wallGfxsAW[i].endDraw();

							if( currentView.isWallCrop(_wallChar) ) {

								double cmili = millis();
								
								System.out.println("RENDERER: ARTWORK UPDATE: doing crop");
								
								Float[] cropValues = currentView.getWallCrop(_wallChar);
//								float f = cropValues[0] / photoX;
								
								cropMask = skewmator.drawCropImage(cropValues);
								cropMask.resize(wallGfxsAW[i].width, wallGfxsAW[i].height);
								g.removeCache(cropMask);
								manualMask(wallGfxsAW[i], cropMask);
								
//								PImage maskedImg = wallGfxsAW[i].get();
//								maskedImg.mask(cropMask);
								
								
//								wallGfxsAW[i].beginDraw();
//								wallGfxsAW[i].clear();
//								wallGfxsAW[i].image(maskedImg,0,0);
//								wallGfxsAW[i].endDraw();

								System.out.println("RENDERER: ARTWORK UPDATE: end crop. time: " +( millis() - cmili));
							}
							
							// mask the wallGfx with shadowmask to make it hide behind obstacles

							manualWallColorMask(wallGfxsAW[i], layers[4]);
							
							
						}
					}
					
					
//					artworksLayer.image(skewmator.skewToWall(wallGfx, values, 0, ySize), 0,0);

					
				}
//				artworksLayer.beginDraw();
//				artworksLayer.clear();
//				for( PGraphics wg : wallGfxsAW ) {
//					artworksLayer.image(wg,0,0);
//				}
//				artworksLayer.endDraw();
//				layers[3] = artworksLayer;
//
				
				
			}
//		}
			
			double endmili = ( millis() - amili);
			
		System.out.println("RENDERER: ARTWORK UPDATE: "+ _wallChar + " end. time: " + endmili);
//		redraw();
		
		Ac++;
		
		dA = dA * ( (Ac-1) / Ac );
		
		dA += endmili * ( 1 / Ac );
		
		lastUpdate = millis();
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
	
	
	private void manualWallColorMask(PGraphics display, PImage mask) {
		
		
		  mask.loadPixels();
		  display.beginDraw();
		  display.loadPixels();
		  for (int i = 0; i < display.pixels.length; i++) {
		 
		    int d = display.pixels[i];
		 
		    // mask apha
		    int m_a = (mask.pixels[i] >> 24) /*>> 16*/ & 0xFF;
		  
		    // display alpha
		    int d_a = (d >> 24) & 0xFF;
		  
		    // output alpha (make display transparent where mask is 100% transparent)

		    int o_a;
		    if( m_a == 0 ) {
		    	o_a = 0;
		    } else {
		    	o_a = d_a;
		    }
		    
		    
		    display.pixels[i] = (o_a << 24) | (0x00FFFFFF & d);

		  }
		  display.updatePixels();
		  display.endDraw();
		}
	
	
	
	@Override
	public void redraw() {
//		System.out.println("RENDERER: DRAW FLAG: "+this.redraw);
//		System.out.println("RENDERER: REDRAW CALLED");
		super.redraw();
//		System.out.println("RENDERER: DRAW FLAG: "+this.redraw);
	}
	
	/**
	 * set boolean: savetyDraw: true
	 */
	public void threadManagerRecall() {
		
		savetyDraw = true;
		isBusy = false;
		busyQueueMax = 0;
		busyQueueProgress = 0;
	}
	
	public void increaseBusyQueueMax() {
		busyQueueMax++;
	}
	
	public void setBusyQueueCurrent(int currentQueueLength) {
		busyQueueProgress = busyQueueMax - currentQueueLength;
		busyclock = 0;
	}
	
	public void post() {
//		System.out.println("RENDERER: POST: savetydraw: " + savetyDraw);
//		System.out.println("RENDERER: POST:     redraw: " + this.redraw);
		
//		System.out.println("RENDERER: POST: repaint will be ordered");
		
		if(savetyDraw && update.getQueueLength() <= 0 ) {
			super.redraw();
			savetyDraw = false;
			isBusy = false;
			busyQueueMax = 0;
			busyQueueProgress = 0;
		}
		
//		this.frame.repaint();  // TODO run from EDT
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				
//			}
//		});
		
//		System.out.println("RENDERER: POST: savetydraw: " + savetyDraw);
//		System.out.println("RENDERER: POST:     redraw: " + this.redraw);
	}
	
	public void onTimerEvent() {

		if( update.getQueueLength() <= 0 && savetyDraw ) {
			this.redraw();
			savetyDraw = false;
		}
		if( update.getQueueLength() <= 0) {
			this.setBusy(false, Lang.rendererBusy);
			this.redraw();
			
//			if( millis() - lastUpdate > 4000 ) {
//				for( char w : wallGfxsId ) {
//					updateArtworksLayer(w);
//					updateLightsLayer(w);
//				}
//				updateRoomColorLayer(null, null, null);
//			}
		}
//		System.out.println("TIMER tiiittitmtem");
//		this.frame.pack();
//		this.frame.repaint();
//		if( this.frame.getIgnoreRepaint() ) System.out.println("ignores repaint");
	}
	
	@Override
	public /*synchronized*/ void draw(){

		
		background(255);
		
		
		int displW = (int)(width  * zoomFact);
		int displH = (int)(height * zoomFact);
		
		int xOff = xOffset;
		int yOff = yOffset;
		
//		System.out.println("RENDERER: DRAW: base");
		
		// draw Base

		if(b1) {
			
			while( layers[0] == null ) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					System.err.println("THIS EXCEPTION GOT CAUGHT: ");
					e.printStackTrace();
				}
				System.out.println("RENDERER: Waiting for layers [9]...");
			}
			
			blendMode(BLEND);
			image(layers[0], xOff, yOff,displW,displH);
			g.removeCache(g);
			
		}

//		System.out.println("RENDERER: DRAW: color");
		
		// draw Farbe

		if(b2) {
			
			while( layers[1] == null ) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					System.err.println("THIS EXCEPTION GOT CAUGHT: ");
					e.printStackTrace();
				}
				System.out.println("RENDERER: Waiting for layers [1]...");
			}
			
			
			pushStyle();
			blendMode(BLEND);
//			tint(vm.getRoomColor());
			image(layers[1], xOff, yOff,displW,displH);
			g.removeCache(g);
			//blend(layers[1], 0, 0, width, height, 0, 0, width, height, MULTIPLY);
			popStyle();
		}

//		System.out.println("RENDERER: DRAW: lights");
		
		// draw Licht

		if(b3 && !colorPreview) {
			pushStyle();
//			tint(255, 70);
						
			tint(vm.getRoomColor());
			
			int id = 0;
			for( PGraphics lg : wallGfxsLG) {
				while(lg == null) { try { System.out.println("RENDERER: waiting on lights");Thread.sleep(100); } catch(Exception e){
					System.err.println("Thread.sleep(): waiting for lights FAILED");
					e.printStackTrace();}
				}
				
				if( lg != null ) {
					try {
						
						boolean wallColor = vm.hasWallColor(wallGfxsId[id]);
						
						if( wallColor ) {
							pushStyle();
							tint(vm.getWallColor(wallGfxsId[id]));
						}
						
						image(lg, xOff, yOff,displW,displH);
						
						if( wallColor ) popStyle();
						
					} catch( Exception e ) {
						if( e.getClass() == java.lang.NullPointerException.class ) {
							System.err.println("RENDERER: DRAW: lights image is null - NullPointer");
						}
					}
					
				}
				
				
				g.removeCache(g);
				id++;
			}
			
			g.removeCache(g);
			popStyle();
		}

//		System.out.println("RENDERER: DRAW: artworks");
		
		// draw Bild

		if(b4) {
			pushStyle();
			blendMode(BLEND);
			for( PGraphics wg : wallGfxsAW) {
				if( wg != null) try{
					image(wg, xOff, yOff,displW,displH);
				} catch (Exception e) { System.err.println("THIS EXCEPTION GOT CAUGHT: ");e.printStackTrace();}
				
				else System.err.println("A WALL GRAPHICS WAS null IN RENDERER DRAW");
				g.removeCache(g);
			}

			popStyle();
		}

		
//		System.out.println("RENDERER: DRAW: shadow");
		
		// draw Schatten

		if(b5) {
			pushStyle();
			blendMode(BLEND);
			tint(255, 65);
			while( layers[4] == null ) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
				System.out.println("RENDERER: Waiting for layers [4]...");
			}
			image(layers[4], xOff, yOff, displW, displH);
			g.removeCache(g);
			popStyle();
		}

//		System.out.println("RENDERER: DRAW: anim+gui");
		
		drawGUI();
		if( frameCount % 3 == 0) System.gc(); 
		

//		menu.mousePos(mouseX, mouseY);
		menu.draw();
		
		if( !menu.isVisible() && !menu.isAnimating() ) noLoop();
		
	}
	
	
	void drawGUI() {
		
		
		// draw busy anim

		if( isBusy || savetyDraw ) {
			
			
			pushStyle();
			
			smooth();
			noStroke();
			
			if( !savetyDraw ) {
				fill(150);
			} else {
				fill(20,20,200);
			}
			rect(0, height -16, width, height);
			
			fill(20,20,200);
			float fake = map( busyclock, 0, 1, 0, ((busyQueueMax - busyQueueProgress)/2));
			rect(0, height-16, map(busyQueueProgress, 0, busyQueueMax, 0, width) + fake, height);
			
			fill(255);
			
			String s = busyMessage;
			busyclock++;
			int mod = busyclock % 5;

			for(int i = 0; i<mod; i++) {
				s += " .";
			}
			text(s, 10, height - 4);
			
			fill(255,5);
			
			rect(0,0,width,height);
			
			popStyle();
		}
		
		
		if( isDrawingPreview) {
			
			pushStyle();
			noStroke();
			fill(150);
			rect(0, height -16, width, height);
			
			fill(20,20,200);
			rect(0, height-16, map(previewAdvance[0], 0, previewAdvance[1], 0, width), height);
			
			String s = previewStatus;
			int mod = frameCount % 5;

			for(int i = 0; i<mod; i++) {
				s += " .";
			}
			
			fill(255);
			text(s, 10, height - 4);

			popStyle();
		}
		
		
		// draw developer GUI
		
		if (devGUI) {
			pushStyle();
			stroke(0);
			blendMode(BLEND);
			if (b1)
				fill(20, 180, 20);
			else
				fill(180, 20, 20);
			//rect(10,10,15,15);
			text("1: basis", 10, 20);
			if (b2)
				fill(20, 180, 20);
			else
				fill(180, 20, 20);
			//rect(30,10,15,15);
			text("2: farbe", 10, 40);
			if (b3)
				fill(20, 180, 20);
			else
				fill(180, 20, 20);
			//rect(50,10,15,15);
			text("3: licht", 10, 60);
			if (b4)
				fill(20, 180, 20);
			else
				fill(180, 20, 20);
			//rect(70, 10, 15,15);
			text("4: bild", 10, 80);
			if (b5)
				fill(20, 180, 20);
			else
				fill(180, 20, 20);
			//rect(90, 10, 15,15);
			text("5: schatten", 10, 100);
			
			fill( frameCount%2 * 255);
			rect( 80, 10, 20, 20 );
			
			fill(255,0,0);
			text("Lights time: " + dL, 120,20);
			text("Artwks time: " + dA, 120,40);
			
			
			popStyle();
		}
	}

	
	public boolean renderPreviewImage( String filename) {
		
		isDrawingPreview = true;
		previewAdvance = new int[2];
		previewStatus = Lang.busyRenderingPreviewToFile;
		
		for( int i=0; i< currentView.getWallChars().length + wallGfxsLG.length + 6; i++ ) {
			previewAdvance[1] = i;
		}
		int pa = 0;
		previewAdvance[0] = pa++;
		
		int w = layers[0].width;
		int h = layers[1].height;
		
		PGraphics img = createGraphics(w, h);
		
		img.beginDraw();
		
		if(b1) {
			img.blendMode(BLEND);
			img.image(layers[0], 0, 0, w, h);
//			img.removeCache(img);
			
		}
		
		previewAdvance[0] = pa++;


		// draw Farbe

		if(b2){
			img.pushStyle();
			img.blendMode(BLEND);
//			tint(vm.getRoomColor());
			img.image(layers[1], 0, 0, w, h);
//			img.removeCache(img);
			//blend(layers[1], 0, 0, width, height, 0, 0, width, height, MULTIPLY);
			img.popStyle();
		}

		previewAdvance[0] = pa++;

		// draw Licht

		if(b3) {
			img.pushStyle();
			//blendMode(ADD);
			img.tint(vm.getRoomColor());
			
			int id = 0;
			for( PGraphics lg : wallGfxsLG) {
				
				boolean wallColor = vm.hasWallColor(wallGfxsId[id]);
				
				if( wallColor ) {
					img.pushStyle();
					img.tint(vm.getWallColor(wallGfxsId[id]));
				}
				
				img.image(lg, 0, 0,w,h);
				
				if( wallColor ) img.popStyle();
				
				id++;
				previewAdvance[0] = pa++;
			}
//			img.removeCache(img);
			img.popStyle();
		}

		previewAdvance[0] = pa++;
		
		// prepare high res artworks-layer:
		
		for( char c : currentView.getWallChars()) {
			updateArtworksLayer( c, SkewMode.FORCE_HIGH);
			previewAdvance[0] = pa++;
		}
		

		// draw Bild

		if(b4) {
			img.pushStyle();
			img.blendMode(BLEND);
			for( PGraphics wg : wallGfxsAW) {
				img.image(wg, 0, 0, w, h);
//				img.removeCache(img);
			}

			img.popStyle();
		}
		
		previewAdvance[0] = pa++;

		// draw Schatten

		if(b5) {
			img.pushStyle();
			img.blendMode(BLEND);
			img.tint(255, 65);
			img.image(layers[4], 0, 0, w, h);
//			img.removeCache(img);
			img.popStyle();
		}
		
		previewAdvance[0] = pa++;
		
		img.endDraw();
		
		img.save(filename);
		
		previewAdvance[0] = pa++;

		isDrawingPreview = false;
		return true;
	}
	
	
	public boolean isSetupRun() {
		return setupRun;
	}
	
	public String getCurrentViewString() {
		return currentViewString;
	}
	
	/* (non-Javadoc)
	 * @see processing.core.PApplet#mousePressed()
	 */
	/* (non-Javadoc)
	 * @see processing.core.PApplet#mousePressed()
	 */
	@Override
	public void mousePressed() {
		
		if( setupRun && mouseButton == LEFT && menu.isVisible() ) {
			
			int val = menu.doClick(mouseX, mouseY);
			
			System.out.println("RENDERER: MENU: returnded " + val);
			
//			if( val == -1 ) menu.close();

			
		} else if( mouseButton == RIGHT && setupRun) {
			
			// the new Menu shows like this:
			
			menu.openAt(mouseX, mouseY, 1);
			
			loop();
			frameRate(60);
			
			// the old Menu shows like this.
			
//			for( JMenuItem m : pMenuViews) {
//				if( m.getActionCommand().equalsIgnoreCase(currentViewString.substring(currentViewString.lastIndexOf('_')+1))) {
//					m.setFont(m.getFont().deriveFont(Font.ITALIC));
//					m.setEnabled(false);
//				} else {
//					m.setFont(m.getFont().deriveFont(Font.PLAIN));
//					m.setEnabled(true);
//				}
//			}
//			pMenu.show(this, mouseX, mouseY);
		}
	}

	
//	@Override
	public void mouseDragged() {
		xOffset += mouseX-pmouseX;
		yOffset += mouseY-pmouseY;

		offsetBounds();
		redraw();
	}
	
	
	@Override
	public void mouseWheel(MouseEvent event) {
//	public void MyMouseWheel(float eventCount) {
		
		float e = event.getCount();
//		float e = eventCount;
		
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

		
		if(zoomFact < 1.0f) {
			zoomFact = 1.0f;
			xOffset = 0;
			yOffset = 0;
		}
		
		
		offsetBounds();

		redraw();
	}
	
	private void offsetBounds() {
		if((xOffset+(width*zoomFact)) < width ) xOffset += width - (xOffset+(width*zoomFact));
		if( xOffset > 0 ) xOffset = 0;
		
		if((yOffset+(height*zoomFact)) < height ) yOffset += height - (yOffset+(height*zoomFact));
		if( yOffset > 0 ) yOffset = 0;
	}
	
 	public boolean isMenuOpen() {
// 		return ((RendererFrame)frame).isMenuOpen();
//		return pMenu.isVisible();
 		return menu.isVisible();
	}

	@Override
	public void keyPressed() {
		if( keyCode == ESC) {
			key = 0;
		} else {
			
			
			
			try {
				devBuff = devBuff.substring(1, 6);
				devBuff += key;
			if( devBuff.equalsIgnoreCase("info++")) {
				SysInfo.displayMessage();
			} else if( devBuff.equalsIgnoreCase("about+")) {
				SysInfo.displayVersionInfo();
			}
			} catch(Exception e) {
				System.err.println("THIS EXCEPTION GOT CAUGHT: ");
				e.printStackTrace();
			}
			
			
			if (devGUI) {
				if (key == '1')
					b1 = !b1;
				if (key == '2')
					b2 = !b2;
				if (key == '3')
					b3 = !b3;
				if (key == '4')
					b4 = !b4;
				if (key == '5')
					b5 = !b5;
			}
			if( key == 'u') {
				for( char w : wallGfxsId ) {
					updateArtworksLayer(w);
					updateLightsLayer(w);
				}
				updateRoomColorLayer(null, null, null);
			}
			if( key == 'i') {
				
			}
			redraw();
		}
	}

	
	@Override
	public Dimension getSize() {
		return new Dimension((int)(ySize * aspect), ySize);
	}

	
	
	
	public char[] getCurrentWallChars() {

		return currentFileStub.substring(currentFileStub.lastIndexOf('_')+1).toCharArray();
	}
	
	
	public void prepareFrameForClosing() {
		
//		tStop = true;
//		tGen.setEnabled(false);
//		tGen.setEnabled(false);
		
//		frame.setVisible(false);
		
		
	}
	
	
	
	@Override
	public void dispose() {
		System.err.println("Renderer goodbye...1");
		
//		tGen.dispose();
		
		
		if( tGen.isEnabled() ) tGen.setEnabled(false);
		
		tGen.dispose();
		
		frame.dispose();

		super.dispose();

	}



}
