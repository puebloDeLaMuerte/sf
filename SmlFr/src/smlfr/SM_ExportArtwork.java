package smlfr;

public class SM_ExportArtwork {
	
	
	// upon init
	private int 				myWallPosX, myWallPosY;
	private int 				myWidth, myHeight;
	private String 				myName;
	private SM_ExportWall 		myWall;
	
	// calculated
	private SM_ExportArtwork 	myNearestNeighbourX;
	private int 				distanceToNeighbourX;
	private SM_ExportArtwork 	myNearestNeighbourY;
	private int 				distanceToNeighbourY;
	
	private boolean				isRightmost;
	private int					distanceToRight;
	private boolean				isLowest;
	private int					distanceToBottom;
	
	private int[] 				myXDistDrawPos;
	private int[]				myYDistDrawPos;
	private int[]				distRightDrawPos;
	private int[]				distBottomDrawPos;
	
//	public enum					dataType { 	WALLPOS_X,		WALLPOS_Y,
//											WIDTH, 			HEIGHT,
//											IS_RIGHTMOST, 	IS_LEFTMOST, 
//											HAS_NEAREST_X, 	HAS_NEAREST_Y,
//											DIST_TO_X,		DIST_TO_Y,
//											DIST_TO_BOTTOM,	DIST_TO_RIGHT,
//											};

	public SM_ExportArtwork( String _myName, int[] _myWallPos, int _myWidth, int _myHeight) {
		super();
		this.myWidth = _myWidth;
		this.myHeight = _myHeight;
		this.myWallPosX = _myWallPos[0];
		this.myWallPosY = _myWallPos[1];
		this.myName = _myName;
	}
	
	public void setWall(SM_ExportWall _myWall) {
		this.myWall = _myWall;
	}
	
	public void reverseHeightCoordinates( int wallHeight) {
		myWallPosY = wallHeight - myWallPosY;
	}
	
	public void calculateNearestNeighbours(SM_ExportArtwork[] artworks) {
	
		// Ycheck (each one look above)
		
		int distance = myWall.getHeight();
		SM_ExportArtwork nearest = null;
		isLowest = true;
		
		for( SM_ExportArtwork it : artworks ) {
			
			if( it != this ) {
	
				// Sind wir Ÿber oder untereinander?
				if( it.getWallPosX()+it.getWidth() > myWallPosX && !(it.getWallPosX() > myWallPosX+myWidth)  ) {
	
					// nŠher als alle anderen?
					int thisDistance = myWallPosY - it.getWallPosY() - it.getHeight();
	
					if( thisDistance >= 0 && thisDistance < distance ) {
						distance = thisDistance;
						nearest = it;
					}
					else if( thisDistance < 0 ) {
						isLowest = false;
					}
					
				}
			}
		}
		
		if( nearest != null) {
			myNearestNeighbourY = nearest;
			distanceToNeighbourY = distance;
		}
		
		
		// Xcheck (each look to the left)
		
		distance = myWall.getWidth();
		nearest = null;
		isRightmost = true;
		
		for( SM_ExportArtwork it : artworks ) {
	
			if ( it != this ) {
				// Sind wir nebeneinander?
				if (it.getWallPosY() + it.getHeight() > myWallPosY && !(it.getWallPosY() > myWallPosY + myHeight)) {
	
					// nŠher als alle anderen?
					int thisDistance = myWallPosX - it.getWallPosX() - it.getWidth();
	
					if ( thisDistance >= 0 && thisDistance < distance) {
						distance = thisDistance;
						nearest = it;
					}
					else if( thisDistance < 0 ) {
						isRightmost = false;
					}
				}
			}
		}
	
		if( nearest != null) {
			myNearestNeighbourX = nearest;
			distanceToNeighbourX = distance;
		}
	
	}

