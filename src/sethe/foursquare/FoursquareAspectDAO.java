package sethe.foursquare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import sethe.datasource.AspectDAOIF;
import sethe.foursquare.model.MessageFoursquare;
import sethe.model.Message;

public class FoursquareAspectDAO implements AspectDAOIF {
	private String[] columns = {"id_price", "id_rating", "id_weather"};
	private Connection conn;

	private int idPriceCounter = 1;
	private int idRatingCounter = 1;
	private int idWeatherCounter = 1;

	private Map<String, Integer> mapId = new HashMap<String, Integer>();

	private PreparedStatement psPriceInsert;
	private PreparedStatement psPriceSearch;
	
	private PreparedStatement psRatingInsert;
	private PreparedStatement psRatingSearch;
	
	private PreparedStatement psWeatherInsert;
	private PreparedStatement psWeatherSearch;

	@Override
	public String[] columnsAspectsId() {
		return columns;
	}

	private void start(Connection conn) throws SQLException {
		this.conn = conn;

		psPriceInsert = conn.prepareStatement("INSERT INTO tb_price(id, value) VALUES (?,?)");
		psPriceSearch = conn.prepareStatement("SELECT id FROM tb_price WHERE value = ?");

		psRatingInsert = conn.prepareStatement("INSERT INTO tb_rating(id, value) VALUES (?,?)");
		psRatingSearch = conn.prepareStatement("SELECT id FROM tb_rating WHERE value = ?");

		psWeatherInsert = conn.prepareStatement("INSERT INTO tb_weather(id,value) VALUES (?,?)");
		psWeatherSearch = conn.prepareStatement("SELECT id FROM tb_weather WHERE value = ?");
	}
	
	@Override
	public void finish() throws SQLException {
		Statement st = conn.createStatement();
		st.execute("ALTER SEQUENCE tb_price_id_seq RESTART WITH " + idPriceCounter);
		st.execute("ALTER SEQUENCE tb_rating_id_seq RESTART WITH " + idRatingCounter);
		st.execute("ALTER SEQUENCE tb_weather_id_seq RESTART WITH " + idWeatherCounter);
	}

	private int insertPrice(double price) throws SQLException {
		Integer id = mapId.get("price_" + price);

		if(id == null) {
			id = idPriceCounter++;
			psPriceInsert.setInt(1, id);
			psPriceInsert.setDouble(2, price);
			psPriceInsert.execute();

			mapId.put("price_" + price, id);
		}

		return id;
	}

//	private Integer searchPrice(double price) throws SQLException {
//		psPriceSearch.setDouble(1, price);
//		ResultSet rs = psPriceSearch.executeQuery();
//
//		if(rs.next()) {
//			return rs.getInt(1);
//		}
//		return null;
//	}
	
	private int insertRating(double rating) throws SQLException {
		Integer id = mapId.get("rating_" + rating);

		if(id == null) {
			id = idRatingCounter++;
			psRatingInsert.setInt(1, id);
			psRatingInsert.setDouble(2, rating);
			psRatingInsert.execute();
			
			mapId.put("rating_" + rating, id);
		}

		return id;
	}

//	private Integer searchRating(double rating) throws SQLException {
//		psRatingSearch.setDouble(1, rating);
//		ResultSet rs = psRatingSearch.executeQuery();
//
//		if(rs.next()) {
//			return rs.getInt(1);
//		}
//		return null;
//	}
	
	private int insertWeather(String weather) throws SQLException {
		Integer id = mapId.get("weather_" + weather);

		if(id == null) {
			id = idWeatherCounter++;
			psWeatherInsert.setInt(1, id);
			psWeatherInsert.setString(2, weather);
			psWeatherInsert.execute();
			mapId.put("weather_" + weather, id);
		}

		return id;
	}

//	private Integer searchWeather(String weather) throws SQLException {
//		psWeatherSearch.setString(1, weather);
//		ResultSet rs = psWeatherSearch.executeQuery();
//
//		if(rs.next()) {
//			return rs.getInt(1);
//		}
//		return null;
//	}

	@Override
	public void putAspectsValues(PreparedStatement ps, int parameterIndex, Message m) throws SQLException {
		if(conn == null) {
			start(ps.getConnection());
		}

		MessageFoursquare mf = (MessageFoursquare)m;

		int idPrice = insertPrice(mf.getPrice());
		int idRating = insertRating(mf.getRating());
		int idWeather = insertWeather(mf.getWeather());

		ps.setInt(parameterIndex++, idPrice);
		ps.setInt(parameterIndex++, idRating);
		ps.setInt(parameterIndex++, idWeather);
	}
}