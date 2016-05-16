/**
 * 
 */
package sfpMenu;

/**
 * @author pht
 *
 */
public class SfpMouseExitedEvent extends SfpMouseEvent {

	/**
	 * @param source
	 * @param actionCommand
	 */
	public SfpMouseExitedEvent(Class source, String actionCommand) {
		super(source, actionCommand);
		super.entered = false;
		super.exited = true;
	}

}
