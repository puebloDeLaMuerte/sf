package sfpMenu;

import processing.core.PApplet;
import processing.core.PVector;

public class SfpComponent {

	// TODO the calculations of sizes in this environment are off! A model needs to be implemented that defines who knows which sizes (self, containing component etc) and who asks whom for which size. totalSize and getMinSize() are similar but different and there's no useage strategy involved! =bad style.

	static final int menuDisabledTextGreyVal = 110;
	static final int menuTextGreyVal = 0;
	static final int menuOpacity = 233;
	static final int menuIdleGreyVal = 243;
	static final int mOverGreyVal = 150;
	static final int menuLineColor = 200;
	
	static final int subMenuDividerOfset = 2;
	
	protected PApplet 			app;
	private SfpComponent 		parent;
	protected SfpComponent[] 	components;
	
	private SfpEventListener[] 	eventListeners;
	private SfpMouseListener[]	mouseListeners;
	
	boolean enabled = true;
	boolean visible = false;
	boolean animation = false;
	
	private String 	myText = "";
	
	PVector 		myTotalSize;
	
	// { oben, unten, links, rechts }
	float[] borders = { 4,4,10,8 };
	
	PVector currentDrawPos;
	private int		animationCounter;
	private boolean mouseOver, pMouseOver;
	
	// --  init  --
	
	public SfpComponent(String text) {
		
		myText = text;
		
		
		components = new SfpComponent[0];
		eventListeners = new SfpEventListener[0];
		mouseListeners = new SfpMouseListener[0];
	}
	
	
	/**
	 * called by the SfpMenus addSfpComponent() method to add the parents PApplet.
	 * @param <b>app</b> - the PApplet on which all components in this menu do their drawing.
	 */
	private void setPApplet(PApplet app) {
		this.app = app;
	}
	
	private void setParent(SfpComponent parent) {
		this.parent = parent;
	}
	
	public void addEventListener(SfpEventListener listener) {
		
		SfpEventListener[] n = new SfpEventListener[eventListeners.length+1];
		for(int i = 0; i < eventListeners.length; i++) {
			n[i] = eventListeners[i];
		}
		n[n.length-1] = listener;
		eventListeners = n;
	}
	
	public void removeEventListener(SfpEventListener listener) {
		
		// TODO: This Function is untested!!
		
		SfpEventListener[] n = new SfpEventListener[eventListeners.length-1];
		
		int newL = 0;
		for(int oldL = 0; oldL < eventListeners.length; oldL++) {
			
			if(eventListeners[oldL] != listener ) {
				n[newL] = eventListeners[oldL];
				newL++;
			}			
		}
		eventListeners = n;
		
		
	}
	
	public void addMouseListener(SfpMouseListener listener) {
		SfpMouseListener[] n = new SfpMouseListener[mouseListeners.length+1];
		for(int i = 0; i < mouseListeners.length; i++) {
			n[i] = mouseListeners[i];
		}
		n[n.length-1] = listener;
		mouseListeners = n;
	}
	
	public void removeMouseListener(SfpMouseListener listener) {
		
		// TODO: This Function is untested!!
		
		SfpMouseListener[] n = new SfpMouseListener[mouseListeners.length-1];
		
		int newL = 0;
		for(int oldL = 0; oldL < mouseListeners.length; oldL++) {
			
			if(mouseListeners[oldL] != listener ) {
				n[newL] = mouseListeners[oldL];
				newL++;
			}			
		}
		mouseListeners = n;
		
		
	}
	
