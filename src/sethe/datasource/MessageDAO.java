package sethe.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import sethe.model.Message;

public class MessageDAO {

	private AspectDAOIF aspectDAO;
	
	private int idCategoryCounter = 1;
	private int idPoiCounter = 1;
	private int idTimeCounter = 1;
	private int idTrajCounter = 1;
	private int idUserCounter = 1;
	
	private int idTrajNow = 0;
	private Map<Integer, Integer> mapPoiRepetition;
	private int numPois = 0;

	protected PreparedStatement psCategoryInsert;
	protected PreparedStatement psCategorySearch;
	protected PreparedStatement psPoiInsert;
	protected PreparedStatement psPoiSearch;
	protected PreparedStatement psUserInsert;
	protected PreparedStatement psUserSearch;

	protected PreparedStatement psTrajectorySearch;
	protected PreparedStatement psTrajectoryInsert;

	protected PreparedStatement psTimeInsert;
	protected PreparedStatement psTimeSearch;
	
	protected PreparedStatement psPoiPosition;
	protected PreparedStatement psPoiRepetition;

	protected PreparedStatement psPoiCategoryInsert;

	private PreparedStatement psFato;

	private Map<String, Integer> mapId = new HashMap<String, Integer>();

	protected Connection conn;
	private static MessageDAO singleton;

	private MessageDAO() {
		mapPoiRepetition = new HashMap<Integer, Integer>();
	}

	private void start() throws SQLException {
		conn = HikariCPDataSource.getConnection();

		psCategoryInsert = conn.prepareStatement("INSERT INTO tb_category(id, name) VALUES (?,?)");
		psCategorySearch = conn.prepareStatement("SELECT id FROM tb_category WHERE name = ?");

		psPoiInsert = conn.prepareStatement("INSERT INTO tb_poi(id, x, y, name) VALUES (?,?,?,?)");
		psPoiSearch = conn.prepareStatement("SELECT id FROM tb_poi WHERE name = ?");

		psPoiCategoryInsert = conn.prepareStatement("INSERT INTO tb_poi_category(id_poi, id_category) VALUES(?,?)");

		psUserInsert = conn.prepareStatement("INSERT INTO tb_user (id, value) VALUES (?, ?)");
		psUserSearch = conn.prepareStatement("SELECT id FROM tb_user WHERE id=?");

		psTrajectoryInsert = conn.prepareStatement("INSERT INTO tb_trajectory (id, id_user, value) VALUES(?, ?, ?)");
		psTrajectorySearch = conn.prepareStatement("SELECT id FROM tb_trajectory WHERE id=?");

		psTimeInsert = conn.prepareStatement("INSERT INTO tb_time (id, minute, hour, day, month, year) VALUES (?,?,?,?,?,?)");
		psTimeSearch = conn.prepareStatement("SELECT id FROM tb_time WHERE minute=? AND hour=? AND day=? AND month=? AND year=?");

		psPoiPosition = conn.prepareStatement("SELECT count(*) FROM fato WHERE id_trajectory = ?");
		psPoiRepetition = conn.prepareStatement("SELECT count(*) FROM fato WHERE id_trajectory = ? AND id_poi = ?");
	}
	
	public void finish() throws SQLException {
		try {
			Statement st = conn.createStatement();
			st.execute("ALTER SEQUENCE tb_category_id_seq RESTART WITH " + idCategoryCounter);
			st.execute("ALTER SEQUENCE tb_poi_id_seq RESTART WITH " + idPoiCounter);
			st.execute("ALTER SEQUENCE tb_time_id_seq RESTART WITH " + idTimeCounter);
			aspectDAO.finish();
		} finally {
			HikariCPDataSource.close();
		}
	}

	public static synchronized MessageDAO getInstance() throws SQLException {
		if(singleton == null) {
			singleton = new MessageDAO();
			singleton.start();
		}
		return singleton;
	}

	public void setAspectDAO(AspectDAOIF dao) throws SQLException {
		aspectDAO = dao;
	}

