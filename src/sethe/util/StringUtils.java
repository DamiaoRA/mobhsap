package sethe.util;

public class StringUtils {
	public static boolean isEmpty(String s) {
		if(s == null)
			return true;
		return s.trim().equals("");
	}

	public static String stringSql(String s) {
		if(s != null) {
			return s.replaceAll("'", "''");
		}
		return null;
	}
}
