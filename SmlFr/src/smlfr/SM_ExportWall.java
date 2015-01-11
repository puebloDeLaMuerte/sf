package smlfr;

public class SM_ExportWall {

	SM_ExportArtwork[] aws;
	int myWidth, myHeight;
	String myName;
	
	
	public SM_ExportWall( String myName, SM_ExportArtwork[] aws, int myWidth, int myHeight) {
		super();
		this.aws = aws;
		this.myWidth = myWidth;
		this.myHeight = myHeight;
		this.myName = myName;
		
		for( SM_ExportArtwork aw : aws) {
			aw.setWall(this);
			aw.reverseHeightCoordinates(myHeight);
		}
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
	
	public SM_ExportArtwork[] getArtworks() {
		return aws;
	}
	
}
