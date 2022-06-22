package sethe.tripbuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import sethe.datasource.AspectDAOIF;
import sethe.model.Message;
import sethe.tripbuilder.model.MessageTripbuilder;

public class TripBuilderAspectDAO implements AspectDAOIF {
	private String[] columns = {"id_transport_mean"};
	
	private Connection conn;

	private int idTmCounter = 1;

	private Map<String, Integer> mapId = new HashMap<String, Integer>();

	private PreparedStatement psTmInsert;

	@Override
	public String[] columnsAspectsId() {
		return columns;
	}

	private void start(Connection conn) throws SQLException {
		this.conn = conn;
		psTmInsert = conn.prepareStatement("INSERT INTO tb_transport_mean(id, value) VALUES (?,?)");
	}

	@Override
	public void finish() throws SQLException {
		Statement st = conn.createStatement();
		st.execute("ALTER SEQUENCE tb_tm_id_seq RESTART WITH " + idTmCounter);
	}

	private int insertTransportMean(String tm) throws SQLException {
		Integer id = mapId.get("tm_" + tm);

		if(id == null) {
			id = idTmCounter++;
			psTmInsert.setInt(1, id);
			psTmInsert.setString(2, tm);
			psTmInsert.execute();

			mapId.put("tm_" + tm, id);
		}

		return id;
	}

	@Override
	public void putAspectsValues(PreparedStatement ps, int parameterIndex, Message m) throws SQLException {
		if(conn == null) {
			start(ps.getConnection());
		}

		MessageTripbuilder mt = (MessageTripbuilder)m;

		int idTm = insertTransportMean(mt.getTransportMeans());

		ps.setInt(parameterIndex++, idTm);
	}
}