package smlfr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import artworkUpdateModel.ArtworkUpdateEvent;
import artworkUpdateModel.ArtworkUpdateListener;

import SMUtils.AWPanel;
import SMUtils.Lang;
import SMUtils.awFileSize;


public class SM_Library extends JFrame implements ArtworkUpdateListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -885702442115456206L;
	
	
	private HashMap<String, SM_Artwork> 	artworks;
	private SM_FileManager 					fm;
	
	private HashMap<String, JPanel>			panels;
	
	
	
	public SM_Library (SM_FileManager _fm, HashMap<String, SM_Artwork> _artworks ) {
		
		artworks = _artworks;
		fm = _fm;
		
	}
	
	
	public final void initUI() {

        JPanel panel = new JPanel(new BorderLayout());
        JPanel controlPanel = new JPanel();

        controlPanel.setBackground(new Color(0.96f,0.96f,0.96f));
        controlPanel.setPreferredSize(new Dimension(250, 35));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        
        JComboBox sort = new JComboBox( Lang.sortOptions.values() );
        sort.setSelectedIndex(1);
        sort.setFocusable(false);
        JLabel sortTxt = new JLabel(Lang.sortLibBy);
        
        JButton imp = new JButton(Lang.importBtn);
        JButton del = new JButton(Lang.deleteBtn);

        imp.setFocusable(false);
        del.setFocusable(false);
             
        controlPanel.add(imp);
        controlPanel.add(del);
        controlPanel.add(Box.createRigidArea(new Dimension(100, 0)));
        controlPanel.add(sortTxt);
        controlPanel.add(sort);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        
        ////////
        
        panels = new HashMap<String, JPanel>();
        JPanel artworkPanel = createArtworkPanel(artworks);
      
        JScrollPane contentPanel = new JScrollPane(artworkPanel);
        artworkPanel.setPreferredSize(new Dimension(contentPanel.WIDTH, 1000));
        contentPanel.setBackground(Color.white);
        contentPanel.setBorder(new EmptyBorder(new Insets(10, 0, 0, 0)));
        contentPanel.setPreferredSize(this.getSize());
        panel.add(contentPanel, BorderLayout.CENTER);
        
        
        //panel.add(_awks, BorderLayout.CENTER);
        
        add(panel);

        pack();

        setTitle("Library");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
    }

	private JPanel createArtworkPanel(HashMap<String, SM_Artwork> _awks) {
		
		JPanel awp = new JPanel();
		awp.setLayout(new FlowLayout());
		awp.setBackground(Color.white);
		
		for( String s : _awks.keySet() ) {
			
			ImageIcon icn = new ImageIcon( fm.getFilePathForArtwork(s, awFileSize.THUMB ).getAbsolutePath());

			JLabel imgLbl = new JLabel(icn);
			JLabel label = new JLabel(s);
			AWPanel panel = new AWPanel(s);
			panel.setBackground(new Color(0.96f,0.96f,0.96f));
			panel.setPreferredSize(new Dimension(120,120));
			panel.add(imgLbl);
			panel.add(label);

			
			awp.add(panel);
			panels.put(s,panel);
//			awp.setFocusable(true);
			
			
		}
		return awp;
	}


	@Override
	public void artworkUpdate(ArtworkUpdateEvent e) {
		
		switch (e.getType()) {
		case WALL:
			panels.get(e.getName()).setBackground(Color.lightGray);
			break;
		default:
			break;
		}
		
	}

	

	
}
