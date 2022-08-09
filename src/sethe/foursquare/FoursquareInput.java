package sethe.foursquare;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import sethe.datasource.InputMessageIF;
import sethe.foursquare.model.MessageFoursquare;
import sethe.model.Message;

public class FoursquareInput implements InputMessageIF{

	private HikariConfig config = new HikariConfig();
	private HikariDataSource ds;
	private Connection conn;
	private ResultSet rs;

	public FoursquareInput() throws SQLException {
		config = new HikariConfig("/foursquareInput.properties");
		ds = new HikariDataSource(config);
//		ds.setSchema(config.getSchema());

		String sql = 
				"SELECT anonymized_user_id, tid, lat, lon, "
						+ "date_time, day, poi_name, poi_category, "
						+ "price, rating, weather "
						+ "FROM data_checkin "
						+ "ORDER BY anonymized_user_id, tid, date_time";

		conn = ds.getConnection();
		rs = conn.createStatement().executeQuery(sql);
	}

	@Override
	public Message nextMessage() throws Exception {
		MessageFoursquare m = null;
		if(rs.next()) {
			m = new MessageFoursquare();
			m.setUserName(rs.getInt("anonymized_user_id")+"");
			m.setTrajectoryName(rs.getInt("tid")+"");
			m.setX(rs.getDouble("lat"));
			m.setY(rs.getDouble("lon"));
			m.setDatetime(rs.getTimestamp("date_time"));

			Set<String> pois = new HashSet<String>();
			pois.add(rs.getString("poi_name"));
			m.setPois(pois);

			Set<String> cats = new HashSet<String>();
			cats.add(rs.getString("poi_category"));
			m.setCategories(cats);

			m.setPrice(rs.getDouble("price"));
			m.setRating(rs.getDouble("rating"));
			m.setWeather(rs.getString("weather"));
		}
		return m;
	}

	public void finish() {
		ds.close();
	}

	public static void main(String[] args) throws Exception {
		FoursquareInput input = new FoursquareInput();
		System.out.println(input.nextMessage().toString()+"\n");
		System.out.println(input.nextMessage().toString()+"\n");
		System.out.println(input.nextMessage().toString()+"\n");
		System.out.println(input.nextMessage().toString()+"\n");
		System.out.println(input.nextMessage().toString()+"\n");
		input.finish();
	}
}