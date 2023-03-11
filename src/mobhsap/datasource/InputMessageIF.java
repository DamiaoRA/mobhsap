package sethe.datasource;

import sethe.model.Message;

public interface InputMessageIF {

	public Message nextMessage() throws Exception;

}
