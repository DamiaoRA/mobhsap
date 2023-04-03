package mobhsap.datasource;

import mobhsap.model.Message;

public interface InputMessageIF {

	public Message nextMessage() throws Exception;

}
