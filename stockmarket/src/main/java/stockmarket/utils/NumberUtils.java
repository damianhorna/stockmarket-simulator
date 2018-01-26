package stockmarket.utils;

/**
 * number utils class
 * @author Damian Horna
 *
 */
public class NumberUtils {

	/**
	 * checks whether a string is numeric type
	 * @param s string to check
	 * @return true if numeric, false otherwise
	 */
	public static boolean isNumeric(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}

	/**
	 * checks whether a string can be a pesel
	 * @param s string to check
	 * @return true if can be pesel, false otherwise
	 */
	public static boolean isPESEL(String s) {
		return s != null && s.matches("\\d{11}");
	}  
	
	/**
	 * checks whether a string can be integer
	 * @param s string to check
	 * @return true if can be integer, false otherwise
	 */
	public static boolean isInteger(String s) {
		return s != null && s.matches("\\d+");
	}  
}
