package SMUtils;

import javax.swing.JMenuItem;

public class ViewMenuItem extends JMenuItem {

	private String myViewName;
	
	public ViewMenuItem(String _menuText, String _viewName) {
		super(_menuText);
		myViewName = _viewName;
	}
	
	public String getViewName() {
		return myViewName;
	}
	
	
}
