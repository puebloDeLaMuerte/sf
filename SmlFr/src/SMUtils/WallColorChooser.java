package SMUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

import smlfr.SM_RoomProjectView;


public class WallColorChooser extends JFrame implements ActionListener, PropertyChangeListener {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1547944709851288007L;
	private JPanel 					content, input, buttons				;
	private JLabel 					msg, r, g, b						  	;
	private JFormattedTextField 	fieldRed, fieldGreen, fieldBlue		    	;
	private ButtonGroup 			radioBtns							      		;
	private JRadioButton 			changeRoom, changeWall				        		;
	private JCheckBox				preview								        			;
	private JButton					okBtn, cancelBtn, colorBtn, noWallColorBtn 					;
	private Color					color, originalColor, wallColor, roomColor				    	;
	private int						redInt, greenInt, blueInt			  								;
	private char					wallOver																;
	
	private boolean					approved = false, canceled = false;
	private SM_RoomProjectView		parent;
	
	public WallColorChooser(SM_RoomProjectView _pa, char _wallOver, int _roomColor, int _wallColor) throws HeadlessException {
		
		wallOver = _wallOver;
		parent = _pa;
		// general Frame stuff:
		
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		
		JPanel msgPanel = new JPanel();
		msg		= new JLabel(Lang.changeColorMessage);
		msgPanel.add(msg);
		input	= new JPanel(new FlowLayout());

		
		// Color Fields and Button:
				
		roomColor = new Color(_roomColor);
		if( _wallColor == 0 ) {
			wallColor = roomColor;
		} else {
			wallColor = new Color(_wallColor);
		}
		
		if(_wallOver == ' ') {
			color = roomColor;
			originalColor = roomColor;
		} else {
			color = wallColor;
			originalColor = wallColor;
		}
		
//		redInt   = (int)_wallColor >> 16 & 0xFF;
//		greenInt = (int)_wallColor >>  8 & 0xFF;
//		blueInt  = (int)_wallColor       & 0xFF;
		
		redInt   = color.getRed();
		greenInt = color.getGreen();
		blueInt  = color.getBlue();
			
		r = new JLabel("r:");
		g = new JLabel("g:");
		b = new JLabel("b:");
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumIntegerDigits(1);
//		format.setMaximumIntegerDigits(3);
		format.setMaximumFractionDigits(0);
		
		fieldRed = new JFormattedTextField(format);
		fieldRed.setColumns(3);
		fieldRed.setValue(redInt);
		fieldRed.setSelectionStart(0);
		fieldRed.setSelectionStart(2);
		fieldRed.addPropertyChangeListener(this);
		
		fieldGreen = new JFormattedTextField(format);
		fieldGreen.setColumns(3);
		fieldGreen.setValue(greenInt);
		fieldGreen.addPropertyChangeListener(this);
		
		fieldBlue = new JFormattedTextField(format);
		fieldBlue.setColumns(3);
		fieldBlue.setValue(blueInt);
		fieldBlue.addPropertyChangeListener(this);
		
		colorBtn = new JButton(Lang.colorPicker);
		colorBtn.addActionListener(this);
		
		input.add(r);
		input.add(fieldRed);
		input.add(g);
		input.add(fieldGreen);
		input.add(b);
		input.add(fieldBlue);
		input.add(colorBtn);
		
		// RadioButtons:
		
		JPanel radioPanel = new JPanel();
		radioPanel.setSize(500, 200);
		radioPanel.setLayout(new BorderLayout());
		radioBtns = new ButtonGroup();
		changeRoom = new JRadioButton(Lang.changeRoomColor);
		changeRoom.setSelected(true);
		changeRoom.addActionListener(this);
		
		
		if(wallOver == ' ') {
			changeWall = new JRadioButton(Lang.changeSingleWallColor_1 + "XXX" + Lang.changeSingleWallColor_2);
			changeWall.setEnabled(false);
		} else {
			changeWall = new JRadioButton(Lang.changeSingleWallColor_1 + wallOver + Lang.changeSingleWallColor_2);
			changeWall.setSelected(true);
			changeRoom.setSelected(false);
		}
		changeWall.addActionListener(this);
			
		radioBtns.add(changeRoom);
		radioBtns.add(changeWall);
		
		radioPanel.add(changeRoom, BorderLayout.PAGE_START);
		radioPanel.add(changeWall, BorderLayout.PAGE_END);

		// Buttons
		
		buttons = new JPanel(new FlowLayout());
		
		preview = new JCheckBox(Lang.preview);
		preview.setSelected(true);
		preview.setFocusable(false);
		preview.addActionListener(this);
		
		noWallColorBtn = new JButton(Lang.noWallColor);
		if(wallOver == ' ') {
			noWallColorBtn.setEnabled(false);
		} else {
			noWallColorBtn.setEnabled(true);
		}
		noWallColorBtn.addActionListener(this);
		
		okBtn = new JButton(Lang.ok);
		okBtn.addActionListener(this);
		
		cancelBtn = new JButton(Lang.cancel);
		cancelBtn.addActionListener(this);
		
		buttons.add(preview);
		buttons.add(noWallColorBtn);
		buttons.add(cancelBtn);
		buttons.add(okBtn);
		
		
		// pack and show dialog
		
		content.add(msgPanel);
		content.add( Box.createRigidArea(new Dimension(10, 10)) );
		content.add(input);
		content.add( Box.createRigidArea(new Dimension(10, 10)) );
		content.add(new JSeparator());
		content.add(radioPanel);
		content.add(new JSeparator());
		content.add(buttons);

	
		this.add(content);
		this.setTitle(Lang.changeColorTitle);
		this.setSize(500, 230);
		
		java.awt.Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setLocation(window.width/2 - 250, window.height/2 - 175);
		
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setVisible(true);
	}

