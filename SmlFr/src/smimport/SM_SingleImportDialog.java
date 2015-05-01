package smimport;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;

import SMUtils.Lang;

public class SM_SingleImportDialog extends JFrame implements ActionListener, DocumentListener {

	private JTextField[]				requiredFields, textFields, requiredNumberFields;
	
	private JTextField					TF_invNr, TF_title, TF_artist, TF_imageLocation;
	private JLabel						L_invNr, L_title, L_artist, L_imageFile, L_width, L_height, L_artworkMeasure, L_frameMeasure, L_pptMeasure;
	private JFormattedTextField			NF_artworkWidth, NF_artworkHeight, NF_frameWidth, NF_frameHeight, NF_pptWidth, NF_pptHeight;
	private JButton						okBtn, cancelBtn, browseBtn, batchBtn;
	
	private JFileChooser				fc;
	
	private boolean						hasValidData;
	private int[]						size, frameSize, pptSize;
	private File						imageFolder;
	private String						imageName;
	
	private SM_Import					parent;
	private File						artLibrarySaveLocation;
	
	
	public SM_SingleImportDialog(SM_Import parent, File _libraryLoc) {
		
		this.parent = parent;
		this.artLibrarySaveLocation = _libraryLoc;
		
		
		// init FileChooser
		
		
		fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Bilder", "png", "jpg");
		fc.setFileFilter(filter);
		
		// init TextFiels:
		
		TF_invNr = new JTextField();
		TF_title = new JTextField();
		TF_artist = new JTextField();
		TF_imageLocation = new JTextField();
		
		TF_invNr.getDocument().addDocumentListener(this);
		TF_title.getDocument().addDocumentListener(this);
		TF_artist.getDocument().addDocumentListener(this);
		TF_imageLocation.getDocument().addDocumentListener(this);
		
		TF_invNr.setColumns(100);
		TF_title.setColumns(100);
		TF_artist.setColumns(100);
		TF_imageLocation.setColumns(100);
		
		// init NumberFields:
		
		NF_artworkWidth 	= makeNumberField();
		NF_artworkHeight 	= makeNumberField();
		NF_frameWidth 		= makeNumberField();
		NF_frameHeight 		= makeNumberField();
		NF_pptWidth 		= makeNumberField();
		NF_pptHeight 		= makeNumberField();
				
		NF_artworkWidth.getDocument().addDocumentListener(this);
		NF_artworkHeight.getDocument().addDocumentListener(this);
		NF_frameWidth.getDocument().addDocumentListener(this);
		NF_frameHeight.getDocument().addDocumentListener(this);
		NF_pptWidth.getDocument().addDocumentListener(this);
		NF_pptHeight.getDocument().addDocumentListener(this);
		
		NF_artworkWidth.setColumns(5);
		NF_artworkHeight.setColumns(5);
		NF_frameWidth.setColumns(5);
		NF_frameHeight.setColumns(5);
		NF_pptWidth.setColumns(5);
		NF_pptHeight.setColumns(5);
		
		// setup textField Arrays
		
		requiredFields = new JTextField[6];
		
		requiredFields[0] = TF_invNr;
		requiredFields[1] = TF_title;
		requiredFields[2] = TF_artist;
		requiredFields[3] = TF_imageLocation;
		requiredFields[4] = NF_artworkWidth;
		requiredFields[5] = NF_artworkHeight;

		
		textFields = new JTextField[4];
		
		textFields[0] = TF_invNr;	
		textFields[1] = TF_title;
		textFields[2] = TF_artist;
		textFields[3] = TF_imageLocation;
		
		requiredNumberFields = new JTextField[2];
		
		requiredNumberFields[0] = NF_artworkWidth;
		requiredNumberFields[1] = NF_artworkHeight;
		 
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
		batchBtn = new JButton(Lang.batchImport);
		batchBtn.addActionListener(this);
		
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
		this.setSize(600, 400);
		
		this.add(inputPanel, BorderLayout.CENTER);
		
		JPanel btnPanel = new JPanel();
		
		btnPanel.add(batchBtn);
		btnPanel.add(cancelBtn);
		btnPanel.add(okBtn);
		
		this.add(btnPanel, BorderLayout.PAGE_END);
		
		this.setTitle(Lang.importTitle);
		this.setVisible(true);
		
		
		
		
	}
	