	/**
	 * 
	 * 
	 * this method must call the setParent(PApplet) method on the <b>SfpComponent</b> that is being added!
	 * @param <b>comp</b> - the SfpComponent that is beeing added to this Component
	 */
	public void addSfpComponent(SfpComponent comp) {
		
		comp.setPApplet(app);
		comp.setParent(this);
		
		SfpComponent[] n = new SfpComponent[components.length+1];
		for(int i = 0; i < components.length; i++) {
			n[i] = components[i];
		}
		n[n.length-1] = comp;
		components = n;
				
//		this.pack();
	}
	
	
	/**
	 * calculates and gets all Sizes of the sub-components and calculates this components totals size.
	 */
	public void pack() {
		
		float x = this.getMinSize().x;
		float y = this.getMinSize().y;
		
		for( SfpComponent comp : components ) {
			
			comp.pack();
			
			PVector cSize = comp.getTotalSize();
			
			if( x < cSize.x ) x = cSize.x;
//			y += cSize.y;
		}
		
		myTotalSize = new PVector(x,y);
	}
	
	
	public SfpComponent getParent() {
		if( parent != null ) return parent;
		else return this;
	}
	
	/**
	 * 
	 * pack() has to be called at least once before this method returns correct data!
	 * 
	 * @return the size of this components including all subcomponents
	 */
 	public PVector getTotalSize() {
		return myTotalSize;
	}
	
	/**
	 * Calculates this components minimum Size requirements according to its Text and contents.
	 * Uses app.textWidth() and app.textHeight() accordingly.
	 * <b>This does NOT return the total Size including subcomponents!</b> Only this individual size is calculated.
	 * @return The individual Size of this component.
	 */
	PVector getMinSize() {
		
		
		float asc = app.textAscent();
		float dsc = app.textDescent();
		float wdh = app.textWidth(myText);
		
		float y = borders[0] + borders[1] + asc + dsc;
		float x = borders[2] + borders[3] + wdh;


		return new PVector(x, y);
		
	}
	
	// --  handling  --
	
	/**
	 * should this component be active or grey?
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * is this component active or grey?
	 * @return returns true if this component is active
	 */
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setText(String txt) {
		this.myText = txt;
	}
	
	public String getText() {
		return this.myText;
	}
	
	/**
	 * Does this component hold any sub-components?
	 * 
	 * @return <b>true</b> if this elements components.length > 0
	 */
	@SuppressWarnings("unused")
	private boolean hasChildren() {
		if( components.length > 0) return true;
		else return false;
	}
	
	
	/**
	 * Does this item have a Sub-Menu?
	 * 
	 * @return <b>true</b> only if this component is of Type sfpMenu.SfpMenu, has a parent and holds one or more components.
	 */
	private boolean isSubMenu() {
		
		
		if( components.length > 0) {
			if (this.getClass() == SfpMenu.class ) {
				if( parent != null ) {
					return true;
				}
				return false;
			}
			return false;
		}
		else return false;
	}
	
