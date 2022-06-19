package sethe.datasource;

import java.sql.SQLException;

import sethe.model.Message;

public class ETL {
	private MessageDAO messageDAO;
	private InputMessageIF input;
	
	public ETL() throws SQLException {
		messageDAO = new MessageDAO();
	}

	public Message nextMessage() throws Exception {
		return input.nextMessage();
	}

	public void start() throws Exception {
		Message m = nextMessage();
		while(m != null) {
//			insertCategory(m);
//			insertPoiInCategory(m);
//			insertUser(m);
//			insertDateTime(m);
//			insertAspects(m);
			insertFato(m);
			m = nextMessage();
		}
	}

//	private void insertCategory(Message m) throws SQLException {
//		messageDAO.insertCategory(m);
//	}

	private void insertFato(Message m) throws SQLException {
		messageDAO.insertFato(m);
	}

//	private void insertPoiInCategory(Message m) throws SQLException {
//		messageDAO.insertPoiInCategory(m);
//	}
	
//	private void insertPoi(Message m) {
//		messageDAO.insertPoi(m);
//	}
//	
//	private void insertUser(Message m) {
//		messageDAO.insertUser(m);
//	}
//
//	private void insertDateTime(Message m) {
//		messageDAO.insertDateTime(m);
//	}
//
//	private void insertAspects(Message m) {
//		messageDAO.insertAspects(m);
//	}

	public InputMessageIF getInput() {
		return input;
	}

	public void setInput(InputMessageIF input) {
		this.input = input;
	}

	public void setAspectDAO(AspectDAOIF aspectDAO) throws SQLException {
		messageDAO.setAspectDAO(aspectDAO);
	}
}