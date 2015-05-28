package sfrenderer;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class RendererUpdateThreadManager {

	private SM_Renderer renderer;
	
	private ArrayList<Thread> threads;
	
	public RendererUpdateThreadManager(SM_Renderer r) {
		
		super();		
		renderer = r;
		threads = new ArrayList<Thread>(0);
	}
	
	public void color( Integer _previewColor, Character _previewWall, Integer _previewWallColor ) {
		
		renderer.setBusy(true);
//		renderer.redraw();
		renderer.loop();

		System.out.println("threads.size: " + threads.size());
		ColorUpdateThread t = new ColorUpdateThread( _previewColor, _previewWall, _previewWallColor);
		threads.add(t);
		
		
		t.start();
		System.out.println("threads.size: " + threads.size());		
		System.out.println("alive: "+t.isAlive());

	}
	
	public void artworks(char _wallChar) {
		
		renderer.setBusy(true);
//		renderer.redraw();
		renderer.loop();

		System.out.println("threads.size: " + threads.size());
		ArtworksUpdateThread t = new ArtworksUpdateThread(_wallChar);
		threads.add( t );
		t.start();		
		System.out.println("alive: "+t.isAlive());
		System.out.println("threads.size: " + threads.size());

	}
	
	public void lights(char _wallChar) {
		
		renderer.setBusy(true);
//		renderer.redraw();
		renderer.loop();

		System.out.println("threads.size: " + threads.size());
		LightsUpdateThread t = new LightsUpdateThread(_wallChar);
		threads.add( t);
		t.start();
		System.out.println("threads.size: " + threads.size());		
		System.out.println("alive: "+t.isAlive());
		
	}
	
	private void unregisterThread(Thread me) {
		
		System.out.println("THREAD MANAGER: threads.size: " + threads.size());
		System.out.println("THREAD MANAGER: unregistering thread: " + me.toString());
		threads.remove(me);
		System.out.println("THREAD MANAGER: threads.size: " + threads.size());
		
		if( threads.size() == 0) {
			System.out.println("THREAD MANAGER: calling recall");
			renderer.setBusy(false);
			renderer.noLoop();
//			renderer.redraw();
			renderer.threadManagerRecall();
			System.out.println("THREAD MANAGER: recall called");
		}
	}
	
	private class ColorUpdateThread extends Thread  {
		
		private Integer previewColor, previewWallColor;
		private Character previewWall;
		
		public ColorUpdateThread( Integer _previewColor, Character _previewWall, Integer _previewWallColor ){
			previewWall = _previewWall;
			previewColor = _previewColor;
			previewWallColor = _previewWallColor;
			
			this.setName("RendererColorUpdateThread");
		}
		
		public void run() {
			
//			try {
//				this.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
						
			renderer.updateRoomColorLayer(previewColor, previewWall, previewWallColor);
			
			unregisterThread(this);
		}
	}

	private class ArtworksUpdateThread extends Thread {
		
		private char wallChar;
		
		public ArtworksUpdateThread( char _wallChar) {
			wallChar = _wallChar;
			
			this.setName("RendererArtworksUpdateThread");
		}
		
		
		public void run() {
			
//			try {
//				this.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			renderer.updateArtworksLayer(wallChar);
			
			unregisterThread(this);
			
		}
	}

	private class LightsUpdateThread extends Thread {
		
		private char wallChar;
		
		public LightsUpdateThread( char _wallChar) {
			wallChar = _wallChar;
			
			this.setName("RendererLightsUpdateThread");
		}
		
		
		public void run() {
			
//			try {
//				this.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			renderer.updateLightsLayer(wallChar);
			
			unregisterThread(this);
		}
	}
}
