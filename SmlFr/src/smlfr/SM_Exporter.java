package smlfr;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import SMUtils.Lang;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PShape;
import processing.pdf.*;

public class SM_Exporter extends PApplet {
	
	SM_ExportWall[] 	walls;
	String 				saveLoc;
	String 				roomRealName;
	String 				projectName;
	PShape				grundriss;
	
	int maxWallX, maxWallY;
	
	public boolean isExportDone = false;
	
	public SM_Exporter(SM_ExportWall[] walls, String saveLoc, String _roomRealName, String _projectName, PShape _grundriss) {
		super();
		this.walls = walls;
		this.saveLoc = saveLoc;
		this.roomRealName = _roomRealName;
		this.projectName = _projectName;
		this.grundriss = _grundriss;
		
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
		
		
		// grundriss
//		drawMisc(null, f);
//		shape(grundriss, 10*f, 10*f, 560*2, 350*2);
		
		boolean firsttime = true;
		for( SM_ExportWall thiswall : walls ) {
			
			if( !firsttime ) pdf.nextPage();
			firsttime = false;
//			size(thiswall.getWidth(), thiswall.getHeight());
			
			background(220);

			// draw room size
			pushStyle();
			strokeWeight(f/2);
			stroke(200);
			fill(255);
			rect(f/4,f/4,thiswall.getWidth()-(f/2), thiswall.getHeight()-(f/2));
			popStyle();
			
			
			// draw miscelaneous
			
			drawMisc(thiswall, f);
			
			
			// draw artworks
			for(SM_ExportArtwork thisAw : thiswall.getArtworks()) {

				// the artwork
				rect(thisAw.getWallPosX(), thisAw.getWallPosY(), thisAw.getWidth(), thisAw.getHeight());
				
				// if necessary scale font for artwork name
				String awName = thisAw.getName();
				pushStyle();
				float x = 1f;
				while( thisAw.getWidth() <= textWidth(awName) ) {
					x *= 0.9f;
					textSize(f*x);
				}
				text(awName,
							thisAw.getWallPosX()+(thisAw.getWidth() /2) - (textWidth(awName) /2) ,
							thisAw.getWallPosY()+(thisAw.getHeight() /2) +(f/2)
					);
				popStyle();
			}
			
			
			// Draw Distances
			for(SM_ExportArtwork aw : thiswall.getArtworks()) {
				
				aw.calculateNearestNeighbours(thiswall.getArtworks());
				aw.calculateDistanceMeasureDrawPos();
				
				if( aw.hasNearestY() ) {
					int[] c = aw.getDistDrawPosY();
					pushStyle();
					stroke(180);
					line(c[0], c[1], c[2], c[3]);
					
					text( aw.getDistanceToNearestY()+" mm", c[0]+(f/2), c[1]-(f/2) );
					
					popStyle();
				}
				
				if( aw.hasNearestX() ) {
					int[] c = aw.getDistDrawPosX();
					pushStyle();

					stroke(180);
					line(c[0], c[1], c[2], c[3]);
					
					text( aw.getDistanceToNearestX()+" mm", c[0]+(f/2), c[1]-(f/2) );
					
					popStyle();
				}
				else {
					
					pushStyle();

					stroke(180);
					line( 0 ,aw.getWallPosY()+(aw.getHeight()/2), aw.getWallPosX(), aw.getWallPosY()+(aw.getHeight()/2));
					
					text( aw.getWallPosX()+" mm", (aw.getWallPosX()/2)+(f/2), aw.getWallPosY()+(aw.getHeight()/2)-(f/2) );
					
					popStyle();
					
					
				}
				
				if( aw.isRightmost() ) {
					
					int[] c = aw.getDistToRightDrawPos();
					
					pushStyle();

					stroke(180);
					line(c[0], c[1], c[2], c[3]);
					
					text( aw.getDistanceToRight()+" mm", c[0]+(f/2)+(aw.getDistanceToRight()/2), c[1]-(f/2) );
					
					popStyle();
					
				}

				if( aw.isLowest() ) {

					int[] c = aw.getDistToBottomDrawPos();

					pushStyle();

					stroke(180);
					line(c[0], c[1], c[2], c[3]);

					text( aw.getDistanceToBottom()+" mm", c[0]+(f/2), c[1]-(f/2)+(aw.getDistanceToBottom()/2) );

					popStyle();

				}
			}
			
			
			
			pdf.nextPage();
			
			/////	 \\\\\
			// THE LIST \\
			/////    \\\\\
			
			drawMisc(thiswall, f);
			
			float tab 	= 10*f;
			float line	= 15*f;
			
			text(Lang.exportListTitle, tab, line-(3*f));
			
			for( SM_ExportArtwork aw : thiswall.getArtworks() ) {
				
				text(aw.getName(), tab, line);
				text(aw.getWallPosX(), tab*2, line);
				text("x", (tab*2)+(4*f), line);
				text(aw.getWallPosY(), (tab*2)+(6*f), line);
				
				line += (f*1.2);
				
				
			}
			
		}
		
		
		
		
		endRecord();
		System.out.println("EXPORTER: end export...");
		isExportDone = true;
	}
	

	private void drawMisc( SM_ExportWall thiswall, float f) {
		
		fill(0);
		text(Lang.measureSheetTitle ,f*2, 3*f);
		text(projectName, f*12, 3*f);
		
		text(Lang.exportRoomName,f*2, 4*f);
		text(roomRealName, f*12, 4*f);
		
		if( thiswall != null ) {
			
			text(Lang.exportWallName,f*2,5*f);
			text(thiswall.getName(), f*12, 5*f);
			
			text(Lang.exportWallMeasures, f*2,6*f);
			text(thiswall.getWidth()+" x "+thiswall.getHeight(), f*12, 6*f);
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		
		text(Lang.exportDate, f*2, 7*f);
		text(dateFormat.format(date), f*12, 7*f);
		noFill();
		rect(f,(f*1.5f),25*f,7*f-(f/2));
	}
	
	
	
	
	
	
}
