package sfrenderer;

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import SMUtils.Lang;
import SMUtils.UpdateThread;

public class RendererUpdateThreadManager {

	private SM_Renderer renderer;
	
	private ArrayList<Thread> threads;
	
//	double delta;
	
	public RendererUpdateThreadManager(SM_Renderer r) {
		
		super();		
		renderer = r;
		threads = new ArrayList<Thread>(0);
	}
	
	public void color( Integer _previewColor, Character _previewWall, Integer _previewWallColor ) {
		
		renderer.increaseBusyQueueMax();
		renderer.setBusy(true, Lang.rendererBusy);
//		renderer.redraw();
		renderer.loop();

//		System.out.println("threads.size: " + threads.size());
		ColorUpdateThread t = new ColorUpdateThread( _previewColor, _previewWall, _previewWallColor);
		threads.add(t);
		
		t.start();
		System.out.println("threads.size: " + threads.size());		
//		System.out.println("alive: "+t.isAlive());

	}
	
	public void artworks(char _wallChar) {

		renderer.increaseBusyQueueMax();
		renderer.setBusy(true, Lang.rendererBusy);
//		renderer.redraw();
		renderer.loop();
		


//		System.out.println("threads.size: " + threads.size());
		ArtworksUpdateThread t = new ArtworksUpdateThread(_wallChar);
		registerThread( t );
		t.start();		
//		System.out.println("alive: "+t.isAlive());
		System.out.println("threads.size: " + threads.size());
		for( int i = 0; i<threads.size(); i++ ) {
			System.out.println(i +": "+threads.get(i).getName()+ " : "+ ((UpdateThread)threads.get(i)).getWallChar()+": "+threads.get(i).getState());
		}

	}
	
	public void lights(char _wallChar) {
		
		renderer.increaseBusyQueueMax();
		renderer.setBusy(true, Lang.rendererBusy);
//		renderer.redraw();
		renderer.loop();

//		System.out.println("threads.size: " + threads.size());
		LightsUpdateThread t = new LightsUpdateThread(_wallChar);
		registerThread( t);
		t.start();
		System.out.println("threads.size: " + threads.size());		
//		System.out.println("alive: "+t.isAlive());
		
	}
	
	private void registerThread(Thread t) {
//		if( getQueueLength() == 0 ) delta = System.currentTimeMillis();
		threads.add(t);
	}
	
	private void unregisterThread(Thread me) {
		
//		System.out.println("THREAD MANAGER: threads.size: " + threads.size());
//		System.out.println("THREAD MANAGER: unregistering thread: " + me.toString());
		threads.remove(me);
		renderer.setBusyQueueCurrent(threads.size());
//		System.out.println("THREAD MANAGER: threads.size: " + threads.size());
		
		if( threads.size() == 0) {
			
//			JOptionPane.showConfirmDialog(null, "AW update time:\n" + (System.currentTimeMillis() - delta ));

			
			renderer.setBusy(false, Lang.rendererBusy);
			renderer.noLoop();
//			renderer.redraw();
			renderer.threadManagerRecall();
		}
	}

	public int getQueueLength() {
		return threads.size();
	}
	
	private class ColorUpdateThread extends Thread implements UpdateThread {
		
		private Integer previewColor, previewWallColor;
		private Character previewWall;
		
		public ColorUpdateThread( Integer _previewColor, Character _previewWall, Integer _previewWallColor ){
			previewWall = _previewWall;
			previewColor = _previewColor;
			previewWallColor = _previewWallColor;
			
			this.setName("RendererColorUpdateThread");
		}
		
		public char getWallChar() {
			return ' ';
		}
		
		public void run() {
			
						
			renderer.updateRoomColorLayer(previewColor, previewWall, previewWallColor);
			
			
			unregisterThread(this);
		}
	}

	private class ArtworksUpdateThread extends Thread implements UpdateThread {
		
		private char wallChar;
		
		public ArtworksUpdateThread( char _wallChar) {
			wallChar = _wallChar;
			
			this.setName("RendererArtworksUpdateThread");
		}
		
		public char getWallChar() {
			return wallChar;
		}
		
		public void run() {
			
			boolean go;
			boolean abort = false;
			char c;
			while( true ) {
				
				go = true;
				
				for( int i = 0; i< threads.size(); i++ ) {
					
					Thread t = threads.get(i);
					try {
						c = ((UpdateThread)t).getWallChar();
					} catch (Exception e) {
						c = ' ';
					}
					
					if( c == wallChar && t != this ) {
						
						go = false;
						
						if ( t.getClass() == this.getClass() ) {
							abort = true;
							go = false;
						}
					}
				}
				
				if(go) break;
				if(abort) {
					System.err.println("thread aborted");
					unregisterThread(this);
					return;
				}
//				System.err.println("update thread waiting... "+this.getName());
			}
			
			
			renderer.updateArtworksLayer(wallChar);
			

			
			unregisterThread(this);
			
		}
	}

	private class LightsUpdateThread extends Thread implements UpdateThread {
		
		private char wallChar;
		
		public LightsUpdateThread( char _wallChar) {
			wallChar = _wallChar;
			
			this.setName("RendererLightsUpdateThread");
		}
		
		public char getWallChar() {
			return wallChar;
		}
		
		public void run() {
			
			boolean go;
			boolean abort = false;
			char c;
			while( true ) {
				
				go = true;
				
				for( int i = 0; i< threads.size(); i++ ) {
					
					Thread t = threads.get(i);
					try {
						c = ((UpdateThread)t).getWallChar();
					} catch (Exception e) {
						c = ' ';
					}
					
					if( c == wallChar && t != this ) {
						
//						go = false;
						
						if ( t.getClass() == this.getClass() ) {
							abort = true;
							go = false;
						}
					}
				}
				
				if(go) break;
				if(abort) {
					System.err.println("thread aborted");
					unregisterThread(this);
					return;
				}
//				System.err.println("update thread waiting... "+this.getName());
			}
			
			renderer.updateLightsLayer(wallChar);
			
			
			unregisterThread(this);
		}
	}

	
}
