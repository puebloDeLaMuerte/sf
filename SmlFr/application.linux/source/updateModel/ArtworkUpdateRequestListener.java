package updateModel;

public interface ArtworkUpdateRequestListener {
	
	void updateRequested( WallUpdateRequestEvent e);
	
	boolean updateRequested( ArtworkUpdateRequestEvent e);
	
	void updateRequested( WallColorUpdateRequestEvent e);
}
