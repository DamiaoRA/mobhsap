package sethe.tripbuilder.model;

import sethe.model.Message;

public class MessageTripbuilder extends Message {
	private String transportMeans;

	public String getTransportMeans() {
		return transportMeans;
	}

	public void setTransportMeans(String transportMeans) {
		this.transportMeans = transportMeans;
	}
	
	public String toString() {
		return super.toString() + transportMeans + "\n";
	}

	@Override
	public String getAspectsToString() {
		return transportMeans;
	}

	@Override
	public String getAspectType() {
		return "transportMeans";
	}
}
