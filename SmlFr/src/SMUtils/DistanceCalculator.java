package SMUtils;

import smlfr.SM_Artwork;
import smlfr.SM_ExportArtwork;
import smlfr.SM_ExportWall;
import smlfr.SM_Wall;

public class DistanceCalculator /*extends Thread*/ {

	private boolean				isReady;
	
	private int 				wallWidth, wallHeight;
	private SM_ExportArtwork[]	exArtworks;
//	private SM_ExportArtwork	querryArtwork;
	private SM_Artwork[]		aws;
	private SM_ExportWall		exWall;
	private SM_Wall				wall;
	
	private boolean				reInvertHeightCoordinates;
	
	public DistanceCalculator(boolean reInvertHeightCoordinates) {
		super();
		this.reInvertHeightCoordinates = reInvertHeightCoordinates;
		isReady = false;
	}
	
	public void prepare( SM_ExportWall wall, SM_ExportArtwork[] artworks, int wallWidth, int wallHeight) {
		
		isReady= false;
	
		this.wallWidth = wallWidth;
		this.wallHeight = wallHeight;
		this.exWall = wall;
		exArtworks = artworks;
	}
	
	public void calculate() {
		
		
				
		for( SM_ExportArtwork e : exArtworks ) {
		
			e.setWall(exWall);
			if( reInvertHeightCoordinates) e.reverseHeightCoordinates(wallHeight);
			e.calculateNearestNeighbours(exWall.getArtworks());
			e.calculateDistanceMeasureDrawPos();
		}
		
		
		isReady = true;
		cleanup();
	}

//	public void prepare( SM_Wall wall, SM_Artwork[] artworks, int wallWidth, int wallHeight) {
//		isReady= false;
//		
//		this.wallWidth = wallWidth;
//		this.wallHeight = wallHeight;
//		this.wall = wall;
//		aws = artworks;
//	}
//
//	public void run() {
//		
//		exArtworks = new SM_ExportArtwork[aws.length];
//		
//		int i = 0;
//		for( SM_Artwork a : aws) {			
//			exArtworks[i] = new SM_ExportArtwork(a.getName(), a.getTotalWallPos(), a.getTotalWidth(), a.getTotalHeight());
//			i++;
//		}
//		
//		exWall = new SM_ExportWall(wall.getWallName(), exArtworks, wallWidth, wallHeight);
//				
//		for( SM_ExportArtwork e : exArtworks ) {
//		
//			e.setWall(exWall);
////			e.reverseHeightCoordinates(wallWidth);
//			e.calculateNearestNeighbours(exArtworks);
//			e.calculateDistanceMeasureDrawPos();
//		}
//		
//		System.err.println("distances calculated");
//		
//		isReady = true;
//		cleanup();
//	}
	
	private void cleanup() {
		aws = null;
		wall = null;
	}
	
	public boolean isReady() {
		return isReady;
	}
	
	public SM_ExportArtwork[] getAllArtworks() {
		
		return exArtworks;
	}
	
	public SM_ExportArtwork getQuerryArtwork(String awName) {
		
		if (isReady) {
			for (SM_ExportArtwork a : exArtworks) {
				if (a.getName().equals(awName)) {
					return a;
				}
			}
		}
		return null;
	}
	
//	/**
//	 * @param type  1 for 
//	 * 
//	 * @return int[] 
//	 */
//	public int[] getData( ) {
//		
//	}

}
