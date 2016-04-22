package SMUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import smlfr.SM_Artwork;
import smlfr.SM_WallArrangementView;

public class WallRelativePositionChooser extends JFrame implements ActionListener, ChangeListener {

	private JSpinner 		spinner;
	private JButton			okBtn, cancelBtn; 
	private JRadioButton	topBtn, btmBtn, leftBtn, rightBtn;
	private ButtonGroup		compass;
	private JLabel			text;
	
	public enum	orientation { TOP, BOTTOM, LEFT, RIGHT };
	
	private LinkedHashMap<String, int[]> 	originalPositions;
	private SM_Artwork[] 					aws;
	private SM_WallArrangementView			parent;

	public WallRelativePositionChooser( SM_WallArrangementView parent, LinkedHashMap<String, int[]> originalPos, SM_Artwork[] aws ) {
		
		
		// data
		
		originalPositions = originalPos;
		this.aws = aws;
		this.parent = parent;
		
		// layout
		
		this.setTitle(Lang.posFromBorder);
		
		SpinnerModel model = new SpinnerNumberModel(1000, 0, 100000, 15);
		JPanel dataPanel = new JPanel( new GridLayout(3, 3, 2, 2) );
		
		spinner 		= new JSpinner(model);
		text			= new JLabel(Lang.distTxt);
		okBtn 			= new JButton(Lang.apply);
		okBtn.setEnabled(false);
		cancelBtn 		= new JButton(Lang.cancel);
		
		compass			= new ButtonGroup();
		
		topBtn			= new JRadioButton(Lang.top);
		btmBtn			= new JRadioButton(Lang.bottom);
		leftBtn			= new JRadioButton(Lang.left);
		rightBtn		= new JRadioButton(Lang.right);
		
		topBtn.setSelected(false);
		topBtn.setFocusable(false);
		topBtn.addChangeListener(this);
		btmBtn.setSelected(false);
		btmBtn.setFocusable(false);
		btmBtn.addChangeListener(this);
		leftBtn.setSelected(false);
		leftBtn.setFocusable(false);
		leftBtn.addChangeListener(this);
		rightBtn.setSelected(false);
		rightBtn.setFocusable(false);
		rightBtn.addChangeListener(this);
		
		compass.add(topBtn);
		compass.add(btmBtn);
		compass.add(leftBtn);
		compass.add(rightBtn);
		
		dataPanel.add( new JLabel() );
		dataPanel.add( topBtn );
		dataPanel.add( new JLabel() );
		dataPanel.add(leftBtn);
		dataPanel.add(spinner);
		dataPanel.add(rightBtn);
		dataPanel.add( new JLabel() );
		dataPanel.add( btmBtn );
		dataPanel.add( new JLabel() );
		
		Border bordA, bordB;
		bordA = BorderFactory.createLineBorder( Color.lightGray, 1 );
		bordB = BorderFactory.createEmptyBorder(6, 6, 6, 6);
		
		
		dataPanel.setBorder( BorderFactory.createCompoundBorder(bordB, bordA));
		
		JPanel btnPanel = new JPanel();
		btnPanel.add(cancelBtn);
		btnPanel.add(okBtn);
		
		JPanel p = new JPanel( new BorderLayout());
		Border padding = BorderFactory.createEmptyBorder(4,4,4,4);
		p.setBorder(padding);
		
		p.add(text, BorderLayout.PAGE_START);
		p.add(dataPanel, BorderLayout.CENTER);
		p.add(btnPanel,  BorderLayout.PAGE_END);
		
		spinner.addChangeListener(this);
		okBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		
		
		this.setContentPane(p);
		this.setAlwaysOnTop(true);
		this.pack();
		this.setSize(250, 187);
//		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		java.awt.Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setLocation((int)(window.width/2 - this.getSize().getWidth()/2), (int)(window.height/2 - this.getSize().getHeight()/2));
		this.setVisible(true);
		
//		System.err.println(this.getWidth()+" x " +this.getHeight());
	}

	@Override
	public void stateChanged(ChangeEvent c) {

		
			
		int o = -1;

		if(   topBtn.isSelected() ) {
			o = 0;
			okBtn.setEnabled(true);
		}
		else if(   btmBtn.isSelected() ) {
			o = 1;
			okBtn.setEnabled(true);
		}
		else if(  leftBtn.isSelected() ) {
			o = 2;
			okBtn.setEnabled(true);
		}
		else if( rightBtn.isSelected() ) {
			o = 3;
			okBtn.setEnabled(true);
		}
		else {
			okBtn.setEnabled(false);
			return;
		}
		
		int value = (Integer)spinner.getModel().getValue();
		
		parent.posFromBorderCallback(aws, value, o);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if( e.getSource() == okBtn ) {
			
			this.setVisible(false);
			
		} else if( e.getSource() == cancelBtn ) {
			
			parent.positionCancelCallback(aws, originalPositions);
			this.setVisible(false);
		}
	}
	
	
	
	
}
