package mobhsap;

/**
 * Class that represents the regular language expression of an aspect of the trajectory.
 *
 */
public class AspectExpression {
	private String value;
	private String type;
	private Double weight;
	/**
	 * Says if the expression contains an until operator, that is, the aspect 
	 * must be the same for all previous stops until the second-to-last expression 
	 * before this one.
	 */
	private boolean until; 
	private int order;

	public AspectExpression() {
		until = false;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public boolean isUntil() {
		return until;
	}
	public void setUntil(boolean until) {
		this.until = until;
	}
}