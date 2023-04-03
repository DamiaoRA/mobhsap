package mobhsap.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSource {
	private static HikariConfig config = new HikariConfig();
	private static HikariDataSource ds;

	static {
		config = new HikariConfig("/dw.properties");
		ds = new HikariDataSource(config);
	}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	public static void close() {
		ds.close();
	}

	private HikariCPDataSource() {
	}

	public static void main(String[] args) throws SQLException {
		long t1 = System.currentTimeMillis();
		Connection con = HikariCPDataSource.getConnection();
		con.isValid(1000);

		con.close();
		long t2 = System.currentTimeMillis();
		System.out.println(t2-t1);

		t1 = System.currentTimeMillis();
		con = HikariCPDataSource.getConnection();
		con.close();
		t2 = System.currentTimeMillis();
		
		System.out.println(t2-t1);
	}
}