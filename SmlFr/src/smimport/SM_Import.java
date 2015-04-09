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
	
	int mediumSize = 350;
	int thumbSize = 50;

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
	
	public String[] batchImport(File _artLibSaveLocation) {
		
		
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
				
				//--    H…HE x BREITE (museumLogik) -->  BREITE x H…HE (SimulFšhrLogik)
				int[]		tmpSize			= ex.getImageSize(i, "imageSize");
				int[]		size			= new int[2];
							size[0] 		= tmpSize[1];
							size[1] 		= tmpSize[0];
				//--
							
				int[]		tmpFrameSize		= ex.getImageSize(i, "frameSize");
				int[]		frameSize			= new int[2];
				if( tmpFrameSize != null) {
							frameSize[0]		= tmpFrameSize[1];
							frameSize[1]		= tmpFrameSize[0];
				}
				
				int[]		tmpPptSize			= ex.getImageSize(i, "passepartoutSize");
				int[]		pptSize				= new int[2];
				if( tmpPptSize != null ) {
							pptSize[0]	= tmpPptSize[1];
							pptSize[1]  = tmpPptSize[0];
				}
				
				FrameStyle	frameStyle;
				if( tmpFrameSize != null ) frameStyle = FrameStyle.STANDART;
				else frameStyle = FrameStyle.NONE;
				
				// passepartout to SimulFormat:
				
				int[] pptFormat = new int[0];
				if( tmpPptSize != null ) {
					
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
				if( tmpFrameSize != null ) {
					
					frameFormat = new int[4];
					
					int xsize = (frameSize[1] - size[1]) / 2;
					int ysize = (frameSize[0] - size[0]) / 2;
										
					frameFormat[0] = xsize;
					frameFormat[1] = xsize;
					frameFormat[2] = ysize;
					frameFormat[3] = ysize;	
					
					if( tmpPptSize != null ) {
						
						frameFormat[0] -= pptFormat[0];
						frameFormat[1] -= pptFormat[1];
						frameFormat[2] -= pptFormat[2];
						frameFormat[3] -= pptFormat[3];
					}
				}
				
				//////   USE IMAGE FILE NAME AS KEY FOR MAP!!!
				/////    (it's the only value not needed to store in the json anyways....
				////	 Michi Special!
				
				importArtworks.put(invNr, cr.makeNewArtworkFile(invNr, artist, title, size, frameFormat, pptFormat, frameStyle));
			}
			catch( Exception e ) {
				e.printStackTrace();
				String non = "";
				non += invNr ;
				non += " - ";
				non += title;
				unimportedArtworks.add(non);
			}	
		}
		
		/////	Artwork Data Loaded												\\\\\
		////  	Now for the images:											     \\\\
		///	  	Michi Special - normally the FileName would be gotten from		  \\\
		//    	the Excel, and appear here as the KeySet();						   \\


//		JPanel prevpnl = new JPanel();
		for( String a : importArtworks.keySet() ) {
			
			JSONObject aw = importArtworks.get(a);
			String iNr = aw.getString("invNr");
			
			try {

				// check if artwork already exists in project
				String[] presentArtworks = fm.getArtLibraryFromProject();
				
				
				for (int i = 0; i < presentArtworks.length; i++) {
					String checkAw = presentArtworks[i];
					if( checkAw.equalsIgnoreCase(iNr)) {
						
						String non = "";
						non += iNr;
						non += " - ";
						non += aw.getString("title");
						JPanel p = new JPanel();
						
						javax.swing.JOptionPane.showMessageDialog(p, Lang.importArtworkAlreadyExists + non, Lang.warning, javax.swing.JOptionPane.WARNING_MESSAGE);
						throw new EmptyStackException();
					}
				}
				
				System.out.println("trying to load this image file: " + excelLocation.getAbsolutePath()+"/"+iNr+".png");
				
				// Load the Image
				PImage fullGfx = loadImage(excelLocation.getAbsolutePath()+"/"+iNr+".png");
				if( fullGfx == null )   {
					throw new EmptyStackException();
				}
				
				// Display the Image
//				String msgs = aw.getString("artist");
//				msgs += "\n"+aw.getString("title");
//				msgs += "\n"+iNr;
//				ImageIcon prev = new ImageIcon((BufferedImage)fullGfx.getNative());
//				javax.swing.JOptionPane.showMessageDialog(prevpnl, msgs, "Import", javax.swing.JOptionPane.INFORMATION_MESSAGE, prev);

				
				// Resize to MEDIUM:
				float fact = 1;
				PImage medGfx = (PImage)fullGfx.clone(); 
				
				if( fullGfx.width > fullGfx.height ) {
					fact = (float)mediumSize / (float)fullGfx.width;
				} else
				if( fullGfx.height > fullGfx.width ) {
					fact = (float)mediumSize / (float)fullGfx.height;
				}
				medGfx.resize( (int)(fullGfx.width * fact), (int)(fullGfx.height * fact));
				
				// Display the Image
//				msgs = aw.getString("artist");
//				msgs += "\n"+aw.getString("title");
//				msgs += "\n"+iNr;
//				prev = new ImageIcon((BufferedImage)fullGfx.getNative());
//				javax.swing.JOptionPane.showMessageDialog(prevpnl, msgs, "Import", javax.swing.JOptionPane.INFORMATION_MESSAGE, prev);

				
				// Resize to MEDIUM:
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
//				msgs = aw.getString("artist");
//				msgs += "\n"+aw.getString("title");
//				msgs += "\n"+iNr;
//				prev = new ImageIcon((BufferedImage)fullGfx.getNative());
//				javax.swing.JOptionPane.showMessageDialog(prevpnl, msgs, "Import", javax.swing.JOptionPane.INFORMATION_MESSAGE, prev);

				
				// SAVE EVERYTHING
				
				
//				File savelocation = excelLocation;
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
		String uim = Lang.couldntImport_1;
		for( String s : unimportedArtworks ) {
			System.err.println( "couldn't import "+s );
			uim += "	- "+s+"\n";
		}
		uim += Lang.couldntImport_2;
//		javax.swing.JOptionPane.showConfirmDialog(p, uim);
		javax.swing.JOptionPane.showMessageDialog(p, uim, Lang.warning, javax.swing.JOptionPane.WARNING_MESSAGE);


		
		return sucessfulImports.toArray(new String[sucessCount]);
	}
	
	
}
