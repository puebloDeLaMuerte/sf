package SMUtils;


public class Lang {
	
	public enum langs {DE}


	public static final Object CollectionNotFound = null;

	
	// misc
	
	public static String	wall = "Wand";
	public static String	room = "Saal";
	public static String 	rendererBusy = "Vorschau berechnen";
	public static String	unsavedChangesTag = "ungesicherte Änderungen";
	public static String	loadingArtworks = "Lade Kunstwerke:  ";
	
	// errors: File Loading
	public static String	warning = 					"Warnung!";
	public static String 	couldntFindResourceFolder = "Der Ordner \"resources\" konnte nicht gefunden werden.\nStellen Sie sicher, dass er sich im selben Verzeichnis mit SimuFöhr.exe befindet.";
	public static String 	couldntFindPrefs = 			"Die Voreinstellungs-Datei konnte nicht gefunden werden! \nBitte überprüfen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadPrefs = 			"Die Voreinstellungs-Datei konnte nicht geladen werden.\nMöglicherweise ist die datei beschädigt.";
	public static String 	couldntFindMuseum = 		"Die Museums-Datei konnte nicht gefunden werden! \nBitte überprüfen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadMuseumData = 	"Die Museumsdaten konnten nicht geladen werden.\nDie Datei scheint beschädigt zu sein.";
	public static String	couldntLoadProject_1 = 		"Das Projekt konnte nicht geladen werden.\nStellen Sie sicher dass sich die Datei ";
	public static String	couldntLoadProject_2 = 		" in diesem Ordner befindet: \n";
	public static String	couldntLoadArtwork = 		"<html><body style='width: 300px'>Die folgenden Library-Dateien konnten nicht geladen werden. Stellen Sie sicher, dass sich der Ordner mit dem Suffix \"_lib\" im selben Ordner wie die Projektadtei befindet, und dass der Sammlungs_Ordner die korrekten Sammlungs-Dateien enthält.\nInventarnummer: ";
	public static String	couldntImport_1 = 			"Diese Kunstwerke konnten leider nicht importiert werden:\n";
	public static String	couldntImport_2 = 			"\nÜberprüfen Sie die Excel-Tabelle auf fehlerhafte Einträge" +
														"\nund stellen Sie sicher, dass sich die angegebenen" +
														"\nBildateien im selben Ordner mit der Tabelle befinden." +
														"\n\nAlle übrigen Kunstwerke wurden erfolgreich importiert!";
	public static String	collectionNotFound_1 =		"Die Sammlungs-Datei wurde nicht gefunden:\n\n\"";
	public static String	collectionNotFound_2 =		"\"\n\nStellen Sie sicher, dass der Sammlungs-Ordner" +
														"\nkorrekt benannt ist, und alle nötigen Dateien enthält." +
														"\n(Excel Tabelle und Bilddateien)";
	public static String	prevVersion_WallColor = 	"Diese Projektdatei wurde von einer früheren SimuFöhr Version erstellt. (v0.1.11 oder früher)" +
														"In der aktuellen Version ist es möglich, die Wandfarbe zu editieren." +
														"Die Wandfarbe wurde auf Weiß (255,255,255) gesetzt. Sie können diese nun ändern.";
	public static String	errorLoadingWallColor_1 = 	"Die Wandfarbe für \"";
	public static String	errorLoadingWallColor_2	=	"\" konnte nicht aus der Projektdatei gelesen werden.\n" +
														"\nWarscheinlich wurde die Projektdatei mit einer früheren Version von SimuFöhr" +
														"\nerstellt (v0.1.11 oder früher). In der aktuellen Version ist es nun möglich," +
														"\ndie Farbe einzelner Wande zu editieren.\n" +
														"\nFalls Probleme auftreten, wenden Sie sich bitte an den Support.";
	public static String	successfulImport = 			" Kunstwerke wurden erfolgreich Importiert.";
	
	// errors: Programm Handling
	
