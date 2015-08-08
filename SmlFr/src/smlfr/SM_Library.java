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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import updateModel.ArtworkUpdateRequestEvent;
import updateModel.UpdateEvent;
import updateModel.UpdateListener;
import updateModel.WallUpdateRequestEvent;


import SMUtils.AWPanel;
import SMUtils.ArtworkMeasurementParent;
import SMUtils.Lang;
import SMUtils.MeasureMenuItem;
import SMUtils.awFileSize;
import SMUtils.ArtworkMeasurementChooser;

public class SM_Library extends JFrame implements UpdateListener, ActionListener, ArtworkMeasurementParent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -885702442115456206L;
	
	
	private HashMap<String, SM_Artwork> 	artworks;
	private SM_FileManager 					fm;
	
	private HashMap<String, AWPanel>		panels;
	private int								ctrlHeight = 32;
	
	private Color							standartColor;
	
	private int 		labelX = 120;
	private int 		labelY = 120;
	private Font 		font = new Font(null, Font.PLAIN, 10);
	
	private JPanel 		artworksPanel;
	private JScrollPane scrollPanel;
	
	private JComboBox 	sort;
	private JButton 	importBtn;
	private JButton 	deleteBtn;
	
	public SM_Library (SM_FileManager _fm, HashMap<String, SM_Artwork> _artworks ) {
		
		artworks = _artworks;
		fm = _fm;
		standartColor = new Color(0.96f,0.96f,0.96f);
		

	}
	
	
	public void setSize(Dimension d) {
		if( panels == null ) super.setSize(d.width, d.height - ctrlHeight);
		else super.setSize(d.width, d.height);
		
	}
	
	public final void initUI() {

        JPanel panel = new JPanel(new BorderLayout());
        JPanel controlPanel = new JPanel();

        controlPanel.setBackground(new Color(0.96f,0.96f,0.96f));
        controlPanel.setPreferredSize(new Dimension(250, ctrlHeight));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        
        sort = new JComboBox( Lang.sortOptions.values() );
        Dimension sortMax = new Dimension(150,50);
        sort.setMaximumSize(sortMax);
        sort.setSelectedIndex(1);
        sort.setFocusable(false);
        sort.addActionListener(this);
        JLabel sortTxt = new JLabel(Lang.sortLibBy);
        
        importBtn = new JButton(Lang.importBtn);
        importBtn.setFocusable(false);
        importBtn.addActionListener(this);
        
        deleteBtn = new JButton(Lang.deleteBtn);
        deleteBtn.setFocusable(false);
        deleteBtn.addActionListener(this);
             
        controlPanel.add(importBtn);
        controlPanel.add(deleteBtn);
        
        Dimension minSize = new Dimension(5, 5);
        Dimension prefSize = new Dimension(5, 5);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 5);
        controlPanel.add(new Box.Filler(minSize, prefSize, maxSize));
        
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

        setTitle( fm.getProjectName() + "  -  Library");
        
        boolean sd = fm.isSaveDirty();
        setSaveDirtyMark(sd);
        
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		ImageIcon icn = new ImageIcon( fm.getImageFilePathForArtwork(awName, awFileSize.THUMB ).getAbsolutePath());

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
		
		AWPanel panel = new AWPanel(this, artworks.get(awName));
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
		int x = 0;
		for( AWPanel p : panels.values()) {
			sortPans[x] = p;
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
		
		artworksPanel.removeAll();
		artworksPanel.validate();
		for (int i = 0; i< sortPans.length; i++) {
			artworksPanel.add(sortPans[i]);
		}
		artworksPanel.validate();
		artworksPanel.repaint();
	}
	
	private boolean comparePanels(AWPanel pan1, AWPanel pan2, Lang.sortOptions sortBy) {
		
		// returns true, if pan2 should be the lower index
		
		int d = 0;
		
		switch (sortBy) {
		case Künstler :
			d = pan1.getArtist().compareToIgnoreCase(pan2.getArtist());
			break;

		case Titel:
			d = pan1.getTitle().compareToIgnoreCase(pan2.getTitle());
			break;
			
		case InvNr:
			d = pan1.getInvNr().compareToIgnoreCase(pan2.getInvNr());
			break;
			
		case Größe:
			if( pan1.getSizeSquared() > pan2.getSizeSquared()) d = 1;
			else d = -1;
			break;
		}
			
		
		if( d > 0 ) return true;
		else return false;

	}
	
	@Override
	public void doUpdate(UpdateEvent e) {
		
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
		if(_sd) setTitle("Library:   " + fm.getProjectName() + "   ("+ Lang.unsavedChangesTag +")");
		else setTitle("Library:   " + fm.getProjectName());
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equalsIgnoreCase(Lang.importBtn)) {
				
			fm.requestImport();
		}
		if (e.getSource() == sort) {
			sortAWPanels();
		}
		if( e.getSource() == deleteBtn) {
			if( getSelectedArtworks().length >0) {
				deleteArtworks();
			}
		}
		if( e.getSource().getClass() == SMUtils.MeasureMenuItem.class) {
			
			if( e.getActionCommand() == Lang.editMeasurements) {
									
				MeasureMenuItem i = (MeasureMenuItem)e.getSource();			
				SMUtils.ArtworkMeasurementChooser awc = new SMUtils.ArtworkMeasurementChooser(this, i.getArtwork());
				
			} else if( e.getActionCommand() == Lang.RemoveArtwork) {
				
				SM_Artwork a = ((MeasureMenuItem)e.getSource()).getArtwork();			
				
				WallUpdateRequestEvent r = new WallUpdateRequestEvent(this, a.getName(), ' ', "Library", a.getRoom(), a.getWallChar());

				fm.updateRequested(r);
			}
		}
	}

	private void deleteArtworks() {
		
		SM_Artwork[] aws = getSelectedArtworks();
		String message = Lang.deleteMessage_1;
		for( SM_Artwork a : aws){
			message += "\n- "+a.getTitle();
		}
		message += Lang.deleteMessage_2;
		
		int decide = JOptionPane.showConfirmDialog(this, message, Lang.deleteTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if( decide == 0) {
			
			for(SM_Artwork aw : aws) {
				;
				
				// Bild abhängen, falls an Wand
				
				if( aw.getWall() != null) {
					
					String wallInfo = aw.getWall();
					String roomName = wallInfo.substring(0, wallInfo.lastIndexOf('_'));
					roomName = roomName.substring(roomName.lastIndexOf('_')+1);
									
					WallUpdateRequestEvent e = new WallUpdateRequestEvent(this, aw.getName(), ' ', "Library", roomName, aw.getWallChar());
					fm.updateRequested(e);
				}
				
				// File manager removes the rest (from programm lists and Files on drive)
				
				fm.deleteArtwork(aw);
								
				
				artworksPanel.remove(panels.get(aw.getName()));
		
				panels.remove(aw.getName());
				
			}
		}
		
        this.validate();
        this.repaint();

		
	}
	
	public SM_Artwork[] getSelectedArtworks() {
		
		ArrayList<SM_Artwork> selectedArtworks = new ArrayList<SM_Artwork>();

		for(AWPanel pan : panels.values()) {
			if( pan.isSelected() ) selectedArtworks.add(pan.getArtwork());
		}
		
		SM_Artwork[] returnArray = selectedArtworks.toArray(new SM_Artwork[selectedArtworks.size()]);
		
		return returnArray;
	}

	public void updateArtworksMap(HashMap<String, SM_Artwork> _artworks) {
		artworks = _artworks;
	}


	
	@Override
	public void artworkMeasurementCallback(LinkedHashMap<String, Object> data) {

		ArtworkUpdateRequestEvent e = new ArtworkUpdateRequestEvent(this, false, -1, data);
		fm.updateRequested(e);
	}
	
}
