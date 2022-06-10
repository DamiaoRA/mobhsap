package sethe.model;

import java.sql.Timestamp;

public class Message {
	private String category;
	private String poi;
	private String user;
	private double x;
	private double y;
	private Timestamp datetime;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPoi() {
		return poi;
	}

	public void setPoi(String poi) {
		this.poi = poi;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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
}
