package mobhsap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mobhsap.util.Constants;

public class Query {
	
	private String name;
	private CompositeQuery query;
	
	private Expression[] arrayExp; // Expressões regulares do PoI

	private Map<String, Double> mapWeight; 			// Peso dos aspectos
	private Map<String, String> mapDistanceFunc; 	// Função de distância
	private Map<String, Double> mapLimit; 			// limite máximo para a função de distância

	private Map<String, Trajectory> mapResultQuery;  //Armazena o resultado da consulta <id, trajetória>

	private String distanceFunction;

	public Query(String name) {
		this.name = name;
		mapWeight = new HashMap<String, Double>();
		mapDistanceFunc = new HashMap<String, String>();
		mapLimit = new HashMap<String, Double>();
		mapResultQuery = new HashMap<String, Trajectory>();
	}

	public void init(int arraySize) {
		arrayExp = new Expression[arraySize];
	}

	public void addExpression(int index, String catExp, String poiExp) {
		Expression exp = new Expression();
		exp.setOrder(index);//listExp.size());
		exp.setValueCategory(catExp);
		exp.setValuePoi(poiExp);
		if(catExp == null || catExp.isEmpty() || catExp.equals(".*"))
			exp.setCategory(false);
		else
			exp.setCategory(true);

		arrayExp[index] = exp;
	}

	public void addAspectExpression(int order, String asp, String value) {
		Expression exp = arrayExp[order];
		exp.addAspect(asp, value);
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

	public Double distance(String aspectType, String aspectValue, int positionExp, PoI p1, PoI p2, Trajectory trajectory) {
		String function = mapDistanceFunc.get(aspectType);
		Double limit = mapLimit.get(aspectType);
		Double weight = mapWeight.get(aspectType);

		Double result = 0d;

		result = arrayExp[positionExp].distance(aspectType, aspectValue, function, limit, weight, p1, p2, trajectory);
		return result;
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
		String regexSqlPoi = regexPoi == null ? "" : "regexp_matches(p.values, '" + regexPoi + "') as rxp";
		String regexSqlCat = regexCat == null ? "" : "regexp_matches(c.values, '" + regexCat + "') as rxc";
		String regex = "";
		if (regexCat != null && regexPoi != null) {
			regex = regexSqlPoi + ", " + regexSqlCat;
		} else {
			regex = regexSqlPoi.isEmpty() ? regexSqlCat : regexSqlPoi;
		}

		// column traj_fk
		String trajfk = regexPoi == null ? "c.traj_fk" : "p.traj_fk";

		// column values
		String values = "p.values AS values_poi, c.values AS values_cat";

		// from
		String tablePoi = schema + ".tb_poi p";
		String tableCat = schema + ".tb_category c";
		String from = " FROM " + tablePoi + "," + tableCat;

		// where
		String where = " WHERE p.traj_fk = c.traj_fk";

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

	public void setQuery(CompositeQuery query) {
		this.query = query;
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