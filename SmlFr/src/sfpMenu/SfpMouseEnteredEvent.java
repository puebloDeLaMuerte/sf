/**
 * 
 */
package sfpMenu;

/**
 * @author pht
 *
 */
public class SfpMouseEnteredEvent extends SfpMouseEvent{

	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public SfpMouseEnteredEvent(Class source, String actionCommand) {
		super(source, actionCommand);
		super.entered = true;
		super.exited = false;
	}

}
