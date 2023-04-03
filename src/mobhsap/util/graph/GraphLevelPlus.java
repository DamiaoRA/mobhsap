package mobhsap.util.graph;

import java.util.List;

import mobhsap.model.Expression;
import mobhsap.model.PoI;

/**
 * This class handle vertices of an expression with + symbol
 *
 */
public class GraphLevelPlus extends GraphLevel {

	public GraphLevelPlus(Expression exp) {
		super(exp);
	}

	@Override
	public void addPoI(PoI p) {
		for(Vertice v : levelVertices) {
			if(v.addPoi(p)) {
				return;
			}
		}

		Vertice newV = createVertice();
		newV.addPoi(p);
		if(fatherLevel != null) {
			List<Vertice> fathers = getFatherLevel().searchFathers(newV);
			if(fathers.isEmpty()) {
				newV = null;
				return;
			}
			newV.setFathers(fathers);
		}
		levelVertices.add(newV);
	}

	@Override
	protected Vertice createVertice() {
		return new VerticePoICollection(this);
	}
}