package sethe.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import sethe.model.Message;

public class DWAccess {

	private AspectDAOIF aspectDAO;

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

	protected Connection conn;
	private static DWAccess singleton;

	private DWAccess() {

	}

	private void start() throws SQLException {
		conn = HikariCPDataSource.getConnection();

		

		psCategoryInsert = conn.prepareStatement("INSERT INTO tb_category(name) VALUES (?)");
		psCategorySearch = conn.prepareStatement("SELECT id FROM tb_category WHERE name = ?");

		psPoiInsert = conn.prepareStatement("INSERT INTO tb_poi(x, y, name) VALUES (?,?,?)");
		psPoiSearch = conn.prepareStatement("SELECT id FROM tb_poi WHERE name = ?");

		psPoiCategoryInsert = conn.prepareStatement("INSERT INTO tb_poi_category(id_poi, id_category) VALUES(?,?)");

		psUserInsert = conn.prepareStatement("INSERT INTO tb_user (id) VALUES (?)");
		psUserSearch = conn.prepareStatement("SELECT id FROM tb_user WHERE id=?");

		psTrajectoryInsert = conn.prepareStatement("INSERT INTO tb_trajectory (id, id_user) VALUES(?, ?)");
		psTrajectorySearch = conn.prepareStatement("SELECT id FROM tb_trajectory WHERE id=?");

		psTimeInsert = conn.prepareStatement("INSERT INTO tb_time (minute, hour, day, month, year) VALUES (?,?,?,?,?)");
		psTimeSearch = conn.prepareStatement("SELECT id FROM tb_time WHERE minute=? AND hour=? AND day=? AND month=? AND year=?");

		psPoiPosition = conn.prepareStatement("SELECT count(*) FROM fato WHERE id_trajectory = ?");
		psPoiRepetition = conn.prepareStatement("SELECT count(*) FROM fato WHERE id_trajectory = ? AND id_poi = ?");
	}

	public static synchronized DWAccess getInstance() throws SQLException {
		if(singleton == null) {
			singleton = new DWAccess();
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

		String sql = "INSERT INTO fato (id_poi, id_trajectory, id_time, position, repetition" + aspectsName + ") "
				+ "VALUES(?,?,?,?,? " + valuesInter + ") ";
		psFato = conn.prepareStatement(sql);
	}

	public Integer insertCategory(Message m) throws SQLException {
		try {
			return searchCategory(m.getCategory());
		} catch(SQLException e) {
			psCategoryInsert.setString(1, m.getCategory());
			psCategoryInsert.execute();
		}

		return searchCategory(m.getCategory());
	}

	private Integer searchCategory(String category) throws SQLException {
		psCategorySearch.setString(1, category);
		ResultSet rs = psCategorySearch.executeQuery();
		if(rs.next()) {
			return rs.getInt(1);
		}
		throw new SQLException("Category " + category + " not found");
	}

	public Integer insertPoiHierarchy(Message m) throws SQLException {
		Integer idCat = insertCategory(m);
		Integer idPoi = searchPoi(m.getPoi());

		if(idPoi == null) {		
			psPoiInsert.setDouble(1, m.getX());
			psPoiInsert.setDouble(2, m.getY());
			psPoiInsert.setString(3, m.getPoi());
			psPoiInsert.execute();

			idPoi = searchPoi(m.getPoi());

			psPoiCategoryInsert.setInt(1, idPoi);
			psPoiCategoryInsert.setInt(2, idCat);
			psPoiCategoryInsert.execute();
		}
		return idPoi;
	}

	private Integer searchPoi(String name) throws SQLException {
		psPoiSearch.setString(1, name);
		ResultSet rs = psPoiSearch.executeQuery();
		if(rs.next()) {
			return rs.getInt(1);
		}
		return null;
	}

	private Integer insertUser(Message m) throws SQLException {
		Integer idUser = searchUser(m.getUserId());
		if(idUser == null) {
			psUserInsert.setInt(1, m.getUserId());
			psUserInsert.execute();
			idUser = m.getUserId();
		}
		return idUser;
	}

	private Integer searchUser(int id) throws SQLException {
		psUserSearch.setInt(1, id);
		ResultSet rs = psUserSearch.executeQuery();
		if(rs.next())
			return rs.getInt(1);
		return null;
	}

	private Integer searchTrajectory(int idTraj) throws SQLException {
		psTrajectorySearch.setInt(1, idTraj);
		ResultSet rs = psTrajectorySearch.executeQuery();
		if(rs.next())
			return rs.getInt(1);
		return null;
	}

//	private Integer insertTrajectory(Message m) throws SQLException {
//		Integer idTraj = searchTrajectory(m.getTrajectoryId());
//		if(idTraj == null) {
//			psTrajectoryInsert.setInt(1, m.getTrajectoryId());
//			idTraj = m.getTrajectoryId();
//		}
//		return idTraj;
//	}

	private Integer insertTrajectoryHierarchy(Message m) throws SQLException {
		Integer idUser = insertUser(m);
		Integer idTraj = searchTrajectory(m.getTrajectoryId());

		if(idTraj == null) {
			psTrajectoryInsert.setInt(1, m.getTrajectoryId());
			psTrajectoryInsert.setInt(2, idUser);
			psTrajectoryInsert.execute();
			idTraj = m.getTrajectoryId();
		}
		return idTraj;
	}


	private Integer insertTimeHierarchy(Message m) throws SQLException {
		Timestamp ts = m.getDatetime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ts);

		Integer idTime = searchTimeId(calendar);
		if(idTime == null) {
			psTimeInsert.setInt(1, calendar.get(Calendar.MINUTE));
			psTimeInsert.setInt(2, calendar.get(Calendar.HOUR_OF_DAY));
			psTimeInsert.setInt(3, calendar.get(Calendar.DATE));
			psTimeInsert.setInt(4, calendar.get(Calendar.MONTH));
			psTimeInsert.setInt(5, calendar.get(Calendar.YEAR));

			psTimeInsert.execute();
			idTime = searchTimeId(calendar);
		}
		return idTime;
	}

