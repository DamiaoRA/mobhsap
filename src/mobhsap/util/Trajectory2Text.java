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

	public void nextMessage(Message m) throws Exception {
		String trajId = m.getTrajectoryNumber();

		insertAspValue(trajId, "poi", m.getPoisName());
		insertAspValue(trajId, "category", m.getCategoriesName());
		for(String asp : m.getaspectsType()) {
			insertAspValue(trajId, asp, m.getAspectValue(asp));
		}
	}

	private void insertAspValue(String trajId, String aspName, String aspValue) throws Exception {
		Long t1 = System.currentTimeMillis();
		
		String key = trajId + "_" + aspName;
		String text = mapTrajectories.get(key);
		if(text == null) {
			text = aspValue;
			insert(trajId, aspName, text);
			
			Long t2 = System.currentTimeMillis();
			t2 = System.currentTimeMillis();
			ComponentStatistics.getInstance().setTraj2TextTime(t2-t1); //statics traj2text
		} else {
			text += separator + aspValue;
			update(trajId, aspName, text);
			
			Long t2 = System.currentTimeMillis();
			t2 = System.currentTimeMillis();
			ComponentStatistics.getInstance().setDataManagerTime(t2-t1); //statics traj2text
		}
		mapTrajectories.put(key, text);
	}

	private void update(String trajId, String aspName, String aspValue) throws Exception {
		Long t1 = System.currentTimeMillis();
		
		dao.update("tb_" + aspName, trajId, aspValue);

		Long t2 = System.currentTimeMillis();
		ComponentStatistics.getInstance().setInputTime(t2-t1); //static dw manager
	}

	private void insert(String trajId, String aspName, String aspValue) throws Exception {
		Long t1 = System.currentTimeMillis();
		
		dao.insert("tb_" + aspName, trajId, aspValue);
		
		Long t2 = System.currentTimeMillis();
		ComponentStatistics.getInstance().setInputTime(t2-t1); //static dw manager
	}

	private void createTable(String aspName) throws SQLException {
		String t = "CREATE TABLE tb_" + aspName + " (trajectory_id varchar(200) NOT NULL, value TEXT)";
		dao.executeDDL(t);
	}

	public Trajectory2Text(String separator) {
		this.separator = separator;
	}
}