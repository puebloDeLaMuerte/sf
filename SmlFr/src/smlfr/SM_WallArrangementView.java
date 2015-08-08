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
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import SMUtils.AllignmentTypes;
import SMUtils.ArtworkMeasurementParent;
import SMUtils.DistanceChooser;
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

public class SM_WallArrangementView extends PApplet implements DropTargetListener, UpdateListener, ActionListener, ArtworkMeasurementParent {

/**
	 * 
	 */
	private static final long serialVersionUID = -8724642767602580803L;

	private SmlFr					base;
	
	private SM_Wall					myWall; 
	private SM_ViewManager  		vm;
	private Dimension				mySize;
	private PGraphics				wlGfx;

	private PImage					lightSprite;
	
	private boolean					ready = false;

	private float					scale;
	private float 					xOffsetPx, yOffsetPx;
	private int						myMidHeight;
	
	private volatile SM_Artwork		awOver, menuAW;
//	private ArrayList<SM_Artwork> 	selectedAws;
	private boolean 				awDrag = false;
	private boolean					selectionDrag = false;
	private boolean					shiftLoc = false;
	private PVector					awDragOfset;
	private PVector					dragStart;
	
	private JPopupMenu				pMenu;
	private JMenuItem				putBack, snapToMidHeight, close;
	private JMenuItem				allignMidHoriz, allignMidVert, allignTop, allignBottom, allignLeft, allignRight;
	private JMenuItem				distEqual, distValue;
	private JMenuItem[]				frameStyles;
	private JMenu					editArtwork, allign, distance;
	private JMenuItem				editMeasurements;
	
	private DropTarget				dt;
	
	private boolean					artworkUpdatePending = true;
	private boolean					firstTime = true;
		
	int 							shadowAmount = 8;
	int 							shadowOfsetAmount = 5;
	
	int count = 0;

	
	public SM_WallArrangementView(SM_Wall _myW, Dimension _size, SM_ViewManager _vm, SmlFr base) {
		super();
		
		this.base = base;
		
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
		
		lightSprite = base.fm.getLightGfx();
		
		
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
		
		snapToMidHeight = new JMenuItem(Lang.snapToMidHeight);
		snapToMidHeight.addActionListener(this);
		snapToMidHeight.setEnabled(false);
		
		editArtwork = new JMenu(Lang.editArtwork);
		editArtwork.setEnabled(true);

		editMeasurements = new JMenuItem(Lang.editMeasurements);
		editMeasurements.addActionListener(this);
		editMeasurements.setEnabled(true);
		editArtwork.add(editMeasurements);

		editArtwork.add(new JSeparator());
		
		JMenuItem select = new JMenuItem(Lang.changeFrameStyle);
		select.setFont(select.getFont().deriveFont(Font.ITALIC));
		select.setEnabled(false);
		editArtwork.add( select );
		editArtwork.add(Box.createRigidArea(new Dimension(0	, 10)) );
		
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
		
		allign = new JMenu(Lang.allign);

		allignMidHoriz = new JMenuItem(Lang.allignMidHor);
		allignMidHoriz.addActionListener(this);
		
		allignMidVert = new JMenuItem(Lang.allignMidVert);
		allignMidVert.addActionListener(this);
		
		allignTop = new JMenuItem(Lang.allignTop);
		allignTop.addActionListener(this);
		
		allignBottom = new JMenuItem(Lang.allignBottom);
		allignBottom.addActionListener(this);
		
		allignLeft = new JMenuItem(Lang.allignLeft);
		allignLeft.addActionListener(this);
		
		allignRight = new JMenuItem(Lang.allignRight);
		allignRight.addActionListener(this);
		
		allign.add(allignMidHoriz);
		allign.add(allignMidVert);
		allign.add(allignTop);
		allign.add(allignBottom);
		allign.add(allignLeft);
		allign.add(allignRight);
		
		distance = new JMenu(Lang.distance);
		
		distEqual = new JMenuItem(Lang.distanceSelection);
		distEqual.addActionListener(this);
		
		distValue = new JMenuItem(Lang.distanceFixed);
		distValue.addActionListener(this);
		
		distance.add(distEqual);
		distance.add(distValue);

		
		close = new JMenuItem(Lang.closeWall);
		close.addActionListener(this);
		close.setEnabled(true);
		
		pMenu.add(snapToMidHeight);
		pMenu.add(putBack);
		pMenu.add(editArtwork);
		pMenu.add(new JSeparator());
		pMenu.add(allign);
		pMenu.add(distance);
		pMenu.add(new JSeparator());
		pMenu.add(close);
		
	}
	
	public Dimension getSize() {
		return mySize;
	}
		
	public String getWallName() {
		return myWall.getWallName();
	}

