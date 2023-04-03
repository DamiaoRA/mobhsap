package mobhsap.util.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mobhsap.model.PoI;

public class VerticePoICollection extends Vertice {

	private List<PoI> pois;

	public VerticePoICollection(GraphLevel level) {
		super(level);
		pois = new ArrayList<PoI>();
	}

	@Override
	public boolean addPoi(PoI p) {
		if(pois.isEmpty()) {
			pois.add(p);
			return true;
		} else {
			if(pois.get(pois.size()-1).getPosition() == (p.getPosition()-1)) {
				pois.add(p);
				return true;
			}
		}
		return false;
	}

	@Override
	public int getFirstPosition() {
		return pois.get(0).getPosition();
	}

	@Override
	public int getLastPosition() {
		return pois.get(pois.size() - 1).getPosition();
	}

	@Override
	public Map<String, String> getAspects() {
		return pois.get(0).getAspects();
	}

	@Override
	public int compareTo(Vertice o) {
		if(pois.isEmpty())
			return -1;

		int pos = getFirstPosition();
		
		if(pos > o.getFirstPosition())
			return 1;

		pos = getLastPosition();
		if(pos < o.getLastPosition())
			return -1;
		return 0;
	}
}