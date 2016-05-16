package sfpMenu;


public class SfpViewMenuItem extends SfpComponent {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 8927197788981971715L;
	
	private String myViewName;
	
	public SfpViewMenuItem(String _menuText, String _viewName) {
		super(_menuText);
		myViewName = _viewName;
	}
	
	public String getViewName() {
		return myViewName;
	}

	
}
