package SMUtils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import smlfr.SM_Artwork;

public class AWPanel extends JPanel implements MouseListener, DragGestureListener, Transferable {

	private boolean isHighlighted;
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK,0);
    private Border redBorder = BorderFactory.createLineBorder(Color.BLACK,5);

    private DragSource	ds;
    private String myArtworkName;
    private SM_Artwork myArtwork;
    
    private JPopupMenu menu;
    private MeasureMenuItem measurements, remove;
    
    public AWPanel(ActionListener ls, SM_Artwork _s) {
        addMouseListener(this);
        setBorder(blackBorder);
        setFocusable(true);
        isHighlighted = false;
		ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);
		myArtworkName = _s.getName();
		myArtwork = _s;
		
		menu = new JPopupMenu();
		measurements = new MeasureMenuItem(Lang.editMeasurements, _s);
		remove = new MeasureMenuItem(Lang.RemoveArtwork, _s);		

		measurements.addActionListener(ls);
		remove.addActionListener(ls);
		
		menu.add(remove);
		menu.add(measurements);
		this.add(menu);
    }
    
    public String getArtist() {
    	return myArtwork.getArtis();
    }
    
    public String getTitle() {
    	return myArtwork.getTitle();
    }
    
    public String getInvNr() {
    	return myArtwork.getName();
    }
    
    public SM_Artwork getArtwork() {
    	return myArtwork;
    }
    
    public boolean isSelected() {
    	return isHighlighted;
    }
    
    public int getSizeSquared() {
    	int[] s = myArtwork.getArtworkSize();
    	return s[0] * s[1];
    }

    public void setToolTipText() {
    	super.setToolTipText(myArtwork.getWallHumanReadable());
    }


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
        
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			
			isHighlighted = !isHighlighted;
			if (isHighlighted)
				setBorder(redBorder);
			else
				setBorder(blackBorder);
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			
			menu.show(this, 30, 30);
		}
	}
	

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		if (myArtwork.getWall() == null) {
			Cursor cursor = null;
			if (dge.getDragAction() == DnDConstants.ACTION_COPY) {
				cursor = DragSource.DefaultCopyDrop;
				//            cursor = DragSource.DefaultLinkDrop;
			}
			dge.startDrag(cursor, this);
		}

		
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
			String[] t = new String[] { myArtworkName, "Library", " ", "SM_Artwork" };
		return t;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] dfs = new DataFlavor[1];
		dfs[0] = SM_DataFlavor.SM_AW_Flavor;
		return dfs;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return false;
	}
	
	

}

