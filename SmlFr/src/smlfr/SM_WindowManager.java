package smlfr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;

import SMUtils.Lang;
import SMUtils.Raster;
import SMUtils.progState;

public class SM_WindowManager{
	
	private progState				state;
	private SM_FileManager  		fm;
	private SmlFr					base;
	
	private Raster					rst;
	
	public SM_WindowManager(SM_FileManager _fm, SmlFr _base) {
		
		state = progState.LOADING;
		fm = _fm;
		base = _base;
		
		rst = new Raster(_base);
		
        
	}
	
	public synchronized void requestStateChange( progState _requestedState, String _requestedRoom) {
		
		
		if( state == progState.LOADING && _requestedState == progState.PROJECT ) {
			if( fm.isMuseumLoaded() && fm.isProjectLoaded() ) {
				
				
				String[] rooms = fm.getRoomNamesInProject();
				int x = 0; int y = 0;
				for(String r : rooms) {
					if( x <= 5) {
						Point loc = rst.getPos(1+(x%2), y);
						base.rooms.get(r).initProjectView(rst.getSize(1, 1), loc, base);
						x++;
						if(x%2==0)y++;
					} else {
						if( x == 6 ) y = 2;
						Point loc = rst.getPos(0, y);
						base.rooms.get(r).initProjectView(rst.getSize(1, 1), loc, base);
						x++;
						y--;
					}
				}
				
				
				base.lib = createLibrary(base.artworks, _requestedState);
				fm.registerUpdateListener(base.lib);
				
				
				state = progState.PROJECT;
			}
		} else
			
			
		if( state == progState.PROJECT && _requestedState == progState.ROOM    ) {
			
			if(fm.isSaveDirty()) {
				int decide = javax.swing.JOptionPane.showOptionDialog(null, Lang.unsavedChanges, Lang.unsavedChangesTitle, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.DEFAULT_OPTION, base.getQuestionIcon(), Lang.yesNoCancelOptions, 0);
				switch (decide) {
				case 0:
					return;
				case 1:
					break;
				case 2:
					fm.requestSave();
					break;
				}
			}
			
			
			String[] rooms = fm.getRoomNamesInProject();
			for(String r : rooms) {
				
//				base.rooms.get(rst).unregisterArtworkUpdateListeners();
				base.rooms.get(r).endView();
					
			}
			
			for( String r : rooms) {
				
				if( r.equalsIgnoreCase(_requestedRoom) ) {
					
					base.lib.setSize(rst.getSize(2, 1));
					base.lib.setLocation(rst.getPos(0, 2));
					base.rooms.get(r).initArrangementView(rst.getSize(1, 1), rst.getPos(2, 2), base);
				}
				
			}
			
			System.gc();
			state = progState.ROOM;
			
		} else
		if( state == progState.PROJECT && _requestedState == progState.LOADING ) {
			
			
		} else
		if( state == progState.ROOM    && _requestedState == progState.LOADING ) {
			
		} else
		if( state == progState.ROOM    && _requestedState == progState.PROJECT ) {
			
			
			if(fm.isSaveDirty()) {
				int decide = javax.swing.JOptionPane.showOptionDialog(null, Lang.unsavedChanges, Lang.unsavedChangesTitle, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.DEFAULT_OPTION, base.getIcon(), Lang.yesNoCancelOptions, 0);
				switch (decide) {
				case 0:
					return;
				case 1:
					break;
				case 2:
					fm.requestSave();
					break;
				}
			}
			
			fm.unregisterArrViewAWUpdateListeners();
//			fm.unregisterViewManagerUpdateListeners();
			
			
			
			String[] rooms = fm.getRoomNamesInProject();
			for(String r : rooms) {
				if( base.rooms.get(r).isEntered() ) {
					base.rooms.get(r).endView();
				}
			}
			System.gc();
			int x = 0; int y = 0;
			for(String r : rooms) {
//				Dimension loc = new Dimension(raster.width+raster.width*(x%2), raster.height*(y));
//				base.rooms.get(rst).initProjectView(raster, loc, base);
//				x++;
//				if(x%2==0)y++;
				if( x <= 5) {
					Point loc = rst.getPos(1+(x%2), y);
					base.rooms.get(r).initProjectView(rst.getSize(1, 1), loc, base);
					x++;
					if(x%2==0)y++;
				} else {
					if( x == 6 ) y = 2;
					Point loc = rst.getPos(0, y);
					base.rooms.get(r).initProjectView(rst.getSize(1, 1), loc, base);
					x++;
					y--;
				}
			}
			
			if( rooms.length >6) {
				base.lib.setSize(rst.getSize(1, 2));
			} else {
				base.lib.setSize(rst.getSize(1, 3));
			}
			base.lib.setLocation(rst.getPos(0, 0));
			
			state = progState.PROJECT;
			System.gc();
		}
	}
	
	public SM_Library createLibrary( HashMap<String, SM_Artwork> _artworks, progState state) {
		
		SM_Library tLib = new SM_Library(fm, _artworks);
		
		switch (state) {
		
		case PROJECT:
			
			int roomCount = fm.getRoomNamesInProject().length;
			
			if( roomCount <= 6) tLib.setSize(rst.getWidthNoDecorations(1), rst.getHeightNoDecorations(3));
			else if( roomCount <= 8) tLib.setSize(rst.getSizeNoDecorations(1, 3-(roomCount-6)));
			else tLib.setSize(rst.getSizeNoDecorations(1, 3));

			
			tLib.setLocation(rst.getPos(0, 0));
			break;
			
		case ROOM:

			tLib.setSize(rst.getSize(2, 1));
			tLib.setLocation(rst.getPos(0, 3));
			break;

		default:
			tLib.setSize(rst.getSize(1, 3));
			tLib.setLocation(0, 0);
			break;
		}
		
		
		tLib.setResizable(true);
		tLib.setBackground(Color.LIGHT_GRAY);
		tLib.setVisible(true);
		tLib.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);

		tLib.initUI();
		
		return tLib;
	}
	
	public boolean isLoading() {
		if( state == progState.LOADING ) return true;
		else return false;
	}
	
	public boolean isProject() {
		if( state == progState.PROJECT ) return true;
		else return false;
	}
	
	public progState getState() {
		return state;
	}
	
	public boolean isRoom() {
		if( state == progState.ROOM ) return true;
		else return false;
	}

	public Dimension getRaster() {
		return rst.getSize(1, 1);
	}

	public Dimension getScreen() {
		return rst.getScreen();
	}

	public Point getLibraryPosition() {
		return  base.lib.getLocationOnScreen();
	}
}
