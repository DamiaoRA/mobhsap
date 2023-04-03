package mobhsap.tripbuilder;

public class Move {
	private String id;
	private Stop from;
	private Stop to;
	private String transport;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Stop getFrom() {
		return from;
	}
	public void setFrom(Stop from) {
		this.from = from;
	}
	public Stop getTo() {
		return to;
	}
	public void setTo(Stop to) {
		this.to = to;
	}
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
}