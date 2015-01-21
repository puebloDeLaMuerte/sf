package SMUtils;

public class Lang {
	
	public enum langs {DE};
	
	// errors: File Loading
	public static String	warning = "Warnung!";
	public static String 	couldntFindResourceFolder = "Der Ordner \"resources\" konnte nicht gefunden werden.\nStellen Sie sicher, dass er sich im selben Verzeichnis mit SimuFöhr.exe befindet.";
	public static String 	couldntFindPrefs = "Die Voreinstellungs-Datei konnte nicht gefunden werden! \nBitte überprüfen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadPrefs = "Die Voreinstellungs-Datei konnte nicht geladen werden.\nMöglicherweise ist die datei beschädigt.";
	public static String 	couldntFindMuseum = "Die Museums-Datei konnte nicht gefunden werden! \nBitte überprüfen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadMuseumData = "Die Museumsdaten konnten nicht geladen werden.\nDie Datei scheint beschädigt zu sein.";
	public static String	couldntLoadProject_1 = "Das Projekt konnte nicht geladen werden.\nStellen Sie sicher das sich die Datei ";
	public static String	couldntLoadProject_2 = " in diesem Ordner befindet: \n";
	public static String	couldntLoadArtwork = "Die Library-Dateien konnten nicht geladen werden.\nStellen Sie sicher, dass sich der Ordner mit dem\nSuffix \"_lib\" im selben Ordner wie die Projektadtei befindet.";
	public static String	couldntImport_1 = "Diese Kunstwerke konnten leider nicht importiert werden:\n\n";
	public static String	couldntImport_2 = "\nÜberprüfen Sie die Excel-Tabelle auf fehlerhafte Einträge" +
			"\nund stellen Sie sicher, dass sich die angegebenen" +
			"\nBildateien im selben Ordner mit der Tabelle befinden." +
			"\n\nAlle übrigen Kunstwerke wurden erfolgreich importiert!";
	public static String	successfulImport = " Kunstwerke wurden erfolgreich Importiert.";
	
	// errors: Programm Handling
	
	public static String	artworkTooBigForWall_1  = "Dieses Kunstwerk passt nicht an diese Wand.\n	Breite des Kunstwerks: ";
	public static String	artworkTooBigForWall_2  = " mm\n	Breite der Wand; ";
	public static String	artworkTooBigForWall_3  = " mm.";
	public static String	wall = "Wand";
	
	// questions
	public static String 	initializeFromWhereTitle = "Willkommen zu SimuFöhr!";
	public static String 	initializeFromWhere_1 = "Zuletzt geöffnetes Projekt:\n\"";
	public static String 	initializeFromWhere_2 = "\"\n\nMöchten Sie dieses Projekt öffnen, ein neues erstellen,\noder ein anderes Projekt laden?";
	public static String	loadProjectTitle = "Projekt laden...";
	public static String 	unsavedChangesTitle = "Änderungen speichern?";
	public static Object 	unsavedChanges = "Es wurden ungesicherte Änderungen vorgenommen,\nwollen Sie das Projekt jetzt speichern?";
	public static String[]	yesNoCancelOptions = new String[] { "Abbrechen", "Nein", "Ja" };
	public static String[]  importNowBtns = new String[] { "später", "Ja" };
	public static String 	importNow = "Wollen Sie jetzt bereits Kunstwerke importieren,\noder ein leeres Projekt erstellen?";
	public static Object 	importNowTitle = "Kunstwerke importieren?";
	public static String	importPleaseWait = "Importiere Kunstwerke ...\nBitte haben Sie etwas Geduld.";
	public static Object 	newProjectName = "Projektname:";
	public static String 	newProjectNameTitle = "Neues Projekt...";
	public static Object[]	newProjectOptions = new String[] {"abbrechen", "erstellen"};
	public static String	create = "erstellen";
	public static String	newProjectLocation_1 = "Wohin möchten Sie ";
	public static String	newProjectLocation_2 = ".sfp speichern?";
	public static String 	wantToSaveBeforExitTitle = "Projekt Speichern?";
	public static String 	wantToSaveBeforExit = "Es bestehen ungespeicherte Änderungen,\nwollen Sie das Projekt sichern?";
	public static Object[] 	saveOnExitOptions = new String[] {"Beenden", "Sichern"};
	public static String	quitSF = "SimuFöhr beenden";
	public static String	saveProject = "Projekt sichern";
	public static String	saved = "Projekt erfolgreich gespeichert.";
	
	// Control Elements
	public static String[]	initializeFromWhereButtons = new String[] { "Neues Projekt", "Projekt laden...", "letztes öffnen" };
	public static String	SM_filetypes = "Nur SimuFöhr Dateien";
	public static enum 		sortOptions { Künstler, Titel, InvNr, Größe };
	public static String	sortLibBy = "sortieren nach:";
	public static String	importBtn = "Importieren";
	public static String	deleteBtn = "Löschen";
	public static String 	RemoveArtwork = "von hier entfernen";
	public static String	changeFrameStyle = "Rahmen ändern";
	public static String	snapToMidHeight = "auf Mittelhöhe setzen";
	public static String 	enterRoom = "Raum betreten";
	public static String 	exitRoom = "Raum verlassen";
	public static String	closeWall = "Wand schließen";
	public static String 	selectView = "Wählen Sie eine Ansicht:";
	
	// Export
	
	public static String	exportMenu 			= "Maße exportieren...";
	public static String	measureSheetTitle 	= "Projekt:";
	public static String	exportRoomName 		= "Raum:";
	public static String	exportWallName 		= "Wand:";
	public static String	exportWallMeasures 	= "Wandmaß (mm):";
	public static String	exportDate			= "Stand:";
	public static String	exportListTitle	    = "Hängung-Listenansicht: (horizontal x vertikal in mm)";
	public static String	exportSuccess_1		= "Die Vermaßungsdatei wurde erfolgreich exportiert.\n\nSie befindet sich in Projektordner unter\n";
	public static String	exportSuccess_2		= "";
	
	}
