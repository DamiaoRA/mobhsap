package mobhsap.tripbuilder;

public class Stop {
	private String id;
	private Move move; //Como o MO saiu desse Stop
	private PoI poi;
	
	private Double lat;
	private Double lon;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public PoI getPoi() {
		return poi;
	}
	public void setPoi(PoI poi) {
		this.poi = poi;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Move getMove() {
		return move;
	}
	public void setMove(Move move) {
		this.move = move;
	}

	public static void main(String[] args) {
		StringBuilder s = new StringBuilder("abc");
		change(s);
		System.out.println(s);
	}
	public static void change(StringBuilder s) {
		s.delete(0, s.length()).append("xyz");
	}
}
