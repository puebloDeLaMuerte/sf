package smlfr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.JFrame;
import artworkUpdateModel.ArtworkUpdateEvent;
import artworkUpdateModel.ArtworkUpdateListener;

import processing.core.PImage;

import SMUtils.Lang;
import SMUtils.ViewMenuItem;

import sfrenderer.SM_Renderer;

public class SM_ViewManager implements ActionListener, WindowListener, ArtworkUpdateListener {
	
	private SM_RoomArrangementView								myRoomArrView;
	private SM_WindowManager									wm;

	
	private SM_ViewAngle[]										viewAngles;
	private String 												currentAngle;
	
	private SM_Renderer											renderer;
	private boolean 											rMenuOpen = false;
	private boolean												rendererUpdate = false;
	
	private LinkedHashMap<String, SM_WallArrangementView>		wallArrangementViews;

	
	public SM_ViewManager(SM_RoomArrangementView _view, SM_WindowManager _wm, SM_ViewAngle[] _vas) {
		
		myRoomArrView = _view;
		
//		view.registerMethod("post", this);
		
		wm = _wm;
		viewAngles = _vas;
		currentAngle = myRoomArrView.myRoom.getDefaultView();
		registerUpdateListener(this);
		
		for( SM_ViewAngle va : viewAngles ) {
			va.sayHi();
		}
		
		for(SM_ViewAngle va : viewAngles ) {
			String defView = myRoomArrView.myRoom.getDefaultView();
			if( va.getName().equalsIgnoreCase(defView)) {
				
				initRenderer(va);
			}
		}
		


		wallArrangementViews = new LinkedHashMap<String, SM_WallArrangementView>();

		initViews();
		
		for(SM_ViewAngle va : viewAngles ) {
			if( va.getName().equalsIgnoreCase(currentAngle)) {
				myRoomArrView.setVisibleViews(va.getWallCharsAsString());
			}
		}
		
		
//		initWallArrangementView('B');
		
	}
	
