/**
 * 
 */
package sfpMenu;

/**
 * @author pht
 *
 */
public class SfpActionEvent {
	
	Class source;
	String actionCommand;
	
	public SfpActionEvent(Class source, String actionCommand) {
		
		this.source = source;
		this.actionCommand = actionCommand;
	}
	
	public Class getSource() {
		return source;
	}
	
	public String getActionCommand() {
		return actionCommand;
	}

}