	public int getSelectedColor() {
		return color.getRGB();
	}

	public boolean isApproved() {
		return approved;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == okBtn) {
			approved = true;
			
			
			if (! color.equals(originalColor)) {

				if (changeRoom.isSelected()) {
					parent.roomColorCallback(color.getRGB(), false);
//					parent.originalColorCallback();

				} else {
					parent.wallColorCallback(color.getRGB(), wallOver, false);
//					parent.originalColorCallback();
				}
				
			}
			this.setVisible(false);
			return;

		}
		
		
		else if(e.getSource() == cancelBtn) {
			
			canceled = true;
			// make Renderer display the data held by the room:
//			parent.roomColorCallback(originalColor.getRGB(),true);
			parent.originalColorCallback();
			
			this.setVisible(false);
		}
		
		else if( e.getSource() == noWallColorBtn) {
			parent.deleteWallColorCallback( wallOver );
			canceled = true;
			this.setVisible(false);
		}
		
		else if( e.getSource() == colorBtn) {
						
			Color newColor = JColorChooser.showDialog(
                    this,
                    Lang.changeColorTitle,
                    color);
			
			System.out.println("COLOR CHOOSER: we returned from the color chooser dialog.");
			
			if( newColor != null) {
				redInt   = (int)newColor.getRed();
				greenInt = (int)newColor.getGreen();
				blueInt  = (int)newColor.getBlue();
				
				fieldRed.setText(""+redInt);
				fieldGreen.setText(""+greenInt);
				fieldBlue.setText(""+blueInt);
				
				evaluateFields();
				sendPreviewCallbacks();
			}

			System.out.println("COLOR CHOOSER: color button pressed");
		}
		
		
		else if( e.getSource() == preview ) {
			
//			propertyChange(null);
			sendPreviewCallbacks();
			
//			if( preview.isSelected() ) {
//				parent.wallColorCallback(color.getRGB(), !changeRoom.isSelected(), wallOver, true);
//			}
//			else {
//				parent.wallColorCallback(originalColor.getRGB(), !changeRoom.isSelected(), wallOver, true);
//			}
		}
		
		
		else if( e.getSource() == changeRoom ) {
			
			originalColor = roomColor;
			noWallColorBtn.setEnabled(false);
			evaluateFields();
			sendPreviewCallbacks();
		}
		
		else if( e.getSource() == changeWall ) {
			
			originalColor = wallColor;
			noWallColorBtn.setEnabled(true);
			evaluateFields();
			sendPreviewCallbacks();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent pe) {

		
			Object s = pe.getSource();
			if (s == fieldRed || s == fieldGreen || s == fieldBlue) {

				evaluateFields();
				sendPreviewCallbacks();
			}
		
	}
	
	private void evaluateFields() {
		
		Integer r = Integer.parseInt(fieldRed.getText());
		Integer g = Integer.parseInt(fieldGreen.getText());
		Integer b = Integer.parseInt(fieldBlue.getText());
		
		if( r<0 || r>255 ) {
			
			r = Math.max(0, Math.min(255, r));
			fieldRed.setText(""+r);
		}

		if( g<0 || g>255 ) {

			g = Math.max(0, Math.min(255, g));
			fieldGreen.setText(""+g);
		}

		if( b<0 || b>255 ) {

			b = Math.max(0, Math.min(255, b));
			fieldBlue.setText(""+b);
		}
		

		color = new Color(r, g, b);
	}
	
	private void sendPreviewCallbacks() {
		
		if(! (canceled || approved) ){
			
			// Room Color
			if( changeRoom.isSelected() ) {
				
				if( preview.isSelected() ) {
					parent.roomColorCallback(color.getRGB(), true);
				} else {
					parent.originalColorCallback();
				}
				
				
			}
			
			// Wall Color
			else {
				
				if( preview.isSelected() ) {
					
					parent.wallColorCallback(color.getRGB(), wallOver, true);

				} else {

					parent.originalColorCallback();
//					parent.wallColorCallback(color.getRGB(), ' ', true);
				}
			}
		}
	}
	
}
