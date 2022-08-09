package sethe.tripbuilder.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QueryDWMain {

	private static Connection con;
	private static Statement st;
	private static String schema = "dw_tripbuilder";

	public static void main(String[] args) throws SQLException {
		try {
			initConnection("localhost", "5432", "postgres", "lsi123");
			List<TimeQ> times = new ArrayList<TimeQ>();
			times.add(queryQ1());
			times.add(queryQ2());
			times.add(queryQ3());
			times.add(queryQ4());
			times.add(queryQ5());
			times.add(queryQ6());
			times.add(queryQ7());
			times.add(queryQ8());
			times.add(queryQ9());
			times.add(queryQ10());
			System.out.println();
			for(TimeQ t : times) {
				System.out.println(t.toString());
			}
		} finally {
			if(con != null)
				con.close();
		}
	}

	public static void initConnection(String url1, String port, String user, String pass) throws SQLException {
		String url = "jdbc:postgresql://" + url1 + ":" + port + "/trajetoria";
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pass);
		con = DriverManager.getConnection(url, props);
		con.setSchema(schema);
		st = con.createStatement();
	}

	private static TimeQ queryQ1() throws SQLException {
		String sql = "SELECT distinct f1.id_trajectory, t.value  FROM fato f1 "
				+ "INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory "
				+ "INNER JOIN tb_category c1 ON c1.id = f1.id_category "
				+ "INNER JOIN tb_category c2 ON c2.id = f2.id_category "
				+ "inner join tb_trajectory t on t.id = f1.id_trajectory "
				+ "WHERE c1.name = 'museidipisa' AND c2.name = 'cappelledipisa' "
				+ "AND f1.position = f2.position-1";

		long t1 = System.currentTimeMillis();
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(2));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q1", count);
		return time;
	}

	private static TimeQ queryQ2() throws SQLException {
		long t1 = System.currentTimeMillis();
		String sql = "SELECT distinct f1.id_trajectory FROM fato f1 " + 
				"INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory " + 
				"INNER JOIN fato f3 ON f1.id_trajectory = f3.id_trajectory " + 
				"INNER JOIN fato f4 ON f1.id_trajectory = f4.id_trajectory " + 
				"INNER JOIN tb_category c1 ON c1.id = f1.id_category " + 
				"INNER JOIN tb_category c2 ON c2.id = f2.id_category " + 
				"INNER JOIN tb_category c3 ON c3.id = f3.id_category " + 
				"INNER JOIN tb_category c4 ON c4.id = f4.id_category " + 
				"WHERE c1.name = 'torridipisa'  " + 
				"AND (c2.name = 'cappelledipisa' OR c2.name = 'chiesedipisa') " + 
				"AND (c3.name = 'cappelledipisa' OR c3.name = 'chiesedipisa') " + 
				"AND c4.name = 'museidipisa' " + 
				"AND f1.position = f2.position-1 " + 
				"and f2.position < f3.position  " + 
				"AND f3.position = f4.position-1";

		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q2", count);
		return time;
	}
	
	private static TimeQ queryQ3() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "SELECT f1.id_trajectory FROM fato f1 " + 
				"INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory " + 
				"INNER JOIN tb_category c1 ON c1.id = f1.id_category " + 
				"INNER JOIN tb_category c2 ON c2.id = f2.id_category " + 
				"WHERE c1.name = 'torridipisa' AND c2.name = 'museidipisa' " + 
				"AND f1.position = f2.position-1";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q3", count);
		return time;
	}
	
	private static TimeQ queryQ4() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "SELECT distinct f1.id_trajectory FROM fato f1 " + 
				"INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory " + 
				"INNER JOIN tb_poi p1 ON p1.id = f1.id_poi " + 
				"INNER JOIN tb_poi p2 ON p2.id = f2.id_poi " + 
				"inner join tb_trajectory t on t.id = f1.id_trajectory  " + 
				"WHERE p1.name = 'Torre_del_Leone' AND p2.name = 'Torre_pendente_di_Pisa' " + 
				"AND (f1.position = f2.position-1 OR f2.position = f1.position-1)";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q4", count);
		return time;
	}
	
	private static TimeQ queryQ5() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "SELECT distinct f1.id_trajectory FROM fato f1 " + 
				"INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory " + 
				"INNER JOIN tb_trajectory t ON t.id = f1.id_trajectory " + 
				"INNER JOIN tb_category c1 ON c1.id = f1.id_category " + 
				"INNER JOIN tb_category c2 ON c2.id = f2.id_category " + 
				"WHERE c1.name = 'museidipisa' AND c2.name = 'cappelledipisa' " + 
				"AND f1.position = 1 AND f2.position = f1.position+1 AND f2.position = t.size ";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q5", count);
		return time;
	}
	
	private static TimeQ queryQ6() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "SELECT distinct f1.id_trajectory FROM fato f1 " + 
				"INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory " + 
				"INNER JOIN tb_trajectory t ON t.id = f1.id_trajectory " + 
				"INNER JOIN tb_category c1 ON c1.id = f1.id_category " + 
				"INNER JOIN tb_category c2 ON c2.id = f2.id_category " + 
				"WHERE c1.name = 'museidipisa'  " + 
				"AND ( " + 
				"((c2.name = 'cappelledipisa' OR c2.name = 'chiesedipisa') AND f2.position = t.size) " + 
				"OR (c2.name <> 'cappelledipisa' OR c2.name <> 'chiesedipisa') " + 
				") " + 
				"AND f1.position <= f2.position";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q6", count);
		return time;
	}

	private static TimeQ queryQ7() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "SELECT distinct f1.id_trajectory, f1.\"position\", t.size FROM fato f1 " + 
				"	INNER JOIN tb_trajectory t ON t.id = f1.id_trajectory " + 
				"	INNER JOIN fato f2 ON t.id = f2.id_trajectory " + 
				"	INNER JOIN fato f3 ON t.id = f3.id_trajectory " + 
				"	INNER JOIN tb_category c1 ON c1.id = f1.id_category " + 
				"	WHERE c1.name = 'cappelledipisa' " + 
				"	AND c1.id = f2.id_category AND c1.id = f3.id_category " + 
				"	AND f1.position = 1 AND f2.position = f1.position+1 and f3.position = t.size";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q7", count);
		return time;
	}
	
	private static TimeQ queryQ8() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "SELECT distinct f1.id_trajectory FROM fato f1 " + 
				"INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory " + 
				"INNER JOIN tb_transport_mean tm ON tm.id = f2.id_transport_mean " + 
				"INNER JOIN tb_category c1 ON c1.id = f1.id_category " + 
				"INNER JOIN tb_category c2 ON c2.id = f2.id_category " + 
				"WHERE c1.name = 'museidipisa' AND c2.name = 'cappelledipisa' " + 
				"AND f1.position = f2.position-1 AND tm.value = 'Bus'";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q8", count);
		return time;
	}
	
	private static TimeQ queryQ9() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "select id_trajectory " + 
				"from ( " + 
				"SELECT distinct f1.id_trajectory, f3.id_time, tm.value as tm, t.size FROM fato f1 " + 
				"INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory " + 
				"inner join tb_trajectory tt on tt.id = f1.id_trajectory " + 
				"INNER JOIN tb_trajectory t ON t.id = f1.id_trajectory " + 
				"INNER JOIN tb_category c1 ON c1.id = f1.id_category " + 
				"INNER JOIN tb_poi p ON p.id = f2.id_poi " + 
				"INNER JOIN fato f3 ON f3.id_trajectory = f1.id_trajectory " + 
				"inner join tb_transport_mean tm on tm.id = f3.id_transport_mean  " + 
				"WHERE f1.position = 1 AND f2.position=t.size " + 
				"AND (c1.name = 'cappelledipisa' OR c1.name = 'chiesedipisa') " + 
				"AND p.name = 'Torre_pendente_di_Pisa' " + 
				") as tab " + 
				"where tm = 'Bus' " + 
				"group by id_trajectory, size  " + 
				"having count(tm) = size-1";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q9", count);
		return time;
	}

	private static TimeQ queryQ10() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "SELECT distinct f1.id_trajectory, tt.value " + 
				"FROM fato f1 " + 
				"INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory " + 
				"INNER JOIN fato f3 ON f1.id_trajectory = f3.id_trajectory " + 
				"INNER JOIN fato f4 ON f1.id_trajectory = f4.id_trajectory " + 
				"inner join tb_trajectory tt on tt.id = f1.id_trajectory " + 
				"INNER JOIN tb_transport_mean tm2 ON tm2.id = f2.id_transport_mean " + 
				"INNER JOIN tb_transport_mean tm3 ON tm3.id = f3.id_transport_mean " + 
				"INNER JOIN tb_category c1 ON c1.id = f1.id_category " + 
				"INNER JOIN tb_category c3 ON c3.id = f3.id_category " + 
				"INNER JOIN tb_category c4 ON c4.id = f4.id_category " + 
				"WHERE c1.name = 'torridipisa'  " + 
				"AND c3.name = 'chiesedipisa' and c4.name = 'palazzidipisa' " + 
				"AND f1.position = 1 " + 
				"AND f2.position > f1.position " + 
				"AND f3.position > f2.position " + 
				"AND f4.position = tt.size " + 
				"and tm2.value = 'Walk' " + 
				"AND tm3.value = 'Bus'";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q10", count);
		return time;
	}
}