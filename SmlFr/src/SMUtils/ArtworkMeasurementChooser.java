package SMUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

import smlfr.SM_Artwork;
import smlfr.SM_WallArrangementView;
import SMUtils.SM_MeasureFormatter;

public class ArtworkMeasurementChooser extends JFrame implements ActionListener {

	
	private JFormattedTextField 	inputFrameHeight, inputFrameWidth;
	private JFormattedTextField		inputPptWidth, inputPptHeight;
	private JFormattedTextField		inputAwWidth, inputAwHeight;
	
	private JCheckBox				light, shadow;
	
	private JButton					okBtn, cancelBtn;
	
	private int						awWidth, prevAwWidth, awHeight, prevAwHeight;
	private int						frameWidth, prevFrameWidth, frameHeight, prevFrameHeight;
	private int						pptWidth, prevPptWidth, pptHeight, prevPptHeight;
	
	private LinkedHashMap<String, Object> packedData;
	
	private ArtworkMeasurementParent		parent;
	private SM_Artwork						artwork;
	
	public ArtworkMeasurementChooser(ArtworkMeasurementParent parent, SM_Artwork artwork) {
		
		this.parent = parent;
		this.artwork = artwork;
		
		awWidth 		= artwork.getArtworkSize()[0];
		prevAwWidth 	= artwork.getArtworkSize()[0];
		awHeight 		= artwork.getArtworkSize()[1];
		prevAwHeight 	= artwork.getArtworkSize()[1];
		
		// setup the measures in human readable format:
		
		if (artwork.hasPassepartout()) {
			pptHeight	= artwork.getPassepartoutMeasure()[0] + artwork.getPassepartoutMeasure()[1] + awHeight;
			pptWidth	= artwork.getPassepartoutMeasure()[2] + artwork.getPassepartoutMeasure()[3] + awWidth;
		} else {
//			pptHeight	= awHeight;
//			pptWidth	= awWidth;
		}
		prevPptHeight	= pptHeight;
		prevPptWidth	= pptWidth;
		
		if (artwork.hasFrame()) {
			frameHeight	= artwork.getFrameMeasure()[0] + artwork.getFrameMeasure()[1] + awHeight;
			frameWidth	= artwork.getFrameMeasure()[2] + artwork.getFrameMeasure()[3] + awWidth;
			
			if( artwork.hasPassepartout() ) {
				frameHeight += artwork.getPassepartoutMeasure()[0] + artwork.getPassepartoutMeasure()[1];
				frameWidth  += artwork.getPassepartoutMeasure()[2] + artwork.getPassepartoutMeasure()[3];
			}
			
		} else {
//			frameHeight	= pptHeight;
//			frameWidth	= pptWidth;
		}
		prevFrameHeight	= frameHeight;
		prevFrameWidth 	= frameWidth;
		
		// Layout:
		
		this.setTitle(Lang.editArtwork + artwork.getName());
		
		JPanel content = new JPanel( new BorderLayout(10, 10));		
		
		content.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		
		content.add( createMeasurementPanel(), BorderLayout.PAGE_START);
		
		content.add(Box.createRigidArea(new Dimension(10,20))); ;
		
		content.add( createCheckboxPanel(), BorderLayout.CENTER );
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		cancelBtn = new JButton(Lang.cancel);
		cancelBtn.addActionListener(this);
		buttonPanel.add(cancelBtn);
		
		okBtn = new JButton(Lang.ok);
		okBtn.addActionListener(this);
		buttonPanel.add(okBtn);
		
		content.add(buttonPanel, BorderLayout.PAGE_END);
				
		this.setContentPane(content);

		this.setSize(440, 280);
		
		java.awt.Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setLocation((int)(window.width/2 - this.getSize().getWidth()/2), (int)(window.height/2 - this.getSize().getHeight()/2));		

		
		this.setVisible(true);
	}
	
