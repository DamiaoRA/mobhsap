package sethe.datasource;

import java.sql.SQLException;

import sethe.model.Message;

public class MessageDAO {

//	private HashMap<String, Integer> mapCategories;
//	private HashMap<String, Integer> mapPois;
	private DWAccess dw;
	
	public MessageDAO() throws SQLException {
//		mapCategories = new HashMap<String, Integer>();
		dw = DWAccess.getInstance();
	}

//	public Integer insertCategory(Message m) throws SQLException {	
//		Integer id = mapCategories.get(m.getCategory());
//
//		if(id == null) {
//			id = dw.insertCategory(m);
//			mapCategories.put(m.getCategory(), id);
//		}
//
//		return id;
//	}

//	public void insertPoiInCategory(Message m) throws SQLException {
//		dw.insertPoiHierarchy(m);
//	}

	public void insertFato(Message m) throws SQLException {
		dw.insertFato(m);
	}

	public void setAspectDAO(AspectDAOIF aspectDAO) throws SQLException {
		dw.setAspectDAO(aspectDAO);
	}
}