	public static String	artworkTooBig_title =			"Kunstwerk zu groß!";
	public static String	artworkTooBigForWall_1  = 		"Dieses Kunstwerk passt nicht an diese Wand.\n	Breite des Kunstwerks: ";
	public static String	artworkTooBigForWall_2  = 		" mm\n	Breite der Wand; ";
	public static String	artworkTooBigForWall_3  = 		" mm.";
	public static String	importArtworkAlreadyExists = 	"Dieses Kunstwerk konnte nicht importiert werden,\n" +
														 	"da sich bereits ein Kunstwerk mit derselben Inventarnummer\n" +
														 	"in diesem Projekt befindet.\n\n";
	
	// questions
	public static String 	initializeFromWhereTitle 		= "Willkommen zu SimuFöhr!";
	public static String 	initializeFromWhere_1 			= "Zuletzt geöffnetes Projekt:\n\"";
	public static String 	initializeFromWhere_2 			= "\"\n\nMöchten Sie dieses Projekt öffnen, ein neues erstellen,\noder ein anderes Projekt laden?";
	public static String	loadProjectTitle 				= "Projekt laden...";
	public static String 	selectRooms						= "Wählen Sie die Räume für dieses Projekt";
	public static String 	unsavedChangesTitle 			= "Anderungen speichern?";
	public static Object 	unsavedChanges 					= "Es wurden ungesicherte Änderungen vorgenommen,\nwollen Sie das Projekt jetzt speichern?";
	public static String[]	yesNoCancelOptions 				= new String[] { "Abbrechen", "Nein", "Ja" };
	public static String[]  importNowBtns 					= new String[] { "später", "importieren" };
	public static String 	importNow_1						= "Wollen Sie jetzt bereits Kunstwerke importieren,";
	public static String 	importNow_2						= "oder ein leeres Projekt erstellen?";
	public static String 	importNowTitle 					= "Kunstwerke importieren?";
	public static String	importArtworks					= "nur Kunstwerke";
	public static String	importColl						= "nur Sammlung";
	public static String	importBoth						= "beides";
	public static String	importPleaseWait			 	= "\n  Importiere Kunstwerke ...\n  Bitte haben Sie etwas Geduld.";
	public static Object 	newProjectName 					= "Projektname:";
	public static String 	newProjectNameTitle 			= "Neues Projekt...";
	public static Object[]	newProjectOptions 				= new String[] {"abbrechen", "erstellen"};
	public static String	create 							= "erstellen";
	public static String	newProjectLocation_1 			= "Wohin möchten Sie ";
	public static String	newProjectLocation_2 			= ".sfp speichern?";
	public static String 	wantToSaveBeforExitTitle 		= "SimuFöhr beenden?";
	public static String 	wantToSaveBeforExit 			= "Wollen Sie SimuFöhr wirklich beenden?\nUngespeicherte Änderungen gehen dabei verloren.";
	public static Object[] 	saveOnExitOptions 				= new String[] {"Abbrechen", "Beenden", "Sichern"};
	public static String	quitSF 							= "SimuFöhr beenden";
	public static String	saveProject 					= "Projekt sichern";
	public static String	saved 							= "Projekt erfolgreich gespeichert.";
	public static String	whereIsExcelFile 				= "Wo befindet sich die Excel-Tabelle?";
	public static String 	whereIsExcelFileForCollection	= "Wählen Sie eine Sammlung:";

	public static String	restoreProjectTitle 			= "Projekt Wiederherstellen?";
	public static String	restoreProjectMessage 			= "\n<html><body style='width: 300px'><b>Es wurde eine Wiederherstellungsdatei für dieses Projekt gefunden!</b><br><br>Wurde SimuFöhr beim letzten mal unvorhergesehen beendet? Falls dies der Fall ist, haben Sie jetzt die Möglichkeit, unngesicherte Speicherstände der letzten Sitzung wiederherzustellen.<br><br><b>HINWEIS!</b><br><i>Bitte beenden Sie SimuFöhr immer über den Menüpunkt <b>\"Simuföhr beenden...\"</b> um diese Meldung in Zukunft zu vermeiden.</i>";
	public static Object[]	restoreProjectOptions 			= new String[] {"Wiederherstellen", "Nein, danke!"};
	public static String	overwrite_1 					= "Es existiert bereits eine Datei mit dem Namen \n\"";
	public static String	overwrite_2 					= "\"\n\nWollen Sie die Datei überschreiben?";
	public static String	overwriteTitle 					= "Datei überschreiben?";
	