	public boolean isOutmostComponent() {
		if( parent == null && this.getClass() == SfpMenu.class) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * is this component visible/opened?
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void openAt(float startX, float startY, int depth) {

		
		// if you're the parent of them all - check for space to show them and adjust if necessary
		
		if (isOutmostComponent()) {
			
			// adjust height
			int y = (int) startY;
			for (SfpComponent c : components) {
				y += c.getTotalSize().y;
			}
			if (y > app.height) {
				startY -= y - app.height;
			}
			
			// adjust width
			if (startX + myTotalSize.x > app.width) {
				startX -= (startX + myTotalSize.x) - app.width;
			} 
		}
		
		// now do the regular opening of yourself and your children
		
		this.currentDrawPos = new PVector(startX, startY);
		this.visible = true;

		closeSubs();

		if( depth-- <= 0 ) return;

		float drawX = startX;
		float drawY = startY;

		if (components.length > 0) {

			components[0].openAt(drawX, drawY, depth);

			if (components.length > 1) {


				for (int i = 1; i < components.length ; i++) {

					drawY += components[i - 1].getMinSize().y;
					components[i].openAt(drawX, drawY, depth);
				} 
			} 
		}		
			
	}
	
	/**
	 * 
	 * Use this to open sub-Menus held by this SfpComponent
	 * 
	 * @param startX: sub - Menu top left X coordinate
	 * @param startY: sub - Menu top left Y coordinate
	 */
	public void openSub(float startX, float startY) {
		
		
		
		if (true) {

			// adjust height
			int y = (int) startY;
			for (SfpComponent c : components) {
				y += c.getTotalSize().y;
			}
			if (y > app.height) {
				startY -= y - app.height;
			}

			// adjust width
			if (startX + myTotalSize.x > app.width) {
//				startX -= (startX + myTotalSize.x) - app.width;
				startX = parent.currentDrawPos.x - myTotalSize.x - subMenuDividerOfset;
			} 
		}
		
		

		if (isEnabled() && components.length > 0) {
						
//			parent.closeSubs();

			components[0].openAt(startX, startY, 0);

			if (components.length > 1) {


				for (int i = 1; i < components.length ; i++) {

					startY += components[i - 1].getMinSize().y;
					components[i].openAt(startX, startY, 0);
				} 
			} 
		}		
	}
	
	public void close() {
		
		for(SfpComponent c : components) {
			c.close();
		}
		
		this.visible = false;
		
		if( !this.isAnimating() ) {
//			currentDrawPos = null;
		}
	}
	
	public void closeSubs() {
		
		if (components.length > 0) {
			
			for( SfpComponent c : components) {
				if( c.isSubMenu() && c.isVisible() ) {
					
					for( SfpComponent csub : c.components ) {
						
						csub.close();
					}
				}
			}
			
		}
	}
	
	/**
	 * determines mouseOver
	 * @param mouseY 
	 * @param mouseX 
	 */
	public boolean checkMouseOver(int mouseX, int mouseY) {
		
//		for( SfpComponent c : components) {
//			c.mousePos(x,y);
//		}
		
//		int appX = app.mouseX;
//		int appY = app.mouseY;
//		
//		float drawX = currentDrawPos.x;
//		float drawY = currentDrawPos.y;
//		float sixe  = getParent().getTotalSize().x;		
		
		if( this.isVisible() ) {
			if( mouseX > currentDrawPos.x && mouseX < currentDrawPos.x + getParent().getTotalSize().x ) {
				if( mouseY > currentDrawPos.y && mouseY < currentDrawPos.y + getMinSize().y ) {
					this.mouseOver = true;
				} else {
					this.mouseOver = false;
				}
			} else {
				this.mouseOver = false;
			}
		} else {
			this.mouseOver = false;
		}
		
		if( mouseOver && mouseOver != pMouseOver ) {
			 mouseEntered();
		}
		
		if( !mouseOver && mouseOver != pMouseOver ) {
			mouseExited();
		}
		
		pMouseOver = mouseOver;
		return pMouseOver;
	}
	
	protected void mouseEntered() {
//		System.out.println("MOUSE ENTERED");
		 
		if( parent != null ) {
			parent.closeSubs();
		}
		
		 if( isSubMenu() ) {
			 
			 float startX = currentDrawPos.x + getParent().myTotalSize.x + subMenuDividerOfset;
			 float startY = currentDrawPos.y;
			 
			 this.openSub(startX, startY);
		 }
		 
		 for(SfpMouseListener l : mouseListeners) {
			 l.MouseEventHappened(new SfpMouseEnteredEvent(getClass(), myText));
		 }
	}
	
	protected void mouseExited() {
//		System.out.println("MOUSE Exited");
		for(SfpMouseListener l : mouseListeners) {
			 l.MouseEventHappened(new SfpMouseExitedEvent(getClass(), myText));
		 }
	}
	
	/**
	 * Checks the click-position at the PApplets mouseX/mouseY. Issues ActionEvents if valid click is performed. Closes the menu if click is out of bounds.
	 * <b>Do not call this method after the call to SfpComponent.openAt(x,y,depth)</b> - it will lead to an instand closing since the menu is opened in a position that leads to the mouseX/Y values being outside the bounds of the menu, thus closing it  again instantaneously. Call this method before the openAt() command, or separate the two commands through flow control (eg. if-statements) inside PApplets mouse-Methods. 
	 * @param mouseY 
	 * @param mouseX 
	 * 
	 *  @return int<br><b>-1</b>	if click was outside of menu bounds<br><b>0</b>	if a disabled (Enabled() == false) component has been clicked<br><b>1</b>	if an Enabled() component has been clicked (but no ActionEvent was issued)<br><b>2</b>	if an action command has been issued
	 */
	public int doClick(int mouseX, int mouseY) {
				
		int ret = -1;
		
//		System.out.println("checking mouse over for:" + this.getText() );
//		System.out.println("app.mouse: " + app.mouseX +" / " + app.mouseY);
//		System.out.println("me.pos   : " + this.currentDrawPos +"\n"+
//						   "me.min   : " + this.getMinSize() +"\n"+
//						   "me.total : " + this.getTotalSize());
//		System.out.println();
		
		checkMouseOver(mouseX, mouseY);
//		System.out.println("check    : " + b);
		
		for( SfpComponent c : components) {
			int r = c.doClick(mouseX, mouseY);
			if( r >= 0 ) {
				ret = r;
				break;
			}
		}
		
		if ( ret == -1) {
			if (mouseOver && isEnabled()) {
				if (eventListeners.length == 0)
					ret = 1;
				for (SfpEventListener l : eventListeners) {

					l.eventHappened(new SfpActionEvent(this.getClass(), myText));
					System.out.println("SfpMENU: event sheduled: " + this.getClass() + " - " + myText);
					ret = 2;
				}
				//			close();
			} else if (mouseOver) {
				ret = 0;
			} 
		}
		//		if( ret == 0 ) setAnimation();
		if( ret == 2 ) setAnimation();
		
		// if you are the outmose menu container
		if( parent == null ) {
			
			if( ret == -1 ) close();
			
			if( ret == 2 ) close();
		}
		
		return ret;
	}
	
	private void setAnimation() {
		animation = true;
		animationCounter = 4;
	}
	
	public boolean isAnimating() {
		
		if( this.animation ) return true;
		
		for( SfpComponent c : components ) {
			if( c.isAnimating() ) return true;
		}
		
		return false;
	}
	
	// --  drawing  --
	
	
	
	/**
	 * draws this component onto the PApplet. 
	 * 
	 */
	protected void drawComponent() {
		
		
		if (visible) {
			
			if( isSubMenu() ) {
				
				for( SfpComponent c : components ) {
					
					if( c.isVisible()) c.drawComponent();
				}
				
			}
			
			app.pushStyle();
			
//			app.stroke(200);
			app.noStroke();
			
			checkMouseOver(app.mouseX, app.mouseY);
			
			if( this.isEnabled() && this.mouseOver ) {
				app.fill(mOverGreyVal, menuOpacity);
//				app.noFill();
			} else {
				app.fill(menuIdleGreyVal, menuOpacity);
//				app.noFill();
			}
			
			float temp1 = getParent().getTotalSize().x;
			float temp2 = getTotalSize().y;
			
			app.rect(currentDrawPos.x, currentDrawPos.y, temp1, temp2);
			
			
			if( this.isEnabled() ) {
				app.fill(menuTextGreyVal);				
			} else {
				app.fill(menuDisabledTextGreyVal);
			}
		
			app.text(myText, currentDrawPos.x + borders[2], currentDrawPos.y + borders[0] + app.textAscent());
			
			app.popStyle();
			
		} else if (animation) {
			if( animationCounter > 0 ) {
				app.pushStyle();
				app.noStroke();

				
				if( animationCounter % 2 == 0 ) {
					app.fill(mOverGreyVal);
				} else {
					app.fill(menuIdleGreyVal);
				}
				
				float temp1 = parent.getTotalSize().x;
				float temp2 = getTotalSize().y;
				app.rect(currentDrawPos.x, currentDrawPos.y, temp1, temp2);
				app.fill(menuTextGreyVal);
				app.text(myText, currentDrawPos.x + borders[2], currentDrawPos.y + borders[0] + app.textAscent());
				
				animationCounter--;
				if( animationCounter == 0) animation = false;
				app.popStyle();
			}
		}
	}
}
