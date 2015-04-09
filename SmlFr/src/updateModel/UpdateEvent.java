package updateModel;

import java.util.EventObject;
import java.util.LinkedHashMap;

public class UpdateEvent extends EventObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8317759613305521733L;
	
	private String artworkName;
	private UpdateType type;
	private LinkedHashMap<String, Object> data;
	
	public UpdateEvent(Object source, String artworkName, UpdateType type, LinkedHashMap<String, Object> _data) {
		super(source);
		this.artworkName = artworkName;
		this.type = type;
		this.data = _data;
	}
	
	public UpdateType getType() {
		return type;
	}
	
	public String getName() {
		return artworkName;
	}

	public LinkedHashMap<String, Object> getData() {
		return data;
	}

}
