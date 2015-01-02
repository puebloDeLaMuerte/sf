package sfrenderer;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import artworkUpdateModel.ArtworkUpdateEvent;
import artworkUpdateModel.ArtworkUpdateListener;

import SMUtils.Lang;
import SMUtils.Skewmator;
import SMUtils.ViewMenuItem;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import smlfr.SM_ViewAngle;
import smlfr.SM_ViewManager;


public class SM_Renderer extends PApplet {

	private SM_ViewManager			vm;
	private JFrame					myFrame;
	
	private SM_ViewAngle			currentView;
	private String					currentViewString;
	private File					generalPath;
	private File					currentPath;
	private String					currentFileStub;
	
	private JPopupMenu				pMenu;
	private ViewMenuItem[]			pMenuViews;				
	
	private PImage[] layers;
	private float r, g, b;
	private boolean b1 = true;
	private boolean b2 = true;
	private boolean b4 = true;
	private boolean b3 = false;
	private boolean b5 = true;

	private PGraphics 				texture;
	private PImage 					textureIMG;
	private PImage 					bild ;
	
	private PGraphics				artworksLayer;

	private double 					aspect;
	private float 					scale;
	private Skewmator				skewmator;

	private int 					ySize = 600;
//	private int 					tint  = 30;
	
	public boolean 					setupRun = false;

	private PVector	tempoOfset;
	private PVector tempLO;
	private PVector tempRO;
	private PVector tempRU;
	private PVector tempLU;
	
	public SM_Renderer(SM_ViewManager _vm, SM_ViewAngle _defaultView, File _filePath) {
		super();
		vm = _vm;
		skewmator = new Skewmator();
		skewmator.init();
		initMenu();
		
		
		generalPath = _filePath;
		currentView = _defaultView;
		currentViewString = currentView.getName();
		setCurrentPath(currentViewString);
		
	}
	
	public void changeView( SM_ViewAngle _view ) {
		
		System.out.println("\n\n\nVIEW CHANGED in rendddd   "+_view.getName());
		
		layers = null;
		System.gc();
		
		currentView = _view;
		currentViewString = currentView.getName();
		setCurrentPath(currentViewString);
		setup();
		redraw();
	}
	
	
	private void setCurrentPath(String _view) {
		
		currentFileStub = _view.substring(2);
		currentFileStub = "/"+currentFileStub;
		currentPath = new File(  generalPath.getAbsolutePath()+currentFileStub   );
	}
	
	public void init(JFrame _frame) {
//		this.frame = _frame;
		myFrame = _frame;
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
		
		myFrame.setTitle(currentViewString.substring(2));
		layers = new PImage[7];
		layers[0] = loadImage(currentPath.getAbsolutePath()+currentFileStub+"_Hintergrund.png");
		
		scale = (float)ySize / (float)layers[0].width;
		
		System.out.println("\n\nthe scale is "+scale);
		
		artworksLayer = createGraphics(layers[0].width, layers[0].height);
		artworksLayer.beginDraw();
		artworksLayer.background(50,190,0);
		artworksLayer.endDraw();


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
//		layers[3] = new PImage(layers[0].width,layers[0].height); // VIEWMANAGER
		layers[3] = artworksLayer;
		layers[4] = new PImage(layers[0].width,layers[0].height);
		layers[5] = loadImage(currentPath.getAbsolutePath()+currentFileStub+"_Farbe.png");
		layers[6] = loadImage(currentPath.getAbsolutePath()+currentFileStub+"_Schatten.png");

		

		aspect = (double)((double)layers[0].width / (double)layers[0].height);
//		size( (int)(ySize * aspect), ySize);
//		frame.setSize( (int)(ySize * aspect), ySize);

		
		for( PImage l : layers) {
			l.resize( (int)(ySize * aspect), ySize);

		}
	
		// Skew Example
//		layers[3] = skewImage(layers[3], new PVector(111,142), new PVector(257,210), new PVector(257, 378), new PVector(111, 483));

		

		// schatten Maske:

		PGraphics s = createGraphics(layers[6].width,layers[6].height);
		s.beginDraw();
		s.fill(0);
		s.endDraw();
		layers[4]  = s.get();
		layers[6].filter(INVERT);
		layers[4].mask(layers[6]);
		layers[4].filter(ERODE);

		// Farbe:

		PGraphics t = createGraphics(layers[5].width,layers[5].height);
		r=0;
		g=104;
		b=49;

		t.beginDraw();
		t.tint(r,g,b);
		t.image(layers[0],0,0,layers[5].width,layers[5].height);
		t.endDraw();
		layers[1] = t.get();
		layers[1].filter(DILATE);
		layers[5].filter(DILATE);
		layers[1].mask(layers[5]);

		noLoop();
		smooth();
		setupRun = true;
		
		
	}

