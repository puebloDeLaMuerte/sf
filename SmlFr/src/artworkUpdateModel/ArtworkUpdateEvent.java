package artworkUpdateModel;

import java.util.EventObject;
import java.util.LinkedHashMap;

public class ArtworkUpdateEvent extends EventObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8317759613305521733L;
	
	private String artworkName;
	private ArtworkUpdateType type;
	private LinkedHashMap<String, Object> data;
	
	public ArtworkUpdateEvent(Object source, String artworkName, ArtworkUpdateType type, LinkedHashMap<String, Object> _data) {
		super(source);
		this.artworkName = artworkName;
		this.type = type;
		this.data = _data;
	}
	
	public ArtworkUpdateType getType() {
		return type;
	}
	
	public String getName() {
		return artworkName;
	}

	public LinkedHashMap<String, Object> getData() {
		return data;
	}

}
