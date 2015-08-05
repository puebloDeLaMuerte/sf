package SMUtils;

import javax.swing.JMenuItem;

public class ViewMenuItem extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8927197788981971715L;
	private String myViewName;
	
	public ViewMenuItem(String _menuText, String _viewName) {
		super(_menuText);
		myViewName = _viewName;
	}
	
	public String getViewName() {
		return myViewName;
	}
	
}
