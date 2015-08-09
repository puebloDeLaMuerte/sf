package SMUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class NewProjectDialog extends JOptionPane {

	JTextField nameField;
	JCheckBox[] roomBoxes;
	
	String[] rooms;
	String[] realNames;
	
	public NewProjectDialog(String[] rooms, String[] realNames) {
		
		this.rooms = rooms;
		this.realNames = realNames;
		
		
	}

	public int showDialog() {
		
		int r =  super.showOptionDialog(null, makePanel(), Lang.newProjectNameTitle, OK_CANCEL_OPTION, QUESTION_MESSAGE, null, getOptions(), 0);
		return r;
	}
	
	private JPanel makePanel() {
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		
		JLabel name = new JLabel((String) Lang.newProjectName);
		p.add(name, Component.LEFT_ALIGNMENT);
		p.add(Box.createRigidArea(new Dimension(0,5)));
		nameField = new JTextField();
		nameField.setColumns(30);
		nameField.addAncestorListener(new RequestFocusListener());
		p.add(nameField, Component.LEFT_ALIGNMENT);
		
		p.add(Box.createRigidArea(new Dimension(0,5)));
		p.add(new JSeparator());
		p.add(Box.createRigidArea(new Dimension(0,5)));
		
		JLabel rooms = new JLabel(Lang.selectRooms);
		p.add(rooms,Component.LEFT_ALIGNMENT);
		p.add(Box.createRigidArea(new Dimension(0,5)));

		
		roomBoxes = new JCheckBox[this.rooms.length];
		
		for (int i = 0; i < roomBoxes.length; i++) {
			roomBoxes[i] = new JCheckBox(this.realNames[i]);
			roomBoxes[i].setSelected(true);
			p.add(roomBoxes[i], Component.LEFT_ALIGNMENT);
		}
		
		
		p.setMinimumSize(new Dimension(400,0));
		p.setSize(400, 200);
		
		
		return p;
	}
	
	public String getProjectName() {
		
		String text = nameField.getText();
		
		if( text.trim().isEmpty() ) {
			return null;
		}
		
		return nameField.getText();
	}
	
	public String[] getSelectedRooms() {
		
		int count = 0;
		String[] ret;
		int[] selectionIndexes;
		
		for (int i = 0; i < roomBoxes.length; i++) {
			if( roomBoxes[i].isSelected())  count++;
		}
		ret = new String[count];
		selectionIndexes = new int[count];
		
		int idx = 0;
		for (int i = 0; i < roomBoxes.length; i++) {
			if( roomBoxes[i].isSelected())  {
				selectionIndexes[idx] = i;
				idx++;
			}
		}
		
		for (int i = 0; i < selectionIndexes.length; i++) {
			ret[i] = rooms[selectionIndexes[i]];
		}
		
		return ret;
	}
	
	public class RequestFocusListener implements AncestorListener {
	    @Override
	    public void ancestorAdded(final AncestorEvent e) {
	        final AncestorListener al = this;
	        SwingUtilities.invokeLater(new Runnable() {

	            @Override
	            public void run() {
	                final JComponent component = e.getComponent();
	                component.requestFocusInWindow();
	                component.removeAncestorListener(al);
	            }
	        });
	    }

	    @Override
	    public void ancestorMoved(final AncestorEvent e) {
	    }

	    @Override
	    public void ancestorRemoved(final AncestorEvent e) {
	    }
	}
}