	private void createFactPreparedStatement() throws SQLException {
		String aspectsName = "";
		String valuesInter = "";
		if(aspectDAO != null) {
			for(String s : aspectDAO.columnsAspectsId()) {
				aspectsName += "," + s;
				valuesInter += ",?";
			}
		}

		String sql = "INSERT INTO fato (id_poi, id_category, id_trajectory, id_time, position, repetition" + aspectsName + ") "
				+ "VALUES(?,?,?,?,?,? " + valuesInter + ") ";

		psFato = conn.prepareStatement(sql);
	}

//	public Integer insertCategory(Message m) throws SQLException {
//		Integer id = mapId.get("cat_" + m.getCategory());
//		if(id == null) {
//			id = idCategoryCounter++;
//
//			psCategoryInsert.setInt(1, id);
//			psCategoryInsert.setString(2, m.getCategory());
//			psCategoryInsert.execute();
//
//			mapId.put("cat_" + m.getCategory(), id);
//			return id;
//		}
//
//		return id;
//	}

//	public Integer insertPoiHierarchy(Message m) throws SQLException {
//		Integer idCat = insertCategory(m);
//		Integer idPoi = mapId.get("poi_" + m.getPoi());
//
//		if(idPoi == null) {
//			idPoi = idPoiCounter++;
//			psPoiInsert.setInt(1, idPoi);
//			psPoiInsert.setDouble(2, m.getX());
//			psPoiInsert.setDouble(3, m.getY());
//			psPoiInsert.setString(4, m.getPoi());
//			psPoiInsert.execute();
//
//			psPoiCategoryInsert.setInt(1, idPoi);
//			psPoiCategoryInsert.setInt(2, idCat);
//			psPoiCategoryInsert.execute();
//
//			mapId.put("poi_" + m.getPoi(), idPoi);
//		}
//		return idPoi;
//	}
	
	public Integer insertPoi(String poi, Message m) throws SQLException {
		Integer idPoi = mapId.get("poi_" + poi);
		if(idPoi == null) {
			idPoi = idPoiCounter++;
			psPoiInsert.setInt(1, idPoi);
			psPoiInsert.setDouble(2, m.getX());
			psPoiInsert.setDouble(3, m.getY());
			psPoiInsert.setString(4, poi);
			psPoiInsert.execute();

			mapId.put("poi_" + poi, idPoi);
		}
		return idPoi;
	}

	public Integer insertCategory(String cat) throws SQLException {
		Integer idCat = mapId.get("cat_" + cat);
		if(idCat == null) {
			idCat = idCategoryCounter++;
			psCategoryInsert.setInt(1, idCat);
			psCategoryInsert.setString(2, cat);
			psCategoryInsert.execute();

			mapId.put("cat_" + cat, idCat);
		}
		return idCat;
	}

	private Integer insertUser(Message m) throws SQLException {
		Integer idUser = mapId.get("user_" + m.getUserName());
		if(idUser == null) {
			idUser = idUserCounter++;
			psUserInsert.setInt(1, idUser);
			psUserInsert.setString(2, m.getUserName());
			psUserInsert.execute();

			mapId.put("user_" + m.getUserName(), idUser);
		}
		return idUser;
	}

	private Integer insertTrajectoryHierarchy(Message m) throws SQLException {
		Integer idUser = insertUser(m);
		Integer idTraj = mapId.get("traj_" + m.getTrajectoryName());

		if(idTraj == null) {
			idTraj = idTrajCounter++;
			psTrajectoryInsert.setInt(1, idTraj);
			psTrajectoryInsert.setInt(2, idUser);
			psTrajectoryInsert.setString(3, m.getTrajectoryName());
			psTrajectoryInsert.execute();

			mapId.put("traj_" + m.getTrajectoryName(), idTraj);
		}
		return idTraj;
	}


	private Integer insertTimeHierarchy(Message m) throws SQLException {
		Timestamp ts = m.getDatetime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ts);

