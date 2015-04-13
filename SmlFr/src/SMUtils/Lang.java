package SMUtils;

public class Lang {
	
	public enum langs {DE};
	
	// misc
	
	public static String	wall = "Wand";
	public static String	room = "Saal";
	
	// errors: File Loading
	public static String	warning = "Warnung!";
	public static String 	couldntFindResourceFolder = "Der Ordner \"resources\" konnte nicht gefunden werden.\nStellen Sie sicher, dass er sich im selben Verzeichnis mit SimuFöhr.exe befindet.";
	public static String 	couldntFindPrefs = "Die Voreinstellungs-Datei konnte nicht gefunden werden! \nBitte überprüfen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadPrefs = "Die Voreinstellungs-Datei konnte nicht geladen werden.\nMöglicherweise ist die datei beschädigt.";
	public static String 	couldntFindMuseum = "Die Museums-Datei konnte nicht gefunden werden! \nBitte überprüfen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadMuseumData = "Die Museumsdaten konnten nicht geladen werden.\nDie Datei scheint beschädigt zu sein.";
	public static String	couldntLoadProject_1 = "Das Projekt konnte nicht geladen werden.\nStellen Sie sicher dass sich die Datei ";
	public static String	couldntLoadProject_2 = " in diesem Ordner befindet: \n";
	public static String	couldntLoadArtwork = "Die Library-Dateien konnten nicht geladen werden.\nStellen Sie sicher, dass sich der Ordner mit dem\nSuffix \"_lib\" im selben Ordner wie die Projektadtei befindet.";
	public static String	couldntImport_1 = "Diese Kunstwerke konnten leider nicht importiert werden:\n\n";
	public static String	couldntImport_2 = "\nÜberprüfen Sie die Excel-Tabelle auf fehlerhafte Einträge" +
												"\nund stellen Sie sicher, dass sich die angegebenen" +
												"\nBildateien im selben Ordner mit der Tabelle befinden." +
												"\n\nAlle übrigen Kunstwerke wurden erfolgreich importiert!";
	public static String	prevVersion_WallColor = "Diese Projektdatei wurde von einer früheren SimuFöhr Version erstellt. (v0.1.11 oder früher)" +
													"In dieser Version ist es möglich, die Wandfarbe zu editieren." +
													"Die Wandfarbe wurde auf Weiß (255,255,255) gesetzt. Sie können diese nun ändern.";
	public static String	errorLoadingWallColor_1 = "Die Wandfarbe für \"";
	public static String	errorLoadingWallColor_2	=	"\" konnte nicht aus der Projektdatei gelesen werden." +
													"\nEs wird versucht, das Projekt trotzdem zu laden." +
													"\nFalls Probleme auftreten, wenden Sie sich bitte an den Support.";
	public static String	successfulImport = " Kunstwerke wurden erfolgreich Importiert.";
	
	// errors: Programm Handling
	
	public static String	artworkTooBigForWall_1  = "Dieses Kunstwerk passt nicht an diese Wand.\n	Breite des Kunstwerks: ";
	public static String	artworkTooBigForWall_2  = " mm\n	Breite der Wand; ";
	public static String	artworkTooBigForWall_3  = " mm.";
	public static String	importArtworkAlreadyExists = "Dieses Kunstwerk konnte nicht importiert werden,\n" +
														 "da sich bereits ein Kunstwerk mit derselben Inventarnummer\n" +
														 "in diesem Projekt befindet.\n\n";
	
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
	public static String	importPleaseWait = "\n  Importiere Kunstwerke ...\n  Bitte haben Sie etwas Geduld.";
	public static Object 	newProjectName = "Projektname:";
	public static String 	newProjectNameTitle = "Neues Projekt...";
	public static Object[]	newProjectOptions = new String[] {"abbrechen", "erstellen"};
	public static String	create = "erstellen";
	public static String	newProjectLocation_1 = "Wohin möchten Sie ";
	public static String	newProjectLocation_2 = ".sfp speichern?";
	public static String 	wantToSaveBeforExitTitle = "SimuFöhr beenden?";
	public static String 	wantToSaveBeforExit = "Wollen Sie SimuFöhr wirklich beenden?\nUngespeicherte Änderungen gehen dabei verloren.";
	public static Object[] 	saveOnExitOptions = new String[] {"Abbrechen", "Beenden", "Sichern"};
	public static String	quitSF = "SimuFöhr beenden";
	public static String	saveProject = "Projekt sichern";
	public static String	saved = "Projekt erfolgreich gespeichert.";
	public static String	whereIsExcelFile = "Wo befindet sich die Excel-Tabelle?";
	public static String	restoreProjectTitle = "Projekt Wiederherstellen?";
	public static String	restoreProjectMessage = "\n<html><body style='width: 300px'><b>Es wurde eine Wiederherstellungsdatei für dieses Projekt gefunden!</b><br><br>Wurde SimuFöhr beim letzten mal unvorhergesehen beendet? Falls dies der Fall ist, haben Sie jetzt die Möglichkeit, unngesicherte Speicherstände der letzten Sitzung wiederherzustellen.<br><br><b>HINWEIS!</b><br><i>Bitte beenden Sie SimuFöhr immer über den Menüpunkt <b>\"Simuföhr beenden...\"</b> um diese Meldung in Zukunft zu vermeiden.</i>";
	public static Object[]	restoreProjectOptions = new String[] {"Wiederherstellen", "Nein, danke!"};
	
