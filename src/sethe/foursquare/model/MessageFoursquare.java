package sethe.foursquare.model;

import sethe.model.Message;

public class MessageFoursquare extends Message {
	private String weather;
	private double rating;
	private double price;
	private String day;

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return super.toString() + "\n" + price + "\n";
	}
}