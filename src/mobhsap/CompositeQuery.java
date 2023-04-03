package mobhsap;

import java.util.Map;
import java.util.PriorityQueue;

public class CompositeQuery {
	private Map<String, Query> mapFilter;
	private PriorityQueue<Trajectory> finalResult;

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
}