package SMUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.event.EventListenerList;

import SMupdateModel.UpdateEvent;
import SMupdateModel.UpdateListener;
import SMupdateModel.UpdateType;


public class FileManagerSheduledUpdateThread extends Thread {

	private UpdateType									queueType;
	private ArrayList<LinkedHashMap<String, Object>>	dataQueue;
	
	EventListenerList									updateListeners;
	EventListenerList									updateListeners_ArrViews;
	
	public FileManagerSheduledUpdateThread( UpdateType type, ArrayList<LinkedHashMap<String, Object>> queue, EventListenerList lstnrs, EventListenerList lstnrsArrView ) {
		
		this.queueType = type;
		this.dataQueue = queue;
		
		this.updateListeners = lstnrs;
		this.updateListeners_ArrViews = lstnrsArrView;
		
		this.setName("FileManagerUpdateSheduler-Thread");
	}
	
	@Override
	public void run() {
		
		System.out.println("UPDATE SHEDULER: run");
		
		fireUpdates();
		
		System.out.println("UPDATE SHEDULER: finished");
		
	}
	
	private void fireUpdates() {
		
		UpdateEvent e2;
		
		if( dataQueue.size() == 0 ) {  // fire blank
			e2 = new UpdateEvent(this, UpdateType.BLANK, null);
		}
		
		e2 = new UpdateEvent(this, queueType, dataQueue);

				
		for(UpdateListener lsnr : updateListeners.getListeners(UpdateListener.class) ) {
			lsnr.doUpdate(e2);

		}
		for(UpdateListener lsnr : updateListeners_ArrViews.getListeners(UpdateListener.class) ) {
			lsnr.doUpdate(e2);

		}
	}
}
