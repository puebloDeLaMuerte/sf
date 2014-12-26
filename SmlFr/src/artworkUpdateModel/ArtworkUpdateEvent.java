package artworkUpdateModel;

import java.util.EventObject;

public class ArtworkUpdateEvent extends EventObject {
	
	private String artworkName;
	private ArtworkUpdateType type;
	
	public ArtworkUpdateEvent(Object source, String artworkName, ArtworkUpdateType type) {
		super(source);
		this.artworkName = artworkName;
		this.type = type;
	}
	
	public ArtworkUpdateType getType() {
		return type;
	}
	
	public String getName() {
		return artworkName;
	}

}
