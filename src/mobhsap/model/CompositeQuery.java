package mobhsap.model;

import java.util.Map;
import java.util.PriorityQueue;

public class CompositeQuery {
	private Map<String, Query> mapFilter;
	private PriorityQueue<Trajectory> finalResult;
	
	private String pkTrajColumnName;
	private String valueColumnName;
	private String delimiter;

	public CompositeQuery() {
		finalResult = new PriorityQueue<Trajectory>();
	}

	public Map<String, Query> getMapFilter() {
		return mapFilter;
	}
	
	public void setMapFilter(Map<String, Query> mapFilter) {
		this.mapFilter = mapFilter;
	}

	public void addFilter(Query f) {
		mapFilter.put(f.getName(), f);
	}

	public void add(Trajectory t) {
		if(t.getCoefficient() == 1) {
			finalResult.remove(t);
			finalResult.add(t);
		} else if(finalResult.contains(t)) {
			finalResult.remove(t);
			Trajectory bigger = t;
			for(Query f : mapFilter.values()) {
				Trajectory o = f.getMapResultQuery().get(t.getId());
				if(o != null) {
					if(bigger.getCoefficient() < o.getCoefficient()) {
						bigger = o;
						f.getMapResultQuery().remove(t);
					}
				}
			}
			finalResult.add(bigger);
		} else {
			finalResult.add(t);
		}

	}

	public PriorityQueue<Trajectory> getFinalResult() {
		return finalResult;
	}

	public String getPkTrajColumnName() {
		return pkTrajColumnName;
	}

	public void setPkTrajColumnName(String pkTrajColumnName) {
		this.pkTrajColumnName = pkTrajColumnName;
	}

	public String getValueColumnName() {
		return valueColumnName;
	}

	public void setValueColumnName(String valueColumnName) {
		this.valueColumnName = valueColumnName;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
}