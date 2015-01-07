package SMUtils;

import java.awt.image.BufferedImage;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import sfrenderer.XTImage;

public class Skewmator extends PApplet {

	int baseX, baseY;
	PGraphics skewGraphics;
	
	public Skewmator( int _photoX, int _photoY) {
		baseX = _photoX;
		baseY = _photoY;
	}

	public PGraphics drawCropImage(Float[] _cropValues) {
		
		PGraphics cropImage = createGraphics(baseX, baseY);

		
		float fact = _cropValues[0] / baseX;
		PVector smallPicAnchor = new PVector(baseX * ((fact-1)/2) , baseY * ((fact-1)/2));
		
		PVector lo = new PVector( _cropValues[2], _cropValues[3] );
		PVector ro = new PVector( _cropValues[4], _cropValues[5] );
		PVector ru = new PVector( _cropValues[6], _cropValues[7] );
		PVector lu = new PVector( _cropValues[8], _cropValues[9] );
		
		lo.sub(smallPicAnchor);
		ro.sub(smallPicAnchor);
		ru.sub(smallPicAnchor);
		lu.sub(smallPicAnchor);
		
		cropImage.beginDraw();
		cropImage.noStroke();
		cropImage.fill(255);
		cropImage.beginShape();

		cropImage.vertex(lo.x, lo.y);
		cropImage.vertex(ro.x, ro.y);
		cropImage.vertex(ru.x, ru.y);
		cropImage.vertex(lu.x, lu.y);
		cropImage.vertex(lo.x, lo.y);

		cropImage.endShape();
		cropImage.endDraw();

		return cropImage;
	}
	
	public PImage skewToWall(PImage inImage, Float[] _vls, int canvasWidth, int canvasHeight) {
		return skewToWall( inImage, new PVector(_vls[2],_vls[3]), new PVector(_vls[4],_vls[5]), new PVector(_vls[6],_vls[7]), new PVector(_vls[8], _vls[9]), _vls[0].intValue(), _vls[1].intValue(), canvasWidth, canvasHeight );
	}
	
	public PImage skewToWall(PImage inImage, PVector lo, PVector ro, PVector ru, PVector lu, int bezugX, int bezugY, int canvasWidth, int canvasHeight) {
		
		float scale; 
		if( canvasWidth >0 ) {
			scale = (float)canvasWidth / (float)baseX;
//			System.out.println("scale calculated from width and baseX: "+scale);
		}
		else if ( canvasHeight > 0) {
			scale = (float)canvasHeight / (float)baseY;
//			System.out.println("scale calculated from height and baseY: "+scale);
		}
		else return null;
		
		return skewToWall(inImage, lo, ro, ru, lu, bezugX, bezugY, scale);
	}
	
