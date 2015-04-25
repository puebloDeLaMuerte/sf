package smimport;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedHashMap;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;

import SMUtils.FrameStyle;
import SMUtils.Lang;

import smlfr.SM_FileManager;
import smlfr.SmlFr;

public class SM_Import extends PApplet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3888359190816520969L;
//	private SmlFr		 			base;
	private JFileChooser			chooser;

	
	private SM_FileManager		 	fm;
	private SM_EXCELReader  		ex;
	private SM_JSONCreator 			cr;
	
	private final int fullSize = 600;
	private final int mediumSize = 350;
	private final int thumbSize = 50;

	public SM_Import (SM_FileManager _fm, SmlFr _base) {
//		base = _base;
		fm = _fm;
		ex = new SM_EXCELReader();
		cr = new SM_JSONCreator(fm);
		
		chooser = new JFileChooser(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
	}
	
	@Deprecated
	public void newProject() {
		
		JPanel chpane = new JPanel();
		int returnVal = chooser.showOpenDialog( chpane );
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: " +
					chooser.getSelectedFile().getName());
		} else return;

		

		
//		cr.makeNewProjectFile("neues Projekt", new String[] {"S1", "S2", "S3"}, chooser.getSelectedFile());
		
	}
	
	public void startImport(File _artLibSaveLocation) {
		
		SM_SingleImportDialog in = new SM_SingleImportDialog(this, _artLibSaveLocation);
		
	}
	
	public void singleImport(File _artLibSaveLocation, SM_SingleImportDialog _in) {
		
		try {
			// get basic data
			
			String invNr	= _in.getInvNr();
			String artist	= _in.getArtist();
			String title	= _in.getTitle();
			
			// get and convert Sizes (+FrameStyle)
			
			FrameStyle frameStyle = null;
			int[] tmpSize, size, tmpFrameSize, frameSize, tmpPptSize, pptSize;
							
			tmpSize 		= _in.getArtworkSize();
			tmpFrameSize 	= _in.getFrameSize();
			tmpPptSize 		= _in.getPptSize();
			
			LinkedHashMap<String, int[]> convertedMeasures = measurementInputFormatToInternalFormat(tmpSize, tmpFrameSize, tmpPptSize);
			size 			= convertedMeasures.get("size");
			frameSize 		= convertedMeasures.get("frame");
			pptSize 		= convertedMeasures.get("ppt");
			
			if( frameSize != null ) frameStyle = FrameStyle.STANDART;
			else frameStyle = FrameStyle.NONE;
			
			JSONObject aw = cr.makeNewArtworkFile(invNr, artist, title, size, frameSize, pptSize, frameStyle);
			
			// load Artwork Image
			
			PImage fullGfx = loadArtworkImage(_in.getImageFolder(), _in.getImageName() );   //  (Folder , Artwork Name without .extension);
			
			// resize image to standart size 
			
			PImage[] sizedImages = resizeToStandartSize(fullGfx);
			
					fullGfx 	= sizedImages[0];
			PImage 	medGfx 		= sizedImages[1];
			PImage 	thumbGfx 	= sizedImages[2];
			
			// save everything to disk
			
			File saveLocation = _artLibSaveLocation;
			
			saveJSONObject(aw, saveLocation.getAbsolutePath()+"/"+invNr+".sfa");
			
			File imageLocation = new File(saveLocation.getAbsoluteFile()+"/"+invNr);
			
			fullGfx.save(imageLocation.getAbsolutePath()+"/"+invNr+"_full.png");
			medGfx.save(imageLocation.getAbsolutePath()+"/"+invNr+"_med.png");
			thumbGfx.save(imageLocation.getAbsolutePath()+"/"+invNr+"_thumb.png");
			
			
			String[] importedArtworks = new String[1];
			importedArtworks[0] = _in.getInvNr();
			fm.importedArtworksIntoProject(importedArtworks);
		}
		catch(Exception e) {
		}
	}
	
	public String[] batchImport(File _artLibSaveLocation, boolean intoExistingProject) {
		
		
		JFrame pan = new JFrame();
		JTextArea txt = new JTextArea(Lang.importPleaseWait);
		pan.setBackground(Color.LIGHT_GRAY);
		pan.add(txt);
		pan.setSize(250, 80);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();		
		pan.setLocation(dim.width/2 - 175, dim.height/2-40);
		pan.setVisible(true);

		System.out.println("will import artworks to this location: "+_artLibSaveLocation.getAbsolutePath());
		
		File excelLocation = ex.loadExcelData();
		
		if(excelLocation == null ) {
			pan.setVisible(false);
			pan = null;
			return null;
		}
		
		System.out.println("we got our exel from here\n we'll get the images from here!\n"+excelLocation.getAbsolutePath());


		
		LinkedHashMap<String, JSONObject> importArtworks = new LinkedHashMap<String, JSONObject>();
		ArrayList<String> unimportedArtworks = new ArrayList<String>();
		ArrayList<String> sucessfulImports = new ArrayList<String>();
		
		
		int howmany = ex.getExcelLength();
		for( int i=0; i< howmany; i++) {
			
			String 		invNr   = "invNr";
			String 		artist  = "artist";
			String 		title   = "title";
			
			try {
				
				// Get Data from EXCEL-reader

				invNr						= ex.getInvNr(i);
				artist						= ex.getArtist(i);
				title						= ex.getTitle(i);
				
				FrameStyle frameStyle = null;
				int[] tmpSize, size, tmpFrameSize, frameSize, tmpPptSize, pptSize;
								
				tmpSize 		= ex.getSizeValues(i, "imageSize");
				tmpFrameSize 	= ex.getSizeValues(i, "frameSize");
				tmpPptSize 		= ex.getSizeValues(i, "passepartoutSize");
				
				if( tmpSize == null ) {
					throw new Exception(Lang.err_noImageSize);

				}

				// Transform Data from Input Format to SimuFšhr Format
				
				LinkedHashMap<String, int[]> convertedMeasures = measurementInputFormatToInternalFormat(tmpSize, tmpFrameSize, tmpPptSize);
				
				size 			= convertedMeasures.get("size");
				frameSize 		= convertedMeasures.get("frame");
				pptSize 		= convertedMeasures.get("ppt");
				
				if( frameSize != null ) frameStyle = FrameStyle.STANDART;
				else frameStyle = FrameStyle.NONE;
				
				
				
				//////   USE IMAGE FILE NAME AS KEY FOR MAP!!!
				/////    (it's the only value not needed to store in the json anyways....
				////	 Michi Special!
				
				importArtworks.put(invNr, cr.makeNewArtworkFile(invNr, artist, title, size, frameSize, pptSize, frameStyle));
			}
			catch( Exception e ) {
				e.printStackTrace();
				String non = "";
				non += invNr ;
				non += " - ";
				non += title;
				non += " - ";
				non += e.getMessage();
				unimportedArtworks.add(non);
			}	
		}
		
		
		/////	Artwork Data Loaded	    -->  LOAD IMAGES						\\\\\
		////  	Now for the images:											     \\\\
		///	  	Michi Special - normally the FileName would be gotten from		  \\\
		//    	the Excel, and appear here as the KeySet();						   \\


		for( String a : importArtworks.keySet() ) {
			
			JSONObject aw = importArtworks.get(a);
			String iNr = aw.getString("invNr");
			
			try {

				
				if( checkAlreadyInProject(iNr) ) {
					throw new Exception(Lang.err_InvNrAlreadyExists);
				}
				
				System.out.println("trying to load this image file: " + excelLocation.getAbsolutePath()+"/"+iNr+".png");
				
				// Load the Image
				
				File imgfolder = new File(excelLocation.getAbsolutePath());
				
				PImage fullGfx = loadArtworkImage(imgfolder, iNr);   //  (Folder , Artwork Name without .extension);
				
				
				// resize to standart size 
				
				PImage[] sizedImages = resizeToStandartSize(fullGfx);
				
						fullGfx 	= sizedImages[0];
				PImage 	medGfx 		= sizedImages[1];
				PImage 	thumbGfx 	= sizedImages[2];
				
				
				// SAVE EVERYTHING
				
				File saveLocation = _artLibSaveLocation;
				
				saveJSONObject(aw, saveLocation.getAbsolutePath()+"/"+iNr+".sfa");
				
				File imageLocation = new File(saveLocation.getAbsoluteFile()+"/"+iNr);
				
				fullGfx.save(imageLocation.getAbsolutePath()+"/"+iNr+"_full.png");
				medGfx.save(imageLocation.getAbsolutePath()+"/"+iNr+"_med.png");
				thumbGfx.save(imageLocation.getAbsolutePath()+"/"+iNr+"_thumb.png");
				
				
				sucessfulImports.add(iNr);
			}
			catch( Exception e ) {
				e.printStackTrace();
				String non = "";
				non += iNr;
				non += " - ";
				non += aw.getString("title");
				non += " - ";
				non += e.getMessage();
				unimportedArtworks.add(non);
				
			}
			
		}
		
		pan.setVisible(false);
		pan = null;
		
		
		// Message: Success
		JPanel p = new JPanel();
		int sucessCount = sucessfulImports.size();
		javax.swing.JOptionPane.showMessageDialog(p, sucessCount+Lang.successfulImport, "Import", javax.swing.JOptionPane.INFORMATION_MESSAGE);
		
		// Messagr: Errors
		
		if( unimportedArtworks.size() > 0) {
			
			String uim = Lang.couldntImport_1;
			for( String s : unimportedArtworks ) {
				System.err.println( "couldn't import "+s );
				uim += "	- "+s+"\n";
			}
			uim += Lang.couldntImport_2;

			javax.swing.JOptionPane.showMessageDialog(p, uim, Lang.warning, javax.swing.JOptionPane.WARNING_MESSAGE);
		}

		String[] returnArray = sucessfulImports.toArray(new String[sucessCount]);

		if( intoExistingProject) {	
			fm.importedArtworksIntoProject( returnArray );
		}
		return returnArray;
	}
	
	private LinkedHashMap<String, int[]> measurementInputFormatToInternalFormat( int[] _tmpSize, int[] _tmpFrameSize, int[] _tmpPptSize  ) {
		 

		
		int[] size			= new int[2];
		size[0] 		= _tmpSize[1];
		size[1] 		= _tmpSize[0];
		
				
		int[] frameSize = new int[2];
		if( _tmpFrameSize != null) {
			frameSize[0]		= _tmpFrameSize[1];
			frameSize[1]		= _tmpFrameSize[0];
		}
		
		int[] pptSize	= new int[2];
		if( _tmpPptSize != null ) {
			pptSize[0]	= _tmpPptSize[1];
			pptSize[1] = _tmpPptSize[0];
		}
		
		
		
		// passepartout to SimulFormat:
		
		int[] pptFormat= new int[0];
		if( _tmpPptSize != null ) {
			
			pptFormat = new int[4];
			
			int xsize = (pptSize[1] - size[1]) / 2;
			int ysize = (pptSize[0] - size[0]) / 2;
			
			pptFormat[0] = xsize-(xsize/8);
			pptFormat[1] = xsize+(xsize/8);
			pptFormat[2] = ysize;
			pptFormat[3] = ysize;	
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
				
				frameFormat[0] -= pptFormat[0];
				frameFormat[1] -= pptFormat[1];
				frameFormat[3] -= pptFormat[3];
				frameFormat[2] -= pptFormat[2];
			}
		}
		
		LinkedHashMap<String, int[]> results = new LinkedHashMap<String, int[]>(3);
		
		results.put("size", size);
		results.put("frame", frameFormat);
		results.put("ppt", pptFormat);
		
		return results;
	}
	
	private PImage[] resizeToStandartSize(PImage fullGfx) throws Exception{
		
		try {
			float fact = 1;
			if( !(fullGfx.width == 600 || fullGfx.height == 600 ) ) {
				
				if( fullGfx.width > fullGfx.height ) {
					fact = (float)fullSize / (float)fullGfx.width;
				} else
				if( fullGfx.height > fullGfx.width ) {
					fact = (float)fullSize / (float)fullGfx.height;
				}
				
				fullGfx.resize( (int)(fullGfx.width * fact), (int)(fullGfx.height * fact));
								
			}				
			
	
			// Display the Image
	//		String msgs = aw.getString("artist");
	//		msgs += "\n"+aw.getString("title");
	//		msgs += "\n"+iNr;
	//		ImageIcon prev = new ImageIcon((BufferedImage)fullGfx.getNative());
	//		javax.swing.JOptionPane.showMessageDialog(prevpnl, msgs, "Import", javax.swing.JOptionPane.INFORMATION_MESSAGE, prev);
	
			
			// Resize to MEDIUM:
			fact = 1;
			PImage medGfx = (PImage)fullGfx.clone(); 
			
			if( fullGfx.width > fullGfx.height ) {
				fact = (float)mediumSize / (float)fullGfx.width;
			} else
			if( fullGfx.height > fullGfx.width ) {
				fact = (float)mediumSize / (float)fullGfx.height;
			}
			medGfx.resize( (int)(fullGfx.width * fact), (int)(fullGfx.height * fact));
			
			// Display the Image
	//		msgs = aw.getString("artist");
	//		msgs += "\n"+aw.getString("title");
	//		msgs += "\n"+iNr;
	//		prev = new ImageIcon((BufferedImage)fullGfx.getNative());
	//		javax.swing.JOptionPane.showMessageDialog(prevpnl, msgs, "Import", javax.swing.JOptionPane.INFORMATION_MESSAGE, prev);
	
			
			// Resize to THUMB:
			fact = 1;
			PImage thumbGfx = (PImage)medGfx.clone();
	
			if( medGfx.width > medGfx.height ) {
				fact = (float)thumbSize / (float)medGfx.width;
			} else
			if( medGfx.height > medGfx.width ) {
				fact = (float)thumbSize / (float)medGfx.height;
			}
			System.out.println("\n\n\n\nthe fact is: "+fact);
			thumbGfx.resize( (int)(medGfx.width * fact), (int)(medGfx.height * fact));
			
			// Display the Image
	//		msgs = aw.getString("artist");
	//		msgs += "\n"+aw.getString("title");
	//		msgs += "\n"+iNr;
	//		prev = new ImageIcon((BufferedImage)fullGfx.getNative());
	//		javax.swing.JOptionPane.showMessageDialog(prevpnl, msgs, "Import", javax.swing.JOptionPane.INFORMATION_MESSAGE, prev);
			
			PImage[] results = new PImage[3];
			
			results[0] = fullGfx;
			results[1] = medGfx;
			results[2] = thumbGfx;
			
			return results;
		}
		catch(Exception e) {
			throw new Exception("something went wrong while resizing the image");
		}
	}

	private boolean checkAlreadyInProject(String invNr) {
		
		String[] presentArtworks = fm.getArtLibraryFromProject();
		boolean exists = false;

		for (int i = 0; i < presentArtworks.length; i++) {
			String checkAw = presentArtworks[i];
			if( checkAw.equalsIgnoreCase(invNr)) {

				exists = true;
			}
		}
		return exists;
	}

	private PImage loadArtworkImage(File imageFolder, String imageName) {
		
		PImage img = null;
		
		boolean testPNG = false;
		boolean testJPG = false;
				
		File test1 = new File(imageFolder.getAbsolutePath()+"/"+imageName+".png");
		File test2 = new File(imageFolder.getAbsolutePath()+"/"+imageName+".jpg");
		if( test1.exists()) testPNG = true;
		if( test2.exists()) testJPG = true;
			
		if( testPNG ) {
			img = loadImage(imageFolder.getAbsolutePath()+"/"+imageName+".png");
		}
		else if( testJPG ) {
			img = loadImage(imageFolder.getAbsolutePath()+"/"+imageName+".jpg");
		}
		
		return img;
	}
	
}
