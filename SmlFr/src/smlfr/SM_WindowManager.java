package smlfr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.JFrame;

import SMUtils.Lang;
import SMUtils.progState;

public class SM_WindowManager {
	
	private progState				state;
	private SM_FileManager  		fm;
	private SmlFr					base;
	
	private java.awt.Dimension 		screen;
	private java.awt.Dimension 		raster;
	
	public SM_WindowManager(SM_FileManager _fm, SmlFr _base) {
		
		state = progState.LOADING;
		fm = _fm;
		base = _base;
		
		screen = Toolkit.getDefaultToolkit().getScreenSize();

		raster = new Dimension(screen.width / 3, screen.height / 3);
	}
	
	public synchronized void requestStateChange( progState _requestedState, String _requestedRoom) {
		
		System.out.println("ProgStateRequest registered. Schreiting zur Tat.. .  .  .    .        .");
		
		if( state == progState.LOADING && _requestedState == progState.PROJECT ) {
			if( fm.isMuseumLoaded() && fm.isProjectLoaded() ) {
				
				System.out.println("we're initializing the project state change");
				
				String[] rooms = fm.getRoomNamesInProject();
				int x = 0; int y = 0;
				for(String r : rooms) {
					Dimension loc = new Dimension(raster.width+raster.width*(x%2), raster.height*(y));
					base.rooms.get(r).initProjectView(raster, loc, fm);
					x++;
					if(x%2==0)y++;
				}
				
				
				base.lib = createLibrary(base.artworks);
				fm.registerUpdateListener(base.lib);
				
				
				state = progState.PROJECT;
			}
		} else
			
			
		if( state == progState.PROJECT && _requestedState == progState.ROOM    ) {
			
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
			
			
			String[] rooms = fm.getRoomNamesInProject();
			for(String r : rooms) {
				if( !r.equalsIgnoreCase(_requestedRoom) ) {
					
					base.rooms.get(r).endView();
					
				} else {
					
					base.rooms.get(r).endView();
					base.rooms.get(r).initArrangementView(raster, new Dimension(raster.width*2,raster.height*2), fm);
					base.lib.setSize(raster.width*2, raster.height);
					base.lib.setLocation(0, raster.height*2);
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
			
			String[] rooms = fm.getRoomNamesInProject();
			for(String r : rooms) {
				if( base.rooms.get(r).isEntered() ) {
					base.rooms.get(r).endView();
				}
			}
			System.gc();
			int x = 0; int y = 0;
			for(String r : rooms) {
				Dimension loc = new Dimension(raster.width+raster.width*(x%2), raster.height*(y));
				base.rooms.get(r).initProjectView(raster, loc, fm);
				x++;
				if(x%2==0)y++;
			}
			
			base.lib.setSize(raster.width, (raster.height*3 -50));
			base.lib.setLocation(0, 0);
			
			state = progState.PROJECT;
		}
	}
	
	public SM_Library createLibrary( HashMap<String, SM_Artwork> _artworks) {
		
		SM_Library tLib = new SM_Library(fm, _artworks);
		tLib.setSize(raster.width, (raster.height*3 -50));
		tLib.setResizable(true);
//		tLib.setUndecorated(true);
		tLib.setBackground(Color.DARK_GRAY);
		tLib.setVisible(true);
		tLib.setLocation(0, 0);

		tLib.initUI();
		
		return tLib;
	}
	
	
	
	public void testFrames() {
		JFrame[] tst = new JFrame[6];
		
		int x = 0; int y = 0;
		for( JFrame f : tst ) {
			f = new JFrame();
			f.setSize(raster);
			f.setBackground(Color.LIGHT_GRAY);
			f.setResizable(false);
			f.setUndecorated(true);
			f.setLocation(raster.width+raster.width*(x%2), raster.height*(y));
			f.setVisible(true);
			x++;
			if (x%2 == 0 ) y++;
		}
	}
	
	public boolean isLoading() {
		if( state == progState.LOADING ) return true;
		else return false;
	}
	
	public boolean isProject() {
		if( state == progState.PROJECT ) return true;
		else return false;
	}
	
	public boolean isRoom() {
		if( state == progState.ROOM ) return true;
		else return false;
	}

	public Dimension getRaster() {
		return raster;
	}
}
