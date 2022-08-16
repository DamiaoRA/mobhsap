package sethe.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import sethe.model.Message;

public class MessageDAO {

	private AspectDAOIF aspectDAO;

	private int idPoiCounter = 1;
	private int idTimeCounter = 1;
	private int idUserCounter = 1;

	private Map<String, StatusTrajectory> mapTraj;
	private Map<String, Integer> mapId = new HashMap<String, Integer>();

	protected PreparedStatement psPoiInsert;
	protected PreparedStatement psUserInsert;
	protected PreparedStatement psTimeInsert;
	private PreparedStatement psFato;
	private PreparedStatement psCalcDistance;

	protected Connection conn;
	private static MessageDAO singleton;

	private MessageDAO() {
		mapTraj = new HashMap<String, StatusTrajectory>();
	}

	private void start() throws SQLException {
		conn = HikariCPDataSource.getConnection();

		psPoiInsert = conn.prepareStatement("INSERT INTO tb_poi(id, x, y, name, category, city, state, country) VALUES (?,?,?,?,?,?,?,?)");
		psUserInsert = conn.prepareStatement("INSERT INTO tb_user (id, name) VALUES (?, ?)");
		psTimeInsert = conn.prepareStatement("INSERT INTO tb_time (id, second, minute, hour, day, month, semester, year, datetime) VALUES (?,?,?,?,?,?,?,?,?)");

		String stDist = "postgis.ST_DISTANCE(" + 
				"			postgis.ST_Transform(?::postgis.geometry, 3857)," + 
				"			postgis.ST_Transform(?::postgis.geometry, 3857)" + 
				"		) / 1000";
		psCalcDistance = conn.prepareStatement("select " + stDist);

		String columns = "id_poi, id_user, id_aspect, id_time, num_trajectory, distance, total_distance, duration, "
				   + "total_duration, historic_poi, historic_category, position";
		String values = "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
		String sql = "INSERT INTO fato (" + columns + ") VALUES(" + values + ") ";
	
		psFato = conn.prepareStatement(sql);
	}

	public void finish() throws SQLException {
		try {
			Statement st = conn.createStatement();
			st.execute("ALTER SEQUENCE tb_poi_id_seq RESTART WITH " + idPoiCounter);
			st.execute("ALTER SEQUENCE tb_user_id_seq RESTART WITH " + idUserCounter);
			st.execute("ALTER SEQUENCE tb_time_id_seq RESTART WITH " + idTimeCounter);
			aspectDAO.finish();
		} finally {
			HikariCPDataSource.close();
		}
	}

	public static synchronized MessageDAO getInstance() throws SQLException {
		if(singleton == null) {
			singleton = new MessageDAO();
			singleton.start();
		}
		return singleton;
	}

	public void setAspectDAO(AspectDAOIF dao) throws SQLException {
		aspectDAO = dao;
	}

	public Integer insertPoiDimension(Message m) throws SQLException {
		Integer idPoi = mapId.get("poi_" + m.poiToString());
		if(idPoi == null) {
			idPoi = idPoiCounter++;
			psPoiInsert.setInt(1, idPoi);
			psPoiInsert.setDouble(2, m.getX());
			psPoiInsert.setDouble(3, m.getY());
			psPoiInsert.setString(4, m.getOnePoi());
			psPoiInsert.setString(5, m.getOneCategory());
			psPoiInsert.setString(6, m.getCity());
			psPoiInsert.setString(7, m.getState());
			psPoiInsert.setString(8, m.getCountry());
			psPoiInsert.execute();

			mapId.put("poi_" + m.poiToString(), idPoi);
		}
		return idPoi;
	}

	private Integer insertUserDimension(Message m) throws SQLException {
		Integer idUser = mapId.get("user_" + m.getUserName());
		if(idUser == null) {
			idUser = idUserCounter++;
			psUserInsert.setInt(1, idUser);
			psUserInsert.setString(2, m.getUserName());
			psUserInsert.execute();

			mapId.put("user_" + m.getUserName(), idUser);
		}
		return idUser;
	}

	private Integer insertTimeDimension(Message m) throws SQLException {
		Timestamp ts = m.getDatetime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ts);

