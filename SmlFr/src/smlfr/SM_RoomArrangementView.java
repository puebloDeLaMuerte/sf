package smlfr;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;

import processing.core.PShape;

import SMUtils.Lang;
import SMUtils.ViewMenuItem;

public class SM_RoomArrangementView extends SM_RoomProjectView implements MouseListener {

	private SM_ViewManager					vm;
	private HashMap<Character, PShape>		wallsOverGfx;
	private HashMap<Character, PShape>		wallsActiveGfx;
	
	private boolean 						showViewMenuPreview = false;
	private char[]							viewMenuCurrentHighlight;
	
	private char[]							activeViews;
	
	

	
	public void init(JFrame _frame, int[] _size, File _filePath, SM_Room _room, SM_ViewAngle[] _vas) {
		super.init(_frame, _size, _filePath, _room);
		
		
		vm = new SM_ViewManager(this, myRoom.getWindowManager(), _vas);
		vm.initViews();
		
		wallsOverGfx = super.getWallsOverGfx();
		wallsActiveGfx = super.getWallsActiveGfx();
	}

	
	public void draw() {

//		vm.checkRendererUpdate();
		
		super.draw();

		if(showViewMenuPreview) {
			if( vm.isRendererMenuOpen() ) {

				for(char c : viewMenuCurrentHighlight ) {

					shape(wallsOverGfx.get(c), 0, 0);

				}
			}
			
		}
		if( activeViews != null ) {

			
			for( char v : activeViews ) {
				shape(wallsActiveGfx.get(v), 0,0);
			}
			
			
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
	
	public void disposeVM() {
		vm.dispose();
	}
	
	public void dispose() {
		System.out.println("closing this Arrview");
		 
		super.dispose();
	}
}
