package smlfr;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import processing.core.PShape;

import SMUtils.ViewMenuItem;

public class SM_RoomArrangementView extends SM_RoomProjectView implements MouseListener {

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
	

	public SM_RoomArrangementView(int w, int h, SmlFr base) {
		super(w,h, base);
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
		
	}

	
	public void draw() {

//		vm.checkRendererUpdate();
		
		super.draw();

		if(showViewMenuPreview) {
			if( vm != null && vm.isRendererMenuOpen() ) {

				for(char c : viewMenuCurrentHighlight ) {

					shape(wallsOverGfx.get(c), 0, 0);

				}
			}
			
		}
		if( activeViews != null ) {
			
			// TODO this is temp - the Graphics fpr actie- visible- and over- need to be sorted out !!!

			for( char v : activeViews ) {
				shape(wallsOverGfx.get(v), 0,0);
			}

		}
		if( visibleViews != null ) {

			for( char v : visibleViews ) {
				shape(wallsActiveGfx.get(v), 0,0);
			}

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
	
	
	public void mouseClicked(MouseEvent e) {
		if( e.getClickCount() > 1 ) {
			char woc = super.getWallOverChar();
			if( woc != ' ' ) {
				
				vm.openWallArr(woc);
				
				
			}
		}
		super.mouseClicked(e);
	}
	
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
	
	public void mouseExited(MouseEvent e) {
		if(e.getSource().getClass().equals(ViewMenuItem.class)) {
			System.out.println("SCHNABLER");
		}
	}
	
	public void disposeVM(SM_Room rom) {
		System.out.println("VM dispose called");
		rom.unregisterUpdateListener(vm);
		vm.dispose();
	}
	
	public void dispose() {
		System.out.println("closing this Arrview");
		super.dispose();
	}
}
