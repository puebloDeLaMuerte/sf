package smimport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;

import SMUtils.FrameStyle;
import SMUtils.Lang;

import smlfr.SM_FileManager;
import smlfr.SM_Wall;
import smlfr.SM_WallArrangementView;
import smlfr.SmlFr;

public class SM_Import extends PApplet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3888359190816520969L;
	
	private SmlFr		 			base;
	private JFileChooser			chooser;
	protected ProgressGui				gui;

	
	private SM_FileManager		 	fm;
	private SM_EXCELReader  		ex;
	private SM_JSONCreator 			cr;
	
	private final int fullSize = 600;
	private final int mediumSize = 350;
	private final int thumbSize = 50;

	public SM_Import (SM_FileManager _fm, SmlFr _base) {
		
		fm = _fm;
		ex = new SM_EXCELReader(_base);
		cr = new SM_JSONCreator(fm);
		base = _base;
		
		chooser = new JFileChooser(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
	}
	
	@Deprecated
	public void newProject() {
		
		JPanel chpane = new JPanel();
		int returnVal = chooser.showOpenDialog( chpane );
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("IMPORT: You chose to open this file: " +
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
			
			if( frameSize.length > 0 ) frameStyle = FrameStyle.STANDART;
			else frameStyle = FrameStyle.NONE;
			
			JSONObject aw = cr.makeNewArtworkFile(invNr, artist, title, size, frameSize, pptSize, frameStyle, false, null);
			
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
	
	public String[] batchImport(File _artLibSaveLocation, boolean _collection, boolean intoExistingProject) {

		
//		gui = initImportGui();
		
		gui = ProgressGui.create();
		
		
//		JFrame pan = new JFrame();
//		JTextArea txt = new JTextArea(Lang.importPleaseWait);
//		pan.setBackground(Color.LIGHT_GRAY);
//		pan.add(txt);
//		pan.setSize(250, 80);
//		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//		pan.setLocation(dim.width/2 - 175, dim.height/2-40);
//		pan.setVisible(true);
		
		gui.setTitle("initializing file...");

		
		File excelLocation = ex.loadExcelData(_collection);
				
		
		if(excelLocation == null ) {
			gui.frame.setVisible(false);
			gui = null;
			return null;
		}
		
		gui.setMax(ex.getExcelLength());
				
//		if( _collection ) {
//			
//			_artLibSaveLocation = new File(base.fm.getCollectionPath() + "/" + base.fm.getCollectionName() + "/imported");
//		}

		System.out.println("IMPORT: will import artworks to this location: "+_artLibSaveLocation.getAbsolutePath());
		System.out.println("IMPORT: we got our exel from here\n we'll get the images from here!\n"+excelLocation.getAbsolutePath());


		
		LinkedHashMap<String, JSONObject> importArtworks = new LinkedHashMap<String, JSONObject>();
		ArrayList<String> unimportedArtworks = new ArrayList<String>();
		ArrayList<String> sucessfulImports = new ArrayList<String>();
		
		gui.setTitle("importing data...");
		
		
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

				// Transform Data from Input Format to SimuF�hr Format
				
				LinkedHashMap<String, int[]> convertedMeasures = measurementInputFormatToInternalFormat(tmpSize, tmpFrameSize, tmpPptSize);
				
				size 			= convertedMeasures.get("size");
				frameSize 		= convertedMeasures.get("frame");
				pptSize 		= convertedMeasures.get("ppt");
				
				if( frameSize.length > 0 ) frameStyle = FrameStyle.STANDART;
				else frameStyle = FrameStyle.NONE;
				
				
				
				//////   USE IMAGE FILE NAME AS KEY FOR MAP!!!
				/////    (it's the only value not needed to store in the json anyways....
				////	 Michi Special!
				
				String colName = null;
				if( _collection ) colName = base.fm.getCollectionName();
				
				importArtworks.put(invNr, cr.makeNewArtworkFile(invNr, artist, title, size, frameSize, pptSize, frameStyle, _collection, colName));
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
			gui.increaseCurrent(1);
		}
		
		
		/////	Artwork Data Loaded	    -->  LOAD IMAGES						\\\\\
		////  	Now for the images:											     \\\\
		///	  	Michi Special - normally the FileName would be gotten from		  \\\
		//    	the Excel, and appear here as the KeySet();						   \\

		gui.setTitle("loading files...");
		gui.increaseMax(importArtworks.size());

		for( String a : importArtworks.keySet() ) {
			
			JSONObject aw = importArtworks.get(a);
			String iNr = aw.getString("invNr");
			
			try {

				
				if( checkAlreadyInProject(iNr) ) {
					throw new Exception(Lang.err_InvNrAlreadyExists);
				}
				
				System.out.println("IMPORT: trying to load this image file: " + excelLocation.getAbsolutePath()+"/"+iNr+".png");
				
				gui.setStatus(iNr);
				
				// Load the Image
				
				File imgfolder = new File(excelLocation.getAbsolutePath());
				
//				if( !_collection ) {
//					imgfolder = new File(excelLocation.getAbsolutePath());
//				} else {
//					imgfolder = new File(excelLocation.getAbsolutePath()+"/images");
//				}
				
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
			gui.increaseCurrent(1);
		}
		
		
		gui.setStatus(Lang.finishingImport);
		
		// Message: Success
		JPanel p = new JPanel();
		int sucessCount = sucessfulImports.size();
		javax.swing.JOptionPane.showMessageDialog(p, sucessCount+Lang.successfulImport, "Import", javax.swing.JOptionPane.INFORMATION_MESSAGE);
		
		// Messagr: Errors
		
		if( unimportedArtworks.size() > 0) {
			
			p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			
			JTextArea i1 = new JTextArea(Lang.couldntImport_1);
			i1.setBackground(p.getBackground());
		
			String uim = "";
			for( String s : unimportedArtworks ) {
				System.err.println( "couldn't import "+s );
				uim += "- "+s+"\n";
			}
			
			JTextArea i2 = new JTextArea(Lang.couldntImport_2);
			i2.setBackground(p.getBackground());
			
			JTextArea textArea = new JTextArea(uim);
			JScrollPane scrollPane = new JScrollPane(textArea);  
			textArea.setLineWrap(false);  
			scrollPane.setPreferredSize( new Dimension( 500, 300 ) );
			
			
			p.add(i1);
			p.add(scrollPane);
			p.add(i2);
			
			
			javax.swing.JOptionPane.showMessageDialog(null, p, Lang.warning, javax.swing.JOptionPane.WARNING_MESSAGE, base.getWarningIcon());
		}

		String[] returnArray = sucessfulImports.toArray(new String[sucessCount]);

		if( intoExistingProject ) {
			fm.importedArtworksIntoProject( returnArray );
		}
		
		gui.setTitle("finishing...");
		gui.setStatus("");
		gui.setVisible(false);
		gui.frame.setVisible(false);
		gui = null;
		
		return returnArray;
	}
	
	@Deprecated
	private ProgressGui initImportGui() {
		
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		

		ProgressGui gui = new ProgressGui();
		

		
		gui.frame = f;

		gui.resize(gui.getSize());
		gui.setPreferredSize(gui.getSize());
		gui.setMinimumSize(gui.getSize());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		gui.frame.setLocation(dim.width/2 - 175, dim.height/2-40);
		gui.frame.setResizable(false);
		gui.frame.add(gui);
		gui.init();
		gui.frame.pack();
		gui.frame.setVisible(true);
//		gui.frame.setLocation(0);
//		wallArr.frame.setTitle(Lang.wall+" "+wallArr.getWallName().substring(wallArr.getWallName().lastIndexOf('_')+1));
		
		return gui;
	}

	private LinkedHashMap<String, int[]> measurementInputFormatToInternalFormat( int[] _tmpSize, int[] _tmpFrameSize, int[] _tmpPptSize  ) {
		 

		
		int[] size			= new int[2];
		size[0] 		= _tmpSize[1];
		size[1] 		= _tmpSize[0];
		
				
		int[] frameSize;
		if( _tmpFrameSize != null) {
			frameSize = new int[2];
			frameSize[0]		= _tmpFrameSize[1];
			frameSize[1]		= _tmpFrameSize[0];
		} else {
			frameSize = new int[0];
		}
		
		int[] pptSize;
		if( _tmpPptSize != null ) {
			pptSize	= new int[2];
			pptSize[0]	= _tmpPptSize[1];
			pptSize[1] = _tmpPptSize[0];
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
				if( fullGfx.height >= fullGfx.width ) {
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
			if( fullGfx.height >= fullGfx.width ) {
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
			if( medGfx.height >= medGfx.width ) {
				fact = (float)thumbSize / (float)medGfx.height;
			}
			System.out.println("IMPORT: the fact is: "+fact);
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
			throw new Exception(Lang.err_onImageResize);
		}
	}

	public boolean checkAlreadyInProject(String invNr) {
		
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

	private PImage loadArtworkImage(File imageFolder, String imageName) throws Exception{
		
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
			
			try {
				img = loadImage(imageFolder.getAbsolutePath()+"/"+imageName+".jpg");
			} catch (Exception e) {
				
				try {
					img = loadImage(imageFolder.getAbsolutePath()+"/"+imageName+".JPG");
				}
				catch (Exception e2) {
					throw new Exception(Lang.err_loadingJpg);
				}

			}
		}
		else {
			throw new Exception(Lang.err_noImageFile + imageName + Lang.err_noImageFile_2);
		}
		
		
		
		return img;
	}
	
}
