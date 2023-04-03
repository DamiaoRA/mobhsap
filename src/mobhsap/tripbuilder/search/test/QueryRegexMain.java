package mobhsap.tripbuilder.search.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import mobhsap.util.TimeQ;

/**
 * 10 consultas extraídas do artigo "Stop-and-move sequence expressions over semantic trajectories" 
 * transformadas em regex
 *
 */
public class QueryRegexMain {
	private static Connection con;
	private static Statement st;
	private static String schema = "tripbuilder";

	public static void main(String[] args) throws SQLException {
		try {
			initConnection("localhost", "25432", "postgres", "postgres");
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
			for(TimeQ t : times) {
				System.out.println(t.toString());
			}
		} finally {
			if(con != null)
				con.close();
		}
	}

	private static TimeQ queryQ1() throws SQLException {

//		String sql = "select traj_fk, regexp_matches(values, '([a-z]*;*)museidipisa((;[a-z]*)*) ([a-z]*;*)cappelledipisa((;[a-z]*)*)'), values "
//				+ "from " + schema + ".tb_category";

		String sql = "select traj_fk, regexp_matches(values, 'museidipisa(;(\\w*))* ((\\w*);)*cappelledipisa'), values "
				+ "from " + schema + ".tb_category";
		long t1 = System.currentTimeMillis();
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(3));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q1", count);
		return time;
	}
	
	private static TimeQ queryQ2() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "select traj_fk, regexp_matches(values, '((torridipisa)(;(\\w*))* ((\\w*);)*(cappelledipisa|chiesedipisa)(;(\\w*))*)(.)*(((\\w*);)*(cappelledipisa|chiesedipisa)(;(\\w*))*) ((\\w*);)*(museidipisa)'), values \n" + 
				"from tripbuilder.tb_category";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(3));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q2", count);
		return time;
	}
	
	private static TimeQ queryQ3() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "select traj_fk, regexp_matches(values, '(torridipisa(;(\\w*))* )+((\\w*);)*museidipisa'), values from tripbuilder.tb_category";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(3));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q3", count);
		return time;
	}

	private static TimeQ queryQ4() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "select traj_fk, regexp_matches(values, 'Torre_del_Leone Torre_pendente_di_Pisa|Torre_pendente_di_Pisa Torre_del_Leone'), values from tripbuilder.tb_poi";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(3));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q4", count);
		return time;
	}

	private static TimeQ queryQ5() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "select traj_fk, regexp_matches(values, '^((\\w*);)*(museidipisa)(;(\\w*))* ((\\w*);)*(cappelledipisa)(;(\\w*))*$'), values from tripbuilder.tb_category";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(3));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q5", count);
		return time;
	}
	
	private static TimeQ queryQ6() throws SQLException {
		long t1 = System.currentTimeMillis();

//		String sql = "select traj_fk, regexp_matches(values, '(museidipisa)(.*)((cappelledipisa|chiesedipisa)(;(\\w*))*$)*'), values from tripbuilder.tb_category";
		String sql = "select traj_fk, regexp_matches(values, 'museidipisa(;(\\w*))*(.*)((cappelledipisa|chiesedipisa)(;(\\w*))*$)*'), values from tripbuilder.tb_category";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(3));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q6", count);
		return time;
	}
	
	private static TimeQ queryQ7() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "select traj_fk, regexp_matches(values, '^(((\\w*);)*(cappelledipisa)(;\\w*)* )(?=(((\\w*);)*)(cappelledipisa))(.*)(cappelledipisa)$'), values from tripbuilder.tb_category";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(3));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q7", count);
		return time;
	}
	
	private static TimeQ queryQ8() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "select traj_fk, regexp_matches(values, '(museidipisa)(;(\\w)+)* Bus ((\\w)+;)*(cappelledipisa)') as rc, values " + 
				"from tripbuilder.tb_category_move tcm";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(3));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q8", count);
		return time;
	}
	
	private static TimeQ queryQ9() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "select  traj_fk, cat, pois, moves from " + 
				"( " + 
				"select " + 
				"c.traj_fk, " + 
				"regexp_matches(c.values, '^(((\\w*);)*(cappelledipisa|chiesedipisa))') as rmc, " + 
				"c.values as cat, " + 
				"regexp_matches(p.values, 'Torre_pendente_di_Pisa$') as rmp, " + 
				"p.values as pois,\n" + 
				"regexp_matches(m.values, '^(N\\/A) (Bus )*(Bus)$') as rmm, " + 
				"m.values as moves " + 
				"from tripbuilder.tb_category c, tripbuilder.tb_move m, tripbuilder.tb_poi p " + 
				"where p.traj_fk = c.traj_fk and p.traj_fk = m.traj_fk " + 
				") as t " + 
				"where rmm is not null  and rmp is not null and rmc is not null";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(3));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q9", count);
		return time;
	}
	
	private static TimeQ queryQ10() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "select traj_fk , " + 
				"regexp_matches(\"values\", '^((\\w*);)*(torridipisa)(;(\\w*))*(.)* Walk (.)* Bus ((\\w*);)*(chiesedipisa)(;(\\w*))* (.)*(palazzidipisa)(;(\\w*))*$'), values " + 
				"from tripbuilder.tb_category_move tcm";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(3));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q10", count);
		return time;
	}

	public static void initConnection(String url1, String port, String user, String pass) throws SQLException {
		String url = "jdbc:postgresql://" + url1 + ":" + port + "/trajetoria";
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pass);
		con = DriverManager.getConnection(url, props);
		st = con.createStatement();
	}
}