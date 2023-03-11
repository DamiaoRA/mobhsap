package sethe.foursquare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import sethe.datasource.AspectDAOIF;
import sethe.model.Message;

public class FoursquareAspectDAO implements AspectDAOIF {
	private String[] columns = {"id_price", "id_rating", "id_weather"};
	private Connection conn;
	private int idAspect = 1;
	private Map<String, Integer> mapId = new HashMap<String, Integer>();

	private PreparedStatement ps;

	@Override
	public String[] columnsAspectsId() {
		return columns;
	}

	private void start(Connection conn) throws SQLException {
		this.conn = conn;
		ps = conn.prepareStatement("INSERT INTO tb_aspect(id, value, type) VALUES (?,?,?)");
	}

	@Override
	public void finish() throws SQLException {
		Statement st = conn.createStatement();
		st.execute("ALTER SEQUENCE tb_asp_id_seq RESTART WITH " + idAspect);
	}

	@Override
	public int putAspectsValues(Connection conn, Message m) throws SQLException {
		if(ps == null) {
			start(conn);
		}

		Integer id = mapId.get(m.getAspectsToString());
		if(id == null) {
			id = idAspect++;
			String value = m.getAspectsToString();
			String type = m.getAspectType();
			ps.setInt(1, id);
			ps.setString(2, value);
			ps.setString(3, type);
			ps.execute();

			mapId.put(value, id);
		}

		return id;
	}
}