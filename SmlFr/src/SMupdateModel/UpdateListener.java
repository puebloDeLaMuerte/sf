package SMupdateModel;

import java.util.EventListener;

public interface UpdateListener extends EventListener {
	
	void doUpdate(UpdateEvent e);
}
