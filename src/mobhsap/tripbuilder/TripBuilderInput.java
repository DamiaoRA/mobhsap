package mobhsap.tripbuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import mobhsap.datasource.InputMessageIF;
import mobhsap.model.Message;
import mobhsap.tripbuilder.model.MessageTripbuilder;

public class TripBuilderInput implements InputMessageIF {

	private HikariConfig config = new HikariConfig();
	private HikariDataSource ds;
	private Connection conn;

	private ResultSet rsTraj;
	private PreparedStatement psMove;
	private PreparedStatement psStop;
	private PreparedStatement psTraj1;
	private ResultSet rsMove;

	private String trajectoryId;
	private Calendar start = Calendar.getInstance();
	private Calendar time;

	public TripBuilderInput() throws SQLException {
		config = new HikariConfig("/tripbuilderInput.properties");
		ds = new HikariDataSource(config);

		//debug
//		String sqlTraj = "select distinct object FROM trajectory WHERE object IN ('http://localhost:8080/resource/TP1158','http://localhost:8080/resource/TP1775','http://localhost:8080/resource/TP2148','http://localhost:8080/resource/TP2149','http://localhost:8080/resource/TP2519','http://localhost:8080/resource/TP2622','http://localhost:8080/resource/TP2672','http://localhost:8080/resource/TP2674','http://localhost:8080/resource/TP2855','http://localhost:8080/resource/TP3280','http://localhost:8080/resource/TP3405','http://localhost:8080/resource/TP373','http://localhost:8080/resource/TP439','http://localhost:8080/resource/TP59','http://localhost:8080/resource/TP631','http://localhost:8080/resource/TP805','http://localhost:8080/resource/TP112','http://localhost:8080/resource/TP1218','http://localhost:8080/resource/TP1524','http://localhost:8080/resource/TP1546','http://localhost:8080/resource/TP168','http://localhost:8080/resource/TP1725','http://localhost:8080/resource/TP2023','http://localhost:8080/resource/TP2503','http://localhost:8080/resource/TP2738','http://localhost:8080/resource/TP2739','http://localhost:8080/resource/TP2820','http://localhost:8080/resource/TP3176','http://localhost:8080/resource/TP3344','http://localhost:8080/resource/TP342','http://localhost:8080/resource/TP371','http://localhost:8080/resource/TP432','http://localhost:8080/resource/TP693','http://localhost:8080/resource/TP2037','http://localhost:8080/resource/TP2149','http://localhost:8080/resource/TP2674','http://localhost:8080/resource/TP2855','http://localhost:8080/resource/TP1233','http://localhost:8080/resource/TP1524','http://localhost:8080/resource/TP1549','http://localhost:8080/resource/TP1660','http://localhost:8080/resource/TP1754','http://localhost:8080/resource/TP1757','http://localhost:8080/resource/TP2023','http://localhost:8080/resource/TP2763','http://localhost:8080/resource/TP295','http://localhost:8080/resource/TP3093','http://localhost:8080/resource/TP3104','http://localhost:8080/resource/TP3110','http://localhost:8080/resource/TP3176','http://localhost:8080/resource/TP3214','http://localhost:8080/resource/TP371','http://localhost:8080/resource/TP432','http://localhost:8080/resource/TP458','http://localhost:8080/resource/TP693','http://localhost:8080/resource/TP1172','http://localhost:8080/resource/TP693','http://localhost:8080/resource/TP1523','http://localhost:8080/resource/TP1248','http://localhost:8080/resource/TP3126','http://localhost:8080/resource/TP1882','http://localhost:8080/resource/TP66','http://localhost:8080/resource/TP1706','http://localhost:8080/resource/TP3214','http://localhost:8080/resource/TP3280','http://localhost:8080/resource/TP2672','http://localhost:8080/resource/TP1775','http://localhost:8080/resource/TP439','http://localhost:8080/resource/TP1158','http://localhost:8080/resource/TP2683','http://localhost:8080/resource/TP1889','http://localhost:8080/resource/TP2622','http://localhost:8080/resource/TP2148','http://localhost:8080/resource/TP1725','http://localhost:8080/resource/TP3405','http://localhost:8080/resource/TP432','http://localhost:8080/resource/TP112','http://localhost:8080/resource/TP59','http://localhost:8080/resource/TP2739','http://localhost:8080/resource/TP3176','http://localhost:8080/resource/TP1158','http://localhost:8080/resource/TP3091','http://localhost:8080/resource/TP2582','http://localhost:8080/resource/TP2008','http://localhost:8080/resource/TP2612','http://localhost:8080/resource/TP580','http://localhost:8080/resource/TP542','http://localhost:8080/resource/TP314','http://localhost:8080/resource/TP2212')"
//				+ " ORDER BY object";

		String sqlTraj = "select distinct object FROM trajectory ORDER BY object";

		conn = ds.getConnection();
		rsTraj = conn.createStatement().executeQuery(sqlTraj);

		String sqlMove = "SELECT transport, from1, to1, move_number "
				   + "FROM move2 "
				   + "WHERE traj= ? ORDER BY move_number::integer";
		psMove = conn.prepareStatement(sqlMove);

		String sqlStop = "SELECT p.object, p.label, p.category, p.lat, p.lon, p.locatedin "
				   + "FROM stop2 s, poi p "
				   + "WHERE s.poi = p.object AND s.object = ? ";
		psStop = conn.prepareStatement(sqlStop);
		
		String sqlTraj1 = "SELECT t, stop, lat, lon, poi, label, category, locatedin "
				   + "FROM trajonepoint WHERE t= ? ";
		psTraj1 = conn.prepareStatement(sqlTraj1);
	}
	
