package smlfr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.Transferable;
//import java.awt.datatransfer.UnsupportedFlavorException;
//import java.awt.dnd.DnDConstants;
//import java.awt.dnd.DragGestureEvent;
//import java.awt.dnd.DragGestureListener;
//import java.awt.dnd.DragSource;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import artworkUpdateModel.ArtworkUpdateEvent;
import artworkUpdateModel.ArtworkUpdateListener;

import SMUtils.AWPanel;
import SMUtils.Lang;
import SMUtils.awFileSize;


public class SM_Library extends JFrame implements ArtworkUpdateListener, ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -885702442115456206L;
	
	
	private HashMap<String, SM_Artwork> 	artworks;
	private SM_FileManager 					fm;
	
	private HashMap<String, AWPanel>		panels;
	
	private Color							standartColor;
	
	private int labelX = 120;
	private int labelY = 120;
	private Font font = new Font(null, Font.PLAIN, 10);
	
	private JPanel artworksPanel;
	private JScrollPane scrollPanel;
	
	private JComboBox sort;
	private JButton importBtn;
	private JButton deleteBtn;
	
	public SM_Library (SM_FileManager _fm, HashMap<String, SM_Artwork> _artworks ) {
		
		artworks = _artworks;
		fm = _fm;
		standartColor = new Color(0.96f,0.96f,0.96f);
		

	}
	
	
	public final void initUI() {

        JPanel panel = new JPanel(new BorderLayout());
        JPanel controlPanel = new JPanel();

        controlPanel.setBackground(new Color(0.96f,0.96f,0.96f));
        controlPanel.setPreferredSize(new Dimension(250, 35));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        
        sort = new JComboBox( Lang.sortOptions.values() );
        sort.setSelectedIndex(1);
        sort.setFocusable(false);
        sort.addActionListener(this);
        JLabel sortTxt = new JLabel(Lang.sortLibBy);
        
        importBtn = new JButton(Lang.importBtn);
        importBtn.setFocusable(true);
        importBtn.addActionListener(this);
        
        deleteBtn = new JButton(Lang.deleteBtn);
        deleteBtn.setFocusable(false);
             
        controlPanel.add(importBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(100, 0)));
        controlPanel.add(sortTxt);
        controlPanel.add(sort);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        
        ////////
        
        panels = new HashMap<String, AWPanel>();
        artworksPanel = createArtworksPanel(artworks);
      
        scrollPanel = new JScrollPane(artworksPanel);
        scrollPanel.setBackground(Color.white);
        scrollPanel.setBorder(new EmptyBorder(new Insets(10, 0, 0, 0)));
        scrollPanel.setPreferredSize(this.getSize());
        
        int r = (int)Math.floor((float)this.getSize().width / (float)labelX);
        
        int contentHeight = (int)(Math.ceil((panels.size() / r)) * (labelY))+labelY;
        

        artworksPanel.setPreferredSize(new Dimension(scrollPanel.getWidth(), contentHeight));
        panel.add(scrollPanel, BorderLayout.CENTER);
        
                
        add(panel);

        pack();

        setTitle("Library");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	private JPanel createArtworksPanel(HashMap<String, SM_Artwork> _awks) {
		
		JPanel awp = new JPanel();
		awp.setLayout(new FlowLayout(FlowLayout.LEFT));
		awp.setBackground(Color.white);
		
		for( String s : _awks.keySet() ) {
			
			createSingleAWPanel(s, awp);
		}
		
		return awp;
	}
	
	private void createSingleAWPanel(String awName, JPanel artworksPanel) {
		
		ImageIcon icn = new ImageIcon( fm.getFilePathForArtwork(awName, awFileSize.THUMB ).getAbsolutePath());

		JLabel imgLbl = new JLabel(icn);
		imgLbl.setMinimumSize(new Dimension(50,50));
		
		JLabel ntxt = new JLabel(awName);
		ntxt.setFont(font);
		
		String artist = artworks.get(awName).getArtis();
		JLabel atxt = new JLabel("<html><p>"+artist+"</p></html>");
		atxt.setFont(font);
		atxt.setBackground(Color.gray);
		atxt.repaint();
		
		String title = artworks.get(awName).getTitle();
		JLabel ttxt = new JLabel("<html><p>"+title+"</p></html>");
		ttxt.setFont(font);
		ttxt.setBackground(Color.gray);
		
		String size = artworks.get(awName).getTotalWidth()+" x "+artworks.get(awName).getTotalHeight();
		JLabel stxt = new JLabel(size);
		stxt.setFont(font);
		
		AWPanel panel = new AWPanel(artworks.get(awName));
		panel.setLayout(new FlowLayout( FlowLayout.LEFT, 2,2 ));
		if(artworks.get(awName).getWall() != null ) {
			panel.setBackground(Color.LIGHT_GRAY);
			panel.setToolTipText();
		} else {
			panel.setBackground(standartColor);
		}
		panel.setPreferredSize(new Dimension(labelX,labelY));
		panel.add(imgLbl);
		panel.add(ntxt);
		panel.add(atxt);
		panel.add(ttxt);
		panel.add(stxt);
		
		
		artworksPanel.add(panel);
		panels.put(awName,panel);
	}

	private void sortAWPanels() {
						
		
		AWPanel[] sortPans = new AWPanel[panels.size()];
		String[]  values = new String[panels.size()];
		int x = 0;
		for( AWPanel p : panels.values()) {
			sortPans[x] = p;
			values[x] = ""+p.getSizeSquared();
			x++;
		}
				
		boolean change = true;
		while( change ) {
			change = false;
			
			for (int i = 0; i < sortPans.length-1; i++) {
				
				AWPanel pan1 = sortPans[i];
				AWPanel pan2 = sortPans[i+1];
				
				boolean changeThis = comparePanels(pan1, pan2, (Lang.sortOptions)sort.getSelectedItem());
				
				if(changeThis) {
					change = true;
					AWPanel t = sortPans[i+1];
					sortPans[i+1] = sortPans[i];
					sortPans[i] = t;
				}
			}
		}
		
//		for (int i = 0; i < sortPans.length; i++) {
//			artworksPanel.remove(sortPans[i]);
//			artworksPanel.repaint();
//		}
		artworksPanel.removeAll();
		artworksPanel.validate();
		for (int i = 0; i< sortPans.length; i++) {
			artworksPanel.add(sortPans[i]);
			values[i] = ""+sortPans[i].getSizeSquared();
		}
		artworksPanel.validate();
		artworksPanel.repaint();
	}
	
	private boolean comparePanels(AWPanel pan1, AWPanel pan2, Lang.sortOptions sortBy) {
		
		// returns true, if pan2 should be the lower index
		
		int d = 0;
		
		switch (sortBy) {
		case KŸnstler :
			d = pan1.getArtist().compareToIgnoreCase(pan2.getArtist());
			break;

		case Titel:
			d = pan1.getTitle().compareToIgnoreCase(pan2.getTitle());
			break;
			
		case InvNr:
			d = pan1.getInvNr().compareToIgnoreCase(pan2.getInvNr());
			break;
			
		case Grš§e:
			if( pan1.getSizeSquared() > pan2.getSizeSquared()) d = 1;
			else d = -1;
			break;
		}
			
		
		if( d > 0 ) return true;
		else return false;

	}
	
	@Override
	public void artworkUpdate(ArtworkUpdateEvent e) {
		
		switch (e.getType()) {
		case WALL:
			
			if(artworks.get(e.getName()).getWall() == null) {
				panels.get(e.getName()).setBackground(standartColor);
				panels.get(e.getName()).setToolTipText();
			}
			else {
				panels.get(e.getName()).setBackground(Color.lightGray);
				panels.get(e.getName()).setToolTipText();
			}
			
			break;
		default:
			break;
		}
		
	}

	public void setSaveDirtyMark( boolean _sd) {
		if(_sd) setTitle("Library (unsaved changes)");
		else setTitle("Library");
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		
		if(e.getActionCommand().equalsIgnoreCase(Lang.importBtn)) {
			
			fm.importArtworks();	
		}
		if (e.getSource() == sort) {
			sortAWPanels();
		}
	}

	

	
	
}
