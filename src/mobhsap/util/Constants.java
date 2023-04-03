package mobhsap.util;

public class Constants {	

	public static String TransportMean = "transport_mean";

	public static String TB_LOCATION = "tb_location";
	public static String TB_POINTS = "tb_points";
	public static String TB_OSM = "context.tb_osm";
	public static final String TB_STRAJ = "tb_straj";
	public static final String TB_STRAJ_LAST = "tb_straj_last";

	public static final String NEAR = "~";
	public static final String ANY_VALUE = ".*";
	public static final String REPEATED = "(?-)";
	public static final String REPEATED_PATTERN = "\\(\\?\\-\\)";
	
	public static final String PROXIMITY = "proximity";

	public static boolean isProximity(String a) {
		if(a == null)
			return false;
		return a.trim().equalsIgnoreCase(PROXIMITY);
	}
}