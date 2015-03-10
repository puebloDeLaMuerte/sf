package artworkUpdateModel;

public interface ArtworkUpdateRequestListener {
	
	void updateRequested( WallUpdateRequestEvent e);
	
	void updateRequested( ArtworkUpdateRequestEvent e);
	
	void updateRequested( WallColorUpdateRequestEvent e);
}