	public void initViews() {
		int ofset = 0;
		for( char w : renderer.getCurrentWallChars() ) {
			System.out.println("getting arrview for "+w);
			wallArrangementViews.put(""+w, initWallArrangementView(w, 0/*ofset*/));
			ofset += 40;
//			renderer.updateArtworksLayer();
			
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		doActiveViews();
	}
	
	private synchronized void initRenderer(SM_ViewAngle _va) {
		

		
		System.out.println("RENDERER...?");
		
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		renderer = new SM_Renderer(this,  _va, myRoomArrView.myRoom.getFilePath(), wm.getRaster().height*2);
		System.out.println("renderer given this height: "+wm.getRaster().height*2);
		
		renderer.frame = f;
		renderer.resize(renderer.getSize());
		renderer.setPreferredSize(renderer.getSize());
		renderer.setMinimumSize(renderer.getSize());
		renderer.frame.add(renderer);
		//
		renderer.init();
		//
//		renderer.resize(renderer.getSize());
		System.out.println("vm: the renderer returns this size: " + renderer.getSize().width+" x "+renderer.getSize().height);
		System.out.println("vm: the renderer-frame seems to be: "+renderer.frame.getWidth()+" x "+renderer.frame.getHeight());
		int wait = 0;
		while( !renderer.setupRun) {
			System.out.println("waiting...  "+wait++);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		renderer.frame.pack();
		renderer.frame.setVisible(true);
		renderer.frame.setLocation(wm.getScreen().width-renderer.getSize().width,0);
		
//		f.setSize(renderer.getSize());
		
		

//		renderer.frame.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);

//		f.setUndecorated(true);

		
		renderer.redraw();
//		f.setResizable(true);
	}
	
	private synchronized SM_WallArrangementView initWallArrangementView(char _wall, int _windowOfset) {
		
		
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.addWindowListener(this);
		
		Dimension maxAvailableSpace;
		
		Point lpos = wm.getLibraryPosition();
		Point rpos = renderer.getLocationOnScreen();
		
		if( lpos.y > 150 && rpos.x > 150 ) {
			maxAvailableSpace = new Dimension(rpos.x, lpos.y-(rpos.y));		
		} else {
			maxAvailableSpace = wm.getRaster();
		}
		SM_WallArrangementView wallArr = new SM_WallArrangementView((SM_Wall)myRoomArrView.myWalls.get(_wall), maxAvailableSpace, this );
		
		
		wallArr.frame = f;
		wallArr.resize(wallArr.getSize());
		wallArr.setPreferredSize(wallArr.getSize());
		wallArr.setMinimumSize(wallArr.getSize());
		wallArr.frame.setResizable(false);
		wallArr.frame.add(wallArr);
		wallArr.init();
		wallArr.frame.pack();
//		wallArr.frame.setVisible(true);
		wallArr.frame.setLocation(0, _windowOfset);
		wallArr.frame.setTitle(Lang.wall+" "+wallArr.getWallName().substring(wallArr.getWallName().lastIndexOf('_')+1));

		
		return wallArr;
		
	}
	
	private void doActiveViews() {
		String retStrg = "";
		for(String w : wallArrangementViews.keySet()) {
			if(wallArrangementViews.get(w) != null && !wallArrangementViews.get(w).isSleeping() ) retStrg += w;
		}
		myRoomArrView.setActiveViews(retStrg);
	}
	
	
	public synchronized int getNumberOfViewAngles() {
		return viewAngles.length;
	}
	
	public synchronized SM_ViewAngle getViewAngle(int _index) {
		return viewAngles[_index];
	}
	
	public synchronized String[] getViewAngleRealNames() {
		
		String[] names = new String[viewAngles.length];
		
		int i=0;
		for( SM_ViewAngle va : viewAngles) {
			names[i] = va.getRealName();
			i++;
		}
		
		
		return names;
	}
	
	public synchronized SM_RoomArrangementView getView() {
		return myRoomArrView;
	}
	
	public synchronized boolean isRendererMenuOpen() {
		if( renderer != null ) return renderer.isMenuOpen();
		else return false;
	}
	
	public synchronized boolean isWallGfxReady(char _wl) {

		if( wallArrangementViews != null ) {
			boolean retBool = false;
			for(String s : wallArrangementViews.keySet()) {
				if( s.equalsIgnoreCase(""+_wl) ) {

					if ( wallArrangementViews.get(s) != null ) {
						
						retBool = wallArrangementViews.get(s).isWallGfxReady();
						return retBool;
					}
					
				}
			} return false;
			
		}
		else return false;
		
		
	}

	public synchronized PImage getWallGfx(Character _wc, int _shdwOfset) {
		System.out.println("VM: getWallGfx:"+_wc);

		if( wallArrangementViews.get(""+_wc) != null ) {
			return wallArrangementViews.get(""+_wc).drawWall( 1 , _shdwOfset );
		}
		else return null;

	}
	
	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if( e.getSource().getClass().equals(ViewMenuItem.class)) {
			System.out.println("This has happened: "+e.getActionCommand());
			ViewMenuItem sourceItem = (ViewMenuItem)e.getSource();
			
			
			for(String s : wallArrangementViews.keySet()) {

				if( ! sourceItem.getActionCommand().contains(s)) {
					if ( wallArrangementViews.get(s) != null ) {
						
						
						
						 // make Artwork Graphics null, hope for heapspace to be cleared!
						//
						SM_Wall w = wallArrangementViews.get(s).getWall();
						HashMap<String, SM_Artwork> aws = w.getArtworks();
						for( String as : aws.keySet() ) {
							SM_Artwork aw = w.getArtwork(as);
							aw.unloadGraphics();
						}
							
						
						
						wallArrangementViews.get(s).setVisible(false);
						wallArrangementViews.get(s).frame.setVisible(false);
						wallArrangementViews.get(s).dispose();
						wallArrangementViews.put(s, null);
					}
				}
			}

			for(char w : sourceItem.getActionCommand().toCharArray() ) {
				if( wallArrangementViews.get(""+w) == null ) {
					wallArrangementViews.put(""+w, initWallArrangementView(w, 0));
				}
			}
			
			for(SM_ViewAngle va : viewAngles ) {
				
				String v = e.getActionCommand();
				
				if( va.getWallCharsAsString().equalsIgnoreCase(v)) {
					renderer.changeView(va);
					myRoomArrView.setVisibleViews(va.getWallCharsAsString());
				}
			}
			doActiveViews();
		}
		System.gc();
	}
	
	public synchronized void openWallArr(char _c) {
		if(wallArrangementViews.get(""+_c) == null ) {
			wallArrangementViews.put(""+_c, initWallArrangementView(_c, 0));
			wallArrangementViews.get(""+_c).frame.setVisible(true);
		} else if( wallArrangementViews.get(""+_c).isSleeping() ) {
			wallArrangementViews.get(""+_c).frame.setVisible(true);
			wallArrangementViews.get(""+_c).setEnabled(true);
			wallArrangementViews.get(""+_c).setVisible(true);
			System.out.println("Wie wecken wir es wieder auf?");
		} else {
			wallArrangementViews.get(""+_c).frame.toFront();
		}
		doActiveViews();
	}
	
	public synchronized void closeWallArr(String t){
		System.out.println("finally, removing this windowwwwww: "+t);
		for(String s : wallArrangementViews.keySet()) {

			if( s.equalsIgnoreCase(t) ) {
				if ( wallArrangementViews.get(s) != null ) {
					wallArrangementViews.get(s).setVisible(false);
					wallArrangementViews.get(s).frame.setVisible(false);
					wallArrangementViews.get(s).dispose();
					wallArrangementViews.put(s, null);
					
				}
			}
		}
		System.out.println("WallArrangementViews: "+wallArrangementViews.size());
		doActiveViews();
	}
	
	public synchronized void sleepWallArr(String t){
		System.out.println("fit's only going to be sleeping for a while: "+t);
		for(String s : wallArrangementViews.keySet()) {

			if( s.equalsIgnoreCase(t) ) {
				if ( wallArrangementViews.get(s) != null ) {
					wallArrangementViews.get(s).setVisible(false);
					wallArrangementViews.get(s).frame.setVisible(false);
					
				}
			}
		}
		System.out.println("WallArrangementViews: "+wallArrangementViews.size());
		doActiveViews();
	}
	
	public void dispose() {
		
		
		if(renderer == null) System.out.println("ALARM GESCHLAGEN");
		renderer.prepareFrameForClosing();
		renderer.dispose();
		renderer = null;
		for( String s : wallArrangementViews.keySet() ) {
			System.out.println("trying to dispose some views here!");
			if( wallArrangementViews.get(s) != null) {
				wallArrangementViews.get(s).setVisible(false);
				wallArrangementViews.get(s).frame.setVisible(false);
				wallArrangementViews.get(s).dispose();
				System.out.println("disposed "+s);
			}
			System.out.println("this is how it went");
		}
		wallArrangementViews = null;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("disposing VM, this is the source from Window closes: " + e.getSource());
		if (true/*e.getSource()*/) {
			System.out.println(e.getSource());
			JFrame f = (JFrame) e.getSource();
			f.setVisible(false);
			String t = f.getTitle();
			t = t.substring(t.length() - 1);
			sleepWallArr(t);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}

	
	public void setRendererUpdate() {
		rendererUpdate = true;
	}
	
	@Deprecated
	public void post() {
		checkRendererUpdate();
	}
	
	@Deprecated
	public synchronized void checkRendererUpdate() {
		
		/*
		 * 
		 * 	eventuell hier nen kleinen check, ob alle schon ein bild gemalt haben? 
		 * 
		 * 
		 *  hmmm?
		 * 
		 * 
		 */
		
		if( rendererUpdate ) {
			System.out.println("VM: rendererUpdate Detected\ntelling renderer to update");
			renderer.updateArtworksLayer( ' ' );
			renderer.setTimer();
			rendererUpdate = false;
		}
	}

	public void registerUpdateListener(ArtworkUpdateListener _l) {
		myRoomArrView.myRoom.registerUpdateListener(_l);
	}
	
	
	public void unregisterUpdateListener(ArtworkUpdateListener _l) {
		myRoomArrView.myRoom.unregisterUpdateListener(_l);
	}

	@Override
	public void artworkUpdate(ArtworkUpdateEvent e) {
		
		LinkedHashMap<String, Object> w = e.getData();
		
		for( String s : w.keySet() ) {
			System.out.println("VM: receiving update request: "+s+": "+w.get(s));
			if( s.contains("wall") ) {
				Object o = w.get(s);
				String os = (String)o;
				if( ! os.contains("Library") ) {
					
					int i = os.lastIndexOf('_');
					i++;
					char ww = os.charAt(i);

					if( renderer != null ) {
						
						renderer.updateArtworksLayer( ww );
					}
					
				}
			}
		}
		
	}
	
	public synchronized void requestRendererUpdate( char _wc) {
		if( renderer != null ) {
			System.out.println("requesting Update");
			renderer.updateArtworksLayer(_wc);
		}
	}

}
