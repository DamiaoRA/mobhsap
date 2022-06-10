package sethe.datasource;

import java.util.HashMap;

import sethe.model.Message;

public class MessageDAO {

	private HashMap<String, Integer> mapCategories;
	private DataWarehouseIF dw;

	public void insertCategory(Message m) {
		Integer id = mapCategories.get(m.getCategory());

		if(id == null) {
			id = dw.insertCategory(m);
			mapCategories.put(m.getCategory(), id);
		}

		// TODO Auto-generated method stub
		INSERT INTO tb_category(id int, name varchar(200) VALUES (?, ?)
	}

}
