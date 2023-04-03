package mobhsap.tripbuilder.transform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import mobhsap.tripbuilder.Trajectory;

/**
 * Stores the trajectories with 1 point extracted from the RDF dataset. 
 *
 */
public class CreateTrajectory1Point {
	private Connection con;
	private Statement st;
	private String schema = "teste";//"tripbuilder";

	PreparedStatement psTraj;
	PreparedStatement psPoi;
	PreparedStatement psStop;
	PreparedStatement psCategory;
	PreparedStatement psLocatedIn;

	public void initConnection(String url1, String port, String user, String pass) throws SQLException {
		String url = "jdbc:postgresql://" + url1 + ":" + port + "/trajetoria";
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pass);
		con = DriverManager.getConnection(url, props);
		st = con.createStatement();

		psTraj = con.prepareStatement("INSERT INTO " + schema + ".tb_trajectory (id, values) VALUES (?, ?)");
		psPoi = con.prepareStatement("INSERT INTO " + schema + ".tb_poi (traj_fk, values) VALUES (?, ?)");
		psStop = con.prepareStatement("INSERT INTO " + schema + ".tb_stop (traj_fk, values) VALUES (?, ?)");
		psCategory = con.prepareStatement("INSERT INTO " + schema + ".tb_category (traj_fk, values) VALUES (?, ?)");
		psLocatedIn = con.prepareStatement("INSERT INTO " + schema + ".tb_locatedin (traj_fk, values) VALUES (?, ?)");
	}

	/**
	 * Retorna uma lista com os ids de todas as trajetórias
	 */
	private void process() throws SQLException {
//		String sql = "select distinct t from " + schema + ".trajonepoint order by traj";
		String sql = "select distinct t from tripbuilder.trajonepoint order by t";
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()) {
			Trajectory traj = new Trajectory();
			traj.setId(rs.getString(1));
			processTraj(traj);
		}
	}

	private String ajust(String s) {
		s = s.replace("patrimonidell'umanitÃ d'italia", "patrimonidellumanitaditalia");
		s = s.replace("cittÃ delvaticano", "cittadelvaticano");
		s = s.replace("Palazzo_del_PodestÃ _(Pisa)", "Palazzo_del_Podesta_(Pisa)");
		s = s.replace("caffÃ¨", "caffe");
		s = s.replace("Ã ", "a");
		s = s.replace("Ã²", "a");
		s = s.replace("Ã¹", "a");
		s = s.replace("'", "");
		s = s.replace("¨", "");
		s = s.replace("¨", "");
		return s;
	}

	/**
	 * Transforma a trajetória em texto
	 * @param traj
	 * @throws SQLException 
	 */
	private void processTraj(Trajectory traj) throws SQLException {
		String textStop = "";
		String textPoi = "";
		String textCategory = "";
		String textLocatedIn = "";

//		String sql = "SELECT t, stop, lat, lon, poi, label, category, locatedin "
//				+ "FROM " + schema + ".trajonepoint WHERE t='" + traj.getId() + "'" ;

		String sql = "SELECT t, stop, lat, lon, poi, label, category, locatedin "
				+ "FROM tripbuilder.trajonepoint WHERE t='" + traj.getId() + "'" ;

		ResultSet rs = st.executeQuery(sql);

		while(rs.next()) {
			textStop = rs.getString(4) + "," + rs.getString(3);
			textPoi = ajust(rs.getString(6)).trim();
			textCategory += ajust(rs.getString(7).trim()) + ";";
			textLocatedIn = rs.getString(8);
		}
		textCategory = textCategory.substring(0, textCategory.length() - 1);

		persistTrajectory(traj.getId(), textStop, textPoi, textCategory, textLocatedIn);
	}

	private void persistTrajectory(String id, String textStop, String textPoi, String textCategory, String textLocatedIn) throws SQLException {

		psTraj.setString(1, id);
		psTraj.setString(2, "");
		psTraj.execute();
//
		psStop.setString(1, id);
		psStop.setString(2, textStop);
		psStop.execute();
//
		psPoi.setString(1, id);
		psPoi.setString(2, textPoi);
		psPoi.execute();
//
		psCategory.setString(1, id);
		psCategory.setString(2, textCategory);
		psCategory.execute();
//
		psLocatedIn.setString(1, id);
		psLocatedIn.setString(2, textLocatedIn);
		psLocatedIn.execute();
	}

	public static void main(String[] args) throws SQLException {
		long t1 = System.currentTimeMillis();
		CreateTrajectory1Point main = new CreateTrajectory1Point();
		main.initConnection("localhost", "25432", "postgres", "postgres");
		main.process();
		long t2 = System.currentTimeMillis();
		System.out.println(t2-t1);
		System.out.println((t2-t1)/1000);
	}
}
