/**
 * 
 */
package SMUtils;

import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import processing.core.PApplet;

/**
 * @author pht
 *
 */
public class SfPApplet extends PApplet {

	@Override
	public void handleDraw() {
		
		try {
//			System.err.println("Invoking the Rnble and waiting from this Thread: " + Thread.currentThread().getName());
			SwingUtilities.invokeAndWait(new Rnble(this) );
		} catch (InterruptedException e) {
			System.err.println("SfPApplet interrupted");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.err.println("SfPApplet interrupted");
			e.printStackTrace();
		}
//		System.err.println("We waited - it has been drawn i guess...");
	}
	
	private void doHandleDraw() {
//		System.err.println("Draw will be handeled on this thread: " + Thread.currentThread().getName());
		super.handleDraw();
//		System.err.println("Draw has been handeled.");
	}
	
//	private void doPaint (Graphics screen) {
//		System.err.println("painting on this Thread: " + Thread.currentThread().getName());
//		super.paint(screen);
//
//	}
	
	private class Rnble implements Runnable{
		
		private SfPApplet ap;
//		private Graphics s;
		
		public Rnble(SfPApplet ap /*, Graphics s*/) {
			this.ap = ap;
//			this.s = s;
		}
		
		@Override
		public void run() {
			ap.doHandleDraw();
//			ap.doPaint(s);
		}
		
	}



//	@Override
//	public void paint(Graphics screen) {
//		
////		if( !SwingUtilities.isEventDispatchThread() ) {
////			System.err.println("redirecting");
////			try {
////				SwingUtilities.invokeAndWait(new Rnble(this, screen));
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (InvocationTargetException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		} else {
////			System.err.println("no redirect - already EDT");
////			super.paint(screen);
////		}
//		System.err.println("paint called. Thread: " + Thread.currentThread().getName());
//		super.paint(screen);
//	}
//
//
//
//	@Override
//	public void paintComponents(Graphics g) {
//		System.err.println("paintComponents called. Thread: " + Thread.currentThread().getName());
//		super.paintComponents(g);
//	}
//
//
//
//	@Override
//	public void paintAll(Graphics g) {
//		System.err.println("paintAll called. Thread: " + Thread.currentThread().getName());
//		super.paintAll(g);
//	}

}