	private Integer searchTimeId(Calendar calendar) throws SQLException {
		psTimeSearch.setInt(1, calendar.get(Calendar.MINUTE));
		psTimeSearch.setInt(2, calendar.get(Calendar.HOUR_OF_DAY));
		psTimeSearch.setInt(3, calendar.get(Calendar.DATE));
		psTimeSearch.setInt(4, calendar.get(Calendar.MONTH));
		psTimeSearch.setInt(5, calendar.get(Calendar.YEAR));

		ResultSet rs = psTimeSearch.executeQuery();
		if(rs.next()) {
			return rs.getInt(1);
		}
		return null;
	}

	private int searchPoiPosition(Integer idTraj) throws SQLException {
//		select count(*) from fato where id_trajectory = 1
		psPoiPosition.setInt(1, idTraj);
		ResultSet rs = psPoiPosition.executeQuery();
		if(rs.next()) {
			return rs.getInt(1);
		}
		return 0;
	}

	private int searchPoiRepetition(Integer idTraj, Integer idPoi) throws SQLException {
//		select count(*) from fato where id_trajectory = 1 and id_poi = 2
		psPoiRepetition.setInt(1, idTraj);
		psPoiRepetition.setInt(2, idPoi);
		ResultSet rs = psPoiRepetition.executeQuery();
		if(rs.next()) {
			return rs.getInt(1);
		}
		return 0;
	}

	/**
	 * Create a Fact register
	 * @param m
	 * @throws SQLException
	 */
	public void insertFato(Message m) throws SQLException {
		if(psFato == null)
			createFactPreparedStatement();
		
		Integer idPoi = insertPoiHierarchy(m);
		Integer idTraj = insertTrajectoryHierarchy(m);
		Integer idTime = insertTimeHierarchy(m);

		psFato.setInt(1, idPoi);
		psFato.setInt(2, idTraj);
		psFato.setInt(3, idTime);

		int poiPosition = searchPoiPosition(idTraj);
		int poiRepetition = searchPoiRepetition(idTraj, idPoi);

		psFato.setInt(4, poiPosition+1);
		psFato.setInt(5, poiRepetition+1);

		aspectDAO.putAspectsValues(psFato, 6, m);

		psFato.execute();
	}
}