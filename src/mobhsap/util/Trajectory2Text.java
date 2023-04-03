package mobhsap.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobhsap.datasource.MessageSearchDAO;
import mobhsap.model.Message;

public class Trajectory2Text {
	private MessageSearchDAO dao;
	private List<String> tables;
	private String separator;
	private Map<String, String> mapTrajectories;

	public Trajectory2Text(String[] aspectsName, String separator) throws SQLException {
		this.separator = separator;
		mapTrajectories = new HashMap<>();
		dao = MessageSearchDAO.getInstance();

		createTable("poi");
		createTable("category");
		for(String asp : aspectsName) {
			createTable(asp);
		}
	}

	public void nextMessage(Message m) throws SQLException {
		String trajId = m.getTrajectoryNumber();

		insertAspValue(trajId, "poi", m.getPoisName());
		insertAspValue(trajId, "category", m.getCategoriesName());
		for(String asp : m.getaspectsType()) {
			insertAspValue(trajId, asp, m.getAspectValue(asp));
		}
	}

	private void insertAspValue(String trajId, String aspName, String aspValue) throws SQLException {
		String key = trajId + "_" + aspName;
		String text = mapTrajectories.get(key);
		if(text == null) {
			text = aspValue;
			insert(trajId, aspName, text);
		} else {
			text += separator + aspValue;
			update(trajId, aspName, text);
		}
		mapTrajectories.put(key, text);
	}

	private void update(String trajId, String aspName, String aspValue) throws SQLException {
		dao.update("tb_" + aspName, trajId, aspValue);
	}

	private void insert(String trajId, String aspName, String aspValue) throws SQLException {
		dao.insert("tb_" + aspName, trajId, aspValue);
	}

	private void createTable(String aspName) throws SQLException {
		String t = "CREATE TABLE tb_" + aspName + " (trajectory_id varchar(200) NOT NULL, value TEXT)";
		dao.executeDDL(t);
	}

	public Trajectory2Text(String separator) {
		this.separator = separator;
	}
}