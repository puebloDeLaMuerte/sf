package SMUtils;

public class Lang {
	
	public enum langs {DE};
	
	// errors
	public static String 	couldntFindResourceFolder = "Der Ordner \"resources\" konnte nicht gefunden werden.\nStellen Sie sicher, dass er sich im selben Verzeichnis mit SimulF�hr.exe befindet.";
	public static String 	couldntFindPrefs = "Die Voreinstellungs-Datei konnte nicht gefunden werden! \nBitte �berpr�fen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadPrefs = "Die Voreinstellungs-Datei konnte nicht geladen werden.\nM�glicherweise ist die datei besch�digt.";
	public static String 	couldntFindMuseum = "Die Museums-Datei konnte nicht gefunden werden! \nBitte �berpr�fen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadMuseumData = "Die Museumsdaten konnten nicht geladen werden.\nDie Datei scheint besch�digt zu sein.";
	public static String	couldntLoadProject_1 = "Das Projekt konnte nicht geladen werden.\nStellen Sie sicher das sich die Datei ";
	public static String	couldntLoadProject_2 = " in diesem Ordner befindet: \n";
	public static String	couldntLoadArtwork = "Die Library-Dateien konnten nicht geladen werden.\nStellen Sie sicher, dass sich der Ordner mit dem\nSuffix \"_lib\" im selben Ordner wie die Projektadtei befindet.";
	// questions
	public static String 	initializeFromWhereTitle = "Willkommen!!";
	public static String 	initializeFromWhere_1 = "Zuletzt ge�ffnetes Projekt:\n\"";
	public static String 	initializeFromWhere_2 = "\"\n\nM�chten Sie dieses Projekt �ffnen, ein neues erstellen,\noder ein anderes Projekt laden?";
	
	// Control Elements
	public static String[]	initializeFromWhereButtons = new String[] { "Neues Projekt", "Projekt laden...", "letztes �ffnen" };
	public static String	SM_filetypes = "Nur SimulF�hr Dateien";
	public static enum 		sortOptions { K�nstler, Titel, InvNr, Gr��e };
	public static String	sortLibBy = "sortieren nach:";
	public static String	importBtn = "Importieren";
	public static String	deleteBtn = "L�schen";
}
