package sfrenderer;

import java.util.ArrayList;

import SMUtils.Lang;
import SMUtils.UpdateThread;

public class RendererUpdateThreadManager {

	private SM_Renderer renderer;
	
	private volatile ArrayList<UpdateThread> threads;
	
//	double delta;
	
	public RendererUpdateThreadManager(SM_Renderer r) {
		
		super();
		renderer = r;
		threads = new ArrayList<UpdateThread>(0);

	}
	
	public void color( Integer _previewColor, Character _previewWall, Integer _previewWallColor ) {
		
//		renderer.increaseBusyQueueMax();
//		renderer.setBusy(true, Lang.rendererBusy);
//		renderer.loop();


//		System.out.println("RENDERER_UPDATE_THREAD_MANAGER threads.size: " + threads.size());
		ColorUpdateThread t = new ColorUpdateThread( _previewColor, _previewWall, _previewWallColor);
		threads.add(t);
		
		t.start();
		System.out.println("RENDERER_UPDATE_THREAD_MANAGER threads.size: " + threads.size());
//		System.out.println("RENDERER_UPDATE_THREAD_MANAGER alive: "+t.isAlive());

	}
	
	public void artworks(char _wallChar) {

//		renderer.increaseBusyQueueMax();
//		renderer.setBusy(true, Lang.rendererBusy);
//		renderer.loop();
		


//		System.out.println("RENDERER_UPDATE_THREAD_MANAGER threads.size: " + threads.size());
		ArtworksUpdateThread t = new ArtworksUpdateThread(_wallChar);
		registerThread( t );
		t.start();
//		System.out.println("RENDERER_UPDATE_THREAD_MANAGER alive: "+t.isAlive());
//		System.out.println("RENDERER_UPDATE_THREAD_MANAGER threads.size: " + threads.size());
//		for( int i = 0; i<threads.size(); i++ ) {
//			System.out.println(i +": "+threads.get(i).getName()+ " : "+ ((UpdateThread)threads.get(i)).getWallChar()+": "+threads.get(i).getState());
//		}

	}
	
	public void lights(char _wallChar) {
		
//		renderer.increaseBusyQueueMax();
//		renderer.setBusy(true, Lang.rendererBusy);
//		renderer.loop();

//		System.out.println("RENDERER_UPDATE_THREAD_MANAGER threads.size: " + threads.size());
		LightsUpdateThread t = new LightsUpdateThread(_wallChar);
		registerThread( t);
		t.start();
		System.out.println("RENDERER_UPDATE_THREAD_MANAGER threads.size: " + threads.size());
//		System.out.println("RENDERER_UPDATE_THREAD_MANAGER alive: "+t.isAlive());
		
	}
	
	private void registerThread(UpdateThread t) {
//		if( getQueueLength() == 0 ) delta = System.currentTimeMillis();
		threads.add(t);
	}
	
	private void unregisterThread(Thread me) {
		
//		System.out.println("RENDERER_UPDATE_THREAD_MANAGER THREAD MANAGER: threads.size: " + threads.size());
//		System.out.println("RENDERER_UPDATE_THREAD_MANAGER THREAD MANAGER: unregistering thread: " + me.toString());
		threads.remove(me);
		renderer.setBusyQueueCurrent(threads.size());
//		System.out.println("RENDERER_UPDATE_THREAD_MANAGER THREAD MANAGER: threads.size: " + threads.size());
		
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
		
		private boolean hasStartedUpdate = false;
		
		public ColorUpdateThread( Integer _previewColor, Character _previewWall, Integer _previewWallColor ){
			previewWall = _previewWall;
			previewColor = _previewColor;
			previewWallColor = _previewWallColor;
			
			this.setName("RendererColorUpdateThread");
		}
		
		@Override
		public char getWallChar() {
			return ' ';
		}
		
		@Override
		public boolean hasStartedUpdate() {
			return hasStartedUpdate;
		}
		@Override
		public void run() {
			
						
			hasStartedUpdate = true;
			
			renderer.increaseBusyQueueMax();
			renderer.setBusy(true, Lang.rendererBusy);
			renderer.loop();

			renderer.updateRoomColorLayer(previewColor, previewWall, previewWallColor);
			
			
			unregisterThread(this);
		}
	}

	private class ArtworksUpdateThread extends Thread implements UpdateThread {
		
		private char wallChar;
		private boolean hasStartedUpdate = false;
		
		public ArtworksUpdateThread( char _wallChar) {
			wallChar = _wallChar;
			
			this.setName("RendererArtworksUpdateThread");
		}
		
		@Override
		public char getWallChar() {
			return wallChar;
		}
		
		@Override
		public boolean hasStartedUpdate() {
			return hasStartedUpdate;
		}
		
		@Override
		public void run() {
			
			boolean go;
			boolean abort = false;
			char c;
			while( true ) {
				
				go = true;
				
				for( int i = 0; i< threads.size(); i++ ) {
					
					UpdateThread t = threads.get(i);
					try {
						c = t.getWallChar();
					} catch (Exception e) {
						c = ' ';
					}
					
					if( c == wallChar && t != this && t.getClass() == this.getClass()) {
						
						go = false;
						
						if ( !t.hasStartedUpdate()) {
							abort = true;
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
				try {
					Thread.sleep(50);
				} catch( Exception e ) {
					System.err.println("THIS EXCEPTION GOT CAUGHT: ");
					e.printStackTrace();
				}
			}
			
			hasStartedUpdate = true;
			
			renderer.increaseBusyQueueMax();
			renderer.setBusy(true, Lang.rendererBusy);
			renderer.loop();
			
			renderer.updateArtworksLayer(wallChar);
			

			
			unregisterThread(this);
			
		}
	}

	private class LightsUpdateThread extends Thread implements UpdateThread {
		
		private char wallChar;
		private boolean hasStartedUpdate = false;
		
		public LightsUpdateThread( char _wallChar) {
			wallChar = _wallChar;
			
			this.setName("RendererLightsUpdateThread");
		}
		
		@Override
		public char getWallChar() {
			return wallChar;
		}
		
		@Override
		public boolean hasStartedUpdate() {
			return hasStartedUpdate;
		}
		
		@Override
		public void run() {
			
			System.err.println("Lights Update Thread named: " + Thread.currentThread().getName());
			
			boolean go;
			boolean abort = false;
			char c;
			while( true ) {
				
				go = true;
				
				for( int i = 0; i< threads.size(); i++ ) {
					
					UpdateThread t = threads.get(i);
					try {
						c = t.getWallChar();
					} catch (Exception e) {
						c = ' ';
					}
					
					if( c == wallChar && t != this && t.getClass() == this.getClass()) {
						
						go = false;
						
						if ( !t.hasStartedUpdate()) {
							abort = true;
						}
					}
				}
				
				if(go) break;
				if(abort) {
//					System.err.println("thread aborted");
					unregisterThread(this);
					return;
				}
//				System.err.println("update thread waiting... "+this.getName());
				try {
					Thread.sleep(50);
				} catch( Exception e ) {
					System.err.println("THIS EXCEPTION GOT CAUGHT: ");
					e.printStackTrace();
				}
			}
			
			hasStartedUpdate = true;

			renderer.increaseBusyQueueMax();
			renderer.setBusy(true, Lang.rendererBusy);
			renderer.loop();

			renderer.updateLightsLayer(wallChar);
			
			
			unregisterThread(this);
		}
	}

	
}
