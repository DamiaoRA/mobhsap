package sethe.foursquare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sethe.datasource.AspectDAOIF;
import sethe.foursquare.model.MessageFoursquare;
import sethe.model.Message;

public class FoursquareAspectDAO implements AspectDAOIF {
	private String[] columns = {"id_price", "id_rating", "id_weather"};
	private Connection conn;

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

		psPriceInsert = conn.prepareStatement("INSERT INTO tb_price(value) VALUES (?)");
		psPriceSearch = conn.prepareStatement("SELECT id FROM tb_price WHERE value = ?");

		psRatingInsert = conn.prepareStatement("INSERT INTO tb_rating(value) VALUES (?)");
		psRatingSearch = conn.prepareStatement("SELECT id FROM tb_rating WHERE value = ?");

		psWeatherInsert = conn.prepareStatement("INSERT INTO tb_weather(value) VALUES (?)");
		psWeatherSearch = conn.prepareStatement("SELECT id FROM tb_weather WHERE value = ?");
	}

	private int insertPrice(double price) throws SQLException {
		Integer id = searchPrice(price);

		if(id == null) {
			psPriceInsert.setDouble(1, price);
			psPriceInsert.execute();
			id = searchPrice(price);
		}

		return id;
	}

	private Integer searchPrice(double price) throws SQLException {
		psPriceSearch.setDouble(1, price);
		ResultSet rs = psPriceSearch.executeQuery();

		if(rs.next()) {
			return rs.getInt(1);
		}
		return null;
	}
	
	private int insertRating(double rating) throws SQLException {
		Integer id = searchRating(rating);

		if(id == null) {
			psRatingInsert.setDouble(1, rating);
			psRatingInsert.execute();
			id = searchRating(rating);
		}

		return id;
	}

	private Integer searchRating(double rating) throws SQLException {
		psRatingSearch.setDouble(1, rating);
		ResultSet rs = psRatingSearch.executeQuery();

		if(rs.next()) {
			return rs.getInt(1);
		}
		return null;
	}
	
	private int insertWeather(String weather) throws SQLException {
		Integer id = searchWeather(weather);

		if(id == null) {
			psWeatherInsert.setString(1, weather);
			psWeatherInsert.execute();
			id = searchWeather(weather);
		}

		return id;
	}

	private Integer searchWeather(String weather) throws SQLException {
		psWeatherSearch.setString(1, weather);
		ResultSet rs = psWeatherSearch.executeQuery();

		if(rs.next()) {
			return rs.getInt(1);
		}
		return null;
	}

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