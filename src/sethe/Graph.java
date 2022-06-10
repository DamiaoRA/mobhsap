package sethe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Graph {
	private List<Vertice> vertices; //level 0

	public Graph() {
		this.vertices = new ArrayList<Vertice>();
	}

	public void createVertice(Expression e, PoI p) {
		if(e.getOrder() == 0) {
			Vertice v = new Vertice();
			v.setExpression(e);
			v.setPoi(p);
			vertices.add(v);
		} else {
			for(Vertice v : vertices) {
				v.createVertice(e, p);
			}
		}
	}

	public void print() {
		for(Vertice v : vertices) {
			v.print();
		}
	}

	/**
	 * Remove os caminhos do grafo que são incompletos
	 */
	public void fix(int height) {
		for(Iterator<Vertice> it = vertices.listIterator();it.hasNext();) {
			Vertice raiz = it.next();
			if(!raiz.isComplete(height)) {
				if(raiz.getChildren().isEmpty())
					it.remove();
			}
		}
	}

	public List<LinkedList<PoI>> createListListPoI() {
		List<LinkedList<PoI>> ll = new ArrayList<LinkedList<PoI>>();
		for(Vertice v : vertices) {
			v.listListPoIs(ll, new LinkedList<PoI>());
		}
		return ll;
	}
}