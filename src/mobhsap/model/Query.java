package mobhsap.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mobhsap.util.Constants;
import mobhsap.util.StringUtils;

public class Query {
	
	private String name;
	private CompositeQuery query;
	
	private Expression[] arrayExp; // Expressões regulares do PoI

	private Map<String, Double> mapWeight; 			// Peso dos aspectos
	private Map<String, String> mapDistanceFunc; 	// Função de distância
	private Map<String, Double> mapLimit; 			// limite máximo para a função de distância

	private Map<String, Trajectory> mapResultQuery;  //Armazena o resultado da consulta <id, trajetória>
	
	private String pkColumn = "traj_fk";
	private String valueColumn = "values";

	private String distanceFunction;

	public Query(String name, CompositeQuery cquery) {
		this.name = name;
		this.query = cquery;
		mapWeight = new HashMap<String, Double>();
		mapDistanceFunc = new HashMap<String, String>();
		mapLimit = new HashMap<String, Double>();
		mapResultQuery = new HashMap<String, Trajectory>();

		if(query.getPkTrajColumnName() != null) pkColumn = query.getPkTrajColumnName();
		if(query.getValueColumnName() != null) valueColumn = query.getValueColumnName();
	}

	public void init(int arraySize) {
		arrayExp = new Expression[arraySize];
	}

	public void addExpression(int index, String catExp, String poiExp, double weight) {
		Expression exp = new Expression(catExp, poiExp);
		exp.setOrder(index);
//		exp.setWeight(weight);
		arrayExp[index] = exp;
	}
	
	/**
	 * Check what Expression can be the final expression of 
	 */
	public void calcLastExpression() {
		Expression lastE = null;
		for(int i = arrayExp.length - 1; i >= 0 ; i--) {
			if(lastE == null) {
				arrayExp[i].setIsFinal(true); 		//the last Expression is the always the end
			} else if(lastE.isFinal() 
					&& lastE.isOptional()) { //if the previous Expression is final and the current one is optional, then arrayExp[i] is also final.
				arrayExp[i].setIsFinal(true);
			} else { //there are no more Expression candidates for the final
				break;
			}
			lastE = arrayExp[i];
		}
	}

	public void addAspectExpression(int order, String asp, String value) {
		if(Constants.isProximity(asp)) {
			addProximityExpression(order, value);
		} else {
			Expression exp = arrayExp[order];
			exp.addAspect(asp, value);
		}
	}

	public void addProximityExpression(int i, String value) {
		Expression exp = arrayExp[i];
		if(value.trim().equals(Constants.NEAR))
			exp.setCheckProximity(true);
	}

	public boolean checkProximity(int index) {
		Expression exp = arrayExp[index];
		return exp.isCheckProximity();
	}

	public void addWeight(String asp, Double value) {
		mapWeight.put(asp, value);
	}

	public void addDistanceFunction(String asp, String value) {
		mapDistanceFunc.put(asp, value);
	}

	public void addLimitAspValue(String asp, Double value) {
		mapLimit.put(asp, value);
	}

	public String regexPoi() {
		String regex = null;
		String expPoi = null;
		for(Expression exp : arrayExp) {
			expPoi = exp.getRegexPoi();
			if(expPoi != null) {
				if(regex != null) {
					regex += "(.)*" + exp.getRegexPoi();
				} else {
					regex = exp.getRegexPoi();
				}
			}
		}

		return regex;
	}

	public String regexCat() {
		String regex = null;
		String expCat = null;
		for (Expression exp : arrayExp) {
			expCat = exp.getRegexCategory();
			if(expCat != null) {
				if(regex != null) {
					regex += "(.)*" + expCat;
				} else {
					regex = expCat;
				}
			}
		}
		return regex;
	}

	public Double weightPoI() {
		return 0d;//TODO colocar peso nos PoI na próxima versão
	}
	
	public String searchFunctionName(String aspectType) {
		return mapDistanceFunc.get(aspectType);
	}
	
	public Double searchLimitValue(String aspectType) {
		return mapLimit.get(aspectType);
	}
	
	public Double searchWeightValue(String aspectType) {
		return mapWeight.get(aspectType);
	}

	public String aspectQueryValue(String aspectType, int indexExp) {
		return arrayExp[indexExp].aspectValue(aspectType);
	}

	public AspectExpression searchAspectExpression(String aspectType, int indexExp) {
		return arrayExp[indexExp].searchAspectExpression(aspectType);
	}

	public int getNumAspects() {
		return arrayExp[0].getMapAspects().keySet().size();
	}

	public Double weight(String key) {
		return mapWeight.get(key);
	}

	public Set<String> getAspects() {
		return arrayExp[0].getMapAspects().keySet();
	}

	@Override
	public String toString() {
		String result = "Query: ";

		for (Expression exp : arrayExp) {
			result += exp.getValue() + ";";
		}
		result += "\n";

		return result;
	}

	/**
	 * 
	 * @return
	 */
	public String createSqlQuery(String schema) {
		String regexPoi = regexPoi();
		String regexCat = regexCat();

		// regex sql
		String regexSqlPoi = regexPoi == null ? "" : "regexp_matches(p." + valueColumn + ", '" + regexPoi + "') as rxp";
		String regexSqlCat = regexCat == null ? "" : "regexp_matches(c." + valueColumn + ", '" + regexCat + "') as rxc";
		String regex = "";
		if (regexCat != null && regexPoi != null) {
			regex = regexSqlPoi + ", " + regexSqlCat;
		} else {
			regex = regexSqlPoi.isEmpty() ? regexSqlCat : regexSqlPoi;
		}

		// column traj_fk
		String trajfk = regexPoi == null ? "c." + pkColumn : "p." + pkColumn;

		// column values
		String values = "p." + valueColumn + " AS values_poi, c." + valueColumn + " AS values_cat";

		// from
		String tablePoi = schema + ".tb_poi p";
		String tableCat = schema + ".tb_category c";
		String from = " FROM " + tablePoi + "," + tableCat;

		// where
		String where = " WHERE p." + pkColumn + " = c." + pkColumn;

		String query = "SELECT " + trajfk + ", " + values + ", " + regex  + " " + from + " " + where;

		if(!regexSqlPoi.isEmpty() && !regexSqlCat.isEmpty()) {
			query = "SELECT * from (" + query + ") as temp WHERE rxp IS NOT NULL AND rxc IS NOT NULL";
			
		}

		return query;
	}

	public Expression[] getArrayExp() {
		return arrayExp;
	}

	public void setArrayExp(Expression[] arrayExp) {
		this.arrayExp = arrayExp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CompositeQuery getQuery() {
		return query;
	}

	public Map<String, Trajectory> getMapResultQuery() {
		return mapResultQuery;
	}

	public void setMapResultQuery(Map<String, Trajectory> mapResulQuery) {
		this.mapResultQuery = mapResulQuery;
	}

	public void addTrajectory(Trajectory t) {
		mapResultQuery.put(t.getId(), t);
	}

	public void setDistanceFunction(String distFun) {
		this.distanceFunction = distFun;
	}

	public String getDistanceFunction() {
		return distanceFunction;
	}
}