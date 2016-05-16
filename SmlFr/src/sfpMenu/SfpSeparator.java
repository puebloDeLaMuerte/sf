/**
 * 
 */
package sfpMenu;

import processing.core.PVector;

/**
 * @author pht
 *
 */
public class SfpSeparator extends SfpComponent {

	/**
	 * 
	 */
	public SfpSeparator() {
		super("");
		enabled = false;
		animation = false;
	}

	@Override
	public void addEventListener(SfpEventListener listener) {}

	@Override
	public void addSfpComponent(SfpComponent comp) {}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(false);
	}

	@Override
	public void checkMouseOver() {}

	@Override
	public void pack() {
		super.myTotalSize = this.getMinSize();
	}
	

	@Override
	PVector getMinSize() {
		return new PVector( borders[2]+borders[3], borders[0]+borders[1] );
	}

	@Override
	protected void drawComponent() {
		
		
		if( visible ) {
			app.pushStyle();

			app.fill(250, 190);
			app.noStroke();
			
			float temp1 = currentDrawPos.x+getParent().getTotalSize().x -1;
			float temp2 = getTotalSize().y;
//			app.rectMode();
			app.rect(currentDrawPos.x, currentDrawPos.y, getParent().getTotalSize().x, temp2);
			app.stroke(200);
			app.line(currentDrawPos.x, currentDrawPos.y+borders[0], temp1, currentDrawPos.y+borders[0]);
			
			app.popStyle();
		}
		
	}
	
	
	

}
