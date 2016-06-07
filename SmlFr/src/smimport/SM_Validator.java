package smimport;

public class SM_Validator {

	
	public static boolean isOnlyWhitespaceOrEmpty( String check) {
		
		boolean result = false;
		
		if( check.matches("\\s+")) result = true;
		if( check.isEmpty()) result = true;
		
		return result;
	}

	public static boolean isValidNumber(String text) {

		boolean result = false;

		try {
			int x = Integer.parseInt(text);
			System.out.println(x + " is a valid number.");
			result = true;
		} catch (Exception e) {
			result = false;
		}
		
		
		return result;
	}
}