	// Control Elements
	public static String[]	initializeFromWhereButtons 	= new String[] { "Neues Projekt", "Projekt laden...", "letztes öffnen" };
	public static String	SM_filetypes 				= "Nur SimuFöhr Dateien";
	
	public static enum 		/* REFACTOR THIS FOR CHANGES!!!*/ sortOptions { Künstler, Titel, InvNr, Größe, Sammlung }; // REFACTOR!!!!!
	public static String	ok 							= "Ok";
	public static String	cancel 						= "Abrechen";
	public static String	apply						= "Anwenden";
	public static String	sortLibBy 					= "sortieren nach: ";
	public static String	importBtn 					= "Importieren";
	public static String	deleteBtn 					= "Löschen";
	public static String 	RemoveArtwork 				= "Kunstwerk abhängen";
	public static String	changeColor 				= "Wandfarbe ändern...";
	public static String	changeColorTitle 			= "Wandfarbe ändern";
	public static String	colorPicker 				= "Farbwähler...";
	public static String	preview 					= "Vorschau";
	public static String	noWallColor					= "wie Raum";
	public static String	changeColorMessage 			= "Geben Sie den RGB Wert (0-255) für die Wandfarbe ein:";
	public static String	changeRoomColor 			= "Farbe für den gesamten Raum ändern";
	public static String	changeSingleWallColor_1 	= "Nur Farbe für Wand ";
	public static String	changeSingleWallColor_2 	= " ändern";
	public static String	changeColorInvalidValues 	= "Die eingegebenen Werte sind ungültig!\nDie Eingabe darf nur aus Ziffern bestehen.";
	public static String	snapToMidHeight 			= "auf Mittelhöhe setzen";
	public static String	posFromBorder				= "Abstand von...";
	public static String 	enterRoom 					= "Raum betreten";
	public static String 	exitRoom 					= "Raum verlassen";
	public static String	closeWall 					= "Wand schließen";
	public static String 	selectView 					= "Wählen Sie eine Ansicht:";
	public static String 	savePreviewImage 			= "Vorschaubild speichern";
	
	// EditArtwork
	
	public static String	editArtwork 						= "Kunstwerk editieren: ";
	public static String	editMeasurements 					= "Werte ändern...";
	public static String	changeFrameStyle					= "Rahmen wählen:";
	public static String	frame 								= "Rahmen";
	public static String	passepartout 						= "Passepartout";
	public static String	artwork								= "Kunstwerk";
	public static String	width 								= "Breite (mm):";
	public static String	height 								= "Höhe (mm):";
	public static String	prevValue 							= "ursprünglicher Wert: ";
	public static String	light 								= "Kunstwerk ausleuchten";
	public static String	shadow 								= "Schatten anzeigen";
	public static String	editAwErrorTitle 					= "ungültige Werte";
	public static String	editAwErrorMessage_1 				= "Bitte korrigieren Sie folgende fehlerhafte Eingaben um fortzufahren:\n";
	public static String	errFrameArtworkWidth 				= "\n- Die Breite des Rahmenmaß darf die des Kunstwerks nicht unterschreiten";
	public static String	errFrameArtworkHeight 				= "\n- Die Höhe des Rahmenmaß darf die des Kunstwerks nicht unterschreiten";
	public static String	errPptWidthSmallerArtwork 			= "\n- Die Breite des Passepartout darf die des Kunstwerks nicht unterschreiten";
	public static String	errPptHeightSmallerArtwork 			= "\n- Die Höhe des Passepartout darf die des Kunstwerks nicht unterschreiten";
	public static String	errFrameWidthSmallerPpt				= "\n- Die Breite des Rahmens darf die des Passepartout nicht unterschreiten";
	public static String	errFrameHeightSmallerPpt			= "\n- Die Höhe des Rahmens darf die des Passepartout nicht unterschreiten";

