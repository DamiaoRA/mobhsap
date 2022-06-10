package sethe.datasource;

import sethe.model.Message;

public abstract class ETL {
	private MessageDAO messageDAO;

	public Message nextMessage() {
		return null;
	}

	public void start() {
		Message m = null;
		while((m=nextMessage()) != null) {
			insertCategory(m);
			insertPoi(m);
			insertUser(m);
			insertDateTime(m);
			insertAspects(m);
		}
	}

	private void insertCategory(Message m) {
		messageDAO.insertCategory(m);
	}
	
	private void insertPoi(Message m) {
		messageDAO.insertPoi(m);
	}
	
	private void insertUser(Message m) {
		messageDAO.insertUser(m);
	}

	private void insertDateTime(Message m) {
		messageDAO.insertDateTime(m);
	}

	private void insertAspects(Message m) {
		messageDAO.insertAspects(m);
	}
}