package smimport;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import SMUtils.Lang;

public class SM_SingleImportDialog extends JFrame implements ActionListener, PropertyChangeListener {

	
	private JTextField					TF_invNr, TF_title, TF_artist, TF_imageLocation;
	private JLabel						L_invNr, L_title, L_artist, L_imageFile, L_width, L_height, L_artworkMeasure, L_frameMeasure, L_pptMeasure;
	private JFormattedTextField			NF_artworkWidth, NF_artworkHeight, NF_frameWidth, NF_frameHeight, NF_pptWidth, NF_pptHeight;
	private JButton						okBtn, cancelBtn, browseBtn;
	
	private SM_Import					parent;
	
	public SM_SingleImportDialog(SM_Import parent) {
		
		this.parent = parent;
		
		
		
		// init TextFiels:
		
		TF_invNr = new JTextField();
		TF_title = new JTextField();
		TF_artist = new JTextField();
		TF_imageLocation = new JTextField();
		
		TF_invNr.addPropertyChangeListener(this);
		TF_title.addPropertyChangeListener(this);
		TF_artist.addPropertyChangeListener(this);
		TF_imageLocation.addPropertyChangeListener(this);
		
		TF_invNr.setColumns(100);
		TF_title.setColumns(100);
		TF_artist.setColumns(100);
		TF_imageLocation.setColumns(100);
		
		// init NumberFields:
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setGroupingUsed(false);
		format.setMinimumIntegerDigits(1);
		format.setMaximumFractionDigits(0);
		
		NF_artworkWidth 	= new JFormattedTextField(format);
		NF_artworkHeight 	= new JFormattedTextField(format);
		NF_frameWidth 		= new JFormattedTextField(format);
		NF_frameHeight 		= new JFormattedTextField(format);
		NF_pptWidth 		= new JFormattedTextField(format);
		NF_pptHeight 		= new JFormattedTextField(format);
		
		NF_artworkWidth.addPropertyChangeListener(this);
		NF_artworkHeight.addPropertyChangeListener(this);
		NF_frameWidth.addPropertyChangeListener(this);
		NF_frameHeight.addPropertyChangeListener(this);
		NF_pptWidth.addPropertyChangeListener(this);
		NF_pptHeight.addPropertyChangeListener(this);
		
		NF_artworkWidth.setColumns(5);
		NF_artworkHeight.setColumns(5);
		NF_frameWidth.setColumns(5);
		NF_frameHeight.setColumns(5);
		NF_pptWidth.setColumns(5);
		NF_pptHeight.setColumns(5);
		
		// init TextLabels:
		
		L_invNr 			= new JLabel(Lang.invNr);
		L_artist 			= new JLabel(Lang.artist);
		L_title 			= new JLabel(Lang.title);
		L_imageFile 		= new JLabel(Lang.imageFile);
		L_width 			= new JLabel(Lang.width);
		L_height 			= new JLabel(Lang.height);
		L_artworkMeasure	= new JLabel(Lang.artworkMeasure);
		L_frameMeasure		= new JLabel(Lang.frameMeasure);
		L_pptMeasure		= new JLabel(Lang.pptMeasure);
		
		// init Buttons:
		
		okBtn = new JButton(Lang.importBtn);
		okBtn.addActionListener(this);
		okBtn.setEnabled(false);
		cancelBtn = new JButton(Lang.cancel);
		cancelBtn.addActionListener(this);
		browseBtn = new JButton(Lang.browse);
		browseBtn.addActionListener(this);
		
		// do Layout
		
		JPanel inputPanel = new JPanel( new GridBagLayout());
		inputPanel.setSize(600, 330);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 0, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// text values:
		
		c.gridx = 0;
		
		c.gridy = 0;
		c.weightx = 0.1f;
		c.anchor = GridBagConstraints.LINE_START;
		
		inputPanel.add(L_invNr, c);
		c.gridy = 1;
		inputPanel.add(L_title, c);
		c.gridy = 2;
		inputPanel.add(L_artist, c);
		
		c.gridx = 1;
		c.gridwidth = 3;
		c.weightx = 0.5f;
				
		c.gridy = 0;
		inputPanel.add(TF_invNr, c);
		c.gridy = 1;
		inputPanel.add(TF_title, c);
		c.gridy = 2;
		inputPanel.add(TF_artist, c);
		
		// separator
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		inputPanel.add(new JSeparator(), c);
		
		// number values
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		inputPanel.add(L_height, c);
		c.gridx = 3;
		inputPanel.add(L_width, c);
		
		
		c.gridx = 0;
		c.weightx = 0.1f;
		
		c.gridy = 5;
		inputPanel.add(L_artworkMeasure, c);
		c.gridy = 6;
		inputPanel.add(L_frameMeasure, c);
		c.gridy = 7;
		inputPanel.add(L_pptMeasure, c);
		
		c.gridx = 1;
		
		c.gridy = 5;
		inputPanel.add(NF_artworkHeight, c);
		c.gridy = 6;
		inputPanel.add(NF_frameHeight, c);
		c.gridy = 7;
		inputPanel.add(NF_pptHeight, c);
		
		c.gridx = 3;
		
		c.gridy = 5;
		inputPanel.add(NF_artworkWidth, c);
		c.gridy = 6;
		inputPanel.add(NF_frameWidth, c);
		c.gridy = 7;
		inputPanel.add(NF_pptWidth, c);
		
		// separator
		
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = GridBagConstraints.REMAINDER;
		inputPanel.add(new JSeparator(), c);
		
		// Image File
		
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 0, 5);
		
		c.gridx = 0;
		c.gridy = 9;
		c.anchor = GridBagConstraints.LINE_START;
		inputPanel.add(L_imageFile, c);
		
		c.gridx = 0;
		c.gridy = 10;
		c.anchor = GridBagConstraints.LINE_START;
		inputPanel.add(browseBtn, c);
		
		c.gridx = 1;
		c.gridy = 10;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		inputPanel.add(TF_imageLocation, c);
		
		
		// frame Layout
		
		this.setLayout(new BorderLayout());
		this.setSize(600, 350);
		
		this.add(inputPanel, BorderLayout.CENTER);
		
		JPanel btnPanel = new JPanel();
		
		btnPanel.add(cancelBtn);
		btnPanel.add(okBtn);
		
		this.add(btnPanel, BorderLayout.PAGE_END);
		
		this.setTitle(Lang.importTitle);
		this.setVisible(true);
		
		
		
		
	}
	
	private boolean evaluateInput() {
		boolean valid = false;
		return valid;
	}
	
	private void packAndSendImportData() {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if( e.getSource() == okBtn ) {
			packAndSendImportData();
			
		} else
		if( e.getSource() == cancelBtn) {
			
			this.setVisible(false);
		}
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if( evaluateInput() ) okBtn.setEnabled(true);
		else okBtn.setEnabled(false);
	}
	
}
