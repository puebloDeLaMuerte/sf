package SMUtils;

public class Lang {
	
	public enum langs {DE};
	
	// errors: File Loading
	public static String	warning = "Warnung!";
	public static String 	couldntFindResourceFolder = "Der Ordner \"resources\" konnte nicht gefunden werden.\nStellen Sie sicher, dass er sich im selben Verzeichnis mit SimuF�hr.exe befindet.";
	public static String 	couldntFindPrefs = "Die Voreinstellungs-Datei konnte nicht gefunden werden! \nBitte �berpr�fen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadPrefs = "Die Voreinstellungs-Datei konnte nicht geladen werden.\nM�glicherweise ist die datei besch�digt.";
	public static String 	couldntFindMuseum = "Die Museums-Datei konnte nicht gefunden werden! \nBitte �berpr�fen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadMuseumData = "Die Museumsdaten konnten nicht geladen werden.\nDie Datei scheint besch�digt zu sein.";
	public static String	couldntLoadProject_1 = "Das Projekt konnte nicht geladen werden.\nStellen Sie sicher das sich die Datei ";
	public static String	couldntLoadProject_2 = " in diesem Ordner befindet: \n";
	public static String	couldntLoadArtwork = "Die Library-Dateien konnten nicht geladen werden.\nStellen Sie sicher, dass sich der Ordner mit dem\nSuffix \"_lib\" im selben Ordner wie die Projektadtei befindet.";
	public static String	couldntImport_1 = "Diese Kunstwerke konnten leider nicht importiert werden:\n\n";
	public static String	couldntImport_2 = "\n�berpr�fen Sie die Excel-Tabelle auf fehlerhafte Eintr�ge" +
			"\nund stellen Sie sicher, dass sich die angegebenen" +
			"\nBildateien im selben Ordner mit der Tabelle befinden." +
			"\n\nAlle �brigen Kunstwerke wurden erfolgreich importiert!";
	public static String	successfulImport = " Kunstwerke wurden erfolgreich Importiert.";
	
	// errors: Programm Handling
	
	public static String	artworkTooBigForWall_1  = "Dieses Kunstwerk passt nicht an diese Wand.\n	Breite des Kunstwerks: ";
	public static String	artworkTooBigForWall_2  = " mm\n	Breite der Wand; ";
	public static String	artworkTooBigForWall_3  = " mm.";
	public static String	wall = "Wand";
	
	// questions
	public static String 	initializeFromWhereTitle = "Willkommen zu SimuF�hr!";
	public static String 	initializeFromWhere_1 = "Zuletzt ge�ffnetes Projekt:\n\"";
	public static String 	initializeFromWhere_2 = "\"\n\nM�chten Sie dieses Projekt �ffnen, ein neues erstellen,\noder ein anderes Projekt laden?";
	public static String	loadProjectTitle = "Projekt laden...";
	public static String 	unsavedChangesTitle = "�nderungen speichern?";
	public static Object 	unsavedChanges = "Es wurden ungesicherte �nderungen vorgenommen,\nwollen Sie das Projekt jetzt speichern?";
	public static String[]	yesNoCancelOptions = new String[] { "Abbrechen", "Nein", "Ja" };
	public static String[]  importNowBtns = new String[] { "sp�ter", "Ja" };
	public static String 	importNow = "Wollen Sie jetzt bereits Kunstwerke importieren,\noder ein leeres Projekt erstellen?";
	public static Object 	importNowTitle = "Kunstwerke importieren?";
	public static String	importPleaseWait = "Importiere Kunstwerke ...\nBitte haben Sie etwas Geduld.";
	public static Object 	newProjectName = "Projektname:";
	public static String 	newProjectNameTitle = "Neues Projekt...";
	public static Object[]	newProjectOptions = new String[] {"abbrechen", "erstellen"};
	public static String	create = "erstellen";
	public static String	newProjectLocation_1 = "Wohin m�chten Sie ";
	public static String	newProjectLocation_2 = ".sfp speichern?";
	public static String 	wantToSaveBeforExitTitle = "Projekt Speichern?";
	public static String 	wantToSaveBeforExit = "Es bestehen ungespeicherte �nderungen,\nwollen Sie das Projekt sichern?";
	public static Object[] 	saveOnExitOptions = new String[] {"Beenden", "Sichern"};
	public static String	quitSF = "SimuF�hr beenden";
	public static String	saveProject = "Projekt sichern";
	public static String	saved = "Projekt erfolgreich gespeichert.";
	
	// Control Elements
	public static String[]	initializeFromWhereButtons = new String[] { "Neues Projekt", "Projekt laden...", "letztes �ffnen" };
	public static String	SM_filetypes = "Nur SimuF�hr Dateien";
	public static enum 		sortOptions { K�nstler, Titel, InvNr, Gr��e };
	public static String	sortLibBy = "sortieren nach:";
	public static String	importBtn = "Importieren";
	public static String	deleteBtn = "L�schen";
	public static String 	RemoveArtwork = "von hier entfernen";
	public static String	changeFrameStyle = "Rahmen �ndern";
	public static String	snapToMidHeight = "auf Mittelh�he setzen";
	public static String 	enterRoom = "Raum betreten";
	public static String 	exitRoom = "Raum verlassen";
	public static String	closeWall = "Wand schlie�en";
	public static String 	selectView = "W�hlen Sie eine Ansicht:";
	
	// Export
	
	public static String	exportMenu 			= "Ma�e exportieren...";
	public static String	measureSheetTitle 	= "Projekt:";
	public static String	exportRoomName 		= "Raum:";
	public static String	exportWallName 		= "Wand:";
	public static String	exportWallMeasures 	= "Wandma� (mm):";
	public static String	exportDate			= "Stand:";
	public static String	exportListTitle	    = "H�ngung-Listenansicht: (horizontal x vertikal in mm)";
	public static String	exportSuccess_1		= "Die Verma�ungsdatei wurde erfolgreich exportiert.\n\nSie befindet sich in Projektordner unter\n";
	public static String	exportSuccess_2		= "";
	
	}
