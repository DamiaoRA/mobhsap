package mobhsap.datasource;

import java.sql.SQLException;

import mobhsap.model.Message;

public class ETL {
	private MessageDAO messageDAO;

	public ETL() throws SQLException {
		messageDAO = MessageDAO.getInstance();
	}

	
	public void nextMessage(Message m) throws Exception {
		insertFato(m);
	}


	private void insertFato(Message m) throws Exception {
		messageDAO.insertFato(m);
	}

	public void setAspectDAO(AspectDAOIF aspectDAO) throws SQLException {
		messageDAO.setAspectDAO(aspectDAO);
	}

	public void finish() throws SQLException {
		messageDAO.finish();
	}
}