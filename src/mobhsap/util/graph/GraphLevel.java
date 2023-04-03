package mobhsap.util.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobhsap.model.Expression;
import mobhsap.model.PoI;

public class GraphLevel {
	protected GraphLevel fatherLevel;
	protected Expression expression;
	protected List<Vertice> levelVertices;
	
//	private Vertice vnull;

	public GraphLevel(Expression exp) {
		this.expression = exp;
		levelVertices = new ArrayList<Vertice>();
//		if(exp.isOptional()) {
//			vnull = new Vertice(this);
////			vnull.setExpression(exp);
//			vnull.setPoi(null);
//			levelVertices.add(vnull);
//		}
	}

	public void addPoI(PoI p) {
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

	//TODO criar outro tipo de level
	//colocar a factory em expression
//	public void tryAddPoI(PoI p) {
//		
//		seartFathers(newV);
//		for(Vertice c : levelVertices) {
//			for(Iterator<Vertice> it = newV.getFathers().iterator; it.hasNext();) {
//				Vertice f = it.next();
//				if(c.getFathers(f)) {
//					c.addPoi(p);
//					newV.removeFather(f);
//				}
//			}
//		}
//		if(newV.getFathers().isEmpty())
//			newV = null;
//	}
	//

	
	
	protected Vertice createVertice() {
//		if(expression.isPlus()) {
//			return new VerticeCollection(this);
//		}
		return new Vertice(this);
	}

	public List<Vertice> searchFathers(Vertice v) {
		List<Vertice> result = new ArrayList<Vertice>();
		for(Vertice lv : levelVertices) {
			if(lv.compareTo(v) < 0) {
				lv.addChild(v);
				result.add(lv);
			}
		}

		if(result.isEmpty()) {
			if(getExpression().isOptional()) {
				if(fatherLevel != null)
					return fatherLevel.searchFathers(v);//searching grandfather
			}
		}
			
		return result;
	}

//	public void createVertice(PoI p) {
//		if(p != null) {
//			Vertice v = createVertice();
//			v.addPoi(p);
//			if(fatherLevel != null)
//				fatherLevel.searchFathers(v);
//
//			if(expression.isPlus()) {
//				for(Vertice vplus : levelVertices) {
//					Vertice newV = vplus.tryAddPoi(v);
//					if(newV != null) {
//						levelVertices.add(newV);
//					}
//				}
//			} else {
//				levelVertices.add(v);
//			}
//			
//			if(expression.isPlus())
//			
//
//			
//			
//			
//			Vertice v = createVertice(); //new Vertice(this);
////			v.setExpression(exp);
//			v.addPoi(p);
//			if(!levelVertices.contains(v)) {
//				levelVertices.add(v);
//				if(expression.getOrder() > 0)
//					fatherLevel.addChild(v);
//			}
//		}
//	}

	public Expression getExpression() {
		return expression;
	}

//	public void addChild(Vertice vChild) {
//		boolean added = false;
//		for(Vertice vfather : levelVertices) {
//			if(vfather.poiIsNull())
//				continue;
//			if(vfather.getPosition() < vChild.getPosition()) {
//				if(!vfather.poiIsNull()) {
//					vfather.getChildren().add(vChild);
//					added = true;
//				}
//			}
//		}
//		if(!added && exp.isOptional())
//			vnull.getChildren().add(vChild);
//	}

	public void fix(){
		for(Iterator<Vertice> it = levelVertices.listIterator();it.hasNext();) {
			Vertice raiz = it.next();
			if(!raiz.isComplete()) {
				if(raiz.getChildren().isEmpty())
					it.remove();
			}
		}
	}

	public GraphLevel getFatherLevel() {
		return fatherLevel;
	}

	public void setFatherLevel(GraphLevel fatherLevel) {
		this.fatherLevel = fatherLevel;
	}

	public void print() {
		for(Vertice v : levelVertices) {
			v.print();
		}
	}

//	public void calcSubSequences(PoI[] subs, List<PoI[]> result) {
//		for(Vertice v : levelVertices) {
//			v.calcSubSequences(subs, result);
//		}
//	}

	public void calcSubSequences(Vertice[] subs, List<Vertice[]> result) {
		for(Vertice v : levelVertices) {
			if(v.getFathers().isEmpty())
				v.calcSubSequences(subs, result);
		}
	}

	public void removeVertice(Vertice v) {
		if(levelVertices != null) 
			levelVertices.remove(v);
	}
}