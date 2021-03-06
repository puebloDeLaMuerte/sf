package smlfr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.LinkedHashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
//import processing.core.PApplet;
import processing.core.PImage;
import sfpMenu.SfpActionEvent;
import sfpMenu.SfpEventListener;
import sfpMenu.SfpViewMenuItem;
import SMUtils.Lang;
import SMUtils.SfFrame;
import SMUtils.ViewMenuItem;
import SMUtils.artworkActionType;
import SMupdateModel.UpdateEvent;
import SMupdateModel.UpdateListener;
import SMupdateModel.UpdateType;

import sfrenderer.ImageExporter;
import sfrenderer.SM_Renderer;

public class SM_ViewManager implements SfpEventListener, ActionListener, WindowListener, UpdateListener {
	
	private SM_RoomArrangementView								myRoomArrView;
	private SM_WindowManager									wm;
	private SmlFr												base;
	
	private SM_ViewAngle[]										viewAngles;
	private String 												currentAngle;
	
	private SM_Renderer											renderer;
//	private boolean 											rMenuOpen = false;
//	private boolean												rendererUpdate = false;
	
	private LinkedHashMap<String, SM_WallArrangementView>		wallArrangementViews;

	
	public SM_ViewManager(SM_RoomArrangementView _view, SM_WindowManager _wm, SM_ViewAngle[] _vas, SmlFr base) {
		
		myRoomArrView = _view;
		this.base = base;
		
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
		for( char w : renderer.getCurrentWallChars() ) {
			System.out.println("VIEW MANAGER: getting arrview for "+w);
			wallArrangementViews.put(""+w, initWallArrangementView(w, 0/*ofset*/));
//			renderer.updateArtworksLayer();
			
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		doActiveViews();
	}
	
	/*
	private synchronized void XinitRenderer(SM_ViewAngle _va) {
		


				
//		JFrame f = new JFrame();
//		RendererFrame f = new RendererFrame();
		
//		f.initMenu(this);
		
		System.err.println("this ViewManager inits a Renderer on this Thread: " + Thread.currentThread().getName());
//		System.err.println(Thread.currentThread().getStackTrace());
		
//		for( StackTraceElement e : Thread.currentThread().getStackTrace() ) {
//			System.err.println("  " + e.toString());
//		}
		
//		f.setLayout(new BorderLayout());
//		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		renderer = new SM_Renderer(this,  _va, myRoomArrView.myRoom.getFilePath(), wm.getRaster().height*2);

		renderer.resize(renderer.getSize());
//		System.out.println("VIEW MANAGER: renderer given this height: "+wm.getRaster().height*2);
		
		PApplet.runSketch(new String[] { PApplet.class.getName()}, renderer );
		renderer.initSpfMenu();
//		renderer.setLocation(wm.getScreen().width-renderer.getSize().width,0);
		
//		f.setRenderer(renderer);
//		renderer.addMouseListener(f);
		
//		renderer.frame = f;
//		renderer.resize(renderer.getSize());
//		renderer.setPreferredSize(renderer.getSize());
//		renderer.setMinimumSize(renderer.getSize());
//
//		renderer.frame.add(renderer);
//		
//		renderer.init();
//		renderer.initSpfMenu();
		
//		renderer.resize(renderer.getSize());
		System.out.println("VIEW MANAGER: vm: the renderer returns this size: " + renderer.getSize().width+" x "+renderer.getSize().height);
		System.out.println("VIEW MANAGER: vm: the renderer-frame seems to be: "+renderer.frame.getWidth()+" x "+renderer.frame.getHeight());
		int wait = 0;
		while( !renderer.setupRun) {
			System.out.println("VIEW MANAGER: waiting on Renderer setup() ...  "+wait++);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//		renderer.frame.setAlwaysOnTop(true);
		
//		renderer.frame.pack();
////		renderer.frame.setIgnoreRepaint(true);
//		renderer.frame.setVisible(true);
//		renderer.frame.setLocation(wm.getScreen().width-renderer.getSize().width,0);
//		renderer.frame.setResizable(false);
//		renderer.frame.setIgnoreRepaint(true);
		

		renderer.redraw();
		
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				
//				
//				
//				
//				
//				
//			}
//		});
	}
	
	*/
	
	private synchronized void initRenderer(SM_ViewAngle _va) {
		


		
		SfFrame f = new SfFrame();
//		RendererFrame f = new RendererFrame();
		
//		f.initMenu(this);
		
		
		f.setLayout(new BorderLayout());
//		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		renderer = new SM_Renderer(this,  _va, myRoomArrView.myRoom.getFilePath(), wm.getRaster().height*2);
		System.out.println("VIEW MANAGER: renderer given this height: "+wm.getRaster().height*2);
		
//		f.setRenderer(renderer);
//		renderer.addMouseListener(f);
		
		renderer.frame = f;
		renderer.resize(renderer.getSize());
		renderer.setPreferredSize(renderer.getSize());
		renderer.setMinimumSize(renderer.getSize());

//		Panel p = new Panel();
//		p.add(renderer);
//		renderer.frame.add(p);

		
		
		renderer.frame.add(renderer);
		
//		System.err.println("Thread that calls init on Renderer: " + Thread.currentThread().getName());

		
		renderer.init();
		renderer.initSpfMenu();
		
//		renderer.resize(renderer.getSize());
		System.out.println("VIEW MANAGER: vm: the renderer returns this size: " + renderer.getSize().width+" x "+renderer.getSize().height);
		System.out.println("VIEW MANAGER: vm: the renderer-frame seems to be: "+renderer.frame.getWidth()+" x "+renderer.frame.getHeight());
		
		
//		int wait = 0;
//		while( !renderer.setupRun) {
//			System.out.println("VIEW MANAGER: waiting on Renderer setup() ...  "+wait++);
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		
		
		
//		renderer.frame.setAlwaysOnTop(true);
		
		renderer.frame.pack();
//		renderer.frame.setIgnoreRepaint(true);
		renderer.frame.setVisible(true);
		renderer.frame.setLocation(wm.getScreen().width-renderer.getSize().width,0);
		renderer.frame.setResizable(false);
//		renderer.frame.setIgnoreRepaint(true);
		

		renderer.redraw();
		
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				
//				
//				
//				
//				
//				
//			}
//		});
	}
	
	/*
	
	private synchronized SM_WallArrangementView XinitWallArrangementView(char _wall, int _windowOfset) {
		
//		
//		JFrame f = new JFrame();
//		f.setLayout(new BorderLayout());
//		f.addWindowListener(this);
		
		Dimension maxAvailableSpace;
		
		Point lpos = wm.getLibraryPosition();
		Point rpos = renderer.getLocationOnScreen();
		
		if( lpos.y > 150 && rpos.x > 150 ) {
			maxAvailableSpace = new Dimension(rpos.x, lpos.y-(rpos.y));
		} else {
			maxAvailableSpace = wm.getRaster();
		}
		SM_WallArrangementView wallArr = new SM_WallArrangementView((SM_Wall)myRoomArrView.myWalls.get(_wall), maxAvailableSpace, this, base );
//		wallArr.setVisible(false);
		wallArr.resize(wallArr.getSize());
		
		PApplet.runSketch(new String[] {PApplet.class.getName()}, wallArr);
		
		wallArr.initMenu();
		wallArr.redraw();
		
//		while( !wallArr.finished) {
//			try {
//				System.out.println("Waiting for WallArr to finish setup() etc...");
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//		wallArr.frame = f;
//		wallArr.frame.setIgnoreRepaint(true);
//		wallArr.resize(wallArr.getSize());
//		wallArr.setPreferredSize(wallArr.getSize());
//		wallArr.setMinimumSize(wallArr.getSize());
//		wallArr.frame.setResizable(false);
//		wallArr.frame.add(wallArr);
//		wallArr.init();
//		wallArr.initMenu();
//		wallArr.frame.pack();
////		wallArr.frame.setVisible(true);
		wallArr.setLocation(0, _windowOfset);
//		wallArr.frame.setTitle(Lang.wall+" "+wallArr.getWallName().substring(wallArr.getWallName().lastIndexOf('_')+1));
//		wallArr.frame.setIgnoreRepaint(true);
		
		return wallArr;
		
	}
	
	*/
	
private synchronized SM_WallArrangementView initWallArrangementView(char _wall, int _windowOfset) {
		
		
		SfFrame f = new SfFrame();
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
		SM_WallArrangementView wallArr = new SM_WallArrangementView((SM_Wall)myRoomArrView.myWalls.get(_wall), maxAvailableSpace, this, base );
		
		
		wallArr.frame = f;
//		wallArr.frame.setIgnoreRepaint(true);
		wallArr.resize(wallArr.getSize());
		wallArr.setPreferredSize(wallArr.getSize());
		wallArr.setMinimumSize(wallArr.getSize());
		wallArr.frame.setResizable(false);
		
//		Panel p = new Panel();
//		
//		p.add(wallArr);
//		wallArr.frame.add(p);
		
		wallArr.frame.add(wallArr);
		
//		System.err.println("Thread that calls init on WallArrangementView: " + Thread.currentThread().getName());
		
		wallArr.init();
		wallArr.initMenu();
		wallArr.frame.pack();
//		wallArr.frame.setVisible(true);
		wallArr.frame.setLocation(0, _windowOfset);
		wallArr.frame.setTitle(Lang.wall+" "+wallArr.getWallName().substring(wallArr.getWallName().lastIndexOf('_')+1));
//		wallArr.frame.setIgnoreRepaint(true);
		
		return wallArr;
		
	}
//
//	public String getLightGfxPath() {
//		return wm.getLightGfxPath();
//	}
	
	private void doActiveViews() {
		String retStrg = "";
		for(String w : wallArrangementViews.keySet()) {
			if(wallArrangementViews.get(w) != null && !wallArrangementViews.get(w).isSleeping() ) retStrg += w;
		}
		myRoomArrView.setActiveViews(retStrg);
	}
	
	public void setCurrentViewAngle(String _va) {
		currentAngle = _va;
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
	
	public synchronized SM_RoomArrangementView getRoomArrView() {
		return myRoomArrView;
	}
	
	public int getRoomColor() {
		return myRoomArrView.myRoom.getRoomColor();
	}
	
	public synchronized boolean isRendererMenuOpen() {
		if( renderer != null ) return renderer.isMenuOpen();
		else return false;
	}

	public /*synchronized  manual*/ PImage getWallGfx(Character _wc, int _shdwOfset) {

		if( wallArrangementViews.get(""+_wc) != null ) {
			return wallArrangementViews.get(""+_wc)._drawWall4Renderer( 1, _shdwOfset);  //       drawWall( 1 , _shdwOfset );
		}
		else return null;
	}
	
	public PImage getWallGfxHiRes(Character _wc, int _shdwOfset) {
		
		if( wallArrangementViews.get(""+_wc) != null ) {
			return wallArrangementViews.get(""+_wc)._drawWall4Renderer( 2, _shdwOfset);  //       drawWall( 1 , _shdwOfset );
		}
		else return null;
	}
	
 	public synchronized   PImage getLightsGfx( Character _wc) {

		if( wallArrangementViews.get(""+_wc) != null ) {
			return wallArrangementViews.get(""+_wc)._drawLights();//          drawLights( 1 );
		}
		else return null;
	}
	
	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		
//		System.err.println(Thread.currentThread().getName());
//		Thread.currentThread().dumpStack();
		
		if( e.getSource().getClass().equals(ViewMenuItem.class)) {
			System.out.println("VIEW MANAGER: This Java-ActionEvent has happened: "+e.getActionCommand());
			ViewMenuItem sourceItem = (ViewMenuItem)e.getSource();
			
			
			for(String s : wallArrangementViews.keySet()) {

				if( ! sourceItem.getActionCommand().contains(s)) {
					if ( wallArrangementViews.get(s) != null ) {
						
						
						
						 // make Artwork Graphics null, hope for heapspace to be cleared!
						//
						SM_Wall w = wallArrangementViews.get(s).getWall();
//						HashMap<String, SM_Artwork> aws = w.getArtworks();
//						for( String as : aws.keySet() ) {
						for( SM_Artwork aw : (SM_Artwork[])w.artwork(artworkActionType.GET_ARRAY, null, null) ) {
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
		
		if( e.getActionCommand().equals(Lang.savePreviewImage)){
			
			System.out.println("VIEW MANAGER: saving preview image now");
			
			String name = "preview";
			name = myRoomArrView.myRoom.getRealName();
			for( SM_ViewAngle a : viewAngles ) {
				if( a.getName().equalsIgnoreCase(currentAngle)) {
					name += " "+a.getRealName();
				}
			}
			
			
			File exportLoc = new File(myRoomArrView.myRoom.getExportPath().getAbsolutePath()+"/"+name+".png");
			
			if( !exportLoc.getParentFile().exists() ) {
//				try {
//					File n = new File(exportLoc.getParent() + "/");
//					n.createNewFile();
					exportLoc.getParentFile().mkdirs();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
			}
			
			

			JFileChooser ch = new JFileChooser(exportLoc);
			ch.setSelectedFile(exportLoc);
			int fcVal = ch.showSaveDialog(null);
			
			System.err.println(fcVal);
			
			if( fcVal == 1 ) return;
			
			exportLoc = ch.getSelectedFile();
			
			int overwrite = 99;
			String message = Lang.overwrite_1 + exportLoc.getName() + Lang.overwrite_2;
			if( fcVal == 0 && exportLoc.exists() ) overwrite = JOptionPane.showConfirmDialog(null, message , Lang.overwriteTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, base.getQuestionIcon());
						
//			boolean success = false;
			if (overwrite == 0 || overwrite == 99) {
				String filename = exportLoc.getAbsolutePath();
				
				ImageExporter ex = new ImageExporter(renderer, filename);
				
				ex.start();
//				success = renderer.renderPreviewImage(filename);
			}
			
//			System.out.println("VIEW MANAGER: success: "+success);
		}
		
		System.gc();
	}
	
	/* (non-Javadoc)
	 * @see sfpMenu.SfpEventListener#eventHappened(sfpMenu.SfpActionEvent)
	 */
	@Override
	public synchronized void eventHappened(SfpActionEvent e) {

		if( e.getSource().equals(SfpViewMenuItem.class)) {
			System.out.println("VIEW MANAGER: This Sfp-ActionEvent has happened: "+e.getActionCommand());
//			SfpViewMenuItem sourceItem = (SfpViewMenuItem)e.getSource();
			
			
			for(String s : wallArrangementViews.keySet()) {

				if( ! e.getActionCommand().contains(s)) {
					if ( wallArrangementViews.get(s) != null ) {
						
						
						
						 // make Artwork Graphics null, hope for heapspace to be cleared!
						//
						SM_Wall w = wallArrangementViews.get(s).getWall();
//						HashMap<String, SM_Artwork> aws = w.getArtworks();
//						for( String as : aws.keySet() ) {
						for( SM_Artwork aw : (SM_Artwork[])w.artwork(artworkActionType.GET_ARRAY, null, null) ) {
							aw.unloadGraphics();
						}
					
						
						wallArrangementViews.get(s).setVisible(false);
						wallArrangementViews.get(s).frame.setVisible(false);
						wallArrangementViews.get(s).dispose();
						wallArrangementViews.put(s, null);
					}
				}
			}

			for(char w : e.getActionCommand().toCharArray() ) {
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
		
		if( e.getActionCommand().equals(Lang.savePreviewImage)){
			
			System.out.println("VIEW MANAGER: saving preview image now");
			
			String name = "preview";
			name = myRoomArrView.myRoom.getRealName();
			for( SM_ViewAngle a : viewAngles ) {
				if( a.getName().equalsIgnoreCase(currentAngle)) {
					name += " "+a.getRealName();
				}
			}
			
			
			File exportLoc = new File(myRoomArrView.myRoom.getExportPath().getAbsolutePath()+"/"+name+".png");
			
			if( !exportLoc.getParentFile().exists() ) {
//				try {
//					File n = new File(exportLoc.getParent() + "/");
//					n.createNewFile();
					exportLoc.getParentFile().mkdirs();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
			}
			
			

			JFileChooser ch = new JFileChooser(exportLoc);
			ch.setSelectedFile(exportLoc);
			int fcVal = ch.showSaveDialog(null);
			
			System.err.println(fcVal);
			
			if( fcVal == 1 ) return;
			
			exportLoc = ch.getSelectedFile();
			
			int overwrite = 99;
			String message = Lang.overwrite_1 + exportLoc.getName() + Lang.overwrite_2;
			if( fcVal == 0 && exportLoc.exists() ) overwrite = JOptionPane.showConfirmDialog(null, message , Lang.overwriteTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, base.getQuestionIcon());
						
//			boolean success = false;
			if (overwrite == 0 || overwrite == 99) {
				String filename = exportLoc.getAbsolutePath();
				
				ImageExporter ex = new ImageExporter(renderer, filename);
				
				ex.start();
//				success = renderer.renderPreviewImage(filename);
			}
			
//			System.out.println("VIEW MANAGER: success: "+success);
		}
		
		System.gc();
		
	}
	
	public synchronized void openWallArr(char _c) {
		if(wallArrangementViews.get(""+_c) == null ) {
			wallArrangementViews.put(""+_c, initWallArrangementView(_c, 0));
			wallArrangementViews.get(""+_c).frame.setVisible(true);
		} else if( wallArrangementViews.get(""+_c).isSleeping() ) {
			wallArrangementViews.get(""+_c).loop();
			wallArrangementViews.get(""+_c).frame.setVisible(true);
			wallArrangementViews.get(""+_c).setEnabled(true);
			wallArrangementViews.get(""+_c).setVisible(true);
			System.out.println("VIEW MANAGER: Wie wecken wir es wieder auf?");
		} else {
			wallArrangementViews.get(""+_c).frame.toFront();
		}
		doActiveViews();
	}
	
	public synchronized void closeWallArr(String t){
		System.out.println("VIEW MANAGER: finally, removing this windowwwwww: "+t);
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
		System.out.println("VIEW MANAGER: WallArrangementViews.size(): "+wallArrangementViews.size());
		doActiveViews();
	}
	
	public synchronized void sleepWallArr(String t){
		
		System.out.println("VIEW MANAGER: fit's only going to be sleeping for a while: "+t);
		
		for(String s : wallArrangementViews.keySet()) {

			if( s.equalsIgnoreCase(t) ) {
				if ( wallArrangementViews.get(s) != null ) {
//					wallArrangementViews.get(s).setVisible(false);
					wallArrangementViews.get(s).noLoop();
					wallArrangementViews.get(s).frame.setVisible(false);
					
				}
			}
		}
		System.out.println("VIEW MANAGER: WallArrangementViews.size(): "+wallArrangementViews.size());
		doActiveViews();
	}
	
	public void dispose() {
		
		
		if(renderer == null) System.out.println("VIEW MANAGER: Arenderer is null !");
		renderer.prepareFrameForClosing();
		renderer.dispose();
		renderer = null;
		for( String s : wallArrangementViews.keySet() ) {
			System.out.println("VIEW MANAGER: trying to dispose some views here!");
			if( wallArrangementViews.get(s) != null) {
				wallArrangementViews.get(s).setVisible(false);
				wallArrangementViews.get(s).frame.setVisible(false);
				wallArrangementViews.get(s).dispose();
				System.out.println("VIEW MANAGER: disposed "+s);
			}
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
		
		System.out.print("disposing VM: ");
		
			SfFrame f = (SfFrame) e.getSource();
			f.setVisible(false);
			String t = f.getTitle();
			System.out.print(t);
			t = t.substring(t.length() - 1);
			System.out.println("VIEW MANAGER:  sleeping this WallArr: "+t);
			sleepWallArr(t);
		
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

	public void registerUpdateListener(UpdateListener _l) {
		myRoomArrView.myRoom.registerUpdateListener(_l);
	}
		
	public void unregisterUpdateListener(UpdateListener _l) {
		myRoomArrView.myRoom.unregisterUpdateListener(_l);
	}

	@Override
	public void doUpdate(UpdateEvent e) {
		
//		System.err.println("View Manager receives UpdateRequest on Thread: " + Thread.currentThread().getName());
		
		UpdateType type = e.getType();
		LinkedHashMap<String, Object> data = e.getData();

		switch (type) {
		
		case POS_IN_WALL:
			
			for (String s : data.keySet()) {
				System.out.println("VIEW MANAGER: receiving update request: POS_IN_WALL: " + s + ": " + data.get(s));
				if (s.contains("wall")) {
					Object o = data.get(s);
					String os = (String) o;
					if (!os.contains("Library")) {

						int i = os.lastIndexOf('_');
						i++;
						char ww = os.charAt(i);

						if (renderer != null) {

							renderer.update.lights(ww);
							renderer.update.artworks(ww);
						}

					}
				}
			}
			break;
			
		case FRAME_STYLE:
			
			for (String s : data.keySet()) {
				System.out.println("VIEW MANAGER: receiving update request: FRAME_STYLE: " + s + ": " + data.get(s));
				if (s.contains("wall")) {
					
					Object o = data.get(s);
					String os = (String) o;

					int i = os.lastIndexOf('_');
					i++;
					char ww = os.charAt(i);

					if (renderer != null) {

					renderer.update.artworks(ww);
					}
				}
			}
			break;
			
		case ORIGINAL_COLOR:
			if (renderer != null) {
				renderer.update.color(null, null, null);
			}
			break;
			
		case ROOM_COLOR:
			
			if (renderer != null) {
				renderer.update.color(null, null, null);
			}
			break;
			
		case ROOM_COLOR_PREVIEW:
			
			if(renderer != null) {
								
				Integer c = (Integer)e.getData().get("color");
				
				renderer.update.color( c, null, null );
			}
			
			break;
			
		case WALL_COLOR_PREVIEW:
			
			if(renderer != null) {
				
				Character w = (Character)e.getData().get("wallChar");
				Integer c = (Integer)e.getData().get("color");
				
				renderer.update.color( null, w, c );
			}
			break;
			
		case WALL_COLOR:
			
			if (renderer != null) {
				renderer.update.color(null, null, null);
			}
			break;
						
		case WALL:
			
			for (String s : data.keySet()) {
				System.out.println("VIEW MANAGER: receiving update request: WALL: " + s + ": " + data.get(s));
				if (s.contains("wall")) {
					
					Object o = data.get(s);
					String os = (String) o;

					int i = os.lastIndexOf('_');
					i++;
					char ww = os.charAt(i);

					if (renderer != null) {

						renderer.update.lights(ww);
						renderer.update.artworks(ww);
					}
				}
			}
			break;
			
		case GENERAL_AW_DATA:
			for (String s : data.keySet()) {
				System.out.println("VIEW MANAGER: receiving update request: GENERAL_AW_DATA" + s + ": " + data.get(s));
				if (s.contains("wall")) {
					
					Object o = data.get(s);
					String os = (String) o;

					int i = os.lastIndexOf('_');
					i++;
					char ww = os.charAt(i);

					if (renderer != null) {

						renderer.update.lights(ww);
						renderer.update.artworks(ww);
					}
				}
			}
			break;
		
		default:
			break;
		}
		
		
		

	}
	
	public synchronized void requestRendererUpdate( char _wc) {
		if( renderer != null ) {
			System.out.println("VIEW MANAGER: requesting Update");
			renderer.update.lights(_wc);
			renderer.update.artworks(_wc);
		}
	}

	
	public boolean hasWallColor(char wc) {
		
		return myRoomArrView.myWalls.get((Character)wc).hasColor();
	}
	
	public int getWallColor(char wc) {
		return myRoomArrView.myWalls.get((Character)wc).getColor();
	}

	
	

}
