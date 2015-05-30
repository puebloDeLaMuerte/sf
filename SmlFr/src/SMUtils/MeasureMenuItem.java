package SMUtils;

import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;

import smlfr.SM_Artwork;

public class MeasureMenuItem extends JMenuItem {

	private SM_Artwork myAw;
	
	public MeasureMenuItem(String text, SM_Artwork aw) {
		super(text);
		myAw = aw;
	}

	@Override
	protected void fireActionPerformed(ActionEvent event) {
		
		event.setSource(myAw);
		
		
		
		System.out.println("the action event COMMAND is this: " + event.getActionCommand());
		System.out.println("the action event SOURCE  is this: " + event.getSource());
		System.out.println("the action event CLASS   is this: " + event.getSource().getClass());
		
		super.fireActionPerformed(event);
	}

	public SM_Artwork getArtwork() {
		return myAw;
	}
	
}