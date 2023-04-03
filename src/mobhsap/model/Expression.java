package mobhsap.model;

import java.util.HashMap;
import java.util.Map;

import mobhsap.util.Constants;
import mobhsap.util.StringUtils;


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
	private boolean isFinal = false;
	private boolean isOptional = false;
	private boolean isPlus = false;
	private boolean anyValue = false;
	private String cleanValue;

	private Map<String, AspectExpression> mapAspects;
	private Map<String, String> mapProximity;

	private boolean checkProximity;

	public Expression(String cat, String namePoi) {
		this.valueCategory = cat;
		this.valuePoi = namePoi;
		
		if(namePoi == null) {
			this.isCategory = true;
			check(cat);
		} else if(cat == null || cat.isEmpty() || cat.equals(".*")) {
			this.isCategory = false;
			check(namePoi);
		} else {
			this.isCategory = true;
			check(cat);
		}

		mapAspects = new HashMap<String, AspectExpression>();
		mapProximity = new HashMap<String, String>();
		checkProximity = false;
		isFinal = false;
	}

	private void check(String text) {
		if(StringUtils.isAnyValue(text)) {
			anyValue = true;
			if(isCategory)
				this.valueCategory = text.replace(".*", "\\w*").replace(".+", "\\w+");
			else
				this.valuePoi = text.replace(".*", "\\w*").replace(".+", "\\w+");
		} else {
			if (text.endsWith("?")) {
				isOptional = true;
				cleanValue = text.replaceAll("\\?", "");
			}
			if (StringUtils.isPlusExpression(text)) { 
				int i = text.lastIndexOf("+");
				StringBuffer sb = new StringBuffer(text);
				sb.deleteCharAt(i);
				cleanValue = sb.toString();
				isPlus = true;
			} 
			if(StringUtils.isPlusAnyExpression(text)) {
				int i = text.lastIndexOf("*");
				StringBuffer sb = new StringBuffer(text);
				sb.deleteCharAt(i);
				cleanValue = sb.toString();
				isOptional = true;
				isPlus = true;
			}
		}

		if(cleanValue == null) {
			cleanValue = text;
		}
	}

	public String getCleanValue() {
		return cleanValue;
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
	
	public String aspectValue(String aspectType) {
		AspectExpression aspQuery = mapAspects.get(aspectType);
		return aspQuery.getValue();
	}
	
	public AspectExpression searchAspectExpression(String aspectType) {
		AspectExpression aspQuery = mapAspects.get(aspectType);
		return aspQuery;
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

	public boolean isFinal() {
		return isFinal;
	}

	public void setIsFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public boolean isPlus() {
		return isPlus;
	}

	public void setPLus(boolean isPLus) {
		this.isPlus = isPLus;
	}

	public boolean isAnyValue() {
		return anyValue;
	}
}