	public void calculateDistanceMeasureDrawPos() {
	
			if( this.hasNearestY() ) {
				
				myYDistDrawPos = new int[4];
				
				if( myNearestNeighbourY.getWallPosX() > myWallPosX ) {
					myYDistDrawPos[0] = myNearestNeighbourY.getWallPosX();
					myYDistDrawPos[2] = myNearestNeighbourY.getWallPosX();
				}
				else {
					myYDistDrawPos[0] = myWallPosX;
					myYDistDrawPos[2] = myWallPosX;
				}
				
				myYDistDrawPos[1] = myNearestNeighbourY.getWallPosY() + myNearestNeighbourY.getHeight();
				myYDistDrawPos[3] = myWallPosY;
					
					
	//			int meLToItL = myNearestNeighbourY.getWallPosX() - myWallPosX;
	//			int meRtoItR = myWallPosX+myWidth - myNearestNeighbourY.getWallPosX()-myNearestNeighbourY.getWidth();
	//
	//			myYDistDrawPos[0] = myWallPosX + meLToItL + (meRtoItR / 2);
	//			myYDistDrawPos[2] = myYDistDrawPos[0];
	//
	//			myYDistDrawPos[1] = myNearestNeighbourY.getWallPosY() + myNearestNeighbourY.getHeight();
	//			myYDistDrawPos[3] = myWallPosY;
			}
	
			if( this.hasNearestX() ) {
	
				myXDistDrawPos = new int[4];
	
				if( myNearestNeighbourX.getWallPosY() > myWallPosY) {
					myXDistDrawPos[1] = myNearestNeighbourX.getWallPosY();
					myXDistDrawPos[3] = myNearestNeighbourX.getWallPosY();
				}
				else {
					myXDistDrawPos[1] = myWallPosY;
					myXDistDrawPos[3] = myWallPosY;
				}
				
				myXDistDrawPos[0] = myNearestNeighbourX.getWallPosX() + myNearestNeighbourX.getWidth();
				myXDistDrawPos[2] = myWallPosX;
				
	//			int meLToItL = myNearestNeighbourX.getWallPosY() - myWallPosY;
	//			int meRtoItR = myNearestNeighbourX.getWallPosY()+myNearestNeighbourX.getHeight() - myWallPosY - myHeight;
	//
	//			myXDistDrawPos[1] = myWallPosY + meLToItL + ((meRtoItR - meLToItL) / 2);
	//			myXDistDrawPos[3] = myXDistDrawPos[1];
	//
	//			myXDistDrawPos[0] = myNearestNeighbourX.getWallPosX() + myNearestNeighbourX.getWidth();
	//			myXDistDrawPos[2] = myWallPosX;
			}
			
			if( isRightmost ) {
				distanceToRight = myWall.getWidth() - myWallPosX - myWidth;
				
				distRightDrawPos = new int[4];
				
				distRightDrawPos[0] = myWallPosX + myWidth;
				distRightDrawPos[1] = myWallPosY + (myHeight/2);
				distRightDrawPos[2] = myWall.getWidth();
				distRightDrawPos[3] = distRightDrawPos[1];
			}
			
			if( isLowest ) {
				distanceToBottom = myWall.getHeight() - myWallPosY - myHeight;
				
				distBottomDrawPos = new int[4];
				
				distBottomDrawPos[0] = myWallPosX + (myWidth / 2);
				distBottomDrawPos[1] = myWallPosY + myHeight;
				distBottomDrawPos[2] = distBottomDrawPos[0];
				distBottomDrawPos[3] = myWall.getHeight();
						
				
			}
			
			
		}

	public int getWallPosX() {
		return myWallPosX;
	}
	
	public int getWallPosY() {
		return myWallPosY;
	}
	
	public int getWidth() {
		return myWidth;
	}
	
	public int getHeight() {
		return myHeight;
	}

	public String getName() {
		return myName;
	}
	
	// position and neighbours::
	
	public boolean isRightmost() {
		return isRightmost;
	}
	
	public boolean isLowest() {
		return isLowest;
	}
	
	public boolean hasNearestX() {
		if( myNearestNeighbourX != null ) {
			return true;
		}
		else return false;
	}

	public boolean hasNearestY() {
		if( myNearestNeighbourY != null ) {
			return true;
		}
		else return false;
	}

	// Distances:
	
	public int getDistanceToNearestX() {
		return distanceToNeighbourX;
	}

	public int getDistanceToNearestY() {
		return distanceToNeighbourY;
	}

	public int getDistanceToBottom() {
		return distanceToBottom;
	}
	
	public int getDistanceToRight() {
		return distanceToRight;
	}
	
	
	// drawPos
	
	public int[] getDistDrawPosX() {
		return myXDistDrawPos;
	}

	public int[] getDistDrawPosY() {
		return myYDistDrawPos;
	}

	public int[] getDistToRightDrawPos() {
		return distRightDrawPos;
	}
	
	public int[] getDistToBottomDrawPos() {
		return distBottomDrawPos;
	}

	
	
	
}