	private boolean evaluateInput() {
		
		boolean valid = true;
		
		// check if all Fields are filled with non Whitespace
		for( JTextField field : requiredFields ) {

			if( SM_Validator.isOnlyWhitespaceOrEmpty( field.getText()) ) valid = false;
		}
		
		// check for correct numbers
		for( JTextField field : requiredNumberFields ) {
			if ( !SM_Validator.isValidNumber( field.getText()) ) valid = false;
		}
		
		
		// check if optional values are paired right
		boolean nf1 = SM_Validator.isValidNumber( NF_frameWidth .getText());
		boolean nf2 = SM_Validator.isValidNumber( NF_frameHeight.getText());
		if( nf1 != nf2 ) valid = false;
		
		nf1 = SM_Validator.isValidNumber( NF_frameWidth .getText());
		nf2 = SM_Validator.isValidNumber( NF_frameHeight.getText());
		if( nf1 != nf2 ) valid = false;
		
		// check if file exists
		File testFile = new File(TF_imageLocation.getText());
		if( !testFile.exists() ) valid = false;
		
		return valid;
	}

	
	private void packImportData() {
		
		size = new int[2];
		size[0] = Integer.parseInt(NF_artworkHeight.getText());
		size[1] = Integer.parseInt(NF_artworkWidth .getText());
		
		frameSize = null;
		if( SM_Validator.isValidNumber(NF_frameWidth.getText()) && SM_Validator.isValidNumber(NF_frameHeight.getText()) ) {
			
			frameSize = new int[2];
			frameSize[0] = Integer.parseInt(NF_frameHeight.getText());
			frameSize[1] = Integer.parseInt(NF_frameWidth .getText());
		}
		
		pptSize = null;
		if( SM_Validator.isValidNumber(NF_pptWidth.getText()) && SM_Validator.isValidNumber(NF_pptHeight.getText()) ) {
			
			pptSize = new int[2];
			pptSize[0] = Integer.parseInt(NF_pptHeight.getText());
			pptSize[1] = Integer.parseInt(NF_pptWidth .getText());
		}
		
		imageFolder = new File(TF_imageLocation.getText());
		imageName = imageFolder.getName();
		imageName = imageName.substring(0, imageName.lastIndexOf('.'));
		imageFolder = imageFolder.getParentFile();
		
		System.out.println("here we have these values:");
		System.out.println("filename: "+imageName);
		System.out.println("filepath: "+imageFolder.getAbsolutePath());
			
		hasValidData = true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if( e.getSource() == okBtn ) {
			
			if( !parent.checkAlreadyInProject( TF_invNr.getText()) ) {
			
				packImportData();
				parent.singleImport(artLibrarySaveLocation, this);
				this.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(this, Lang.err_InvNrAlreadyExists, Lang.editAwErrorTitle, JOptionPane.WARNING_MESSAGE);
			}
			
		} else
		if( e.getSource() == cancelBtn) {
			
			this.setVisible(false);
		}
		else
		if( e.getSource() == batchBtn) {
			
			this.setVisible(false);
			parent.batchImport(artLibrarySaveLocation, true);
		}
		else
		if ( e.getSource() == browseBtn ) {
			
			fc.showOpenDialog(this);			
			TF_imageLocation.setText(fc.getSelectedFile().getAbsolutePath());
		}
	}

	private void evaluateAndSetOkBtn() {

		if( evaluateInput() ) {
			okBtn.setEnabled(true);
		}
		else {
			okBtn.setEnabled(false);
			hasValidData = false;
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		
		evaluateAndSetOkBtn();		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		evaluateAndSetOkBtn();		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		evaluateAndSetOkBtn();		
	}

	private JFormattedTextField makeNumberField() {
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setGroupingUsed(false);
		format.setMinimumIntegerDigits(0);
		format.setMaximumFractionDigits(0);
		
		return new JFormattedTextField(new NumberFormatter(format) {
		    @Override
		    public Object stringToValue(String text) throws ParseException {
		        if(text.trim().isEmpty())
		            return null;
		        return super.stringToValue(text);
		    }
		});
	}
	
	public int[] getArtworkSize() {
		
		if (hasValidData) {
			return size;
		}
		else {
			return null;
		}
			
	}

	public int[] getFrameSize() {
		
		if (hasValidData) {
			return frameSize;
		}
		else {
			return null;
		}
	}

	public int[] getPptSize() {
		
		if (hasValidData) {
			return pptSize;
		}
		else {
			return null;
		}
	}

	public String getInvNr() {
		if (hasValidData) {
			return TF_invNr.getText().trim();
		}
		else {
			return null;
		}
	}

	public String getArtist() {
		if(hasValidData) {
			return TF_artist.getText().trim();
		}else{
			return null;
		}
	}
	
	public String getTitle() {
		if (hasValidData) {
			return TF_title.getText().trim();
		}
		else {
			return null;
		}
	}

	public File getImageFolder() {
		if(hasValidData) {
			return imageFolder;
		} else {
			return null;
		}
		
	}

	public String getImageName() {
		if(hasValidData) {
			return imageName;
		}else{
			return null;
		}
	}
}
