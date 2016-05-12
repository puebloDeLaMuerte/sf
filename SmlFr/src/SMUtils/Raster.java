package SMUtils;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;

import smlfr.SmlFr;

public class Raster {
	
	private java.awt.Dimension 		screen;
	private Rectangle				realscreen;
	private java.awt.Dimension 		raster;
	private int						topOffset;
	private int						windowDecorX, windowDecorY;
	private Insets					insets;
	
	public Raster(SmlFr _base) {
		
		realscreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
//		System.out.println(realscreen.width+" x "+realscreen.height);
//
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println("RASTER: screensize: "+screen.width+" x "+screen.height);
		
		
		
		screen = new Dimension(realscreen.width, realscreen.height);
		System.out.println("RASTER: realscreen: "+screen.width+" x "+screen.height);

		raster = new Dimension(screen.width / 3, screen.height / 3);
		
		
		JFrame blah = new JFrame();
		blah.pack();
		
//		System.out.println("top: " + blah.getInsets().top);
//		System.out.println("bot: " + blah.getInsets().bottom);
//		System.out.println("lft: " + blah.getInsets().left);
//		System.out.println("rgt: " + blah.getInsets().right);

		windowDecorX = blah.getInsets().left + blah.getInsets().right;
		windowDecorY = blah.getInsets().top  + blah.getInsets().bottom;
		
        GraphicsConfiguration config = _base.getGraphicsConfiguration();
        GraphicsDevice sd = config.getDevice();
        
//        System.out.println(sd.getIDstring());
//        System.out.println("x: "+sd.getDefaultConfiguration().getBounds().x);
//        System.out.println("y: "+sd.getDefaultConfiguration().getBounds().y);
//        System.out.println("w: "+sd.getDefaultConfiguration().getBounds().width);
//        System.out.println("h: "+sd.getDefaultConfiguration().getBounds().height);
        insets = Toolkit.getDefaultToolkit().getScreenInsets(sd.getDefaultConfiguration());
//        System.out.println("task bar top:  "+insets.top);
//        System.out.println("task bar bottom:  "+insets.bottom);
//        System.out.println("task bar left:  "+insets.left);
//        System.out.println("task bar right:  "+insets.right);
        
        topOffset = insets.top;
		
	}
	
	public Point getPos(int x, int y) {
		return new Point( (insets.left + (raster.width * x)) , (insets.top + (raster.height * y)) );
	}

	public Dimension getSize(int xFact, int yFact) {
		return new Dimension( raster.width * xFact, raster.height * yFact);
	}
	
	public Dimension getSizeNoDecorations(int xFact, int yFact) {
		return new Dimension( (raster.width * xFact) - windowDecorX, raster.height * yFact - windowDecorY);
	}
	
	public int getWidth(int factor) {
		return raster.width * factor;
	}
	
	public int getHeight(int factor) {
		return raster.height * factor;
	}
	
	public int getWidthNoDecorations(int factor) {
		return (raster.width - windowDecorX) * factor;
	}
	
	public int getHeightNoDecorations(int factor) {
		return (raster.height - windowDecorY) * factor;
	}
	
	public Dimension getScreen() {
		return screen;
	}
}
