package SMUtils;

public class Lang {
	
	public enum langs {DE};
	
	// errors
	public static String 	couldntFindResourceFolder = "Der Ordner \"resources\" konnte nicht gefunden werden.\nStellen Sie sicher, dass er sich im selben Verzeichnis mit SimulFöhr.exe befindet.";
	public static String 	couldntFindPrefs = "Die Voreinstellungs-Datei konnte nicht gefunden werden! \nBitte überprüfen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadPrefs = "Die Voreinstellungs-Datei konnte nicht geladen werden.\nMöglicherweise ist die datei beschädigt.";
	public static String 	couldntFindMuseum = "Die Museums-Datei konnte nicht gefunden werden! \nBitte überprüfen Sie, ob sie sich im Ordner \"resources\" befindet.";
	public static String 	couldntLoadMuseumData = "Die Museumsdaten konnten nicht geladen werden.\nDie Datei scheint beschädigt zu sein.";
	public static String	couldntLoadProject_1 = "Das Projekt konnte nicht geladen werden.\nStellen Sie sicher das sich die Datei ";
	public static String	couldntLoadProject_2 = " in diesem Ordner befindet: \n";
	public static String	couldntLoadArtwork = "Die Library-Dateien konnten nicht geladen werden.\nStellen Sie sicher, dass sich der Ordner mit dem\nSuffix \"_lib\" im selben Ordner wie die Projektadtei befindet.";
	// questions
	public static String 	initializeFromWhereTitle = "Willkommen!!";
	public static String 	initializeFromWhere_1 = "Zuletzt geöffnetes Projekt:\n\"";
	public static String 	initializeFromWhere_2 = "\"\n\nMöchten Sie dieses Projekt öffnen, ein neues erstellen,\noder ein anderes Projekt laden?";
	
	// Control Elements
	public static String[]	initializeFromWhereButtons = new String[] { "Neues Projekt", "Projekt laden...", "letztes öffnen" };
	public static String	SM_filetypes = "Nur SimulFöhr Dateien";
	public static enum 		sortOptions { Künstler, Titel, InvNr, Größe };
	public static String	sortLibBy = "sortieren nach:";
	public static String	importBtn = "Importieren";
	public static String	deleteBtn = "Löschen";
}
