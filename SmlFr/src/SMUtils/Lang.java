package SMUtils;

public class Lang {
	
	public enum langs {DE};
	
	// misc
	
	public static String	wall = "Wand";
	public static String	room = "Saal";
	
	// errors: File Loading
	public static String	warning = "Warnung!";
	public static String 	couldntFindResourceFolder = "Der Ordner \"resources\" konnte nicht gefunden werden.\nStellen Sie sicher, dass er sich im selben Verzeichnis mit SimuF�hr.exe befindet.";
	public static String 	couldntFindPrefs = "Die Voreinstellungs-Datei konnte nicht gefunden werden! \nBitte �berpr�fen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadPrefs = "Die Voreinstellungs-Datei konnte nicht geladen werden.\nM�glicherweise ist die datei besch�digt.";
	public static String 	couldntFindMuseum = "Die Museums-Datei konnte nicht gefunden werden! \nBitte �berpr�fen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadMuseumData = "Die Museumsdaten konnten nicht geladen werden.\nDie Datei scheint besch�digt zu sein.";
	public static String	couldntLoadProject_1 = "Das Projekt konnte nicht geladen werden.\nStellen Sie sicher dass sich die Datei ";
	public static String	couldntLoadProject_2 = " in diesem Ordner befindet: \n";
	public static String	couldntLoadArtwork = "Die Library-Dateien konnten nicht geladen werden.\nStellen Sie sicher, dass sich der Ordner mit dem\nSuffix \"_lib\" im selben Ordner wie die Projektadtei befindet.";
	public static String	couldntImport_1 = "Diese Kunstwerke konnten leider nicht importiert werden:\n\n";
	public static String	couldntImport_2 = "\n�berpr�fen Sie die Excel-Tabelle auf fehlerhafte Eintr�ge" +
												"\nund stellen Sie sicher, dass sich die angegebenen" +
												"\nBildateien im selben Ordner mit der Tabelle befinden." +
												"\n\nAlle �brigen Kunstwerke wurden erfolgreich importiert!";
	public static String	prevVersion_WallColor = "Diese Projektdatei wurde von einer fr�heren SimuF�hr Version erstellt. (v0.1.11 oder fr�her)" +
													"In dieser Version ist es m�glich, die Wandfarbe zu editieren." +
													"Die Wandfarbe wurde auf Wei� (255,255,255) gesetzt. Sie k�nnen diese nun �ndern.";
	public static String	errorLoadingWallColor_1 = "Die Wandfarbe f�r \"";
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
	public static String	importPleaseWait = "\n  Importiere Kunstwerke ...\n  Bitte haben Sie etwas Geduld.";
	public static Object 	newProjectName = "Projektname:";
	public static String 	newProjectNameTitle = "Neues Projekt...";
	public static Object[]	newProjectOptions = new String[] {"abbrechen", "erstellen"};
	public static String	create = "erstellen";
	public static String	newProjectLocation_1 = "Wohin m�chten Sie ";
	public static String	newProjectLocation_2 = ".sfp speichern?";
	public static String 	wantToSaveBeforExitTitle = "SimuF�hr beenden?";
	public static String 	wantToSaveBeforExit = "Wollen Sie SimuF�hr wirklich beenden?\nUngespeicherte �nderungen gehen dabei verloren.";
	public static Object[] 	saveOnExitOptions = new String[] {"Abbrechen", "Beenden", "Sichern"};
	public static String	quitSF = "SimuF�hr beenden";
	public static String	saveProject = "Projekt sichern";
	public static String	saved = "Projekt erfolgreich gespeichert.";
	public static String	whereIsExcelFile = "Wo befindet sich die Excel-Tabelle?";
	public static String	restoreProjectTitle = "Projekt Wiederherstellen?";
	public static String	restoreProjectMessage = "\n<html><body style='width: 300px'><b>Es wurde eine Wiederherstellungsdatei f�r dieses Projekt gefunden!</b><br><br>Wurde SimuF�hr beim letzten mal unvorhergesehen beendet? Falls dies der Fall ist, haben Sie jetzt die M�glichkeit, unngesicherte Speicherst�nde der letzten Sitzung wiederherzustellen.<br><br><b>HINWEIS!</b><br><i>Bitte beenden Sie SimuF�hr immer �ber den Men�punkt <b>\"Simuf�hr beenden...\"</b> um diese Meldung in Zukunft zu vermeiden.</i>";
	public static Object[]	restoreProjectOptions = new String[] {"Wiederherstellen", "Nein, danke!"};
	
	// Control Elements
	public static String[]	initializeFromWhereButtons = new String[] { "Neues Projekt", "Projekt laden...", "letztes �ffnen" };
	public static String	SM_filetypes = "Nur SimuF�hr Dateien";
	
	public static enum 		/* REFACTOR THIS FOR CHANGES!!!*/ sortOptions { K�nstler, Titel, InvNr, Gr��e }; // REFACTOR!!!!!
	public static String	ok = "Ok";
	public static String	cancel = "Abrechen";
	public static String	sortLibBy = "sortieren nach:";
	public static String	importBtn = "Importieren";
	public static String	deleteBtn = "L�schen";
	public static String 	RemoveArtwork = "von hier entfernen";
	public static String	changeColor = "Wandfarbe �ndern...";
	public static String	changeColorTitle = "Wandfarbe �ndern";
	public static String	colorPicker = "Farbw�hler...";
	public static String	preview = "Vorschau";
	public static String	changeColorMessage = "Geben Sie den RGB Wert (0-255) f�r die Wandfarbe ein:";
	public static String	changeRoomColor = "Farbe f�r den gesamten Raum �ndern";
	public static String	changeSingleWallColor_1 = "Nur Farbe f�r Wand ";
	public static String	changeSingleWallColor_2 = " �ndern";
	public static String	changeColorInvalidValues = "Die eingegebenen Werte sind ung�ltig!\nDie Eingabe darf nur aus Ziffern bestehen.";
	public static String	snapToMidHeight = "auf Mittelh�he setzen";
	public static String 	enterRoom = "Raum betreten";
	public static String 	exitRoom = "Raum verlassen";
	public static String	closeWall = "Wand schlie�en";
	public static String 	selectView = "W�hlen Sie eine Ansicht:";
	
	// EditArtwork
	
	public static String	editArtwork = "Kunstwerk editieren...";
	public static String	editMeasurements = "Ma�e �ndern...";
	public static String	frame = "Rahmen";
	public static String	passepartout = "Passepartout";
	public static String	artwork	= "Kunstwerk";
	public static String	width = "Breite (mm):";
	public static String	height = "H�he (mm):";
	public static String	prevValue = "urspr�nglicher Wert: ";

	// Import
	
	public static String	importTitle = "Kunstwerk importieren";
	public static String	invNr = "Inventarnummer:";
	public static String	title = "Titel:";
	public static String	artist = "K�nstler:";
	public static String	frameMeasure  = "Rahmenma�:";
	public static String	artworkMeasure = "Bildgr��e:";
	public static String	pptMeasure = "Passepartoutma�:";
	public static String	imageFile = "Bilddatei:";
	public static String	browse = "durchsuchen...";
	
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
	
	// Errors
	
	public static String	err_loadImageFile = "Bilddatei konnte nicht geladen werden";
	public static String	err_InvNrAlreadyExists = "Es existiert bereits ein Kunstwerk mit dieser Inventarnummer";
	public static String	err_noImageSize = "Es wurden keine Ma�e in der Tabelle gefunden";
	
	}
