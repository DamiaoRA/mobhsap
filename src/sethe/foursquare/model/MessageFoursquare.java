package sethe.foursquare.model;

import sethe.model.Message;

public class MessageFoursquare extends Message {
	private String weather;
	private double rating;
	private double price;

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

	@Override
	public String toString() {
		return getAspectsToString();
	}

	@Override
	public String getAspectsToString() {
		return weather + "," + rating + "," + price;
	}

	@Override
	public String getAspectType() {
		return "weather,rating,price";
	}
}