	public SM_Wall getWall() {
		return myWall;
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
		
		image(  getGraphics(0, 0, 0)  ,0,0);
		
		
		// DRAW mouseOver
		
		
		if( awOver != null && !isValidDrag() && !selectionDrag ) {
			
			PVector totalPos = wptos( new PVector(awOver.getTotalWallPos()[0], awOver.getTotalWallPos()[1]), scale );
			PVector totalSize = astos( new PVector(awOver.getTotalWidth(), awOver.getTotalHeight()), scale);
			
			pushStyle();
			fill(200,100,100,80);
			noStroke();
			rect( totalPos.x , totalPos.y, totalSize.x, totalSize.y );
			popStyle();
		}
		
		
		// DRAW Artwork drag
		
		if( awOver != null && isValidDrag() ) {
			
			SM_Artwork[] dragAws;
			if( !awOver.isSelected() ) {
				dragAws = new SM_Artwork[1];
				dragAws[0] = awOver;
			} else {
				dragAws = getSelectedArtworks();
			}
			
			PVector referencePos = wptos(awOver.getTotalWallPos()[0], awOver.getTotalWallPos()[1], scale);
			
			for(SM_Artwork a : dragAws) {
				
				PVector thisPos	= wptos(a.getTotalWallPos()[0], a.getTotalWallPos()[1], scale);
				
				thisPos.sub(referencePos);
			
				PVector wh = astos( new PVector(a.getTotalWidth(), a.getTotalHeight()), scale);
				
				float y;
				if( shiftLoc ) {
					y = wptos(0,a.getTotalWallPos()[1], scale).y - thisPos.y;
				} else {
					y = mouseY+awDragOfset.y;
				}
				
				thisPos.x += mouseX+awDragOfset.x;
				thisPos.y += y;
				
				pushStyle();
				noFill();
				rect(thisPos.x, thisPos.y, wh.x, wh.y);
				popStyle();
			}
			
			
		}
		
		// DRAW selected
		
		if(myWall.getArtworksArray().length > 0 ) {
			for( SM_Artwork a : myWall.getArtworksArray() ) {
				
				if(a != null && a.isSelected() && !isValidDrag() ) {
				
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
		
		// DRAW selection frame
		
		if( selectionDrag ) {
			
			drawSelectionFrame(dragStart, new PVector(mouseX, mouseY), g);
			
			checkSelectionFrame(dragStart, new PVector(mouseX, mouseY));
			
		}
		
		
		
		
		ready = true;
		if( firstTime ) {
			vm.requestRendererUpdate(myWall.getWallChar());
			firstTime = false;
		}
	}

	/**
	 *  0 for Wall
	 *  1 for Lights
	 */
	public synchronized PGraphics getGraphics(int what, int mode, int shadowOfset) {
		switch (what) {
		
		case 0: 	return _drawWall( mode, shadowOfset);
		case 1: 	return _drawLights( mode );
		default:	return null;

		}
	}
	
	/**
	 * 
	 * @param _mode <b>mode: 1</b> if it is for renderer, <b>mode: 0</b> if it is for WallArrangementView, <b>mode: 2</b> if it is for HighResImage
	 * @param shadowOfset
	 * @return null if mode is not 0, 1, or 2
	 */
	public /*synchronized*/ PGraphics _drawWall( int _mode, int shadowOfset) {
		
		float drawScale;
		
//		wlGfxReady = false;
		loadMissingAWGraphics();
		
		PGraphics gfx;
		
		if( _mode == 0 ) {
			
// for WallArrangementView
			
			gfx = wlGfx;
			drawScale = scale;
		}
		else if( _mode == 1 ) {
			
// for Renderer
			
			float fact = 1;
			
			if( height < 600 ) {
				
				fact = 600f / (float)height;
			}
			if( width < 600f  && width < height) {
				
				fact = 600f / (float)width;
			}
			if( fact > 3f) fact = 3f;
			System.err.println("fact: " + fact);
			
			gfx = createGraphics( (int)(width*fact), (int)(height*fact));
			
//			System.err.println("wall    size: " + myWall.getWidth() +" x " + myWall.getHeight());
//			System.err.println("norm    size: " + width +" x "+ height);
//			System.err.println("wallGfx size: " + gfx.width +" x " + gfx.height);
			
			drawScale = ((float)gfx.width ) / ((float)myWall.getWidth());
			
		} else if( _mode == 2 ) {
			
// for SaveImage (hi res)
			
			gfx = createGraphics(width*3, height*3);
			drawScale = ((float)gfx.width ) / ((float)myWall.getWidth());
			
		} else {
			return null;
		}
		
		
		gfx.clear();
		gfx.beginDraw();
		
		
		// DRAW Schatten
		
		if( _mode == 1 || _mode == 2 ) {
			if(myWall.getArtworksArray().length > 0 ) {

				if(!awDrag && _mode == 0) awOver = null;

				for( SM_Artwork a : myWall.getArtworksArray() ) {

					if( a.hasShadow() ) {	
						int[] tmpPos = a.getTotalWallPos();
						PVector totalPos = wptos( new PVector(tmpPos[0]-shadowAmount-(shadowOfsetAmount * shadowOfset), tmpPos[1]-shadowAmount-(shadowAmount*2)), drawScale );
						PVector totalSize = astos( new PVector(a.getTotalWidth()+(shadowAmount*2), a.getTotalHeight()+(shadowAmount*0.5f)), drawScale);
	
						gfx.pushStyle();
						gfx.fill(50);
						gfx.rect( totalPos.x, totalPos.y, totalSize.x, totalSize.y );
						gfx.popStyle();
					}
				}
			}

			int i = gfx.width / 350;
			if( i > 6) i = 6;
			if( i < 3) i = 3;
			
			System.err.println("BLUR VALUE: "+ i);
			
			gfx.filter(BLUR, /*3*/ i);
		}
		
		if(myWall.getArtworksArray().length > 0 ) {
			
			if(!awDrag && _mode == 0) awOver = null;
			
			for( SM_Artwork a : myWall.getArtworksArray() ) {

				
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
				
// 	----->   	// check and assign awOver
				
				if( !awDrag && _mode == 0) {
					if( mouseX > totalPos.x && mouseX < (totalPos.x + totalSize.x) ) {
						if( mouseY > totalPos.y && mouseY < (totalPos.y + totalSize.y) ) {
							awOver = a;
						}
					}
				}
				
				
				// make transparent if drag
				
				gfx.fill(230,230,230,100);
				
				if( _mode == 0 && isValidDrag() ) {
					
					if( awOver != null && !awOver.isSelected() ) {
						
						if( a == awOver ) {
							gfx.tint(255,75);
						} else {
							gfx.tint(255,255);
						}
						
					} else {
						
						if( a.isSelected() ) {
							gfx.tint(255,75);
						} else {
							gfx.tint(255,255);
						}
						
					}
				} else {
					gfx.tint(255, 255);
				}
					
				
				int shadowFact = 5;
				

				// draw frame
				
				if(a.hasFrame()) {
					gfx.image(a.getFrameGfx(), totalPos.x, totalPos.y, totalSize.x, totalSize.y);
				}
				
				// draw ppt
				
				if(a.hasPassepartout()) {
					gfx.noStroke();
					gfx.pushStyle();
					if( _mode == 0 &&  isValidDrag() && a.isSelected() ) {
						gfx.fill(200,190,170,75);
					} else {						
						gfx.fill(200,190,170,255);
					}
					gfx.rect(pptPos.x, pptPos.y, pptSize.x, pptSize.y);
					gfx.popStyle();
				}
				
				// draw artwork
				PImage aa = a.getGfx();
				
				if (aa != null) {
					gfx.image(aa, artworkPos.x, artworkPos.y, artworkSize.x,
							artworkSize.y);
				} else {
					System.err.println("ES W€HRE DA GEWESEN, in WallArrangementView, nŠmlich!");
				}
				g.removeCache(gfx);

				
				
			}
		}
		gfx.endDraw();
		ready = true;
		if(_mode == 1) artworkUpdatePending = false;
		return gfx;
	}
	
	public synchronized PGraphics _drawLights(int mode) {
		
		
		// prepare Draw...
		
		PGraphics lGfx; 

		float drawScale;

		if( mode == 0 ) {
			lGfx = createGraphics(width, height);
			drawScale = ((float)lGfx.width ) / ((float)myWall.getWidth());
		}
		else{

			// TODO test if results in renderer are better if high values are chosen

//			lGfx = createGraphics(width, height);
			lGfx = createGraphics(width/2, height/2);
//			lGfx = createGraphics((int)(width*0.8f), (int)(height*0.8f));
			
			drawScale = ((float)lGfx.width ) / ((float)myWall.getWidth());
		}	
		
		// prepare Light Sprite ( size to wall-drawing Size )
		
		PImage wallLightSprite;
		
		try {
			
			wallLightSprite = (PImage) lightSprite.clone();
			PVector thisSize = astos( 2500, 4142, drawScale );
			wallLightSprite.resize((int)thisSize.x, (int)thisSize.y);

			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			wallLightSprite = null;
		}
		
		// draw Lights...
		
		lGfx.beginDraw();
		
		if(myWall.getArtworksArray().length > 0 ) {

			for( SM_Artwork a : myWall.getArtworksArray() ) {

				if( a.hasLight() ) {
					
					
					int lightsCount = a.getLightsCount();
					int thisLightOffsetX, thisLightOffsetY;

					PVector awSize = astos( new PVector(a.getTotalWidth(), a.getTotalHeight()), drawScale);
					
					// resize LightSprite According to awSize
					
					PImage thisLightSprite = null;
					float awSizeFact =1;
					
					try {
						thisLightSprite = (PImage) wallLightSprite.clone();
						
						awSizeFact = (float)(a.getTotalWidth() + a.getTotalHeight()) / 4000f;
						
						if( awSizeFact < 1) {
							awSizeFact += (1 - awSizeFact) * 0.6f;
						}
						thisLightSprite.resize((int)(thisLightSprite.width * awSizeFact), (int)(thisLightSprite.height * awSizeFact));
						
						thisLightOffsetX = thisLightSprite.width / 2;
						thisLightOffsetY = (int)(thisLightSprite.height * 0.6f);
						
					} catch(Exception e) {
						thisLightOffsetX = 0;
						thisLightOffsetY = 0;
					}
					
					// calculate Position
					
					float thisLightOfset = awSize.x / ( lightsCount +1 );
					float thisInitialOfset;
					if( lightsCount >1) {
						thisInitialOfset = thisLightOfset / 2;
						thisLightOfset = thisLightOfset * lightsCount;
						thisLightOfset /= (lightsCount -1);
					} else {
						thisInitialOfset = thisLightOfset;
					}
					
					for (int i = 0; i < lightsCount; i++) {
						
						PVector lightPos = wptos( new PVector(a.getTotalWallPos()[0], a.getTotalWallPos()[1]), drawScale );
						
						lightPos.x -= thisLightOffsetX;
//						lightPos.x += thisLightOfset * i;
						lightPos.x += thisInitialOfset + ((thisLightOfset) * i);
						
						lightPos.y -= thisLightOffsetY;
						lightPos.y += (awSize.y / 2) * awSizeFact;

						lGfx.image(thisLightSprite, lightPos.x , lightPos.y , thisLightSprite.width, thisLightSprite.height );
						
//						lGfx.pushStyle();
//						lGfx.noFill();
//						lGfx.stroke(255);
//						lGfx.rect(lightPos.x , lightPos.y , thisLightSprite.width, thisLightSprite.height);
//						lGfx.line(lightPos.x, lightPos.y, lightPos.x + thisLightSprite.width, lightPos.y + thisLightSprite.height);
//						lGfx.line(lightPos.x, lightPos.y + thisLightSprite.height, lightPos.x + thisLightSprite.width, lightPos.y);
//						lGfx.popStyle();
					}
				}
			}
		}
		lGfx.endDraw();
		return lGfx;
	}
	
	
	// artwork Size to Screen
	private PVector astos(int _inX, int _inY, float scl) {
		return astos(new PVector(_inX, _inY), scl );
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

	private void drawSelectionFrame(PVector lo, PVector ru, PGraphics g) {
		
		g.pushStyle();
		g.smooth();
		
//		strokeWeight(3);
		g.fill(200, 200);
//		noFill();
		
		float hor = abs(lo.x - ru.x) / 4;
		float vrt = abs(lo.y - ru.y) / 4;
		
		// oben unten
		for (float i = 1; i <= hor; i++) {
		  float x = lerp(lo.x, ru.x, i/hor);
		  g.point(x, lo.y);
		  g.point(x, ru.y);
		}
		
		// links rechts
		for (float i = 1; i <= vrt; i++) {
			float y = lerp(lo.y, ru.y, i/vrt);
			g.point(lo.x, y);
			g.point(ru.x, y);
		}
		g.noSmooth();
		g.popStyle();
		
	}
	
	public void loadMissingAWGraphics() {
		
		for( String a : myWall.getArtworks().keySet() ) {
			SM_Artwork aw = myWall.getArtworks().get(a);
			if( ! aw.hasGfx() ) {
				aw.setGfx( loadImage(aw.getFilePath().getAbsolutePath())  );
			}
		}
		
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
	
	private SM_Artwork[] getSelectedArtworks() {
		
		ArrayList<SM_Artwork> selectedList = new ArrayList<SM_Artwork>();
		
		for( SM_Artwork aw : myWall.getArtworksArray() ) {
			if( aw.isSelected() ) selectedList.add(aw);
		}
		
		return selectedList.toArray(new SM_Artwork[selectedList.size()]);
	}
	
	private void checkSelectionFrame(PVector lo, PVector ru) {
		
		
		int maxX, maxY, minX, minY;
		
		if( lo.x > ru.x ) {
			maxX = (int) lo.x;
			minX = (int) ru.x;
		} else {
			maxX = (int) ru.x;
			minX = (int) lo.x;
		}
		
		if( lo.y > ru.y ) {
			maxY = (int) lo.y;
			minY = (int) ru.y;
		} else {
			maxY = (int) ru.y;
			minY = (int) lo.y;
		}
		
		for (SM_Artwork a : myWall.getArtworksArray()) {
			
			if( !shiftLoc ) a.setSelected(false);
			
			PVector awPos  = wptos(a.getTotalWallPos()[0], a.getTotalWallPos()[1], scale);
			PVector awSize = astos(a.getTotalWidth(), a.getTotalHeight(), scale);
			
			if( awPos.x > minX && awPos.x < maxX  ||  (awPos.x + awSize.x) > minX && (awPos.x + awSize.x) < maxX || awPos.x < minX && (awPos.x + awSize.x) > maxX ) {
				if( awPos.y > minY &&  awPos.y < maxY || (awPos.y + awSize.y) > minY && (awPos.y + awSize.y) < maxY || awPos.y < minY && (awPos.y + awSize.y) > maxY ) {
				
					a.setSelected(true);
				}
			} 
			
		}

		
		
	}
	
	
	
	private void deselectAll() {
		
		for( SM_Artwork a : myWall.getArtworksArray() ) a.setSelected(false);
	}

	public void mousePressed() {

		dragStart = new PVector(mouseX,mouseY);
		
		if( awOver != null ) {
			awDrag = true;
			awDragOfset = wptos(awOver.getTotalWallPos()[0],awOver.getTotalWallPos()[1], scale);
			awDragOfset.sub(dragStart);
		}
	}
	
	public void mouseReleased() {
		
		if( awDrag ) {

			awDrag = false;

			if (mouseX != dragStart.x || mouseY != dragStart.y) {
				
				PVector npos = new PVector(mouseX, mouseY);
				npos.add(awDragOfset);
				PVector nPos = ptowp(npos, scale);
				
				// if multiple moved
				
				if(awOver != null && awOver.isSelected() && getSelectedArtworks().length > 1) {
					
					PVector referencePos = new PVector(awOver.getTotalWallPos()[0], awOver.getTotalWallPos()[1]);
					
					boolean first = true;
					for (SM_Artwork a : getSelectedArtworks() ) {

						PVector thisPos	= new PVector(a.getTotalWallPos()[0], a.getTotalWallPos()[1]);
						
						thisPos.sub(referencePos);
						thisPos.add(nPos);
						
						// check for wallBounds
						
						if( thisPos.x < 0) {
							thisPos.x = 0;
						}
						if( (thisPos.y - a.getTotalHeight()) < 0) {
							thisPos.y = a.getTotalHeight();
						}
						if( (thisPos.x + a.getTotalWidth()) > myWall.getWidth()) {
							thisPos.x -= ( thisPos.x + a.getTotalWidth() - myWall.getWidth() );
						}
						if( thisPos.y > myWall.getHeight()) {
							thisPos.y = myWall.getHeight() ;
						}
						
					
						ArtworkUpdateRequestEvent e;
						
						int count = 0;
						if(first) count = getSelectedArtworks().length;
						first = false;
						
						if (shiftLoc) {
							e = new ArtworkUpdateRequestEvent(this, true, count, a.getName(), (int) thisPos.x, a.getTotalWallPos()[1]);
						} else {
							e = new ArtworkUpdateRequestEvent(this, true, count, a.getName(), (int) thisPos.x, (int) thisPos.y);
						}
						myWall.myRoom.fireUpdateRequest(e);
					}
					
				} else if( awOver != null ) {  
					
					// fire single update
					
					// check for wallBounds
					
					if( nPos.x < 0) {
						nPos.x = 0;
					}
					if( (nPos.y - awOver.getTotalHeight()) < 0) {
						nPos.y = awOver.getTotalHeight();
					}
					if( (nPos.x + awOver.getTotalWidth()) > myWall.getWidth()) {
						nPos.x -= ( nPos.x + awOver.getTotalWidth() - myWall.getWidth() );
					}
					if( nPos.y > myWall.getHeight()) {
						nPos.y = myWall.getHeight() ;
					}
				
					ArtworkUpdateRequestEvent e;
				
					if (shiftLoc) {
						e = new ArtworkUpdateRequestEvent(this, false, -1, awOver.getName(), (int) nPos.x, awOver.getTotalWallPos()[1]);
					} else {
						e = new ArtworkUpdateRequestEvent(this, false, -1, awOver.getName(), (int) nPos.x, (int) nPos.y);
					}
					myWall.myRoom.fireUpdateRequest(e);
				}
			}
		} else if( selectionDrag ){
			
			selectionDrag = false;
			
		}
	}
		
	public void mouseDragged() {
		if( awOver == null) selectionDrag = true;
	}
	
	public void mouseClicked() {
		
		if( awOver != null && mouseButton != RIGHT) {			

			awOver.toggleSelected();

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
			
			if( getSelectedArtworks().length > 1 ) {
				allign.setEnabled(true);
				distance.setEnabled(true);
				snapToMidHeight.setEnabled(true);
			} else {
				allign.setEnabled(false);
				distance.setEnabled(false);
			}
			
			
			pMenu.show(this, mouseX, mouseY);
		}
	}

	public void keyPressed() {
		if( keyCode == ESC ) {
			key =0;
		} else {
			if(keyCode == SHIFT) {
				shiftLoc = true;
			}
		}
	}
	
	public void keyReleased() {
		if(keyCode == SHIFT) {
			shiftLoc = false;
		}
	}
	
	private boolean isValidDrag() {

		if( awDrag && (dragStart.x != mouseX || dragStart.y != mouseY) ) return true;
		else return false;
	}

	@Override
 	public void doUpdate(UpdateEvent e) {
		artworkUpdatePending = true;
//		awOver = null;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String action = e.getActionCommand();
		
		if(action.equalsIgnoreCase(Lang.closeWall)) {
//			this.frame.setVisible(false);
			vm.sleepWallArr(""+myWall.getWallChar());
		}
		
		else
		
		if(action.equalsIgnoreCase(Lang.snapToMidHeight)) {
				
			snapToMidHeight();
		}
		
		else 
		
		if( action.equalsIgnoreCase(Lang.RemoveArtwork) && awOver != null) {
			WallUpdateRequestEvent r = new WallUpdateRequestEvent(this, awOver.getName(), ' ', "Library", myWall.myRoom.getName(), awOver.getWallChar());
			awOver = null;
			myWall.myRoom.fireUpdateRequest(r);
		}
		
		else
			
		if( action.equalsIgnoreCase(Lang.editMeasurements) ) {
			
			SMUtils.ArtworkMeasurementChooser chooser = new SMUtils.ArtworkMeasurementChooser(this, menuAW );

			

		}
		
		else if( e.getSource() == allignMidHoriz) allignArtworks( getSelectedArtworks(), AllignmentTypes.MID_HORIZONTAL);
		else if( e.getSource() == allignMidVert	) allignArtworks( getSelectedArtworks(), AllignmentTypes.MID_VERTICAL);
		else if( e.getSource() == allignTop		) allignArtworks( getSelectedArtworks(), AllignmentTypes.TOP);
		else if( e.getSource() == allignBottom	) allignArtworks( getSelectedArtworks(), AllignmentTypes.BOTTOM);
		else if( e.getSource() == allignLeft	) allignArtworks( getSelectedArtworks(), AllignmentTypes.LEFT);
		else if( e.getSource() == allignRight	) allignArtworks( getSelectedArtworks(), AllignmentTypes.RIGHT);
		
		else if( e.getSource() == distEqual	) distanceEqual( getSelectedArtworks() ); 
		else if( e.getSource() == distValue ) distanceValue( getSelectedArtworks() );
		
	
		// if it's none of the above it must have been a frameStyle selected. Let's find out which one!
		else {

			for (JMenuItem item : frameStyles) {
				if( e.getActionCommand().equalsIgnoreCase(item.getText())) {
					
					FrameStyle style = FrameStyle.valueOf(item.getText());
					
					ArtworkUpdateRequestEvent request = new ArtworkUpdateRequestEvent(this, false, -1,  menuAW.getName(), style);
					System.out.println(menuAW.getName());
					
					myWall.myRoom.fireUpdateRequest(request);
					
					break;
				}
			}
		}
	}
	
	private void allignArtworks( SM_Artwork[] aws, AllignmentTypes type) {
		
		
		switch (type) {
		
		case MID_HORIZONTAL:
			
			
			int averageMid = 0;
			for(SM_Artwork a : aws ) {
				
				averageMid += a.getArtworkWallPos()[1] - ( a.getArtworkSize()[1] / 2 );
			}
			averageMid /= aws.length;			
			
			boolean first = true;
			for( SM_Artwork a : aws ) {
				
				int offsetY =  a.getArtworkWallPos()[1] - ( a.getArtworkSize()[1] / 2 ) - averageMid;
				
				int newY = a.getTotalWallPos()[1] - offsetY;
				int newX = a.getTotalWallPos()[0];
				
				ArtworkUpdateRequestEvent r;
				if(first) {
					r = new ArtworkUpdateRequestEvent(this, true, aws.length, a.getName(), newX, newY);
				} else {
					r = new ArtworkUpdateRequestEvent(this, true, 0, a.getName(), newX, newY);
				}
				first = false;
				
				myWall.myRoom.fireUpdateRequest(r);
			}
			break;

		case MID_VERTICAL:
			
			averageMid = 0;
			for(SM_Artwork a : aws ) {
				
				averageMid += a.getArtworkWallPos()[0] + ( a.getArtworkSize()[0] / 2 );
			}
			averageMid /= aws.length;			
			
			first = true;
			for( SM_Artwork a : aws ) {
				
				int offsetX =  a.getArtworkWallPos()[0] + ( a.getArtworkSize()[0] / 2 ) - averageMid;
				
				int newY = a.getTotalWallPos()[1];
				int newX = a.getTotalWallPos()[0] - offsetX;
				
				ArtworkUpdateRequestEvent r;
				if(first) {
					r = new ArtworkUpdateRequestEvent(this, true, aws.length, a.getName(), newX, newY);
				} else {
					r = new ArtworkUpdateRequestEvent(this, true, 0, a.getName(), newX, newY);
				}
				first = false;
				
				myWall.myRoom.fireUpdateRequest(r);
			}
			break;
			
		case TOP:
			
			// finde die oberkante
		
			int topYpos = aws[0].getTotalWallPos()[1];
			for( SM_Artwork a: aws) {
				if( a.getTotalWallPos()[1] > topYpos) topYpos = a.getTotalWallPos()[1];
			}
			
			// wieviele multiple updates werden es sein?
			
			int multipleCount = 0;
			for( SM_Artwork a : aws ) {	
				if( a.getTotalWallPos()[1] < topYpos ) {
					multipleCount++;
				}
			}
			
			// feuer
			
			first = true;
			for( SM_Artwork a : aws ) {
				if( a.getTotalWallPos()[1] < topYpos ) {
					
					ArtworkUpdateRequestEvent r;
					if(first) {
						r = new ArtworkUpdateRequestEvent(this, true, multipleCount, a.getName(), a.getTotalWallPos()[0], topYpos);
					} else {
						r = new ArtworkUpdateRequestEvent(this, true, 0, a.getName(), a.getTotalWallPos()[0], topYpos);
					}
					first = false;
					
					myWall.myRoom.fireUpdateRequest(r);
				}
			}
			
			break;
			
		case BOTTOM:
			
			// finde tiefstes
			
			int lowest = aws[0].getTotalWallPos()[1] - aws[0].getTotalHeight();
			
			for( SM_Artwork a: aws) {
				
				int thisLower = (a.getTotalWallPos()[1] - a.getTotalHeight());
				
				if( thisLower  < lowest) lowest = thisLower;
			}
			
			// wieviele Updates werden kommen?
			
			multipleCount = 0;
			for( SM_Artwork a : aws ) {

				int thisLower = (a.getTotalWallPos()[1] - a.getTotalHeight());
				if(  thisLower > lowest ) {
					multipleCount++;
				}
			}
			
			// feuer!
			
			first = true;
			for( SM_Artwork a : aws ) {
				
				int thisLower = (a.getTotalWallPos()[1] - a.getTotalHeight());
				
				if(  thisLower > lowest ) {
					
					ArtworkUpdateRequestEvent r;
					
					if(first) {
						r = new ArtworkUpdateRequestEvent(this, true, multipleCount, a.getName(), a.getTotalWallPos()[0], lowest + a.getTotalHeight());
					} else {
						r = new ArtworkUpdateRequestEvent(this, true, 0, a.getName(), a.getTotalWallPos()[0], lowest + a.getTotalHeight());
					}
					first = false;
					myWall.myRoom.fireUpdateRequest(r);
				}
			}
			break;
			
		case LEFT:
			
			int leftmost = aws[0].getTotalWallPos()[0];
			
			for( SM_Artwork a : aws) {
				if( a.getTotalWallPos()[0] < leftmost ) leftmost = a.getTotalWallPos()[0]; 
			}
			
			first = true;
			for( SM_Artwork a : aws ) {
				
					
				ArtworkUpdateRequestEvent r;
				if(first) {
					r = new ArtworkUpdateRequestEvent(this, true, aws.length, a.getName(), leftmost, a.getTotalWallPos()[1] );
				} else {
					r = new ArtworkUpdateRequestEvent(this, true, 0, a.getName(), leftmost, a.getTotalWallPos()[1] );
				}
				myWall.myRoom.fireUpdateRequest(r);
				first = false;
			}
			break;
			
		case RIGHT:
			
			int rightmost = aws[0].getTotalWallPos()[0] + aws[0].getTotalWidth();
			
			for (SM_Artwork a : aws) {
				if( a.getTotalWallPos()[0] + a.getTotalWidth() > rightmost ) rightmost = a.getTotalWallPos()[0] + a.getTotalWidth(); 
			}
			
			first = true;
			for (SM_Artwork a : aws) {
					
				int count = 0;
					if(first) count = aws.length;
					first = false;
					ArtworkUpdateRequestEvent r = new ArtworkUpdateRequestEvent(this, true, count, a.getName(), rightmost - a.getTotalWidth(), a.getTotalWallPos()[1] );
					myWall.myRoom.fireUpdateRequest(r);
				
			}
			
			break;
			
		default:
			break;
		}	
	}
	
	private void distanceEqual( SM_Artwork[] aws ) {
		if( aws.length <= 2 ) return;
		
		// sort artworks left to right
		
		boolean sorted = true;
		while(sorted) {
			sorted = false;
			for (int i = 0; i < aws.length-1; i++) {
				if( aws[i].getTotalWallPos()[0] > aws[i+1].getTotalWallPos()[0] ) {
					SM_Artwork a = aws[i];
					aws[i] = aws[i+1];
					aws[i+1] = a;
					sorted = true;
				}
			}
		}

		SM_Artwork leftmost = aws[0];
		SM_Artwork rightmost = aws[aws.length-1];

		
		// calculate gap
		
		int totalSpace = rightmost.getTotalWallPos()[0] + rightmost.getTotalWidth() - leftmost.getTotalWallPos()[0];
		int awSpace = 0;
		for (SM_Artwork a : aws) {
			awSpace += a.getTotalWidth();
		}
		int totalFreeSpace = totalSpace - awSpace;
		int gap = totalFreeSpace / (aws.length - 1);
		
		// do the magic
		
		for (int i = 1; i < aws.length-1; i++) {
			SM_Artwork a = aws[i];
			
			int newX = aws[i-1].getTotalWallPos()[0] + aws[i-1].getTotalWidth() + gap;
			
			int count = 0;
			if(i==1) count = aws.length-2;
			
			ArtworkUpdateRequestEvent r = new ArtworkUpdateRequestEvent(this, true, count, a.getName(), newX, a.getTotalWallPos()[1]);
			myWall.myRoom.fireUpdateRequest(r);	
		}
		
	}
	
	private void distanceValue( SM_Artwork[] aws ) {
		
		LinkedHashMap<String, int[]> pos = new LinkedHashMap<String, int[]>(aws.length);
		
		// sort left to right
		
		boolean sorted = true;
		while(sorted) {
			sorted = false;
			for (int i = 0; i < aws.length-1; i++) {
				if( aws[i].getTotalWallPos()[0] > aws[i+1].getTotalWallPos()[0] ) {
					SM_Artwork a = aws[i];
					aws[i] = aws[i+1];
					aws[i+1] = a;
					sorted = true;
				}
			}
		}
		
		// calculate initial distance
		
		// calculate gap
		
				int totalSpace = aws[aws.length-1].getTotalWallPos()[0] + aws[aws.length-1].getTotalWidth() - aws[0].getTotalWallPos()[0] ;
				int awSpace = 0;
				for (SM_Artwork a : aws) {
					awSpace += a.getTotalWidth();
				}
				int totalFreeSpace = totalSpace - awSpace;
				int gap = totalFreeSpace / (aws.length - 1);
		
		
		for (SM_Artwork a : aws) {
			pos.put(a.getName(), a.getTotalWallPos());
		}
		
		DistanceChooser d = new DistanceChooser(this, pos, aws, gap);
	}
	
	public void distanceCallback(int dist, SM_Artwork[] aws) {
		
		for (int i = 1; i < aws.length; i++) {
			SM_Artwork a = aws[i];
			
			int newX = aws[i-1].getTotalWallPos()[0] + aws[i-1].getTotalWidth() + dist;
			
			int count = 0;
			if(i==1) count = aws.length-1;
			
			ArtworkUpdateRequestEvent r = new ArtworkUpdateRequestEvent(this, true, count, a.getName(), newX, a.getTotalWallPos()[1]);
			myWall.myRoom.fireUpdateRequest(r);	
		}
		
	}
	
	public void distanceCancelCallback(SM_Artwork[] aws, LinkedHashMap<String, int[]> oPos) {
		
		boolean first = true;
		for (SM_Artwork a : aws) {
			
			int[] p = oPos.get(a.getName());
			
			int count = 0;
			if(first) count = aws.length;
			first = false;
			ArtworkUpdateRequestEvent r = new ArtworkUpdateRequestEvent(this, true, count, a.getName(), p[0], p[1]);
			myWall.myRoom.fireUpdateRequest(r);	
		}
		
	}
	
	private void snapToMidHeight() {
		
		SM_Artwork[] aws = getSelectedArtworks();
		
//		if (awOver != null) {
//			aws = new SM_Artwork[1];
//			aws[0] = awOver;
//		} else {
//			aws = getSelectedArtworks();
//		}
		
		boolean first = true;
		for (SM_Artwork a : aws) {
			
			int count = 0;
			if(first) count = aws.length;
			first = false;
			ArtworkUpdateRequestEvent rq = new ArtworkUpdateRequestEvent(this, true, count, a.getName(), a.getTotalWallPos()[0], (myMidHeight)+(a.getTotalHeight()/2)  );
			myWall.myRoom.fireUpdateRequest(rq);
		}
	}
	
	public void artworkMeasurementCallback(LinkedHashMap<String, Object> data) {
		ArtworkUpdateRequestEvent e = new ArtworkUpdateRequestEvent(this, false, -1, data);
		myWall.myRoom.fireUpdateRequest(e);
	}

	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		
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
		
	}


	public boolean isSleeping(){
		if(frame.isVisible() ) return false;
		else return true;
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

}

