package artworkUpdateModel;

import java.util.EventListener;

public interface ArtworkUpdateListener extends EventListener {
	
	void artworkUpdate(ArtworkUpdateEvent e);
}
