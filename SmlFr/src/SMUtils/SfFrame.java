/**
 * 
 */
package SMUtils;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.AWTKeyStroke;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.MenuBar;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.im.InputContext;
import java.awt.im.InputMethodRequests;
import java.awt.image.BufferStrategy;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.ComponentPeer;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.accessibility.AccessibleContext;
import javax.swing.SwingUtilities;

import sun.tools.java.SyntaxError;

/**
 * @author pht
 *
 */
public class SfFrame extends Frame {
//
//	@Override
//	public Color getForeground() {
//		return new Color(0, 255, 0);
//	}
//	
//	@Override
//	public Color getBackground() {
//		return new Color(255, 0, 0);
//	}
	
//	private class ItemHolder<T> implements Runnable{
//		private T item;
//		
//		public void run() {
//			
//		}
//		
//	    public void set(T item) {
//	    	
//	    }
//	    
//	    public T get() {
//	    	return (T)item;
//	    }
//	}
	private ArrayList<String> methods = new ArrayList<String>();
	

	
	
	public SfFrame(GraphicsConfiguration gc) {
		super(gc);
//		methods = new ArrayList<String>();
	}
	
	public SfFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
//		methods = new ArrayList<String>();
	}

	public SfFrame() throws HeadlessException {
		super();
//		methods = new ArrayList<String>();

	}

	public SfFrame(String title) throws HeadlessException {
		super(title);
//		methods = new ArrayList<String>();
	}
	
	public void printMethods() {
		
		System.err.println("\n#####################################");
		System.err.println("methods for frame: " + super.getName() + " - " + super.getTitle());
		
		for (int i = 0; i < methods.size(); i++) {
			
			System.err.println(" - " + methods.get(i));
			
			
		}
		
		System.err.println("#####################################\n");
	}
	
	public void printStackT(Thread t) {
		System.err.println("\n # # # sacktrack # # #");
		System.err.println(super.getName() + " - " + super.getTitle());
		System.err.println(t.getName());
		int i = 0;
		for( StackTraceElement s : t.getStackTrace()) {
			if( i>1) {				
				System.err.println("  " + s.toString());
			}
			i++;
		}
		
		
	}

	@Override
	public void addNotify() {
		super.addNotify();
	}

	@Override
	public AccessibleContext getAccessibleContext() {
		return super.getAccessibleContext();
	}

	@Override
	public int getCursorType() {
		return super.getCursorType();
	}

	@Override
	public int getExtendedState() {
		return super.getExtendedState();
	}

	@Override
	public Image getIconImage() {
		return super.getIconImage();
	}

	@Override
	public Rectangle getMaximizedBounds() {
		return super.getMaximizedBounds();
	}

	@Override
	public MenuBar getMenuBar() {
		return super.getMenuBar();
	}

	@Override
	public synchronized int getState() {
		return super.getState();
	}

	@Override
	public String getTitle() {
		return super.getTitle();
	}

	@Override
	public boolean isResizable() {
		return super.isResizable();
	}

	@Override
	public boolean isUndecorated() {
		return super.isUndecorated();
	}

	@Override
	protected String paramString() {
		return super.paramString();
	}

	@Override
	public void remove(MenuComponent arg0) {
		super.remove(arg0);
	}

	@Override
	public void removeNotify() {
		super.removeNotify();
	}

	@Override
	public void setCursor(int arg0) {
		super.setCursor(arg0);
	}

	@Override
	public void setExtendedState(int arg0) {
		super.setExtendedState(arg0);
	}

	@Override
	public void setIconImage(Image arg0) {
		super.setIconImage(arg0);
	}

	@Override
	public synchronized void setMaximizedBounds(Rectangle arg0) {
		super.setMaximizedBounds(arg0);
	}

	@Override
	public void setMenuBar(MenuBar arg0) {
		super.setMenuBar(arg0);
	}

	@Override
	public void setResizable(boolean arg0) {
		super.setResizable(arg0);
	}

	@Override
	public synchronized void setState(int arg0) {
		super.setState(arg0);
	}

	@Override
	public void setTitle(String arg0) {
		super.setTitle(arg0);
	}

	@Override
	public void setUndecorated(boolean arg0) {
		super.setUndecorated(arg0);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener arg0) {
		super.addPropertyChangeListener(arg0);
	}

	@Override
	public void addPropertyChangeListener(String arg0, PropertyChangeListener arg1) {
		super.addPropertyChangeListener(arg0, arg1);
	}

	@Override
	public synchronized void addWindowFocusListener(WindowFocusListener arg0) {
		super.addWindowFocusListener(arg0);
	}

	@Override
	public synchronized void addWindowListener(WindowListener arg0) {
		super.addWindowListener(arg0);
	}

	@Override
	public synchronized void addWindowStateListener(WindowStateListener arg0) {
		super.addWindowStateListener(arg0);
	}

	@Override
	public void applyResourceBundle(ResourceBundle arg0) {
		super.applyResourceBundle(arg0);
	}

	@Override
	public void applyResourceBundle(String arg0) {
		super.applyResourceBundle(arg0);
	}

	@Override
	public void createBufferStrategy(int arg0) {
		super.createBufferStrategy(arg0);
	}

	@Override
	public void createBufferStrategy(int arg0, BufferCapabilities arg1) throws AWTException {
		super.createBufferStrategy(arg0, arg1);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public BufferStrategy getBufferStrategy() {
		return super.getBufferStrategy();
	}

	@Override
	public Component getFocusOwner() {
		return super.getFocusOwner();
	}

	@Override
	public Set<AWTKeyStroke> getFocusTraversalKeys(int arg0) {
		return super.getFocusTraversalKeys(arg0);
	}

	@Override
	public boolean getFocusableWindowState() {
		return super.getFocusableWindowState();
	}

	@Override
	public GraphicsConfiguration getGraphicsConfiguration() {
		return super.getGraphicsConfiguration();
	}

	@Override
	public List<Image> getIconImages() {
		return super.getIconImages();
	}

	@Override
	public InputContext getInputContext() {
		return super.getInputContext();
	}

	@Override
	public <T extends EventListener> T[] getListeners(Class<T> arg0) {
		return super.getListeners(arg0);
	}

	@Override
	public Locale getLocale() {
		return super.getLocale();
	}

	@Override
	public ModalExclusionType getModalExclusionType() {
		return super.getModalExclusionType();
	}

	@Override
	public Component getMostRecentFocusOwner() {
		return super.getMostRecentFocusOwner();
	}

	@Override
	public Window[] getOwnedWindows() {
		return super.getOwnedWindows();
	}

	@Override
	public Window getOwner() {
		return super.getOwner();
	}

	@Override
	public Toolkit getToolkit() {
		return super.getToolkit();
	}

	@Override
	public synchronized WindowFocusListener[] getWindowFocusListeners() {
		return super.getWindowFocusListeners();
	}

	@Override
	public synchronized WindowListener[] getWindowListeners() {
		return super.getWindowListeners();
	}

	@Override
	public synchronized WindowStateListener[] getWindowStateListeners() {
		return super.getWindowStateListeners();
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public boolean isActive() {
		return super.isActive();
	}

	@Override
	public boolean isAlwaysOnTopSupported() {
		return super.isAlwaysOnTopSupported();
	}

	@Override
	public boolean isFocused() {
		return super.isFocused();
	}

	@Override
	public boolean isLocationByPlatform() {
		return super.isLocationByPlatform();
	}

	@Override
	public boolean isShowing() {
		return super.isShowing();
	}

	@Override
	public void pack() {
		super.pack();
	}

	@Override
	public void paint(Graphics arg0) {
		super.paint(arg0);
	}

	@Override
	public boolean postEvent(Event arg0) {
		return super.postEvent(arg0);
	}

	@Override
	protected void processEvent(AWTEvent arg0) {
		super.processEvent(arg0);
	}

	@Override
	protected void processWindowEvent(WindowEvent arg0) {
		super.processWindowEvent(arg0);
	}

	@Override
	protected void processWindowFocusEvent(WindowEvent arg0) {
		super.processWindowFocusEvent(arg0);
	}

	@Override
	protected void processWindowStateEvent(WindowEvent arg0) {
		super.processWindowStateEvent(arg0);
	}

	@Override
	public synchronized void removeWindowFocusListener(WindowFocusListener arg0) {
		super.removeWindowFocusListener(arg0);
	}

	@Override
	public synchronized void removeWindowListener(WindowListener arg0) {
		super.removeWindowListener(arg0);
	}

	@Override
	public synchronized void removeWindowStateListener(WindowStateListener arg0) {
		super.removeWindowStateListener(arg0);
	}

	@Override
	public void reshape(int arg0, int arg1, int arg2, int arg3) {
		super.reshape(arg0, arg1, arg2, arg3);
	}

	@Override
	public void setBounds(Rectangle arg0) {
		super.setBounds(arg0);
	}

	@Override
	public void setBounds(int arg0, int arg1, int arg2, int arg3) {
		super.setBounds(arg0, arg1, arg2, arg3);
	}

	@Override
	public void setCursor(Cursor arg0) {
		super.setCursor(arg0);
	}

	@Override
	public void setFocusableWindowState(boolean arg0) {
		super.setFocusableWindowState(arg0);
	}

	@Override
	public synchronized void setIconImages(List<? extends Image> arg0) {
		super.setIconImages(arg0);
	}

	@Override
	public void setLocationByPlatform(boolean arg0) {
		super.setLocationByPlatform(arg0);
	}

	@Override
	public void setLocationRelativeTo(Component arg0) {
		super.setLocationRelativeTo(arg0);
	}

	@Override
	public void setMinimumSize(Dimension arg0) {
		super.setMinimumSize(arg0);
	}

	@Override
	public void setModalExclusionType(ModalExclusionType arg0) {
		super.setModalExclusionType(arg0);
	}

	@Override
	public void setSize(Dimension arg0) {
		super.setSize(arg0);
	}

	@Override
	public void setSize(int arg0, int arg1) {
		super.setSize(arg0, arg1);
	}

	@Override
	public void setVisible(boolean arg0) {
		super.setVisible(arg0);
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void toBack() {
		super.toBack();
	}

	@Override
	public void toFront() {
		super.toFront();
	}

	@Override
	public Component add(Component arg0) {
		return super.add(arg0);
	}

	@Override
	public Component add(String arg0, Component arg1) {
		return super.add(arg0, arg1);
	}

	@Override
	public Component add(Component arg0, int arg1) {
		return super.add(arg0, arg1);
	}

	@Override
	public void add(Component arg0, Object arg1) {
		super.add(arg0, arg1);
	}

	@Override
	public void add(Component arg0, Object arg1, int arg2) {
		super.add(arg0, arg1, arg2);
	}

	@Override
	public synchronized void addContainerListener(ContainerListener arg0) {
		super.addContainerListener(arg0);
	}

	@Override
	protected void addImpl(Component arg0, Object arg1, int arg2) {
		super.addImpl(arg0, arg1, arg2);
	}

	@Override
	public void applyComponentOrientation(ComponentOrientation arg0) {
		super.applyComponentOrientation(arg0);
	}

	@Override
	public boolean areFocusTraversalKeysSet(int arg0) {
		return super.areFocusTraversalKeysSet(arg0);
	}

	@Override
	public int countComponents() {
		return super.countComponents();
	}

	@Override
	public void deliverEvent(Event arg0) {
		super.deliverEvent(arg0);
	}

	@Override
	public void doLayout() {
		super.doLayout();
	}

	@Override
	public Component findComponentAt(Point arg0) {
		return super.findComponentAt(arg0);
	}

	@Override
	public Component findComponentAt(int arg0, int arg1) {
		return super.findComponentAt(arg0, arg1);
	}

	@Override
	public float getAlignmentX() {
		return super.getAlignmentX();
	}

	@Override
	public float getAlignmentY() {
		return super.getAlignmentY();
	}

	@Override
	public Component getComponent(int arg0) {
		return super.getComponent(arg0);
	}

	@Override
	public Component getComponentAt(Point arg0) {
		return super.getComponentAt(arg0);
	}

	@Override
	public Component getComponentAt(int arg0, int arg1) {
		return super.getComponentAt(arg0, arg1);
	}

	@Override
	public int getComponentCount() {
		return super.getComponentCount();
	}

	@Override
	public int getComponentZOrder(Component arg0) {
		return super.getComponentZOrder(arg0);
	}

	@Override
	public Component[] getComponents() {
		return super.getComponents();
	}

	@Override
	public synchronized ContainerListener[] getContainerListeners() {
		return super.getContainerListeners();
	}

	@Override
	public FocusTraversalPolicy getFocusTraversalPolicy() {
		return super.getFocusTraversalPolicy();
	}

	@Override
	public Insets getInsets() {
		return super.getInsets();
	}

	@Override
	public LayoutManager getLayout() {
		return super.getLayout();
	}

	@Override
	public Dimension getMaximumSize() {
		return super.getMaximumSize();
	}

	@Override
	public Dimension getMinimumSize() {
		return super.getMinimumSize();
	}

	@Override
	public Point getMousePosition(boolean arg0) throws HeadlessException {
		return super.getMousePosition(arg0);
	}

	@Override
	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}

	@Override
	public Insets insets() {
		return super.insets();
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public boolean isAncestorOf(Component arg0) {
		return super.isAncestorOf(arg0);
	}

//	@Override
//	public boolean isFocusCycleRoot() {
//		return super.isFocusCycleRoot();
//	}

	@Override
	public boolean isFocusCycleRoot(Container arg0) {
		return super.isFocusCycleRoot(arg0);
	}

	@Override
	public boolean isFocusTraversalPolicySet() {
		return super.isFocusTraversalPolicySet();
	}

	@Override
	public void layout() {
		super.layout();
	}

	@Override
	public void list(PrintStream arg0, int arg1) {
		super.list(arg0, arg1);
	}

	@Override
	public void list(PrintWriter arg0, int arg1) {
		super.list(arg0, arg1);
	}

	@Override
	public Component locate(int arg0, int arg1) {
		return super.locate(arg0, arg1);
	}

	@Override
	public Dimension minimumSize() {
		return super.minimumSize();
	}

	@Override
	public void paintComponents(Graphics arg0) {
		super.paintComponents(arg0);
	}

	@Override
	public Dimension preferredSize() {
		return super.preferredSize();
	}

	@Override
	public void print(Graphics arg0) {
		super.print(arg0);
	}

	@Override
	public void printComponents(Graphics arg0) {
		super.printComponents(arg0);
	}

	@Override
	protected void processContainerEvent(ContainerEvent e) {
		super.processContainerEvent(e);
	}

	@Override
	public void remove(int index) {
		super.remove(index);
	}

	@Override
	public void remove(Component comp) {
		super.remove(comp);
	}

	@Override
	public void removeAll() {
		super.removeAll();
	}

	@Override
	public synchronized void removeContainerListener(ContainerListener l) {
		super.removeContainerListener(l);
	}

	@Override
	public void setComponentZOrder(Component comp, int index) {
		super.setComponentZOrder(comp, index);
	}

//	@Override
//	public void setFocusCycleRoot(boolean focusCycleRoot) {
//		super.setFocusCycleRoot(focusCycleRoot);
//	}

	@Override
	public void setFocusTraversalKeys(int id, Set<? extends AWTKeyStroke> keystrokes) {
		super.setFocusTraversalKeys(id, keystrokes);
	}

	@Override
	public void setFocusTraversalPolicy(FocusTraversalPolicy policy) {
		super.setFocusTraversalPolicy(policy);
	}

	@Override
	public void setFont(Font f) {
		super.setFont(f);
	}

	@Override
	public void setLayout(LayoutManager mgr) {
		super.setLayout(mgr);
	}

	@Override
	public void transferFocusBackward() {
		super.transferFocusBackward();
	}

	@Override
	public void transferFocusDownCycle() {
		super.transferFocusDownCycle();
	}

	@Override
	public void update(Graphics g) {
		super.update(g);
	}

	@Override
	public void validate() {
		super.validate();
	}

	@Override
	protected void validateTree() {
		super.validateTree();
	}

	@Override
	public boolean action(Event arg0, Object arg1) {
		return super.action(arg0, arg1);
	}

	@Override
	public synchronized void add(PopupMenu arg0) {
		super.add(arg0);
	}

	@Override
	public synchronized void addComponentListener(ComponentListener arg0) {
		super.addComponentListener(arg0);
	}

	@Override
	public synchronized void addFocusListener(FocusListener arg0) {
		super.addFocusListener(arg0);
	}

	@Override
	public void addHierarchyBoundsListener(HierarchyBoundsListener arg0) {
		super.addHierarchyBoundsListener(arg0);
	}

	@Override
	public void addHierarchyListener(HierarchyListener arg0) {
		super.addHierarchyListener(arg0);
	}

	@Override
	public synchronized void addInputMethodListener(InputMethodListener arg0) {
		super.addInputMethodListener(arg0);
	}

	@Override
	public synchronized void addKeyListener(KeyListener arg0) {
		super.addKeyListener(arg0);
	}

	@Override
	public synchronized void addMouseListener(MouseListener arg0) {
		super.addMouseListener(arg0);
	}

	@Override
	public synchronized void addMouseMotionListener(MouseMotionListener arg0) {
		super.addMouseMotionListener(arg0);
	}

	@Override
	public synchronized void addMouseWheelListener(MouseWheelListener arg0) {
		super.addMouseWheelListener(arg0);
	}

	@Override
	public Rectangle bounds() {
		return super.bounds();
	}

	@Override
	public int checkImage(Image arg0, ImageObserver arg1) {
		return super.checkImage(arg0, arg1);
	}

	@Override
	public int checkImage(Image arg0, int arg1, int arg2, ImageObserver arg3) {
		return super.checkImage(arg0, arg1, arg2, arg3);
	}

	@Override
	protected AWTEvent coalesceEvents(AWTEvent arg0, AWTEvent arg1) {
		return super.coalesceEvents(arg0, arg1);
	}

	@Override
	public boolean contains(Point p) {
		return super.contains(p);
	}

	@Override
	public boolean contains(int x, int y) {
		return super.contains(x, y);
	}

	@Override
	public Image createImage(ImageProducer producer) {
		return super.createImage(producer);
	}

	@Override
	public Image createImage(int width, int height) {
		return super.createImage(width, height);
	}

	@Override
	public VolatileImage createVolatileImage(int width, int height) {
		return super.createVolatileImage(width, height);
	}

	@Override
	public VolatileImage createVolatileImage(int width, int height, ImageCapabilities caps) throws AWTException {
		return super.createVolatileImage(width, height, caps);
	}

	@Override
	public void disable() {
		super.disable();
	}

	@Override
	public void enable() {
		super.enable();
	}

	@Override
	public void enable(boolean b) {
		super.enable(b);
	}

	@Override
	public void enableInputMethods(boolean enable) {
		super.enableInputMethods(enable);
	}

	@Override
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	@Override
	protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	@Override
	protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	@Override
	public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	@Override
	public void firePropertyChange(String propertyName, char oldValue, char newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	@Override
	public void firePropertyChange(String propertyName, short oldValue, short newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	@Override
	public void firePropertyChange(String propertyName, long oldValue, long newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	@Override
	public void firePropertyChange(String propertyName, float oldValue, float newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	@Override
	public void firePropertyChange(String propertyName, double oldValue, double newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	@Override
	public Color getBackground() {
		return super.getBackground();
	}

	@Override
	public int getBaseline(int width, int height) {
		return super.getBaseline(width, height);
	}

	@Override
	public BaselineResizeBehavior getBaselineResizeBehavior() {
		return super.getBaselineResizeBehavior();
	}

	@Override
	public Rectangle getBounds() {
		return super.getBounds();
	}

	@Override
	public Rectangle getBounds(Rectangle rv) {
		return super.getBounds(rv);
	}

	@Override
	public ColorModel getColorModel() {
		return super.getColorModel();
	}

	@Override
	public synchronized ComponentListener[] getComponentListeners() {
		return super.getComponentListeners();
	}

	@Override
	public ComponentOrientation getComponentOrientation() {
		return super.getComponentOrientation();
	}

	@Override
	public Cursor getCursor() {
		return super.getCursor();
	}

	@Override
	public synchronized DropTarget getDropTarget() {
		return super.getDropTarget();
	}

//	@Override
//	public Container getFocusCycleRootAncestor() {
//		return super.getFocusCycleRootAncestor();
//	}

	@Override
	public synchronized FocusListener[] getFocusListeners() {
		return super.getFocusListeners();
	}

	@Override
	public boolean getFocusTraversalKeysEnabled() {
		return super.getFocusTraversalKeysEnabled();
	}

	@Override
	public Font getFont() {
		return super.getFont();
	}

	@Override
	public FontMetrics getFontMetrics(Font font) {
		return super.getFontMetrics(font);
	}

	@Override
	public Color getForeground() {
		return super.getForeground();
	}

	@Override
	public Graphics getGraphics() {
		return super.getGraphics();
	}

	@Override
	public int getHeight() {
		return super.getHeight();
	}

	@Override
	public synchronized HierarchyBoundsListener[] getHierarchyBoundsListeners() {
		return super.getHierarchyBoundsListeners();
	}

	@Override
	public synchronized HierarchyListener[] getHierarchyListeners() {
		return super.getHierarchyListeners();
	}

	@Override
	public boolean getIgnoreRepaint() {
		return super.getIgnoreRepaint();
	}

	@Override
	public synchronized InputMethodListener[] getInputMethodListeners() {
		return super.getInputMethodListeners();
	}

	@Override
	public InputMethodRequests getInputMethodRequests() {
		return super.getInputMethodRequests();
	}

	@Override
	public synchronized KeyListener[] getKeyListeners() {
		return super.getKeyListeners();
	}

	@Override
	public Point getLocation() {
		return super.getLocation();
	}

	@Override
	public Point getLocation(Point rv) {
		return super.getLocation(rv);
	}

	@Override
	public Point getLocationOnScreen() {
		return super.getLocationOnScreen();
	}

	@Override
	public synchronized MouseListener[] getMouseListeners() {
		return super.getMouseListeners();
	}

	@Override
	public synchronized MouseMotionListener[] getMouseMotionListeners() {
		return super.getMouseMotionListeners();
	}

	@Override
	public Point getMousePosition() throws HeadlessException {
		return super.getMousePosition();
	}

	@Override
	public synchronized MouseWheelListener[] getMouseWheelListeners() {
		return super.getMouseWheelListeners();
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public Container getParent() {
		return super.getParent();
	}

	@Override
	public ComponentPeer getPeer() {
		return super.getPeer();
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return super.getPropertyChangeListeners();
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		return super.getPropertyChangeListeners(propertyName);
	}

	@Override
	public Dimension getSize() {
		return super.getSize();
	}

	@Override
	public Dimension getSize(Dimension rv) {
		return super.getSize(rv);
	}

	@Override
	public int getWidth() {
		return super.getWidth();
	}

	@Override
	public int getX() {
		return super.getX();
	}

	@Override
	public int getY() {
		return super.getY();
	}

	@Override
	public boolean gotFocus(Event evt, Object what) {
		return super.gotFocus(evt, what);
	}

	@Override
	public boolean handleEvent(Event evt) {
		return super.handleEvent(evt);
	}

	@Override
	public boolean hasFocus() {
		return super.hasFocus();
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		return super.imageUpdate(img, infoflags, x, y, w, h);
	}

	@Override
	public boolean inside(int x, int y) {
		return super.inside(x, y);
	}

	@Override
	public boolean isBackgroundSet() {
		return super.isBackgroundSet();
	}

	@Override
	public boolean isCursorSet() {
		return super.isCursorSet();
	}

	@Override
	public boolean isDisplayable() {
		return super.isDisplayable();
	}

	@Override
	public boolean isDoubleBuffered() {
		return super.isDoubleBuffered();
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@Override
	public boolean isFocusOwner() {
		return super.isFocusOwner();
	}

	@Override
	public boolean isFocusTraversable() {
		return super.isFocusTraversable();
	}

	@Override
	public boolean isFocusable() {
		return super.isFocusable();
	}

	@Override
	public boolean isFontSet() {
		return super.isFontSet();
	}

	@Override
	public boolean isForegroundSet() {
		return super.isForegroundSet();
	}

	@Override
	public boolean isLightweight() {
		return super.isLightweight();
	}

	@Override
	public boolean isMaximumSizeSet() {
		return super.isMaximumSizeSet();
	}

	@Override
	public boolean isMinimumSizeSet() {
		return super.isMinimumSizeSet();
	}

	@Override
	public boolean isOpaque() {
		return super.isOpaque();
	}

	@Override
	public boolean isPreferredSizeSet() {
		return super.isPreferredSizeSet();
	}

	@Override
	public boolean isValid() {
		return super.isValid();
	}

	@Override
	public boolean isVisible() {
		return super.isVisible();
	}

	@Override
	public boolean keyDown(Event evt, int key) {
		return super.keyDown(evt, key);
	}

	@Override
	public boolean keyUp(Event evt, int key) {
		return super.keyUp(evt, key);
	}

	@Override
	public void list() {
		super.list();
	}

	@Override
	public void list(PrintStream out) {
		super.list(out);
	}

	@Override
	public void list(PrintWriter out) {
		super.list(out);
	}

	@Override
	public Point location() {
		return super.location();
	}

	@Override
	public boolean lostFocus(Event evt, Object what) {
		return super.lostFocus(evt, what);
	}

	@Override
	public boolean mouseDown(Event evt, int x, int y) {
		return super.mouseDown(evt, x, y);
	}

	@Override
	public boolean mouseDrag(Event evt, int x, int y) {
		return super.mouseDrag(evt, x, y);
	}

	@Override
	public boolean mouseEnter(Event evt, int x, int y) {
		return super.mouseEnter(evt, x, y);
	}

	@Override
	public boolean mouseExit(Event evt, int x, int y) {
		return super.mouseExit(evt, x, y);
	}

	@Override
	public boolean mouseMove(Event evt, int x, int y) {
		return super.mouseMove(evt, x, y);
	}

	@Override
	public boolean mouseUp(Event evt, int x, int y) {
		return super.mouseUp(evt, x, y);
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
	}

	@Override
	public void nextFocus() {
		super.nextFocus();
	}

	@Override
	public void paintAll(Graphics g) {
		super.paintAll(g);
	}

	@Override
	public boolean prepareImage(Image image, ImageObserver observer) {
		return super.prepareImage(image, observer);
	}

	@Override
	public boolean prepareImage(Image image, int width, int height, ImageObserver observer) {
		return super.prepareImage(image, width, height, observer);
	}

	@Override
	public void printAll(Graphics g) {
		super.printAll(g);
	}

	@Override
	protected void processComponentEvent(ComponentEvent e) {
		super.processComponentEvent(e);
	}

	@Override
	protected void processFocusEvent(FocusEvent e) {
		super.processFocusEvent(e);
	}

	@Override
	protected void processHierarchyBoundsEvent(HierarchyEvent e) {
		super.processHierarchyBoundsEvent(e);
	}

	@Override
	protected void processHierarchyEvent(HierarchyEvent e) {
		super.processHierarchyEvent(e);
	}

	@Override
	protected void processInputMethodEvent(InputMethodEvent e) {
		super.processInputMethodEvent(e);
	}

	@Override
	protected void processKeyEvent(KeyEvent e) {
		super.processKeyEvent(e);
	}

	@Override
	protected void processMouseEvent(MouseEvent e) {
		super.processMouseEvent(e);
	}

	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
		super.processMouseMotionEvent(e);
	}

	@Override
	protected void processMouseWheelEvent(MouseWheelEvent e) {
		super.processMouseWheelEvent(e);
	}

	@Override
	public synchronized void removeComponentListener(ComponentListener l) {
		super.removeComponentListener(l);
	}

	@Override
	public synchronized void removeFocusListener(FocusListener l) {
		super.removeFocusListener(l);
	}

	@Override
	public void removeHierarchyBoundsListener(HierarchyBoundsListener l) {
		super.removeHierarchyBoundsListener(l);
	}

	@Override
	public void removeHierarchyListener(HierarchyListener l) {
		super.removeHierarchyListener(l);
	}

	@Override
	public synchronized void removeInputMethodListener(InputMethodListener l) {
		super.removeInputMethodListener(l);
	}

	@Override
	public synchronized void removeKeyListener(KeyListener l) {
		super.removeKeyListener(l);
	}

	@Override
	public synchronized void removeMouseListener(MouseListener l) {
		super.removeMouseListener(l);
	}

	@Override
	public synchronized void removeMouseMotionListener(MouseMotionListener l) {
		super.removeMouseMotionListener(l);
	}

	@Override
	public synchronized void removeMouseWheelListener(MouseWheelListener l) {
		super.removeMouseWheelListener(l);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		super.removePropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		super.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	@Override
	public void repaint(long tm) {
		super.repaint(tm);
	}

	@Override
	public void repaint(int x, int y, int width, int height) {
		super.repaint(x, y, width, height);
	}

	@Override
	public void repaint(long tm, int x, int y, int width, int height) {
		super.repaint(tm, x, y, width, height);
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
	}

	@Override
	protected boolean requestFocus(boolean temporary) {
		return super.requestFocus(temporary);
	}

	@Override
	public boolean requestFocusInWindow() {
		return super.requestFocusInWindow();
	}

	@Override
	protected boolean requestFocusInWindow(boolean temporary) {
		return super.requestFocusInWindow(temporary);
	}

	@Override
	public void resize(Dimension d) {
		super.resize(d);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void setBackground(Color c) {
		super.setBackground(c);
	}

	@Override
	public void setComponentOrientation(ComponentOrientation o) {
		super.setComponentOrientation(o);
	}

	@Override
	public synchronized void setDropTarget(DropTarget dt) {
		super.setDropTarget(dt);
	}

	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
	}

	@Override
	public void setFocusTraversalKeysEnabled(boolean focusTraversalKeysEnabled) {
		super.setFocusTraversalKeysEnabled(focusTraversalKeysEnabled);
	}

	@Override
	public void setFocusable(boolean focusable) {
		super.setFocusable(focusable);
	}

	@Override
	public void setForeground(Color c) {
		super.setForeground(c);
	}

	@Override
	public void setIgnoreRepaint(boolean ignoreRepaint) {
		super.setIgnoreRepaint(ignoreRepaint);
	}

	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
	}

	@Override
	public void setLocation(Point p) {
		super.setLocation(p);
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
	}

	@Override
	public void setMaximumSize(Dimension maximumSize) {
		super.setMaximumSize(maximumSize);
	}

	@Override
	public void setName(String name) {
		super.setName(name);
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
	}

	@Override
	public void show(boolean b) {
		super.show(b);
	}

	@Override
	public Dimension size() {
		return super.size();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public void transferFocus() {
		super.transferFocus();
	}

	@Override
	public void transferFocusUpCycle() {
		super.transferFocusUpCycle();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	

	
	
	
}
	
	
//	void foo() {
//		
//		
//		if( !SwingUtilities.isEventDispatchThread() ) {
//			
//			Runnable r = new ItemHolder< >() {
//				
//				@Override
//				public void run() {
//					item = 
//				}
//				
//				
//			};
//			
//			try {
//				SwingUtilities.invokeAndWait(r);
//				
//				return ()r.get();
//				
//			} catch (InterruptedException e) {e.printStackTrace();} catch (InvocationTargetException e) {e.printStackTrace();}
//		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	






