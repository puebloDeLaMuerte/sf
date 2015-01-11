package smlfr;


import processing.core.PApplet;
import processing.core.PFont;
import processing.pdf.*;

public class SM_Exporter extends PApplet {
	
	SM_ExportWall[] 	walls;
	String 				saveLoc;
	String 				roomRealName;
	
	int maxWallX, maxWallY;
	
	
	public SM_Exporter(SM_ExportWall[] walls, String saveLoc, String _roomRealName) {
		super();
		this.walls = walls;
		this.saveLoc = saveLoc;
		this.roomRealName = _roomRealName;
		
		maxWallX = 1;
		maxWallY = 1;
		for(SM_ExportWall w : walls) {
			if(w.getWidth()  > maxWallX) maxWallX = w.getWidth();
			if(w.getHeight() > maxWallY) maxWallY = w.getHeight();
		}
	}
	
	public void setup() {
		
		System.out.println("EXPORTER: starting export...");
		
		size(maxWallX, maxWallY);
		
		PGraphicsPDF pdf = (PGraphicsPDF)createGraphics(maxWallX, maxWallY, PDF, saveLoc);
		float f = maxWallX / 100 * 0.7f;
		PFont font = createDefaultFont(f);
		beginRecord(pdf);
		textFont(font);
		noFill();
		
		for( SM_ExportWall thiswall : walls ) {
			
			size(thiswall.getWidth(), thiswall.getHeight());
			
			fill(0);
			text("Dies ist ein test.",f*2, 2*f);
			text(roomRealName,f*2, 3*f);
			text(thiswall.getName(),f*2,4*f);
			text("Wandma§: "+thiswall.getWidth()+" x "+thiswall.getHeight()+" in mm", f*2,5*f);
			noFill();
			rect(f,(f/2),20*f,6*f-(f/2));
			
			// draw room size
			rect(0,0,thiswall.getWidth(), thiswall.getHeight());
			
			// draw artworks
			for(SM_ExportArtwork thisAw : thiswall.getArtworks()) {
				rect(thisAw.getWallPosX(), thisAw.getWallPosY(), thisAw.getWidth(), thisAw.getHeight());
			}
			
			
			// Draw Distances
			for(SM_ExportArtwork aw : thiswall.getArtworks()) {
				
				aw.calculateNearestNeighbours(thiswall.getArtworks());
				aw.calculateDistanceMeasureDrawPos();
				
				if( aw.hasNearestY() ) {
					int[] c = aw.getDistDrawPosY();
					pushStyle();
//					strokeWeight(60);
//					stroke(0, 100);
					line(c[0], c[1], c[2], c[3]);
					
					text( aw.getDistanceToNearestY()+" mm", c[0], c[1] );
					
					popStyle();
				}
				
				if( aw.hasNearestX() ) {
					int[] c = aw.getDistDrawPosX();
					pushStyle();
//					strokeWeight(60);
//					stroke(0, 100);
					line(c[0], c[1], c[2], c[3]);
					
					text( aw.getDistanceToNearestX()+" mm", c[0], c[1] );
					
					popStyle();
				}
				
			}
			
			
			
			pdf.nextPage();
			
		}
		
		
		
		
		endRecord();
		System.out.println("EXPORTER: end export...");
		
	}
	

	
	
	
	
	
	
}
