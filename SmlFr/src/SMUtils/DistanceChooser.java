package SMUtils;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import smlfr.SM_Artwork;
import smlfr.SM_WallArrangementView;

public class DistanceChooser extends JFrame implements ActionListener, ChangeListener {
	
	private JSpinner 	spinner;
	private JButton		okBtn, cancelBtn;
	private JLabel		text;
	
	private LinkedHashMap<String, int[]> 	originalPositions;
	private SM_Artwork[] 					aws;
	private SM_WallArrangementView			parent;
	
	public DistanceChooser( SM_WallArrangementView parent, LinkedHashMap<String, int[]> originalPos, SM_Artwork[] aws) {
		
		// Data
		
		originalPositions = originalPos;
		this.aws = aws;
		this.parent = parent;
		
		// Layout
		
		SpinnerModel model = new SpinnerNumberModel(200, 0, 10000, 15); 
		
		
		spinner 		= new JSpinner(model);
		text			= new JLabel(Lang.distTxt);
		okBtn 			= new JButton(Lang.apply);
		cancelBtn 		= new JButton(Lang.cancel);
		
		spinner.addChangeListener(this);
		okBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		
		JPanel p = new JPanel( new BorderLayout());
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);		
		p.setBorder(padding);
		
		JPanel b = new JPanel();
		JPanel s = new JPanel();
		
		b.add(cancelBtn);
		b.add(okBtn);
		
		s.add(text);
		s.add(spinner);
		
		p.add(s, BorderLayout.CENTER);
		p.add(b, BorderLayout.PAGE_END);

		this.setContentPane(p);
		this.pack();
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		java.awt.Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setLocation((int)(window.width/2 - this.getSize().getWidth()/2), (int)(window.height/2 - this.getSize().getHeight()/2));		
		this.setVisible(true);
		
		parent.distanceCallback((Integer)spinner.getModel().getValue(), aws);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if( e.getSource() == okBtn ) {
			this.setVisible(false);
		}
		
		if( e.getSource() == cancelBtn ) {
			parent.distanceCancelCallback(aws, originalPositions);
			this.setVisible(false);
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {

		int dist = (Integer)spinner.getModel().getValue();
		
		parent.distanceCallback(dist, aws);
		
	}

}
