package stockmarket.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * unit enumeration type
 * @author Damian Horna
 *
 */
public enum Unit implements Serializable{
	TONNE,
	BARREL,
	OUNCE,
	GALLON,
	POUND;
	
	public static String[] getNames(Class<? extends Enum<?>> e) {
	    return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
	}
}