	private JPanel createMeasurementPanel() {
		
		JPanel fp = new JPanel();
		
		Border border = new LineBorder(new Color(0.8f,0.8f,0.8f));
	    Border margin = new EmptyBorder(2,2,2,2);
	    fp.setBorder(new CompoundBorder(border, margin));
		
		inputAwHeight = makeNumberField();
		inputAwHeight.setColumns(4);
		inputAwHeight.setText(""+awHeight);
		inputAwWidth = makeNumberField();
		inputAwWidth.setColumns(4);
		inputAwWidth.setText(""+awWidth);
		
		inputFrameHeight = makeNumberField();
		inputFrameHeight.setColumns(4);
		inputFrameHeight.setText(""+frameHeight);
		inputFrameWidth  = makeNumberField();
		inputFrameWidth.setColumns(4);
		inputFrameWidth.setText(""+frameWidth);
		
//		if(frameHeight == pptHeight) inputFrameHeight.setValue(null);
//		if(frameWidth  == pptWidth ) inputFrameWidth.setValue(null);
		
		inputPptHeight = makeNumberField();
		inputPptHeight.setColumns(4);
		inputPptHeight.setText(""+pptHeight);
		inputPptWidth  = makeNumberField();
		inputPptWidth.setColumns(4);
		inputPptWidth.setText(""+pptWidth);
		
//		if(pptHeight == awHeight) inputPptHeight.setValue(null);
//		if(pptWidth  == awWidth ) inputPptWidth.setValue(null);

		
		JLabel labelWidth = new JLabel(Lang.width);
		JLabel labelHeight = new JLabel(Lang.height);
		
		fp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 0, 5);
		c.weightx = 0.5;

		c.gridy = 0;
		c.gridx = 1;
		fp.add(labelHeight, c);
		c.gridx = 3;
		fp.add(labelWidth, c);
//		c.gridx = 4;
//		fp.add(new JLabel(Lang.prevValue), c);
		
		
		c.anchor = GridBagConstraints.LINE_START;
		c.gridy++;
		c.gridx = 0;
		fp.add(new JLabel(Lang.artworkMeasure), c);
		c.anchor = GridBagConstraints.CENTER;
		
		c.gridx = 1;
		fp.add(inputAwHeight, c);
		
		c.gridx = 2;
		fp.add(new JLabel("x"), c);
		
		c.gridx = 3;
		fp.add(inputAwWidth, c);
		
		c.gridx = 4;
		JLabel prevAw= new JLabel( prevAwHeight +" x "+prevAwWidth );
		fp.add(prevAw, c);		
		
		
		c.anchor = GridBagConstraints.LINE_START;
		c.gridy++;
		c.gridx = 0;
		fp.add(new JLabel(Lang.pptMeasure), c);
		c.anchor = GridBagConstraints.CENTER;
		
		c.gridx = 1;
		fp.add(inputPptHeight, c);
		
		c.gridx = 2;
		fp.add(new JLabel("x"), c);
		
		c.gridx = 3;
		fp.add(inputPptWidth, c);
		
		c.gridx = 4;
		JLabel prevPpt = new JLabel( prevPptHeight +" x "+prevPptWidth );
		fp.add(prevPpt, c);
		
		
		c.anchor = GridBagConstraints.LINE_START;
		c.gridy++;
		c.gridx = 0;
		fp.add(new JLabel(Lang.frameMeasure), c);
		c.anchor = GridBagConstraints.CENTER;
		
		c.gridx = 1;
		fp.add(inputFrameHeight, c);
		
		c.gridx = 2;
		fp.add(new JLabel("x"), c);
		
		c.gridx = 3;
		fp.add(inputFrameWidth, c);
		
		c.gridx = 4;
		JLabel prevFrame = new JLabel( prevFrameHeight +" x "+prevFrameWidth );
		fp.add(prevFrame, c);
		
