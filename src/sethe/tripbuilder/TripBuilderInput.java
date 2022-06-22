package sethe.tripbuilder;

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

import sethe.datasource.InputMessageIF;
import sethe.model.Message;
import sethe.tripbuilder.model.MessageTripbuilder;

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
		config = new HikariConfig("/tripbuilder.properties");
		ds = new HikariDataSource(config);

		String sqlTraj = "select distinct object FROM tripbuilder.trajectory order BY object";

		conn = ds.getConnection();
		rsTraj = conn.createStatement().executeQuery(sqlTraj);

		String sqlMove = "SELECT transport, from1, to1, move_number "
				   + "FROM tripbuilder.move2 "
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
				return searchMessage(stopId, trajectoryId, null);
			} else {
				Message m = searchMessageOnePoin(trajectoryId);
				trajectoryId = null;
				rsMove = null;
				return m;
			}
		}

		if(rsMove.next()) {
			String move = rsMove.getString("transport");
			if(move != null) move = move.substring(31);
			String stopId = rsMove.getString("to1");
			return searchMessage(stopId, trajectoryId, move);
		} else {
			trajectoryId = null;
			rsMove = null;
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
		m.setTrajectoryName(trajectoryId);
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
		m.setTrajectoryName(trajectoryId);
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