		Integer idTime = searchTimeId(calendar);
		if(idTime == null) {
			int semester = calendar.get(Calendar.MONTH) < Calendar.JULY ? 1 : 2;
			idTime = idTimeCounter++;
			psTimeInsert.setInt(1, idTime);
			psTimeInsert.setInt(2, calendar.get(Calendar.SECOND));
			psTimeInsert.setInt(3, calendar.get(Calendar.MINUTE));
			psTimeInsert.setInt(4, calendar.get(Calendar.HOUR_OF_DAY));
			psTimeInsert.setInt(5, calendar.get(Calendar.DATE));
			psTimeInsert.setInt(6, calendar.get(Calendar.MONTH));
			psTimeInsert.setInt(7, semester);
			psTimeInsert.setInt(8, calendar.get(Calendar.YEAR));
			psTimeInsert.setTimestamp(9, ts);

			psTimeInsert.execute();

			mapId.put("time_"
					+ calendar.get(Calendar.SECOND)
					+ calendar.get(Calendar.MINUTE)
					+ calendar.get(Calendar.HOUR_OF_DAY)
					+ calendar.get(Calendar.DATE)
					+ calendar.get(Calendar.MONTH)
					+ calendar.get(Calendar.YEAR), idTime);
		}
		return idTime;
	}

	private Integer searchTimeId(Calendar calendar) throws SQLException {
		Integer idTime = mapId.get("time_"
						+ calendar.get(Calendar.SECOND)
						+ calendar.get(Calendar.MINUTE)
						+ calendar.get(Calendar.HOUR_OF_DAY)
						+ calendar.get(Calendar.DATE)
						+ calendar.get(Calendar.MONTH)
						+ calendar.get(Calendar.YEAR));
		return idTime;
	}

	/**
	 * Create a Fact register
	 * @param m
	 * @throws SQLException
	 */
	public void insertFato(Message m) throws SQLException {
		//for(String poi : m.getPois()) { //TODO For while, there is only one PoI per category

			Integer idUser = insertUserDimension(m);
			Integer idTime = insertTimeDimension(m);
			Integer idPoi = insertPoiDimension(m);
			Integer idAspect = aspectDAO.putAspectsValues(conn, m);

			psFato.setInt(1, idPoi);
			psFato.setInt(2, idUser);
			psFato.setInt(3, idAspect);
			psFato.setInt(4, idTime);
			psFato.setString(5, m.getTrajectoryNumber());

			calcMedidas(m);

			psFato.execute();
		//}
	}

	private void calcMedidas(Message m) throws SQLException {
		StatusTrajectory status = mapTraj.get(m.getTrajectoryNumber());
//		double distance = 0;
//		double totalDistance = 0;
//		double duration = 0;
//		double totalDuration = 0;
//		String historicPoi = m.getOnePoi();
//		String historicCat = m.getOneCategory();
//		int position = 1;

		if(status == null) {
			status = new StatusTrajectory(m);
			mapTraj.put(m.getTrajectoryNumber(), status);
		} else {
//			distance = status.calcDistance(m);
//			duration = status.calcDuration(m);
//			totalDuration = status.calcTotalDuration(m);

			status.newLastPoi(m, psCalcDistance);
//
//			distance = status.totalDistance;
//			totalDistance = status.totalDistance;
//			historicPoi = status.historicPois;
//			historicCat = status.historicCat;
//			position = status.numPoi;
		}

		psFato.setDouble(6, status.distance);
		psFato.setDouble(7, status.totalDistance);
		psFato.setDouble(8, status.duration);
		psFato.setDouble(9, status.totalDuration);
		psFato.setString(10, status.historicPois);
		psFato.setString(11, status.historicCat);
		psFato.setInt(12, status.position);
	}
}

class StatusTrajectory {
	double distance = 0;
	double totalDistance = 0;
	double duration = 0;
	double totalDuration = 0;
	String historicPois;
	String historicCat;
	int position = 1;
	String numTrajectory;

	Message firstpoint;
	Message lastpoint;

	StatusTrajectory(Message m) {
		firstpoint = m;
		lastpoint  = m;

		distance = 0;
		totalDistance = 0;
		duration = 0;
		totalDuration = 0;
		historicPois = m.getOnePoi();
		historicCat = m.getOneCategory();
		position = 1;
		numTrajectory = m.getTrajectoryNumber();
	}

	public void newLastPoi(Message m, PreparedStatement ps) throws SQLException {
		historicPois += "," + m.getOnePoi();
		historicCat += "," + m.getOneCategory();
		distance = calcDistance(m, ps);
		totalDistance += distance;
		duration = calcDuration(m);
		totalDuration = calcTotalDuration(m);

		position++;

		lastpoint = m;
	}

	/**
	 * Duration in minutes
	 * @param m
	 * @return
	 */
	public double calcDuration(Message m) {
//		long diffMilli = Math.abs(lastpoint.getDatetime().getTime() - m.getDatetime().getTime());
//		long diff = TimeUnit.SECONDS.convert(diffMilli, TimeUnit.MILLISECONDS);
//
//		return diff;

		double diffMilli = Math.abs(lastpoint.getDatetime().getTime() - m.getDatetime().getTime());
		double hours = (diffMilli/1000)/60/60;
		return hours;
	}

	/**
	 * Duration in minutes
	 * @param m
	 * @return
	 */
	public double calcTotalDuration(Message m) {
		double diffMilli = Math.abs(firstpoint.getDatetime().getTime() - m.getDatetime().getTime());
		double hours = (diffMilli/1000)/60/60;
		return hours;
	}

	public double calcDistance(Message m, PreparedStatement psCalcDistance) throws SQLException {
//		String stDist = "ST_DISTANCE(" + 
//				"			ST_Transform('SRID=4326;POINT (" + m.getX() + " " + m.getY() + ")'::geometry, 3857)," + 
//				"			ST_Transform('SRID=4326;POINT (" + lastpoint.getX() + " " + lastpoint.getY() + ")'::geometry, 3857)" + 
//				"		) / 1000";

		String p1 = "SRID=4326;POINT (" + m.getX() + " " + m.getY() + ")";
		String p2 = "SRID=4326;POINT (" + lastpoint.getX() + " " + lastpoint.getY() + ")";

		psCalcDistance.setString(1, p1);
		psCalcDistance.setString(2, p2);

		ResultSet rs = psCalcDistance.executeQuery();
		rs.next();
		Double value = rs.getDouble(1); 
		return value;

//		double distance = Math.sqrt(
//				Math.pow((m.getX() - lastpoint.getX()), 2) + 
//				Math.pow((m.getY() - lastpoint.getY()), 2)
//				);
//
//		return distance;
	}


//	public double calcTotalDistance(Message m) {
//		totalDistance += calcDistance(m);
//		return totalDistance;
//
////		double distance = Math.sqrt(
////				Math.pow((m.getX() - firstpoint.getX()), 2) + 
////				Math.pow((m.getY() - firstpoint.getY()), 2)
////				);
////
////		return distance;
//	}
}