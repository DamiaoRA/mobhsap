package mobhsap.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MessageSearchDAO {

	protected HikariDataSource ds;
	protected Connection conn;
	protected Statement st;
	private static MessageSearchDAO singleton;
	private Map<String, PreparedStatement> psInsertMap;
	private Map<String, PreparedStatement> psUpdateMap;

	private MessageSearchDAO() throws SQLException {
		HikariConfig config = new HikariConfig();
		config = new HikariConfig("/textdb.properties");
		ds = new HikariDataSource(config);
		conn = ds.getConnection();
		st = conn.createStatement();
		psInsertMap = new HashMap<>();
		psUpdateMap = new HashMap<>();
	}

	public void close() {
		ds.close();
	}

	public static synchronized MessageSearchDAO getInstance() throws SQLException {
		if(singleton == null) {
			singleton = new MessageSearchDAO();
		}
		return singleton;
	}

	public void executeDDL(String t) throws SQLException {
		st.execute(t);
	}

	public void insert(String tableName, String trajId, String aspValue) throws SQLException {
		PreparedStatement ps = createInsertPreparedStatement(tableName);
		ps.setString(1, trajId);
		ps.setString(2, aspValue);
		ps.execute();
	}

	private PreparedStatement createInsertPreparedStatement(String tableName) throws SQLException {
		PreparedStatement ps = psInsertMap.get(tableName);
		if(ps == null) {
			ps = conn.prepareStatement("INSERT INTO " + tableName + "(trajectory_id, value) VALUES (?, ?)");
			psInsertMap.put(tableName, ps);
		}
		return ps;
	}

	public void update(String tableName, String trajId, String aspValue) throws SQLException {
		PreparedStatement ps = createUpdatePreparedStatement(tableName);
		ps.setString(1, aspValue);
		ps.setString(2, trajId);
		ps.execute();
	}

	private PreparedStatement createUpdatePreparedStatement(String tableName) throws SQLException {
		PreparedStatement ps = psUpdateMap.get(tableName);
		if(ps == null) {
			ps = conn.prepareStatement("UPDATE " + tableName + " SET value=? WHERE trajectory_id=?");
			psUpdateMap.put(tableName, ps);
		}
		return ps;
	}
}
