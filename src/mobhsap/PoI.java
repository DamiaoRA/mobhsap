package mobhsap;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PoI {
	private int id;
	private int idTrajectory;
	private double lat;
	private double lon;
	private Timestamp time;
	private int position;
	private String name;
	private String category;
	private Map<String, String> aspects;

	public PoI() {
		aspects = new HashMap<String, String>();
	}

	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public Map<String, String> getAspects() {
		return aspects;
	}
	public void setAspects(Map<String, String> aspects) {
		this.aspects = aspects;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void addAspect(String key, String v) {
		aspects.put(key, v);
	}

	public Set<String> aspectNameSet() {
		return aspects.keySet();
	}

	public String getAspectValue(String a) {
		return aspects.get(a);
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKey() {
		return name + getLat() + getLon();
	}
	public int getIdTrajectory() {
		return idTrajectory;
	}
	public void setIdTrajectory(int idTrajectory) {
		this.idTrajectory = idTrajectory;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getStringExpression(boolean isCategory) {
		if(isCategory)
			return getCategory();
		return getName();
	}

	public String toStringPoI() {
		String s = "[" + getPosition() + " " + getName() + " ";
		for(String key : aspects.keySet()) {
			s += key + ":" + aspects.get(key) + " ";
		}
		s += "] ";
		
		return s;
	}
	
	public String toStringCategory() {
		String s = "[" + getPosition() + " " + getCategory() + " ";
		for(String key : aspects.keySet()) {
			s += key + ":" + aspects.get(key) + " ";
		}
		s += "] ";
		
		return s;
	}
}