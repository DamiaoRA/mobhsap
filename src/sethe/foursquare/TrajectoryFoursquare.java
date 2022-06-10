package sethe.foursquare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;

import sethe.PoI;
import sethe.Trajectory;

/**
 * Converte o BD de trajetórias Foursquare no BD de textos
 *
 */
public class TrajectoryFoursquare {
//	private Connection con;
//	private Statement st;
//	private PreparedStatement psPoi;
//	private PreparedStatement psTrajPoiDateTime;
//	private PreparedStatement psUpdateTraj;
//
//	private Map<String, Integer> mapCategories;
//	private Map<String, PreparedStatement> mapAspStatement;
//	private Map<String, Integer> mapPoI;
//
////	private String schema = "traj10sem";
//	private String schema = "trajsem";
//
//	public void initConnection(String url1, String port, String user, String pass) throws SQLException {
//		String url = "jdbc:postgresql://" + url1 + ":" + port + "/trajetoria";
//		Properties props = new Properties();
//		props.setProperty("user", user);
//		props.setProperty("password", pass);
//		con = DriverManager.getConnection(url, props);
//		st = con.createStatement();
//
//		mapCategories = new HashMap<String, Integer>();
//		mapPoI = new HashMap<String, Integer>();
//		mapAspStatement = new HashMap<String, PreparedStatement>();
//	}
//
//	/**
//	 * Retorna uma lista com os ids de todas as trajetórias
//	 */
//	private List<Integer> listTrajectoriesId() throws SQLException {
//		String sql = "select distinct tid from data_checkin order by tid";
//		ResultSet rs = st.executeQuery(sql);
//		List<Integer> ids = new ArrayList<Integer>();
//		while (rs.next()) {
//			ids.add(rs.getInt(1));
//		}
//		return ids;
//	}
//
//	private List<PoI> listPoIs(int idT) throws SQLException {
//		String sql = "select lat, lon, date_time, poi_name, poi_category, day, price, rating, weather "
//				+ "from public.data_checkin " + "where tid = " + idT + " order by date_time";
//		String price = "-1";
//		String rating = "-1";
//		int position = 1;
//
//		List<PoI> pois = new ArrayList<PoI>();
//		ResultSet rs = st.executeQuery(sql);
//		while (rs.next()) {
//			PoI p = new PoI();
//			p.setIdTrajectory(idT);
//			p.setLat(rs.getDouble("lat"));
//			p.setLon(rs.getDouble("lon"));
//			p.setTime(rs.getTimestamp("date_time"));
//			p.setName(rs.getString("poi_name"));
//			p.setCategory(rs.getString("poi_category"));
//			p.setPosition(position);
//			position++;
//
//			price = rs.getString("price");
//			price = price.equals("-1")? "1" : price;
//
//			rating = rs.getString("rating");
//			rating = rating.equals("-1")? "7.3" : rating;
//
//			p.addAspect("day", rs.getString("day"));
//			p.addAspect("price", price);
//			p.addAspect("rating", rating);
//			p.addAspect("weather", rs.getString("weather"));
//
//			pois.add(p);
//		}
//		return pois;
//	}
//
//	private void save(Trajectory t) throws SQLException {
//		String sql = "INSERT INTO " + schema + ".tb_trajectory (id) VALUES (" + t.getId() + ")";
//		st.execute(sql);
//	}
//
//	public Integer searchCategoryId(String cat) throws SQLException {
//		Integer idCat = null;
//		String sql = "SELECT id FROM " + schema + ".tb_category WHERE name = ?";
//		PreparedStatement ps = con.prepareStatement(sql);
//		ps.setString(1, cat);
//		ResultSet rs = ps.executeQuery();
//		if(rs.next()) {
//			idCat= rs.getInt(1);
//		}
//		return idCat;
//	}
//
//	public int savePoICategory(String catName) throws SQLException {
//		Integer id = mapCategories.get(catName);
//		if(id == null) {
//			PreparedStatement ps = con.prepareStatement("INSERT INTO " + schema + ".tb_category (name) VALUES(?)");
//			ps.setString(1, catName);
//			ps.execute();
//
//			id = searchCategoryId(catName);
//			mapCategories.put(catName, id);
//		}
//		return id;
//	}
//
//	/**
//	 * Procura pelo PoI através do nome, lat e long
//	 * @throws SQLException
//	 */
//	public Integer searchPoiId(PoI p) throws SQLException {
//		Integer idPoi = mapPoI.get(p.getKey());
//		if(idPoi == null) {
//			String sql = "SELECT id FROM " + schema + ".tb_poi WHERE name = ? AND lat = ? AND lon = ?";
//			PreparedStatement ps = con.prepareStatement(sql);
//
//			ps.setString(1, p.getName());
//			ps.setDouble(2, p.getLat());
//			ps.setDouble(3, p.getLon());
//			ResultSet rs = ps.executeQuery();
//			if(rs.next()) {
//				idPoi = rs.getInt(1);
//				mapPoI.put(p.getKey(), idPoi);
//			}
//		}
//		return idPoi;
//	}
//
//	/**
//	 * Salva um PoI de uma trajetória
//	 * @throws SQLException
//	 */
//	public void save(PoI p, Trajectory t) throws SQLException {
//		int catFk = savePoICategory(p.getCategory()); //id da categoria
//		Integer id = searchPoiId(p);
//
//		if(id == null) {
//			//salvando o poi
//			if(psPoi == null) {
//				String sql = "INSERT INTO " + schema + ".tb_poi (category_fk, name, lat, lon) "
//					+ "VALUES (?,?,?,?)";
//				psPoi = con.prepareStatement(sql);
//			}
//			psPoi.setInt(1, catFk);
//			psPoi.setString(2, p.getName());
//			psPoi.setDouble(3, p.getLat());
//			psPoi.setDouble(4, p.getLon());
//			psPoi.execute();
//			id = searchPoiId(p);
//		}
//		p.setId(id);
//		savePoITraj(p, t.getId());
//	}
//
//	private void savePoITraj(PoI p, Integer idTraj) throws SQLException {
//		if(psTrajPoiDateTime == null) {
//			String sql = "INSERT INTO " + schema + ".tb_poi_traj_date (traj_fk, poi_fk, position, date_time) VALUES (?,?,?,?)";
//			psTrajPoiDateTime = con.prepareStatement(sql);
//		}
//
//		psTrajPoiDateTime.setInt(1, idTraj);
//		psTrajPoiDateTime.setInt(2, p.getId());
//		psTrajPoiDateTime.setInt(3, p.getPosition());
//		psTrajPoiDateTime.setTimestamp(4, p.getTime());
//
//		psTrajPoiDateTime.execute();
//	}
//
//	private String createTable(String key) throws SQLException {
//		String tb_name = "tb_" + key;
//		String sql = "CREATE TABLE IF NOT EXISTS " + schema + "." + tb_name + 
//				"(traj_fk int, values text, " + 
//				"constraint " + schema + tb_name + "_trajfk " + 
//				"foreign key (traj_fk) references tb_trajectory(id))";
//		
//		st.executeUpdate(sql);
//
//		return tb_name;
//	}
//
//	private void save(Trajectory t, String tableName, String aspects) throws SQLException {
//		PreparedStatement ps = mapAspStatement.get(tableName);
//		if(ps == null) {
//			String sql = "INSERT INTO " + schema + "." + tableName + " VALUES (?, ?)";
//			ps = con.prepareStatement(sql);
//			mapAspStatement.put(tableName, ps);
//		}
//		ps.setInt(1, t.getId());
//		ps.setString(2, aspects);
//		ps.execute();
//	}
//
//	private void saveAspects(Trajectory t, HashMap<String, Queue<String>> mapAsp) throws SQLException {
//		for(String key : mapAsp.keySet()) {
//			String tableName = createTable(key);
//			String asp = "";
//			while(mapAsp.get(key).size() > 0) {
//				asp += mapAsp.get(key).remove() + " ";
//			}
//			asp.trim();
//			save(t, tableName, asp);
//		}
//	}
//
//	private void updateTrajectoryText(Integer id, StringBuffer trajText) throws SQLException {
//		if(psUpdateTraj == null) {
//			String sql = "UPDATE " + schema + ".tb_trajectory SET traj_sem = ? WHERE id = ?";
//			psUpdateTraj = con.prepareStatement(sql);
//		}
//		psUpdateTraj.setString(1, trajText.toString());
//		psUpdateTraj.setInt(2, id);
//		psUpdateTraj.execute();
//	}
//
//	public void process() throws SQLException {
//		try {
////			int numPois = 0;
//
//			List<Integer> listTrajId = listTrajectoriesId();
//			for (int idT : listTrajId) {
//				HashMap<String, Queue<String>> mapAsp = new HashMap<String, Queue<String>>();
//				Trajectory t = new Trajectory();
//				t.setId(idT);
//				save(t);
//
//				mapAsp.put("poi_traj", new LinkedList<String>());
//				mapAsp.put("poi_id_traj", new LinkedList<String>());
//				mapAsp.put("category_traj", new LinkedList<String>());
//				StringBuffer trajText = new StringBuffer();
//
//				List<PoI> listPoIid = listPoIs(idT);
//				for (PoI p : listPoIid) {
////					numPois++;
//					save(p, t);
//
//					trajText.append(p.getName() + " ");
//					trajText.append(p.getId() + " ");
//					trajText.append(p.getCategory() + " ");
//
//					mapAsp.get("poi_traj").add(p.getName());
//					mapAsp.get("poi_id_traj").add(p.getId()+"");
//					mapAsp.get("category_traj").add(p.getCategory());
//
//					Set<String> aspectsSet = p.aspectNameSet();
//					for (String a : aspectsSet) {
//						Queue<String> listAsp = mapAsp.get(a);
//						if (listAsp == null) {
//							listAsp = new LinkedList<String>();
//							mapAsp.put(a, listAsp);
//						}
//
//						listAsp.add(p.getAspectValue(a));
//
//						trajText.append(p.getAspectValue(a) + " ");
//					}
//
////					if(numPois == 10 ) {
////						numPois = 0;
////						break;
////					}
//				}
//
//				updateTrajectoryText(t.getId(), trajText);
//				saveAspects(t, mapAsp);
//			}
//		} finally {
//			con.close();
//		}
//	}
//
//	public static void main(String[] args) throws SQLException {
//		long t1 = System.currentTimeMillis();
//		TrajectoryFoursquare main = new TrajectoryFoursquare();
//		main.initConnection("localhost", "25432", "postgres", "postgres");
//		main.process();
//		long t2 = System.currentTimeMillis();
//		System.out.println(t2-t1);
//		System.out.println((t2-t1)/1000);
//	}
}