	public static String	allign								= "Ausrichten";
	public static String	allignMidHor						= "mittig Horizontal";
	public static String	allignMidVert						= "mittig Vertikal";
	public static String	allignTop							= "an Oberkante";
	public static String	allignBottom						= "an Unterkante";
	public static String	allignLeft							= "linksbündig";
	public static String	allignRight							= "rechtsbündig";
	public static String	distance							= "Abstände verteilen...";
	public static String	distanceSelection					= "gleichmäßig";
	public static String	distanceFixed						= "fester Wert";
	public static String	distTxt								= "Abstand in mm";
	public static String	top									= "oben";
	public static String	bottom								= "unten";
	public static String	left								= "links";
	public static String	right								= "rechts";
	
	// Import
	
	public static String	importTitle 		= "Kunstwerk importieren";
	public static String	invNr 				= "Inventarnummer:";
	public static String	title 				= "Titel:";
	public static String	artist 				= "Künstler:";
	public static String	frameMeasure 		= "Rahmenmaß:";
	public static String	artworkMeasure 		= "Bildgröße:";
	public static String	pptMeasure 			= "Passepartoutmaß:";
	public static String	imageFile 			= "Bilddatei:";
	public static String	browse 				= "durchsuchen...";
	public static String	batchImport 		= "Aus Tabelle...";
	public static String	importCollection	= "Sammlung importieren";
	public static String 	includeCollection	= "Sammlung importieren";
	public static String	finishingImport		= "finalisiern...";
	
	// Export
	
	public static String	exportMenu 					= "Maße exportieren...";
	public static String	measureSheetTitle 			= "Projekt:";
	public static String	exportRoomName 				= "Raum:";
	public static String	exportWallName 				= "Wand:";
	public static String	exportWallMeasures 			= "Wandmaß (mm):";
	public static String	exportDate					= "Stand:";
	public static String	exportListTitle	    		= "Hängung-Listenansicht: (horizontal x vertikal in mm)";
	public static String	exportSuccess_1				= "Die Vermaßungsdatei wurde erfolgreich exportiert.\n\nSie befindet sich in Projektordner unter\n";
	public static String	exportSuccess_title			= "Datei gespeichert";
	public static String 	busyRenderingPreviewToFile 	= "Speichere Vorschau";

	
	// Errors
	
	public static String	err_loadImageFile 			= "Bilddatei konnte nicht geladen werden";
	public static String	err_InvNrAlreadyExists 		= "Es existiert bereits ein Kunstwerk mit dieser Inventarnummer";
	public static String	err_noImageSize 			= "Es wurden keine Maße in der Tabelle gefunden";
	public static String	err_onImageResize			= "Die Bilddatei konnte nicht verarbeitet werden.";
	public static String	err_noImageFile				= "Es existiert keine Bilddatei mit diesem Namen ";
	public static String	err_noImageFile_2			= " und der Endung auf .jpg, .JPG, oder .png.";
	public static String	err_loadingJpg				= "Beim laden des Bildes ist ein Fehler aufgetreten.";
	
	
	public static String 	deleteMessage_1 			= "\nWollen Sie folgende Kunstwerke wirklich löschen?\n" +
															"Beachten Sie dass das Projekt dabei gesichert werden muss.\n";
	public static String 	deleteMessage_2				= "\n\nProjekt jetzt sichern und Kunstwerke löschen?";
	public static String 	deleteTitle 				= "Kunstwerke löschen...";
	public static String 	noGraphicsfound				= "Keine Bilddatei gefunden für:";
	public static Object 	noCollectionSpecified		= "\n<html><body style='width: 300px'>Es wurde kein Pfad für die Sammlungs-Dateien in den Voreinstellungen gefunden.<br><i>Vermeiden Sie es, die 'prefs.txt' Datei manuell zu ändern!";


	
	
	}
