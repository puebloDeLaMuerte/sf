package SMUtils;

import java.util.LinkedHashMap;

public class SM_MeasureFormatter {
	/**
	 * <p>
	 * Umrechnen der Daten von User-Input Logik in SimuFöhr Format.
	 * <p>
	 * <b> HÖHExBREITE  -->  WIDTH x HEIGHT</b>
	 *
	 * If no Frame or Passepartout is present, the respective input params
	 * must be set to null. The respective output params will be an empty
	 * int[]. Because this is the way the saved JSON files return 'no Frame'.
	 * 
	 * @param _tmpSize 			Values in mm: [0] = Height [1] = Width
	 * @param _tmpFrameSize  	Values in mm: [0] = Height [1] = Width
	 * @param _tmpPptSize 		Values in mm: [0] = Height [1] = Width
	 * @return a LinkedHashmap<String, Integer[]> with keys being: "size", "frame", "ppt"
	 */
	public static LinkedHashMap<String, int[]> measurementInputFormatToInternalFormat( int[] _tmpSize, int[] _tmpFrameSize, int[] _tmpPptSize  ) {
		 

		// scale up numbers by 10 to get rid of rounding errors
		
		int[] size			= new int[2];
		size[0] 		= _tmpSize[1] *10;
		size[1] 		= _tmpSize[0] *10;
		
				
		int[] frameSize;
		if( _tmpFrameSize != null) {
			frameSize = new int[2];
			frameSize[0]		= _tmpFrameSize[1] *10;
			frameSize[1]		= _tmpFrameSize[0] *10;
		} else {
			frameSize = new int[0];
		}
		
		int[] pptSize;
		if( _tmpPptSize != null ) {
			pptSize	= new int[2];
			pptSize[0] = _tmpPptSize[1] *10;
			pptSize[1] = _tmpPptSize[0] *10;
		} else {
			pptSize	= new int[0];
		}
		
		
		
		// passepartout to SimulFormat:
		
		int[] pptFormat= new int[0];
		if( _tmpPptSize != null ) {
			
			pptFormat = new int[4];
						
			int ysize = (pptSize[1] - size[1]) / 2;
			int xsize = (pptSize[0] - size[0]) / 2;
			
			pptFormat[0] = ysize-(ysize/8);
			pptFormat[1] = ysize+(ysize/8);
			pptFormat[2] = xsize;
			pptFormat[3] = xsize;
		}
		
		// frameSize to SimulFormat:
		
		int[] frameFormat = new int[0];
		if( _tmpFrameSize != null ) {
			
			frameFormat = new int[4];
						
			int xsize = (frameSize[1] - size[1]) / 2;
			int ysize = (frameSize[0] - size[0]) / 2;
								
			frameFormat[0] = xsize;
			frameFormat[1] = xsize;
			frameFormat[2] = ysize;
			frameFormat[3] = ysize;
			
			if( _tmpPptSize != null ) {
				
				// auskommentiert weil falsch:
				// die passepartout-Format Werte sind OBEN UNTEN verschoben, das überträgt sich hier auf den Rahmen!
				
//				frameFormat[0] -= pptFormat[0];
//				frameFormat[1] -= pptFormat[1];
//				frameFormat[3] -= pptFormat[3];
//				frameFormat[2] -= pptFormat[2];
				
				// Alternativ: ( pptSize sind die Außenmaße des Ppt. )

				
				frameFormat[0] -= (pptSize[1] - size[1]) / 2;
				frameFormat[1] -= (pptSize[1] - size[1]) / 2;
				frameFormat[3] -= (pptSize[0] - size[0]) / 2;
				frameFormat[2] -= (pptSize[0] - size[0]) / 2;
				
			}
		}
		
		
		// divide by ten and round/ceil alternating to keep the sum as the user has input it
		
		for (int i = 0; i < size.length; i++) {
			int j = size[i];
			size[i] = j/10;
		}
		
		for (int i = 0; i < frameFormat.length; i++) {
			int j = frameFormat[i];
			
			double jd;
			
			if(i%2==0)	jd = Math.floor( (float)j / 10f );
			else		jd = Math.ceil( (float)j / 10f );
						
			frameFormat[i] = (int)jd;
		}
		
		
		for (int i = 0; i < pptFormat.length; i++) {
			int j = pptFormat[i];
			
			double jd;
			
			if(i%2==0)	jd = Math.floor( (float)j / 10f );
			else		jd = Math.ceil( (float)j / 10f );
			
			pptFormat[i] = (int)jd;
		}
		
		
		LinkedHashMap<String, int[]> results = new LinkedHashMap<String, int[]>(3);
		
		results.put("size", size);
		results.put("frame", frameFormat);
		results.put("ppt", pptFormat);
		
		return results;
	}
}