	public PImage skewToWall(PImage inImage, PVector lo, PVector ro, PVector ru, PVector lu, int bezugX, int bezugY, float scale) {

		

		float fact = bezugX / baseX;
		PVector smallPicAnchor = new PVector(baseX * ((fact-1)/2) , baseY * ((fact-1)/2));

		
		// calculate the skew-box (max/min x and y values from corners)
		
		PVector[] box = bigBox(lo, ro, ru, lu);
		/**{smallestx, biggestx, smallesty, biggesty}**/

		float boxXsize =  box[1].x - box[0].x;
		float boxYsize =  box[3].y - box[2].y;
		
		// calculate the top-left anchor point of the skew-box
		// 1) relative to the big box specified by bezugX/Y
		// 2) relative to the zero-point (top left anchor) of the background image
		
		PVector boxAnchorBigPicture = new PVector( box[0].x, box[2].y );
		PVector boxAnchorSmallPicRelative = new PVector( boxAnchorBigPicture.x - smallPicAnchor.x,
														 boxAnchorBigPicture.y - smallPicAnchor.y  );
		
		// boxRelativeCoordinates
		// skew-coordinates from skew-box anchor to the skewImage-desired corners
		
		PVector boxRelativeLO = new PVector( lo.x-boxAnchorBigPicture.x, lo.y-boxAnchorBigPicture.y   );
		PVector boxRelativeRO = new PVector( ro.x-boxAnchorBigPicture.x, ro.y-boxAnchorBigPicture.y   );
		PVector boxRelativeRU = new PVector( ru.x-boxAnchorBigPicture.x, ru.y-boxAnchorBigPicture.y   );
		PVector boxRelativeLU = new PVector( lu.x-boxAnchorBigPicture.x, lu.y-boxAnchorBigPicture.y   );

		// finally skew the image
		// first, fill the skewbox with the image (distorting it)
		
		System.out.println("box: "+boxXsize+" X "+boxYsize);
		
		skewGraphics = createGraphics((int)boxXsize, (int)boxYsize);
		skewGraphics.beginDraw();
		skewGraphics.image(inImage,0,0,skewGraphics.width, skewGraphics.height);
		skewGraphics.endDraw();
		
		PImage skewedImage = skewImage(skewGraphics, boxRelativeLO, boxRelativeRO, boxRelativeRU, boxRelativeLU);

		//   now, scale the returnImage if you need to 
		/** (remember, it's all set to be 1200x800 fixed size till now!!) */
		//   draw the skewed image onto a returnimage of desired scale
			   ///////
		      ///////
		//float scale = 0.7f;
		    ///////
		   ///////
		
		//scale = 1;
//		System.out.println("SCALE: "+scale);
		PGraphics sizedReturnImage = createGraphics((int)(baseX*scale), (int)(baseY*scale));
		
		sizedReturnImage.beginDraw();

		sizedReturnImage.image(skewedImage, boxAnchorSmallPicRelative.x*scale, boxAnchorSmallPicRelative.y*scale, skewedImage.width*scale, skewedImage.height*scale );
		sizedReturnImage.endDraw();
		
		// the image is now ready to be drawn onto any surface that 
		// fits the scale value (1200*scale x 800*scale)

		// bitteschšn!
		
		return sizedReturnImage;
	}
	
	

	public PVector[] bigBox(PVector v1, PVector v2, PVector v3, PVector v4) {

		PVector[] ar = new PVector[] {v1,v2,v3,v4};

		// smallestX
		PVector smallestx = v1;
		for( int i = 1; i<ar.length; i++ ) {
			if( ar[i].x < smallestx.x) smallestx = ar[i];
		}
//		System.out.println("smallest x: "+ smallestx.x+" x "+smallestx.y);

		// biggestx
		PVector biggestx = v1;
		for( int i = 1; i<ar.length; i++ ) {
			if( ar[i].x > biggestx.x) biggestx = ar[i];
		}
//		System.out.println("biggest x: "+ biggestx.x+" x "+biggestx.y);

		// smallestY
		PVector smallesty = v1;
		for( int i = 1; i<ar.length; i++ ) {
			if( ar[i].y < smallesty.y) smallesty = ar[i];
		}
//		System.out.println("smallest y: "+ smallesty.x+" x "+smallesty.y);

		// biggesty
		PVector biggesty = v1;
		for( int i = 1; i<ar.length; i++ ) {
			if( ar[i].y > biggesty.y) biggesty = ar[i];
		}
//		System.out.println("biggest y: "+ biggesty.x+" x "+biggesty.y);

		return new PVector[] {smallestx, biggestx, smallesty, biggesty};
	}
	
	private PImage skewImage(PImage inputImage, PVector lo, PVector ro, PVector ru, PVector lu) {
		
		
		PImage wall;
		XTImage  img;
	
		img = new XTImage((BufferedImage)inputImage.getNative());

		
		// skew the image
		img.setCorners((int)lo.x , (int)lo.y, (int)ro.x, (int)ro.y, (int)ru.x, (int)ru.y, (int)lu.x, (int)lu.y);
		
//		System.out.println("sk: "+img.getWidth()+" x "+img.getHeight());


		// convert the skewed image back to PImage by manually drawing
		
		PGraphics pg = createGraphics(img.getWidth(), img.getHeight());
		pg.beginDraw();
		for(int x = 0; x < pg.width; x++) {
			for(int y = 0; y < pg.height; y++) {
				
				pg.set(x, y, img.getColor(x, y).getRGB() );
				
			}
		}
		pg.endDraw();
		wall = pg.get();
				
		// mask the image		
		//wall.mask(mask);
				
		return wall;
		
	}

}
