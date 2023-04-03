package mobhsap.foursquare.model;

import mobhsap.model.Message;

public class MessageFoursquare extends Message {
	private String weather;
	private double rating;
	private double price;
	private String day;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

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
		return weather + "," + rating + "," + price+ "," + day;
	}

	@Override
	public String getAspectType() {
		return "weather,rating,price,day";
	}

	@Override
	public String[] getaspectsType() {
		String a[] = {"weather","rating","price","day"};
		return a;
	}

	@Override
	public String getAspectValue(String asp) {
		if(asp.equals("weather"))
			return weather;
		if(asp.equals("rating"))
			return rating+"";
		if(asp.equals("price"))
			return price+"";
		if(asp.equals("day"))
			return day;
		return null;
	}
}