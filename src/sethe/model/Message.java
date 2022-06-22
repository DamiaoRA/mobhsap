package sethe.model;

import java.sql.Timestamp;
import java.util.Set;

public class Message {
	private Set<String> categories;
	private Set<String> pois;
	private String trajectoryName;
	private String userName;
	private double x;
	private double y;
	private Timestamp datetime;

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

	public String getTrajectoryName() {
		return trajectoryName;
	}

	public void setTrajectoryName(String trajectoryName) {
		this.trajectoryName = trajectoryName;
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

	public String toString() {
		return getUserName() + "\n" + getTrajectoryName() + "\n" + getDatetime() + "\n" + getPois() + "\n" + getCategories()
				+ "\n";
	}
}
