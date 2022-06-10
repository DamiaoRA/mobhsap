package sethe.tripbuilder.transform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import sethe.tripbuilder.Move;
import sethe.tripbuilder.PoI;
import sethe.tripbuilder.Stop;
import sethe.tripbuilder.Trajectory;

/**
 * ETL que transforma dados extraídos do tripbuilder em trajetórias textuais
 *
 */
public class TrajectoryTripBuilder {
	
	private Connection con;
	private Statement st;
	private String schema = "tripbuilder";

	PreparedStatement psTraj;
	PreparedStatement psPoi;
	PreparedStatement psStop;
	PreparedStatement psCategory;
	PreparedStatement psLocatedIn;
	PreparedStatement psMove;
	PreparedStatement psPoiMove;
	PreparedStatement psCatMove;
	
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
		psMove = con.prepareStatement("INSERT INTO " + schema + ".tb_move (traj_fk, values) VALUES (?, ?)");

		psPoiMove = con.prepareStatement("INSERT INTO " + schema + ".tb_poi_move (traj_fk, values) VALUES (?, ?)");
		psCatMove = con.prepareStatement("INSERT INTO " + schema + ".tb_category_move (traj_fk, values) VALUES (?, ?)");
	}
	
	/**
	 * Retorna uma lista com os ids de todas as trajetórias
	 */
	private void process() throws SQLException {
		String sql = "select distinct traj from " + schema + ".move2 order by traj";
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			Trajectory traj = new Trajectory();
			traj.setId(rs.getString(1));
			processTraj(traj);
		}
	}

	private Stop searchStop(String stopId) throws SQLException {
		String sql = "select distinct object, lat, lon, poi from " + schema + ".stop2 "
				+ "WHERE object = '" + stopId + "' "
				+ "order by object";
		ResultSet rs = st.executeQuery(sql);
		if(rs.next()) {
			Stop stop = new Stop();
			stop.setId(rs.getString(1));
			stop.setLat(rs.getDouble(2));
			stop.setLon(rs.getDouble(3));
			PoI poi = searchPoi(rs.getString(4));
			stop.setPoi(poi);
			return stop;
		}
		return null;
	}

