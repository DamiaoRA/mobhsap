package mobhsap.tripbuilder;

import java.util.ArrayList;
import java.util.List;

public class PoI {
	private String id;
	private String label;
	private List<String> categories;
	private String locatedIn;

	public PoI() {
		categories = new ArrayList<String>();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public String getLocatedIn() {
		return locatedIn;
	}
	public void setLocatedIn(String locatedIn) {
		this.locatedIn = locatedIn;
	}
	public void addCategory(String c) {
		categories.add(c);
	}

	public String getCategoriesText() {
		String value = "";
		for(String cat : categories) {
			value += cat + ";";
		}
		if(!value.isEmpty()) {
			value = value.substring(0, value.length()-1);
		}
		return value;
	}
}