	// Control Elements
	public static String[]	initializeFromWhereButtons = new String[] { "Neues Projekt", "Projekt laden...", "letztes öffnen" };
	public static String	SM_filetypes = "Nur SimuFöhr Dateien";
	
	public static enum 		/* REFACTOR THIS FOR CHANGES!!!*/ sortOptions { Künstler, Titel, InvNr, Größe }; // REFACTOR!!!!!
	public static String	ok = "Ok";
	public static String	cancel = "Abrechen";
	public static String	sortLibBy = "sortieren nach:";
	public static String	importBtn = "Importieren";
	public static String	deleteBtn = "Löschen";
	public static String 	RemoveArtwork = "von hier entfernen";
	public static String	changeColor = "Wandfarbe ändern...";
	public static String	changeColorTitle = "Wandfarbe ändern";
	public static String	colorPicker = "Farbwähler...";
	public static String	preview = "Vorschau";
	public static String	changeColorMessage = "Geben Sie den RGB Wert (0-255) für die Wandfarbe ein:";
	public static String	changeRoomColor = "Farbe für den gesamten Raum ändern";
	public static String	changeSingleWallColor_1 = "Nur Farbe für Wand ";
	public static String	changeSingleWallColor_2 = " ändern";
	public static String	changeColorInvalidValues = "Die eingegebenen Werte sind ungültig!\nDie Eingabe darf nur aus Ziffern bestehen.";
	public static String	snapToMidHeight = "auf Mittelhöhe setzen";
	public static String 	enterRoom = "Raum betreten";
	public static String 	exitRoom = "Raum verlassen";
	public static String	closeWall = "Wand schließen";
	public static String 	selectView = "Wählen Sie eine Ansicht:";
	
	// EditArtwork
	
	public static String	editArtwork = "Kunstwerk editieren...";
	public static String	editMeasurements = "Maße ändern...";
	public static String	frame = "Rahmen";
	public static String	passepartout = "Passepartout";
	public static String	artwork	= "Kunstwerk";
	public static String	width = "Breite (mm):";
	public static String	height = "Höhe (mm):";
	public static String	prevValue = "ursprünglicher Wert: ";

	// Import
	
	public static String	importTitle = "Kunstwerk importieren";
	public static String	invNr = "Inventarnummer:";
	public static String	title = "Titel:";
	public static String	artist = "Künstler:";
	public static String	frameMeasure  = "Rahmenmaß:";
	public static String	artworkMeasure = "Bildgröße:";
	public static String	pptMeasure = "Passepartoutmaß:";
	public static String	imageFile = "Bilddatei:";
	public static String	browse = "durchsuchen...";
	
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
	
	// Errors
	
	public static String	err_loadImageFile = "Bilddatei konnte nicht geladen werden";
	public static String	err_InvNrAlreadyExists = "Es existiert bereits ein Kunstwerk mit dieser Inventarnummer";
	public static String	err_noImageSize = "Es wurden keine Maße in der Tabelle gefunden";
	
	}
