package smlfr;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import processing.core.PShape;
import sfpMenu.SfpMouseEvent;
import sfpMenu.SfpMouseListener;
import sfpMenu.SfpViewMenuItem;
import SMUtils.ViewMenuItem;

public class SM_RoomArrangementView extends SM_RoomProjectView implements MouseListener, SfpMouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3950035428584333737L;
	private SM_ViewManager					vm;
	private HashMap<Character, PShape>		wallsOverGfx;
	private HashMap<Character, PShape>		wallsActiveGfx;
	
	private boolean 						showViewMenuPreview = false;
	private char[]							viewMenuCurrentHighlight;
	private char[]							activeViews;
	private char[]							visibleViews;
	
	private boolean							isinitialized = false;
	
	public SM_RoomArrangementView(int w, int h, SmlFr base) {
		super(w,h, base);
	}
	
//	public void init() {
//		super.init();
//		isinitialized = true;
//	}

	
	
	public void initFileAndRoomAndViewangles(File _filePath, SM_Room _room, SM_ViewAngle[] _vas) {
		
		super.initFileAndRoom(_filePath, _room);
		
		vm = new SM_ViewManager(this, myRoom.getWindowManager(), _vas, base);
		
		wallsOverGfx = super.getWallsOverGfx();
		wallsActiveGfx = super.getWallsActiveGfx();
	}
	
	public void setWallsGfx() {
		wallsOverGfx = super.getWallsOverGfx();
		wallsActiveGfx = super.getWallsActiveGfx();
	}
	
	public void init(File _filePath, SM_Room _room, SM_ViewAngle[] _vas) {
		
		super.init(_filePath, _room);
		
		vm = new SM_ViewManager(this, myRoom.getWindowManager(), _vas, base);
		
		wallsOverGfx = super.getWallsOverGfx();
		wallsActiveGfx = super.getWallsActiveGfx();
		
		// disable wallGfx-file-style for custom color stuff etc
//		for(Character c : wallsActiveGfx.keySet() ) {
//			PShape s = wallsActiveGfx.get(c);
//			s.disableStyle();
//		}
		isinitialized = true;
	}
	
	public boolean isInitialized() {
		return isinitialized;
	}

	
	@Override
	public void draw() {

//		vm.checkRendererUpdate();
		
		super.draw();

		if( wallsOverGfx == null ) wallsOverGfx = super.getWallsOverGfx();
		if( wallsActiveGfx == null ) wallsActiveGfx = super.getWallsActiveGfx();
		
		
		
		if(showViewMenuPreview) {
			
			boolean ropen = vm.isRendererMenuOpen();
			boolean wgfxNotNull = wallsOverGfx != null;
			boolean vmNotNull  = vm != null; 
			
			if( vmNotNull && ropen && wgfxNotNull) {

				for(char c : viewMenuCurrentHighlight ) {

					shape(wallsOverGfx.get(c), 0, 0);

				}
			} else {
				showViewMenuPreview = false;
			}
			
		}
		if( activeViews != null && wallsOverGfx != null) {
			

			for( char v : activeViews ) {
				shape(wallsOverGfx.get(v), 0,0);
			}

		}
		if( visibleViews != null && wallsActiveGfx != null) {

			for( char v : visibleViews ) {
				shape(wallsActiveGfx.get(v), 0,0);
			}

		}
		
		if( this.getClass() == SM_RoomArrangementView.class ) {
			super.menu.draw();
		}
	}
	
	public void setVisibleViews( String _vv) {
		visibleViews = new char[_vv.length()];
		for( int i = 0; i< _vv.length(); i++ ) {
			visibleViews[i] = _vv.charAt(i);
		}
	}
	
	
	public void setActiveViews(String _v) {
		activeViews = new char[_v.length()];
		for( int i = 0; i< _v.length(); i++ ) {
			activeViews[i] = _v.charAt(i);
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if( e.getClickCount() > 1 ) {
			char woc = super.getWallOverChar();
			if( woc != ' ' ) {
				
				vm.openWallArr(woc);
				
				
			}
		}
		super.mouseClicked(e);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource().getClass().equals(ViewMenuItem.class)) {

			ViewMenuItem itm = (ViewMenuItem)e.getSource();
			int l = itm.getActionCommand().length();
			viewMenuCurrentHighlight = new char[l];
			for( int i = 0; i<l;i++) {
				viewMenuCurrentHighlight[i] = itm.getActionCommand().charAt(i);
			}
			showViewMenuPreview = true;
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource().getClass().equals(ViewMenuItem.class)) {
//			System.out.println("SCHNABLER");
		}
	}
	
	/* (non-Javadoc)
	 * @see sfpMenu.SfpMouseListener#MouseEventHappened(sfpMenu.SfpMouseEvent)
	 */
	@Override
	public void MouseEventHappened(SfpMouseEvent e) {
		
		if ( e.getSource().equals(SfpViewMenuItem.class) ) {
			if (e.mouseEntered()) {

				int l = e.getActionCommand().length();
				viewMenuCurrentHighlight = new char[l];
				for( int i = 0; i<l;i++) {
					viewMenuCurrentHighlight[i] = e.getActionCommand().charAt(i);
				}
				showViewMenuPreview = true;
				
				
			} else if (e.mouseExited()) {

//				showViewMenuPreview = false;
				
			} 
		}
		
	}
	

	
	public void disposeVM(SM_Room rom) {
		System.out.println("ROOM_ARR_VIEW: VM dispose called");
		rom.unregisterUpdateListener(vm);
		vm.dispose();
	}
	
	@Override
	public void dispose() {
		System.out.println("ROOM_ARR_VIEW: closing this Arrview");
		super.dispose();
	}


	



}