		return fp;
	}

	private JPanel createCheckboxPanel() {

		JPanel pp = new JPanel(new GridLayout(2,1));
		
		Border border = new LineBorder(new Color(0.8f,0.8f,0.8f));
	    Border margin = new EmptyBorder(2,2,2,2);
	    pp.setBorder(new CompoundBorder(border, margin));
	    
		light = new JCheckBox(Lang.light);
		light.setFocusable(true);
		if( artwork.hasLight() ) light.setSelected(true);
		else light.setSelected(false);
		
		shadow = new JCheckBox(Lang.shadow);
		shadow.setFocusable(true);
		if( artwork.hasShadow() ) shadow.setSelected(true);
		else shadow.setSelected(false);

		
		pp.add(light);
		pp.add(shadow);
		
		return pp;
	}

	private JFormattedTextField makeNumberField() {
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setGroupingUsed(false);
		format.setMinimumIntegerDigits(0);
		format.setMaximumFractionDigits(0);
		
		JFormattedTextField field = new JFormattedTextField(new NumberFormatter(format) {
		    @Override
		    public Object stringToValue(String text) throws ParseException {
		        if(text.trim().isEmpty())
		            return 0;
		        return super.stringToValue(text);
		    }
		}); 
		
//		field.getDocument().addDocumentListener(this);
		
		return field;
	}
	
	private boolean validateData() {
		
		String errors = "<html><body style='width: 300px'>" + Lang.editAwErrorMessage_1;
				
		textFieldsToIntegers();
		
		boolean valid = true;
		
		// frame smaller than artwork?
		if( frameWidth  != 0 && frameWidth  < awWidth  ) {
			valid = false;
			errors += Lang.errFrameArtworkWidth;
		}
		if( frameHeight != 0 && frameHeight < awHeight ) {
			valid = false;
			errors += Lang.errFrameArtworkHeight;
		}

		// frame smaller than passepartout?
		if( frameWidth	!= 0 && frameWidth	< pptWidth ) {
			valid = false;
			errors += Lang.errFrameWidthSmallerPpt;
		}
		if( frameHeight	!= 0 && frameHeight	< pptHeight) {
			valid = false;
			errors += Lang.errFrameHeightSmallerPpt;
		}
		
		// passepartout smaller than artwork?
		if( pptWidth	!= 0 && pptWidth	< awWidth )	 {
			valid = false;
			errors += Lang.errPptWidthSmallerArtwork;
		}
		if( pptHeight	!= 0 && pptHeight	< awHeight)	 {
			valid = false;
			errors += Lang.errPptHeightSmallerArtwork;
		}

		
		if( !valid ) JOptionPane.showMessageDialog(this,  errors, Lang.editAwErrorTitle, JOptionPane.WARNING_MESSAGE);
		
				
		return valid;
	}
	
	private void textFieldsToIntegers() {
		
		awWidth		= Integer.parseInt(inputAwWidth.getText());
		awHeight	= Integer.parseInt(inputAwHeight.getText());
		
		frameWidth 	= Integer.parseInt(inputFrameWidth.getText());
		frameHeight	= Integer.parseInt(inputFrameHeight.getText());
		
		pptWidth	= Integer.parseInt(inputPptWidth.getText());
		pptHeight	= Integer.parseInt(inputPptHeight.getText());
	}
	
	private LinkedHashMap<String, Object> packData() {
		
		textFieldsToIntegers();
		
		int[] size = {awHeight, awWidth};
		
		int[] frame;
		if( frameWidth == 0 || frameHeight == 0 || (frameWidth == awWidth && frameHeight == awHeight)){
			frame = null;
		} else {
			frame = new int[2];
			frame[0] = frameHeight;
			frame[1] = frameWidth;
		}
		
		int[] ppt;
		if( pptWidth == 0 || pptHeight == 0 || (pptWidth == awWidth && pptHeight == awHeight)){
			ppt = null;
		} else {
			ppt = new int[2];
			ppt[0] = pptHeight;
			ppt[1] = pptWidth;
		}
		
		LinkedHashMap<String, int[]> pValues = SM_MeasureFormatter.measurementInputFormatToInternalFormat(size, frame, ppt);
		
		packedData = new LinkedHashMap<String, Object>(5);
		
		packedData.put("Name", 		artwork.getName());
		packedData.put("size", 		pValues.get("size"));
		packedData.put("frame", 	pValues.get("frame"));
		packedData.put("ppt", 		pValues.get("ppt"));
		packedData.put("light", 	light.isSelected());
		packedData.put("shadow", 	shadow.isSelected());
		
		return packedData;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == cancelBtn) this.setVisible(false);
		if(e.getSource() == okBtn) {
			if( validateData() ) {
				
				parent.artworkMeasurementCallback(packData());
				this.setVisible(false);
			}
		}
	}
	
}
















