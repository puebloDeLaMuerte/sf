/**
 * 
 */
package sfpMenu;

/**
 * @author pht
 *
 */
public class SfpMouseEvent {

	
	private Class source;
	protected boolean entered, exited;
	private String actionCommand;
	
	protected SfpMouseEvent(Class source, String actionCommand) {
		
		this.source = source;
		this.actionCommand = actionCommand;
	}
	
	public Class getSource() {
		return source;
	}
	
	public String getActionCommand() {
		return actionCommand;
	}
	
	public boolean mouseEntered() {
		return entered;
	}
	
	public boolean mouseExited() {
		return exited;
	}
}
