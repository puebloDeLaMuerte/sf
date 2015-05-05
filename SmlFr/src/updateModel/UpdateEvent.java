package updateModel;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class UpdateEvent extends EventObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8317759613305521733L;
	

	private UpdateType type;
	private ArrayList<LinkedHashMap<String, Object>> data;
	
	private int pointer;
	
	public UpdateEvent(Object source, UpdateType type, ArrayList<LinkedHashMap<String, Object>> _data) {
		super(source);
		this.type = type;
		this.data = _data;
		
		pointer = 0;
	}
	
	public UpdateType getType() {
		return type;
	}
	
	public String getName() {
		String name;
		try {
			name = (String) data.get(pointer).get("name");
			return name;
		} catch (Exception e) {
			return null;
		}
	}

	public LinkedHashMap<String, Object> getData() {
		LinkedHashMap<String, Object> returnData;
		try {
			returnData = data.get(pointer);
		} catch (Exception e) {
			return null;
		}
		return returnData;
	}
	
	public boolean next() {
		
		pointer++;
		
		if(pointer >= data.size()) return false;
		else return true;
	}

}
