package mobhsap.model;

import java.sql.Timestamp;
import java.util.Set;

public abstract class Message {
	private Set<String> categories;
	private Set<String> pois;
	private String trajectoryNumber;
	private String userName;
	private double x;
	private double y;
	private Timestamp datetime;
	private String city;
	private String state;
	private String country;

	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	public Set<String> getPois() {
		return pois;
	}

	public void setPois(Set<String> pois) {
		this.pois = pois;
	}

	public String getTrajectoryNumber() {
		return trajectoryNumber;
	}

	public void setTrajectoryNumber(String num) {
		this.trajectoryNumber = num;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Timestamp getDatetime() {
		return datetime;
	}

	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String toString() {
		return getUserName() + "\n" + getTrajectoryNumber() + "\n" + getDatetime() + "\n" + getPois() + "\n" + getCategories()
				+ "\n";
	}

	public String getOneCategory() {
		if(getCategories().isEmpty())
			return null;
		for(String c : getCategories()) return c;
		return null;
	}

	public String getOnePoi() {
		if(getPois().isEmpty())
			return null;
		for(String p:getPois()) return p;
		return null;
	}

	public String poiToString() {
		return x + y + getOnePoi() + getOneCategory() + city + state + country;
	}

	public abstract String getAspectsToString();
	
	public abstract String getAspectType();

	public abstract String[] getaspectsType();
	
	public String getPoisName() {
		String result = "";
		if(!pois.isEmpty()) {
			for(String p : pois) {
				result +=";" + p;
			}
			result = result.substring(1);
		}
		return result;
	}

	public String getCategoriesName() {
		String result = "";
		if(!pois.isEmpty()) {
			for(String p : categories) {
				result +=";" + p;
			}
			result = result.substring(1);
		}
		return result;
	}

	public abstract String getAspectValue(String asp);
}