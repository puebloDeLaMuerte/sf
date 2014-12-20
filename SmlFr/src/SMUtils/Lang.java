package SMUtils;

public class Lang {
	
	public enum langs {DE};
	
	// errors
	public static String 	couldntFindResourceFolder = "Der Ordner >resources< konnte nicht gefunden werden.\nStellen Sie sicher, dass er sich im selben Verzeichnis mit SimulFšhr befindet.";
	public static String 	couldntFindPrefs = "Die Voreinstellungs-Datei konnte nicht gefunden werden! \nBitte ŸberprŸfen Sie, ob sie sich im Ordner >resources< befindet.";
	public static String 	couldntLoadPrefs = "Die Voreinstellungs-Datei konnte nicht geladen werden.\nMšglicherweise ist die datei beschŠdigt.";
	public static String 	couldntFindMuseum = "Die Museums-Datei konnte nicht gefunden werden! \nBitte ŸberprŸfen Sie, ob sie sich im Ordner >resources< befindet.";
	public static String 	couldntLoadMuseumData = "Die Museumsdaten konnten nicht geladen werden.\nDie Datei scheint beschŠdigt zu sein.";
	public static String	couldntLoadProject_1 = "Das Projekt konnte nicht geladen werden.\nStellen Sie sicher das sich die Datei ";
	public static String	couldntLoadProject_2 = " in diesem Ordner befindet: \n";
	// questions
	public static String 	initializeFromWhereTitle = "Was wollen Sie tun?";
	public static String 	initializeFromWhere_1 = "Zuletzt gešffnetes Projekt:\n\n";
	public static String 	initializeFromWhere_2 = "\n\nMšchten Sie das letzte Projekt šffnen, ein neues Projekt erstellen,\noder ein anderes Projekt laden?";
	public static String[]	initializeFromWhereButtons = new String[] { "Neues Projekt", "Projekt laden...", "letztes šffnen" };
	
}
