package smlfr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import artworkUpdateModel.ArtworkUpdateListener;

import processing.core.PGraphics;
import processing.core.PImage;

import SMUtils.Lang;
import SMUtils.ViewMenuItem;

import sfrenderer.SM_Renderer;

public class SM_ViewManager implements ActionListener, WindowListener {
	
	private SM_RoomArrangementView								view;
	private SM_WindowManager									wm;

	
	private SM_ViewAngle[]										viewAngles;
	private String 												currentAngle;
	
	private SM_Renderer											renderer;
	private boolean 											rMenuOpen = false;
	private boolean												rendererUpdate = false;
	
	private LinkedHashMap<String, SM_WallArrangementView>		wallArrangementViews;

	
	public SM_ViewManager(SM_RoomArrangementView _view, SM_WindowManager _wm, SM_ViewAngle[] _vas) {
		
		view = _view;
		view.registerMethod("post", this);
		wm = _wm;
		viewAngles = _vas;
		currentAngle = view.myRoom.getDefaultView();
		
		
		for( SM_ViewAngle va : viewAngles ) {
			va.sayHi();
		}
		
		for(SM_ViewAngle va : viewAngles ) {
			String defView = view.myRoom.getDefaultView();
			if( va.getName().equalsIgnoreCase(defView)) {
				
				initRenderer(va);
			}
		}
		

		wallArrangementViews = new LinkedHashMap<String, SM_WallArrangementView>();

		
		
//		initWallArrangementView('B');
		
	}
	
	public void initViews() {
		int ofset = 0;
		for( char w : renderer.getCurrentWallChars() ) {
			System.out.println("getting arrview for "+w);
			wallArrangementViews.put(""+w, initWallArrangementView(w, ofset));
			ofset += 40;
//			renderer.updateArtworksLayer();
		}
		doActiveViews();
	}
	
	private synchronized void initRenderer(SM_ViewAngle _va) {
		
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		renderer = new SM_Renderer(this,  _va, view.myRoom.getFilePath());
		f.add(renderer);		
		renderer.init(f);
		f.setVisible(true);
		int wait = 0;
		while( !renderer.setupRun) {
			System.out.println("waiting...  "+wait++);
			
		}
//		f.setVisible(true);
		f.setSize(renderer.getSize());
		f.setMaximumSize(renderer.getSize());
		f.setMinimumSize(renderer.getSize());
//		f.setUndecorated(true);

		f.setLocation(wm.getRaster().width,0);
		
		renderer.redraw();
//		f.setResizable(true);
	}
	
	private synchronized SM_WallArrangementView initWallArrangementView(char _wall, int _of) {
		
		
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.addWindowListener(this);
		Dimension s = wm.getRaster();
//		s.width  *= 2;
//		s.height *=2;
		SM_WallArrangementView wallArr = new SM_WallArrangementView((SM_Wall)view.myWalls.get(_wall), s, new Dimension(400, 10), this );
		
		
		wallArr.frame = f;
		


		wallArr.resize(wallArr.getSize());
		wallArr.setPreferredSize(wallArr.getSize());
		wallArr.setMinimumSize(wallArr.getSize());
		wallArr.frame.add(wallArr);
		wallArr.init();
		wallArr.frame.pack();
		wallArr.frame.setVisible(true);
		wallArr.frame.setLocation(0, _of);
		wallArr.frame.setTitle(Lang.wall+" "+wallArr.getWallName().substring(wallArr.getWallName().lastIndexOf('_')+1));

		
		return wallArr;
		
	}
	
	private void doActiveViews() {
		String retStrg = "";
		for(String w : wallArrangementViews.keySet()) {
			if(wallArrangementViews.get(w) != null) retStrg += w;
		}
		view.setActiveViews(retStrg);
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
		return view;
	}
	
	public synchronized boolean isRendererMenuOpen() {
		return renderer.isMenuOpen();
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

	public synchronized PImage getWallGfx(Character _wc) {
		if( isWallGfxReady(_wc) ) {
			
			
			if( wallArrangementViews.get(""+_wc) != null ) {
				return wallArrangementViews.get(""+_wc).getWallGfx();
			}
			else return null;
		}
		else return null;
	}
	
	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if( e.getSource().getClass().equals(ViewMenuItem.class)) {
			System.out.println("This has happened: "+e.getActionCommand());
			ViewMenuItem sourceItem = (ViewMenuItem)e.getSource();
			
			System.out.println("###\nlooking for this viewAngle: "+e.getActionCommand()+"\nin this list of viewAngles:");
			for(SM_ViewAngle va : viewAngles ) {
				System.out.println(va.getWallCharsAsString()+".");
			}
			
			
			
			for(SM_ViewAngle va : viewAngles ) {
				
				String v = e.getActionCommand();
				
//				System.out.println();
//				System.out.println("looking for this sequence from event:\n  "+v);
//				System.out.println("comparing with this from view:\n  "+va.getWallCharsAsString());
				
				if( va.getWallCharsAsString().equalsIgnoreCase(v)) {
					renderer.changeView(va);
				}
			}
			
			
			
			for(String s : wallArrangementViews.keySet()) {

				if( ! sourceItem.getActionCommand().contains(s)) {
					if ( wallArrangementViews.get(s) != null ) {
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
			doActiveViews();
		}
		System.gc();
	}
	
	public synchronized void openWallArr(char _c) {
		if(wallArrangementViews.get(""+_c) == null ) {
			wallArrangementViews.put(""+_c, initWallArrangementView(_c, 0));
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
			closeWallArr(t);
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
	
	public void post() {
		checkRendererUpdate();
	}
	
	public synchronized void checkRendererUpdate() {
		
		if( rendererUpdate ) {
			System.out.println("VM: rendererUpdate Detected\ntelling renderer to update");
			renderer.updateArtworksLayer();
			rendererUpdate = false;
		}
	}

	public void registerUpdateListener(ArtworkUpdateListener _l) {
		view.myRoom.registerUpdateListener(_l);
	}
	
	
	public void unregisterUpdateListener(ArtworkUpdateListener _l) {
		view.myRoom.unregisterUpdateListener(_l);
	}

}
