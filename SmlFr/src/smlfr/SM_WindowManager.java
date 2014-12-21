package smlfr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.JFrame;

import SMUtils.progState;

public class SM_WindowManager {
	
	private progState				state;
	private SM_FileManager  		fm;
	private SmlFr					base;
	
	private java.awt.Dimension 		screen;
	private java.awt.Dimension 		roomNavSize;
	
	public SM_WindowManager(SM_FileManager _fm, SmlFr _base) {
		state = progState.LOADING;
		fm = _fm;
		base = _base;
		
		screen = Toolkit.getDefaultToolkit().getScreenSize();

		roomNavSize = new Dimension(screen.width / 3,screen.height / 3);
	}
	
	public synchronized void requestStateChange( progState _requestedState, String _requestedRoom) {
		
		if( state == progState.LOADING && _requestedState == progState.PROJECT ) {
			if( fm.isMuseumLoaded() && fm.isProjectLoaded() ) {
				
				String[] rooms = fm.getRoomNamesInProject();
				
				
				
				
			}
		} else
		if( state == progState.PROJECT && _requestedState == progState.ROOM    ) {
			
			
		} else
		if( state == progState.PROJECT && _requestedState == progState.LOADING ) {
			
			
		} else
		if( state == progState.ROOM    && _requestedState == progState.LOADING ) {
			
		}
	}
	
	public SM_Library createLibrary( HashMap<String, SM_Artwork> _artworks) {
		
		SM_Library tLib = new SM_Library(fm, _artworks);
		tLib.setSize(roomNavSize.width, (roomNavSize.height*3 -50));
		tLib.setResizable(true);
//		testlib.setUndecorated(true);
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
			f.setSize(roomNavSize);
			f.setBackground(Color.LIGHT_GRAY);
			f.setResizable(false);
			f.setUndecorated(true);
			f.setLocation(roomNavSize.width+roomNavSize.width*(x%2), roomNavSize.height*(y));
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
}