	public void updateArtworksLayer() {
		
		System.out.println("updating...");
		
		artworksLayer.clear();
		
		for( Character wc : currentView.getWallChars()) {
			System.out.println("  Asking Wall "+wc+"...");
			if( vm.isWallGfxReady(wc) ) {
				System.out.println("  painting...");
				artworksLayer.beginDraw();
				
				PImage wallGfx = vm.getWallGfx(wc);
				
//				wallGfx = skewImage(wallGfx, new PVector(-10, 0), new PVector(560, 20), new PVector(560, 207), new PVector(20, 227));
				
				int bezugX = 6000;
				int bezugY = 4000;
//				
				PVector loD = new PVector(2400,1707);
				PVector roD = new PVector(2836,1911);
				PVector ruD = new PVector(2837,2065);
				PVector luD = new PVector(2400,2245);
//				
				PVector loK = new PVector(3168,1909);
				PVector roK = new PVector(4608,1130);
				PVector ruK = new PVector(4608,2716);
				PVector luK = new PVector(3168,2073);
				
				
				if( artworksLayer != null && wallGfx != null) {
					if(wc == 'd' || wc == 'D') artworksLayer.image(skewmator.skewToWall(wallGfx, loD, roD, ruD, luD, bezugX, bezugY, 0, ySize),0,0);
					if(wc == 'k' || wc == 'K') artworksLayer.image(skewmator.skewToWall(wallGfx, loK, roK, ruK, luK, bezugX, bezugY, 0, ySize ),0,0);

					System.out.println("  painted.");
				}
				artworksLayer.endDraw();
				layers[3] = artworksLayer;
			}
		}
		System.out.println("end update.\n");
		redraw();
	}
	
