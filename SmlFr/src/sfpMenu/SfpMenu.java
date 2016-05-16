package sfpMenu;

import processing.core.PApplet;

/**
 *	A popupmenu class to use inside processings PApplet class. It is meant avoid Threading issues with JPopupMenus drawn over PApplets.
 *
 * @author pht
 *
 */
public class SfpMenu extends SfpComponent {
	
	
	
	
	
	public SfpMenu(PApplet app, String name) {
		
		super(name);
		
		super.app = app;
		
	}
	
	
	public void draw() {
		
		
		for(SfpComponent comp : components) {
			
			comp.drawComponent();
		}
	}

	public void addSeparator() {
		this.addSfpComponent(new SfpSeparator());
	}
	
	
}
