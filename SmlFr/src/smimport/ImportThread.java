package smimport;

import java.io.File;

import smlfr.SM_WindowManager;

public class ImportThread extends Thread {

	// data
	
	private SM_Import 				parent;
	private File					artLibrarySaveLocation;
	private SM_SingleImportDialog 	dialog;
	private SM_WindowManager		manager;
	
	private boolean collection, intoExistingProject;
	
	String[] importedArtworks;
	
	// meta
	
	private boolean		waitForResults = false;
	private boolean		waitForProject = false;
	private boolean 	singleImport = false;
	private boolean 	batchImport = false;
	private boolean 	isPrepared = true;
	
	public ImportThread(String name) {
		super(name);
	}
	
	public void prepareSingleImport(SM_Import parent, File artLibrarySaveLocation, SM_SingleImportDialog dialog) {
		this.parent = parent;
		singleImport = true;
		batchImport = false;
		this.artLibrarySaveLocation = artLibrarySaveLocation;
		this.dialog = dialog;
		this.parent = parent;
		isPrepared = true;
	}
	
	public void prepareBatchImport(SM_Import parent, File _artLibSaveLocation, boolean _collection, boolean intoExistingProject) {
		this.parent = parent;
		singleImport = false;
		batchImport = true;
		this.artLibrarySaveLocation = _artLibSaveLocation;
		importedArtworks = null;
		
		this.collection = _collection;
		this.intoExistingProject = intoExistingProject;
		
		isPrepared = true;
	}
	
	/**
	 * Tells the Thread it should wait with cleanup until the getImportedArtworks() function had been called. (sets hasResults() to false again)
	 */
	public void setWaitForResults() {
		
		waitForResults = true;
	}
	
	public void setWaitForProject(SM_WindowManager manager) {
		this.manager = manager;
		waitForProject = true;
	}
	
	public boolean hasResults() {
		
		if( importedArtworks == null ) return false;
		else return true;
	}
	
	public String[] getImportedArtworks() {
		
		String[] ret = importedArtworks;
		importedArtworks = null;
		return ret;
	}
	
	@Override
	public void run() {
		if( !isPrepared ) return;
		
		if( waitForProject ) {
			while( manager.getState() != SMUtils.progState.PROJECT ) {
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			intoExistingProject = true;
		}
		
		
		if( singleImport ) {
			
			parent.singleImport(artLibrarySaveLocation, dialog);
		}
		else if( batchImport ) {
			
			parent.batchImport(artLibrarySaveLocation, collection, intoExistingProject);
		}
		
		
		// wait for caller to pick up Results then perform cleanup
		if( waitForResults) {
			while( hasResults() ) {
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					System.err.println("THIS EXCEPTION GOT CAUGHT: ");
					e.printStackTrace();
				}
			}
		}
		
		// cleanup
		waitForProject = false;
		isPrepared = false;
		singleImport = false;
		batchImport = false;
		parent = null;
		dialog = null;
		artLibrarySaveLocation = null;
		manager = null;
	}


}
