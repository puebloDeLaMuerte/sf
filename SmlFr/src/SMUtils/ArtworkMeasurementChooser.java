package SMUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import smlfr.SM_Artwork;
import smlfr.SM_WallArrangementView;

public class ArtworkMeasurementChooser extends JFrame implements ActionListener {

	
	private JFormattedTextField 	inputFrameHeight, inputFrameWidth;
	private JFormattedTextField		inputPptTop, inputPptBottom, inputPptLeft, inputPptRight;
	private JFormattedTextField		inputAwWidth, inputAwHeight;
	
	private JTabbedPane 			tabs;
	private JButton					okBtn, cancelBtn;
	
	private int						awWidth, prevAwWidth, awHeight, prevAwHeight;
	private int						frameWidth, prevFrameWidth, frameHeight, prevFrameHeight;
	private int[]					pptMeasures, prevPptMeasures;
	
	SM_WallArrangementView			parent;
	SM_Artwork						artwork;
	
	public ArtworkMeasurementChooser(SM_WallArrangementView parent, SM_Artwork artwork) {
		
		this.parent = parent;
		this.artwork = artwork;
		
		this.awWidth = artwork.getArtworkSize()[0];
		this.prevAwWidth = artwork.getArtworkSize()[1];
		this.awHeight = artwork.getArtworkSize()[0];
		this.prevAwHeight = artwork.getArtworkSize()[1];
		
		this.frameWidth = artwork.getFrameMeasure()[0];
		this.prevFrameWidth = artwork.getFrameMeasure()[0];
		this.frameHeight = artwork.getFrameMeasure()[1];
		this.prevFrameHeight = artwork.getFrameMeasure()[1];
		
		this.pptMeasures = artwork.getPassepartoutMeasure();
		this.prevPptMeasures = artwork.getPassepartoutMeasure();
		
		
		
		this.setLayout( new BorderLayout(5, 10));
		this.setTitle(Lang.editArtwork);
		
		tabs = new JTabbedPane();
		
		tabs.addTab(Lang.frame, createFrameTab());
		tabs.addTab(Lang.passepartout, createPptTab());
		tabs.addTab(Lang.artwork, createArtworkTab());
		
		this.add(tabs, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		cancelBtn = new JButton(Lang.cancel);
		cancelBtn.addActionListener(this);
		buttonPanel.add(cancelBtn);
		
		okBtn = new JButton(Lang.ok);
		okBtn.addActionListener(this);
		buttonPanel.add(okBtn);
		
		this.add(buttonPanel, BorderLayout.PAGE_END);
		
	}
	
	private JPanel createFrameTab() {
		
		JPanel fp = new JPanel();
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumIntegerDigits(1);
//		format.setMaximumIntegerDigits(3);
		format.setMaximumFractionDigits(0);
		
		
		inputFrameHeight = new JFormattedTextField(format);
		inputFrameHeight.setColumns(3);
		inputFrameHeight.setValue(frameHeight);
		inputFrameWidth  = new JFormattedTextField(format);
		inputFrameWidth.setColumns(3);
		inputFrameWidth.setValue(frameWidth);
		
		JLabel labelWidth = new JLabel(Lang.width);
		JLabel labelHeight = new JLabel(Lang.height);
		
		fp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 0, 0, 5);
		
		c.gridx = 0;
		c.gridy = 0;
		fp.add(labelHeight, c);
		
		c.gridx = 1;
		fp.add(inputFrameHeight, c);
		
		c.gridx = 2;
		fp.add(new JLabel(Lang.prevValue + prevFrameHeight), c);
		
		c.gridx = 0;
		c.gridy = 1;
		fp.add(labelWidth, c);
		
		c.gridx = 1;
		fp.add(inputFrameWidth, c);
		
		c.gridx = 2;
		fp.add(new JLabel(Lang.prevValue + prevFrameWidth), c);
		
		
		return fp;
	}

	private JPanel createPptTab() {

		JPanel pp = new JPanel();
		
		
		

		
		return pp;
	}

	private JPanel createArtworkTab() {

		JPanel ap = new JPanel();

		
		return ap;
	}

	private void approveAction() {
		parent.artworkMeasurementCallback();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == cancelBtn) this.setVisible(false);
		if(e.getSource() == okBtn) approveAction();
	}
	
}
















