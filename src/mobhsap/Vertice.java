package mobhsap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Vertice {
	private Expression expression;
	private PoI poi;

	private List<Vertice> children;
	private List<Vertice> fathers;

	public Vertice() {
		this.children = new ArrayList<Vertice>();
		this.fathers = new ArrayList<Vertice>();
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public List<Vertice> getChildren() {
		return children;
	}

	public List<Vertice> getFathers() {
		return fathers;
	}

	public void addFather(Vertice f) {
		fathers.add(f);
	}

	public void createVertice(Expression e, PoI poiChild) {
		if(poiChild.getPosition() < poi.getPosition())
			return;

		int fatherPosition = getPosition();

		if(poiChild.getPosition() <= fatherPosition)
			return;

		if(e.getOrder() == expression.getOrder() + 1) {
			Vertice v = new Vertice();
			v.setExpression(e);
			v.setPoi(poiChild);
			v.addFather(this);
			children.add(v);
		} else {
			for(Vertice v : children) {
				v.createVertice(e, poiChild);
			}
		}
	}

	public void print() {
		String traco = "";
		for(int i = 0; i <= expression.getOrder(); i++) {
			traco += "-";
		}
		System.out.print(traco + " ");

		System.out.print("[" + poi.getPosition() + " : " + poi.getCategory() + " : " +  poi.getName() + "] ");

		System.out.println();
		for(Vertice v : children) {
			v.print();
		}
	}

	public boolean isComplete(int height) {
		if(children.isEmpty()) {
			return expression.getOrder() == height;
		}
		for(Iterator<Vertice> it = children.iterator(); it.hasNext();) {
			Vertice v = it.next();
			if(!v.isComplete(height)) {
				it.remove();
			}
		}

		if(children.isEmpty()) {
			return expression.getOrder() == height;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void listListPoIs(List<LinkedList<PoI>> ll, LinkedList<PoI> lk) {
//		for(Vertice v : children) {
//			if(children.size() > 1) {
//				LinkedList<PoI> clone = (LinkedList<PoI>)(lk.clone());//uma nova lista para cada ramo da árvore
//				clone.add(poi);
//				v.listListPoIs(ll, clone);
//			} else {
//				lk.add(poi);
//				v.listListPoIs(ll, lk);
//			}
//		}
		
			if(children.size() > 1) {
				for(Vertice v : children) {
					LinkedList<PoI> clone = (LinkedList<PoI>)(lk.clone());//uma nova lista para cada ramo da árvore
					clone.add(poi);
					v.listListPoIs(ll, clone);
				}
			} else if(children.size() == 1) {
				lk.add(poi);
				Vertice v = children.get(0);
				v.listListPoIs(ll, lk);
			}

		if(children.isEmpty()) {
			lk.add(poi);
			ll.add(lk);
		}
	}

	public int getPosition() {
		return poi.getPosition();
	}

	public PoI getPoi() {
		return poi;
	}

	public void setPoi(PoI poi) {
		this.poi = poi;
	}
}