package mobhsap.tripbuilder.model;

import mobhsap.model.Message;
import mobhsap.util.StringUtils;

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
		return "move";
	}

	@Override
	public String[] getaspectsType() {
		String [] a = {"move"};
		return a;
	}

	@Override
	public String getAspectValue(String asp) {
		if(StringUtils.isEmpty(transportMeans))
			return "N/A";
		return transportMeans;
	}
}
