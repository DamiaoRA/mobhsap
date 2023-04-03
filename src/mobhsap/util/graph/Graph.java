package mobhsap.util.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import mobhsap.model.Expression;
import mobhsap.model.PoI;

public class Graph {
//	@Deprecated
//	private List<Vertice> vertices; //level 0
//	private int height = 0;

	private GraphLevel[] levels;

	public Graph(Expression[] exps) {
		levels = new GraphLevel[exps.length];

		for(Expression e : exps) {
			GraphLevel gl = createGraphLevel(e);
			if(e.getOrder() > 0)
				gl.setFatherLevel(levels[e.getOrder()-1]);
			levels[e.getOrder()] = gl;
		}

//		this.vertices = new ArrayList<Vertice>();
	}

	protected GraphLevel createGraphLevel(Expression e) {
		if(e.isPlus()) {
			return new GraphLevelPlus(e);
		}
		return new GraphLevel(e);
	}

	public void createVertice(Expression e, PoI p) {
		levels[e.getOrder()].addPoI(p);
	}

//	public Graph() {
//		this.vertices = new ArrayList<Vertice>();
//	}

//	@Deprecated
//	public void createVertice(Expression e, PoI p) {
//		if(e.getOrder() == 0) {
//			Vertice v = new Vertice();
//			v.setExpression(e);
//			v.setPoi(p);
//			vertices.add(v);
//		} else {
//			for(Vertice v : vertices) {
//				v.createVertice(e, p);
//			}
//		}
//
////		if(height < e.getOrder())???
////			height = e.getOrder();
//	}

	public void print() {
		levels[0].print();

//		for(Vertice v : vertices) {
//			v.print();
//		}
	}

	/**
	 * Remove os caminhos do grafo que são incompletos
	 */
	public void fix(){//int height) {
		levels[0].fix();

//		for(Iterator<Vertice> it = vertices.listIterator();it.hasNext();) {
//			Vertice raiz = it.next();
//			if(!raiz.isComplete()) {//height)) {
//				if(raiz.getChildren().isEmpty())
//					it.remove();
//			}
//		}
	}

//	@Deprecated
//	public List<LinkedList<PoI>> createListListPoI() {
//		List<LinkedList<PoI>> ll = new ArrayList<LinkedList<PoI>>();
//		for(Vertice v : vertices) {
//			v.listListPoIs(ll, new LinkedList<PoI>());
//		}
//		return ll;
//	}

//	public List<PoI[]> extractPoiSubSequences() {
//		List<PoI[]> result = new ArrayList<PoI[]>();
//		PoI[] subs = new PoI[levels.length];
//		levels[0].calcSubSequences(subs, result);
//		return result;
//	}

	public List<Vertice[]> extractPoiSubSequences() {
		List<Vertice[]> result = new ArrayList<Vertice[]>();
		Vertice[] subs = new Vertice[levels.length];
		for(GraphLevel gl : levels) {
			gl.calcSubSequences(subs, result);
		}
		return result;
	}
}