	boolean extractMoveTo = false;

	@Override
	public Message nextMessage() throws Exception {
		if(trajectoryId == null) {
			if(rsTraj.next()) {
				trajectoryId = rsTraj.getString(1);
				time = Calendar.getInstance();
				time.setTime(start.getTime());
			} else {
				return null;
			}
		}

		if(rsMove == null) {
			psMove.setString(1, trajectoryId);
			rsMove = psMove.executeQuery();

			if(rsMove.next()) {
				String stopId = rsMove.getString("from1");
				extractMoveTo = true;
				return searchMessage(stopId, trajectoryId, null);
			} else {
				Message m = searchMessageOnePoin(trajectoryId);
				trajectoryId = null;
				rsMove = null;
				return m;
			}
		}
		
		if(extractMoveTo) {
			String move = rsMove.getString("transport");
			if(move != null) move = move.substring(31);
			String stopId = rsMove.getString("to1");
			extractMoveTo = false;
			return searchMessage(stopId, trajectoryId, move);
		} else if(rsMove.next()) {
			String move = rsMove.getString("transport");
			if(move != null) move = move.substring(31);
			String stopId = rsMove.getString("to1");
			return searchMessage(stopId, trajectoryId, move);
		} else {
			trajectoryId = null;
			rsMove = null;
			extractMoveTo = false;
			return nextMessage();
		}
	}

	public Message searchMessage(String stopId, String trajId, String move) throws SQLException {
		psStop.setString(1, stopId);
		ResultSet rs = psStop.executeQuery();
		if(!rs.next()) {
			return searchMessageOnePoin(trajId);
		}

		Set<String> categories = new HashSet<String>();
		Set<String> pois = new HashSet<String>();
		MessageTripbuilder m = new MessageTripbuilder();
		do {
			categories.add(rs.getString("category"));
			pois.add(rs.getString("label"));

			m.setX(rs.getDouble("lon"));
			m.setY(rs.getDouble("lat"));
		} while(rs.next());
		m.setTrajectoryNumber(trajectoryId);
		m.setCategories(categories);
		m.setPois(pois);
		m.setTransportMeans(move);
		m.setDatetime(new Timestamp(time.getTimeInMillis()));
		time.add(Calendar.HOUR_OF_DAY, 1);

		return m;
	}

	private Message searchMessageOnePoin(String trajId) throws SQLException {
		psTraj1.setString(1, trajId);
		ResultSet rs = psTraj1.executeQuery();

		if(!rs.next()) {
			return null;
		}

		Set<String> categories = new HashSet<String>();
		Set<String> pois = new HashSet<String>();

		MessageTripbuilder m = new MessageTripbuilder();
		do {
			m.setY(rs.getDouble("lat"));
			m.setX(rs.getDouble("lon"));

			categories.add(rs.getString("category"));
			pois.add(rs.getString("label"));
		} while(rs.next());
		m.setTrajectoryNumber(trajectoryId);
		m.setCategories(categories);
		m.setPois(pois);
		m.setDatetime(new Timestamp(time.getTimeInMillis()));
		time.add(Calendar.HOUR_OF_DAY, 1);
		return m;
	}

	public static void main(String[] args) throws Exception {
		TripBuilderInput input = new TripBuilderInput();
		Message m = input.nextMessage();
		while (m != null) {
			System.out.println(m);
			m = input.nextMessage();
		}
	}
}