		Integer idTime = searchTimeId(calendar);
		if(idTime == null) {
			idTime = idTimeCounter++;
			psTimeInsert.setInt(1, idTime);
			psTimeInsert.setInt(2, calendar.get(Calendar.MINUTE));
			psTimeInsert.setInt(3, calendar.get(Calendar.HOUR_OF_DAY));
			psTimeInsert.setInt(4, calendar.get(Calendar.DATE));
			psTimeInsert.setInt(5, calendar.get(Calendar.MONTH));
			psTimeInsert.setInt(6, calendar.get(Calendar.YEAR));

			psTimeInsert.execute();

			mapId.put("time_" 
					+ calendar.get(Calendar.MINUTE)
					+ calendar.get(Calendar.HOUR_OF_DAY)
					+ calendar.get(Calendar.DATE)
					+ calendar.get(Calendar.MONTH)
					+ calendar.get(Calendar.YEAR), idTime);
		}
		return idTime;
	}

	private Integer searchTimeId(Calendar calendar) throws SQLException {
		Integer idTime = mapId.get("time_" 
						+ calendar.get(Calendar.MINUTE)
						+ calendar.get(Calendar.HOUR_OF_DAY)
						+ calendar.get(Calendar.DATE)
						+ calendar.get(Calendar.MONTH)
						+ calendar.get(Calendar.YEAR));
		return idTime;
	}

	private int searchPoiPosition(int idTraj) throws SQLException {
		if(idTrajNow != idTraj) {
			idTrajNow = idTraj;
			numPois = 0;
			mapPoiRepetition = new HashMap<Integer, Integer>();
		}
		return ++numPois;

//		select count(*) from fato where id_trajectory = 1
//		psPoiPosition.setInt(1, idTraj);
//		ResultSet rs = psPoiPosition.executeQuery();
//		if(rs.next()) {
//			return rs.getInt(1);
//		}
//		return 0;
	}

	private int searchPoiRepetition(int idTraj, Integer idPoi) throws SQLException {
		if(idTrajNow != idTraj) {
			idTrajNow = idTraj;
			numPois = 0;
			mapPoiRepetition = new HashMap<Integer, Integer>();
		}

		Integer rep = mapPoiRepetition.get(idPoi);
		if(rep == null) {
			rep = 0;
		}
		rep++;
		mapPoiRepetition.put(idPoi, rep);
		return rep;

//		select count(*) from fato where id_trajectory = 1 and id_poi = 2
//		psPoiRepetition.setInt(1, idTraj);
//		psPoiRepetition.setInt(2, idPoi);
//		ResultSet rs = psPoiRepetition.executeQuery();
//		if(rs.next()) {
//			return rs.getInt(1);
//		}
//		return 0;
	}

	/**
	 * Create a Fact register
	 * @param m
	 * @throws SQLException
	 */
	public void insertFato(Message m) throws SQLException {
		int poiPosition = 0;
		int poiRepetition = 0;

		for(String poi : m.getPois()) {
			for(String cat : m.getCategories()) {
				if(psFato == null)
					createFactPreparedStatement();

				Integer idPoi = insertPoi(poi, m);
				Integer idCat = insertCategory(cat);
				Integer idTraj = insertTrajectoryHierarchy(m);
				Integer idTime = insertTimeHierarchy(m);

				psFato.setInt(1, idPoi);
				psFato.setInt(2, idCat);
				psFato.setInt(3, idTraj);
				psFato.setInt(4, idTime);

				if(poiPosition == 0)
					poiPosition = searchPoiPosition(idTraj);
				poiRepetition = searchPoiRepetition(idTraj, idPoi);

				psFato.setInt(5, poiPosition);
				psFato.setInt(6, poiRepetition);

				aspectDAO.putAspectsValues(psFato, 7, m);
				psFato.execute();
			}
		}
	}

	public void calculateTrajectorySize() throws SQLException {
		String sql = "UPDATE tb_trajectory set size = ("
				   + "SELECT max(position) "
				   + "FROM fato "
				   + "WHERE fato.id_trajectory = tb_trajectory.id)";

		conn.createStatement().executeLargeUpdate(sql);		
	}
}