	private PImage skewBox(PImage _i, float[] _wallskew){
		
		PVector lo = new PVector(2400,1707);
		PVector ro = new PVector(2836,1911);
		PVector ru = new PVector(2837,2065);
		PVector lu = new PVector(2400,2245);
		
		int baseX = 1200;
		int baseY = 800;
		
		int bezugX = 6000;
		int bezugY = 4000;
		
		int fact = bezugX / baseX;
		
		System.out.println("fact: "+fact);
		System.out.println("scle: "+scale);
		
		PVector stp1 = new PVector(      baseX * ((fact-1)/2)  ,     baseY * ((fact-1)/2)    );
		System.out.println("stp1: "+stp1.x+" x "+stp1.y);
		
		stp1.mult(scale);
		lo.sub(stp1);
		tempLO = lo;
		tempLO.mult(scale);
		ro.sub(stp1);
		tempRO = ro;
		tempRO.mult(scale);
		ru.sub(stp1);
		tempRU = ru;
		tempRU.mult(scale);
		lu.sub(stp1);
		tempLU = lu;
		tempLU.mult(scale);
		
		PVector stp2 = new PVector(lo.x, lo.y);
		stp2.sub(stp1);
		System.out.println("stp2: "+stp2.x+" x "+stp2.y);
		
		tempoOfset = stp2;
		
		PVector[] ar = new PVector[] {lo, ro, ru, lu};
		
		// smallestX
		PVector smallestx = lo;
		for( int i = 1; i<ar.length; i++ ) {
			if( ar[i].x < smallestx.x) smallestx = ar[i];
		}
		System.out.println("smallest x: "+ smallestx.x+" x "+smallestx.y);
		
		// biggestx
		PVector biggestx = lo;
		for( int i = 1; i<ar.length; i++ ) {
			if( ar[i].x > biggestx.x) biggestx = ar[i];
		}
		System.out.println("biggest x: "+ biggestx.x+" x "+biggestx.y);
		
		// smallestY
		PVector smallesty = lo;
		for( int i = 1; i<ar.length; i++ ) {
			if( ar[i].y < smallesty.y) smallesty = ar[i];
		}
		System.out.println("smallest y: "+ smallesty.x+" x "+smallesty.y);

		// biggesty
		PVector biggesty = lo;
		for( int i = 1; i<ar.length; i++ ) {
			if( ar[i].y > biggestx.y) biggesty = ar[i];
		}
		System.out.println("biggest y: "+ biggesty.x+" x "+biggesty.y);

		
		
		float boxXsize =  biggestx.x - smallestx.x;
		float boxYsize =  biggesty.y - smallesty.y;
		System.out.println("box: "+boxXsize+" x "+boxYsize);
		

		PGraphics skewBox = createGraphics((int)boxXsize, (int)boxYsize);

		
		
		
		
//		PVector[] returnArr = new PVector[4];
		
		float loX = lo.x - smallestx.x;
		float loY = lo.y - smallesty.y;
		PVector boxLO = new PVector(loX,loY);
//		returnArr[0] = boxLO;
		
		float roX = ro.x - smallestx.x;
		float roY = ro.y - smallesty.y;
		PVector boxRO = new PVector(roX, roY);
//		returnArr[1] = boxRO;
		
		float ruX = ru.x - smallestx.x;
		float ruY = ru.y - smallesty.y;
		PVector boxRU = new PVector(ruX,ruY);
//		returnArr[2] = boxRU;
		
		float luX = lu.x - smallestx.x;
		float luY = lu.y - smallesty.y;
		PVector boxLU = new PVector(luX,luY);
//		returnArr[3] = boxLU;
		
		System.out.println("boxLO : "+ loX+" * "+loY);
		System.out.println("boxRO : "+ roX+" * "+roY);
		System.out.println("boxRU : "+ ruX+" * "+ruY);
		System.out.println("boxLU : "+ luX+" * "+luY);
		
		
		skewBox.beginDraw();
		skewBox.line(loX, loY, roX, roY);
		skewBox.line(roX, roY, ruX, ruY);
		skewBox.line(ruX, ruY, luX, luY);
		skewBox.line(luX, luY, loX, loY);
		skewBox.endDraw();
		
		PGraphics tg = createGraphics(baseX, baseY);

		tg.beginDraw();
		tg.line(boxLO.x, boxLO.y, boxRO.x, boxRO.y);
		tg.line(boxRO.x, boxRO.y, boxRU.x, boxRU.y);
		tg.line(boxRU.x, boxRU.y, boxLU.x, boxLU.y);
		tg.line(boxLU.x, boxLU.y, boxLO.x, boxLO.y);
		tg.endDraw();
		
//		skewBox.image(skewImage(_i, boxLO, boxRO, boxRU, boxLU), 0,0,boxXsize, boxYsize);
		
//		PImage retSkewBox = skewImage(skewBox, boxLO, boxRO, boxRU, boxLU);
		
//		PImage retSkewBox = skewBox.get();
		PImage retSkewBox = tg.get();
		
		System.out.println(((float)boxXsize/fact)+" x "+((float)boxYsize/fact));
//		retSkewBox.resize((int)(boxXsize*scale), (int)(boxYsize*scale));
//		retSkewBox.resize(((int)(float)boxXsize/fact),(int)((float)boxYsize/fact));
		
//		retSkewBox.resize((int)(boxXsize*scale), (int)(boxYsize*scale));
		retSkewBox.resize((int)(retSkewBox.width*scale), (int)(retSkewBox.height*scale));
		
		return retSkewBox;
		
//		return skewBox;
	}
	
	public void draw(){
		

		
		
		
		background(255);


		// draw Base

		if(b1) {
			blendMode(BLEND);
			image(layers[0], 0,0,width,height);
		}


		// draw Farbe

		if(b2){
			pushStyle();
			blendMode(BLEND);
			tint(235,250);
			image(layers[1], 0,0,width,height);
			//blend(layers[1], 0, 0, width, height, 0, 0, width, height, MULTIPLY);
			popStyle();
		}


		// draw Licht

		if(b3) {
			pushStyle();
			//blendMode(ADD);
			tint(255, 70);
			image(layers[2], 0,0,width,height);
			popStyle();
		}  


		// draw Bild

		if(b4) {
			pushStyle();
			blendMode(BLEND);
			image(layers[3], 0,0,width,height);
			popStyle();
		}

		// draw Schatten

		if(b5) {
			pushStyle();
			blendMode(BLEND);
			tint(255, 65);
			image(layers[4], 0,0,width,height);
			popStyle();
		}

		drawGUI();
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

	public boolean isMenuOpen() {
		return pMenu.isVisible();
	}

	public void keyPressed() {

		if( key == '1') b1 = !b1;
		if( key == '2') b2 = !b2;
		if( key == '3') b3 = !b3;
		if( key == '4') b4 = !b4;
		if( key == '5') b5 = !b5;

		if( key == 'u') updateArtworksLayer();
		
		redraw();
	}

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
	
	public char[] getCurrentWallChars() {
		System.out.println(currentFileStub);
		return currentFileStub.substring(currentFileStub.lastIndexOf('_')+1).toCharArray();
	}
	
	public void prepareFrameForClosing() {
		myFrame.setVisible(false);
	}
	
	public void dispose() {
		System.out.println("Renderer goodbye...");
//		myFrame.dispose();
		super.dispose();
	}

}
