package smimport;

import java.awt.Dimension;

import processing.core.PApplet;
import processing.core.PImage;

public class ImportGui extends PApplet {

	private String		status, title;
	private int			current, max;
	private PImage		statusImage;
	private boolean		setupRun = false;
	
	public void setup() {
		size( 200,20 );
		current = 0;
		max = 1;
		status = "pending";
		setupRun = true;
	}
	
	public Dimension getSize() {
		return new Dimension(200,20);
	}
	
	public boolean isSetupRun() {
		return setupRun;
	}
	
	public void draw() {
		this.frame.setTitle(title);
		background(150);
		fill( 20,20,200 );
		noStroke();
		rect( 0,0, map(current, 0, max, 0, width) , height );
		fill(255);
		text(status, (width/2)-(textWidth(status)/2)  ,15);
	}
	
	public void setTitle(String title) {
		
		this.title = title;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public void increaseMax( int increment ) {
		this.max += increment;
	}
	
	public void increaseCurrent( int increment ) {
		current += increment;
	}
}
