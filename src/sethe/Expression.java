package sethe;

import java.util.HashMap;
import java.util.Map;

import sethe.util.Constants;


/**
 * An expression is an annotation in regular language with the aim of finding a 
 * point or set of points on a trajectory.
 *
 */
public class Expression {
	private String valueCategory;
	private String valuePoi;
	private int order;
	private boolean isCategory;

	private Map<String, AspectExpression> mapAspects;
	private Map<String, String> mapProximity;

	private boolean checkProximity;

	public Expression() {
		mapAspects = new HashMap<String, AspectExpression>();
		mapProximity = new HashMap<String, String>();
		checkProximity = false;
	}
	
	public void addProximity(String aspectName, String value) {
		mapProximity.put(aspectName, value.trim());
	}

	public String getValueCategory() {
		return valueCategory;
	}

	public void setValueCategory(String valueCategory) {
		this.valueCategory = valueCategory;
	}

	public String getValuePoi() {
		return valuePoi;
	}

	public void setValuePoi(String valuePoi) {
		this.valuePoi = valuePoi;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getValue() {
		if(this.isCategory)
			return valueCategory;
		return valuePoi;
	}

	public void addAspect(String aspName, String value) {
		
		AspectExpression exp = new AspectExpression();
		exp.setType(aspName);
		if(isUntilValue(value)) exp.setUntil(true);
		value = value.replaceAll(Constants.REPEATED_PATTERN, "").trim();
		exp.setValue(value);
		exp.setOrder(order);

		mapAspects.put(aspName, exp);
	}

	public String getRegexPoi() {
		if(getValuePoi() == null)
			return null;
		if(getValuePoi().trim().equals(".*") || getValuePoi().trim().equals("."))
			return "(.)*";
		return "(" + getValuePoi() + ")";
	}

	public String getRegexCategory() {
		if(getValueCategory() == null)
			return null;
		if(getValueCategory().trim().equals(".*") || getValueCategory().trim().equals("."))
			return "(.)*";
		return "(" + getValueCategory() + ")";
	}

	public boolean isCategory() {
		return isCategory;
	}

	public void setCategory(boolean isCategory) {
		this.isCategory = isCategory;
	}

	public Map<String, AspectExpression> getMapAspects() {
		return mapAspects;
	}

	public void setMapAspects(Map<String, AspectExpression> mapAspects) {
		this.mapAspects = mapAspects;
	}

	public Map<String, String> getMapProximity() {
		return mapProximity;
	}

	public void setMapProximity(Map<String, String> mapProximity) {
		this.mapProximity = mapProximity;
	}

	public Double distance(String aspectType, String aspectValue, String function, Double limit, Double weight, PoI p1, PoI p2, Trajectory trajectory) {
		AspectExpression aspQuery = mapAspects.get(aspectType);
		String idealValue = aspQuery.getValue();

		if(idealValue.equals(Constants.ANY_VALUE))
			return 1d;

		double result = 0d;

		//
		if(aspQuery.isUntil()) {
			int pos1 = p1.getPosition();
			int pos2 = p2.getPosition();

			Double value = 0d;
			String tempAspectValue = "";
			for(int i = pos1+1; i <= pos2; i++) {
				tempAspectValue = trajectory.getAspectValuePoI(i, aspectType);
				value += Distance.calc(function, tempAspectValue, idealValue, weight, limit);
			}

			result = value/((pos2-pos1));
		//
		} else {
			result = Distance.calc(function, aspectValue, idealValue, weight, limit);
		}
		return result;
	}

	private boolean isUntilValue(String aspect) {
		if(aspect != null && !aspect.isEmpty()) {
			if(aspect.trim().startsWith(Constants.REPEATED))
				return true;
		}
		return false;
	}

	public boolean isCheckProximity() {
		return checkProximity;
	}

	public void setCheckProximity(boolean checkProximity) {
		this.checkProximity = checkProximity;
	}
}