package mobhsap.datasource;

import java.sql.SQLException;

import mobhsap.model.Message;

public class ETL {
	private MessageDAO messageDAO;
//	private InputMessageIF input;

	public ETL() throws SQLException {
		messageDAO = MessageDAO.getInstance();
	}

//	public Message nextMessage() throws Exception {
//		return input.nextMessage();
//	}
	
	public void nextMessage(Message m) throws SQLException {
		insertFato(m);
	}

//	public void start() throws Exception {
//		Message m = nextMessage();
//		while(m != null) {
//			insertFato(m);
//			m = nextMessage();
//		}
////		messageDAO.calculateTrajectorySize();
//		messageDAO.finish();
//	}

	private void insertFato(Message m) throws SQLException {
		messageDAO.insertFato(m);
	}

//	public InputMessageIF getInput() {
//		return input;
//	}
//
//	public void setInput(InputMessageIF input) {
//		this.input = input;
//	}

	public void setAspectDAO(AspectDAOIF aspectDAO) throws SQLException {
		messageDAO.setAspectDAO(aspectDAO);
	}

	public void finish() throws SQLException {
		messageDAO.finish();
	}
}