//	private Move seachMove(Stop from) throws SQLException {
//		String sql = "select distinct object, transport, from1, to1, move_number from " + schema + ".move "
//				+ "WHERE from1 = '" + from.getId() + "' "
//				+ "order by object";
//		ResultSet rs = st.executeQuery(sql);
//		if(rs.next()) {
//			Move move = new Move();
//			move.setId(rs.getString(1));
//			String transport = rs.getString(2);
//			transport = transport.substring(transport.lastIndexOf("/") + 1);
//			move.setTransport(transport);
//			move.setFrom(from);
//			return move;
//		}
//		return null;
//	}
	
	private Move searchMove(String id, int count) throws SQLException {
		String sql = "select distinct object, transport, from1, to1, move_number from " + schema + ".move2 "
				+ "WHERE traj = '" + id + "' AND move_number = '" + count + "' "
				+ "order by object";
		ResultSet rs = st.executeQuery(sql);
		if(rs.next()) {
			Move move = new Move();
			move.setId(rs.getString(1));
			String transport = rs.getString(2);
			transport = transport.substring(transport.lastIndexOf("/") + 1);
			move.setTransport(transport);

			Stop from = searchStop(rs.getString(3));
			Stop to = searchStop(rs.getString(4));

			move.setFrom(from);
			move.setTo(to);
			return move;
		}
		return null;
	}

	private PoI searchPoi(String poiId) throws SQLException {
		String sql = "select distinct object, label, category, locatedin from " + schema + ".poi "
				+ "WHERE object = '" + poiId + "' "
				+ "order by object";
		ResultSet rs = st.executeQuery(sql);		
		PoI poi = null;
		while(rs.next()) {
			if(poi == null) {
				poi = new PoI();
				poi.setId(rs.getString(1));
				poi.setLabel(ajust(rs.getString(2)));
				poi.setLocatedIn(rs.getString(4));
			}
			poi.addCategory(ajust(rs.getString(3)));
		}
		return poi;
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
//		s = s.replace("Ã", "");
//		s = s.replace("Ã", "");
		s = s.replace("¨", "");
		s = s.replace("¨", "");

//	update 
//	tripbuilder.tb_category
//	set "values" = replace (replace(values, 'patrimonidellumanit ditalia', 'patrimonidellumanitditalia'), 'citt delvaticano', 'cittadelvaticano')
//	where "values" like '%patrimonidellumanit ditalia%' or "values" like '%citt delvaticano%'

//		update 
//		tripbuilder.tb_poi 
//		set "values" = replace(values, 'Palazzo_del_Podest _(Pisa)', 'Palazzo_del_Podesta_(Pisa)')
//		where "values" like '%Palazzo_del_Podest _(Pisa)%'		
		
//		Palazzo_del_PodestÃ _(Pisa)
//		patrimonidell'umanitÃ d'italia
//		ministeroperibenieleattivitÃ culturali
//		maestÃ 
//		maestÃ 
//		societÃ maxplanck
//		societÃ maxplanck
//		patrimonidell'umanitÃ d'italia
//		cittÃ delvaticano
//		cittÃ delvaticano
//		cittÃ delvaticano
//		cittÃ delvaticano
//		universitÃ diroma
//		universitÃ diroma
//		cittÃ delvaticano
//		universitÃ diroma
//		cittÃ delvaticano
//		cittÃ delvaticano
//		universitÃ diroma
//		universitÃ diroma
//		cittÃ delvaticano
//		cittÃ delvaticano
//		patrimonidell'umanitÃ d'italia
//		cittÃ delvaticano
//		universitÃ diroma
//		universitÃ diroma
//		cittÃ delvaticano
//		universitÃ diroma
//		palazzidellacittÃ delvaticano
//		cittÃ delvaticano

		return s;
	}

	/**
	 * Transforma a trajetória em texto
	 * @param traj
	 * @throws SQLException 
	 */
	private void processTraj(Trajectory traj) throws SQLException {
		int count = 1;
		Move move = searchMove(traj.getId(), count);

		//label category locatedIn move 
		String textTraj = "";
		String textPoi = "";
		String textStop = "";
		//TreeCategory treeCat = new TreeCategory();
		String textCategory = "";
		String textLocatedIn = "";
		String textMove = "";
		String textPoiMove = "";
		String textCatMove = "";

		if(move != null) { //tratando o "from" do "move"
			Stop stop = move.getFrom();
			textStop += stop.getLon() + "," + stop.getLat() + " ";
			textPoi += stop.getPoi().getLabel() + " ";
			//treeCat.addCategory(stop.getPoi().getCategories());
			textLocatedIn += stop.getPoi().getLocatedIn() + " ";
			textMove += "N/A ";
			textCategory += stop.getPoi().getCategoriesText() + " ";
			textPoiMove += stop.getPoi().getLabel() + " ";
			textCatMove += stop.getPoi().getCategoriesText() + " ";
		}

		while (move != null) { //trantando o "to" do "move"
			Stop stop = move.getTo();

			textStop += stop.getLon() + "," + stop.getLat() + " ";
			textPoi += stop.getPoi().getLabel() + " ";
//			treeCat.addCategory(stop.getPoi().getCategories());
			textCategory += stop.getPoi().getCategoriesText() + " ";
			textLocatedIn += stop.getPoi().getLocatedIn() + " ";
			textMove += move.getTransport() + " ";
			textPoiMove += move.getTransport() + " " + stop.getPoi().getLabel() + " ";
			textCatMove += move.getTransport() + " " + stop.getPoi().getCategoriesText() + " ";

			//stop = move.getTo();
			move = searchMove(traj.getId(), ++count); //pegando a próxima movimentação
		}

		textStop = textStop.substring(0, textStop.length() - 1);
		textPoi = textPoi.substring(0, textPoi.length() - 1);
		textLocatedIn = textLocatedIn.substring(0, textLocatedIn.length() - 1);
		textMove = textMove.substring(0, textMove.length() - 1);
		textCategory = textCategory.substring(0, textCategory.length() - 1);
		textPoiMove = textPoiMove.substring(0, textPoiMove.length() - 1);
		textCatMove = textCatMove.substring(0, textCatMove.length() - 1);
//		persistTrajectory(traj.getId(), textPoi, treeCat, textLocatedIn, textMove, textTraj);
		persistTrajectory(traj.getId(), textStop, textPoi, textCategory, textLocatedIn, 
						  textMove, textTraj, textPoiMove, textCatMove);
	}

	private void persistTrajectory(String id, String textStop, String textPoi, String textCategory, String textLocatedIn,
			String textMove, String textTraj, String textPoiMove, String textCatMove) throws SQLException {

		psStop.setString(1, id);
		psStop.setString(2, textStop);
		psStop.execute();
	}

	public static void main(String[] args) throws SQLException {
		long t1 = System.currentTimeMillis();
		TrajectoryTripBuilder main = new TrajectoryTripBuilder();
		main.initConnection("localhost", "25432", "postgres", "postgres");
		main.process();
		long t2 = System.currentTimeMillis();
		System.out.println(t2-t1);
		System.out.println((t2-t1)/1000);
	}
}