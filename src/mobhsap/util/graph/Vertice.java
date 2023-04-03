package mobhsap.util.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mobhsap.model.Expression;
import mobhsap.model.PoI;

public class Vertice implements Comparable<Vertice>{
//	private Expression expression;
	private PoI poi;
	private List<Vertice> children;

	private List<Vertice> fathers;
	private GraphLevel level;

	public Vertice(GraphLevel level) {
		children = new ArrayList<Vertice>();
		fathers = new ArrayList<Vertice>();
		this.level = level;
	}

	public Expression getExpression() {
		return level.getExpression();
//		return expression;
	}

//	public void setExpression(Expression expression) {
//		this.expression = expression;
//	}

	public void addChild(Vertice child) {
		children.add(child);
	}

	public List<Vertice> getChildren() {
		return children;
	}

	public String toStringPoI() {
		return poi.toStringPoI();
	}

	public String toStringCategory() {
		return poi.toStringCategory();
	}

	/**
	 * 
	 * @param e
	 * @param poiChild
	 * @return 	-1 no added and the expression is optional, <br/>
	 * 			0 no added and it is ok<br/>
	 * 			1 added <br/>
	 */
//	@Deprecated
//	public int createVertice(Expression e, PoI poiChild) {
//		if(poiChild != null) {
//			int fatherPosition = getPosition();
//	
//			if(poiChild.getPosition() < fatherPosition) { //this means that the poiChild is in a position before the poi in the trajectory 
//				if(getExpression().isOptional())
//					return -1;
//				return 0;
//			}
//
//			if(poiChild.getPosition() == fatherPosition)
//				return 0;
//		}
//
//		if(e.getOrder() == getExpression().getOrder() + 1) {
//			Vertice v = new Vertice();
////			v.setExpression(e);
//			v.setPoi(poiChild);
//			v.addFather(this);
//			children.add(v);
//			return 1;
//		} else {
//			for(Vertice v : children) {
//				return v.createVertice(e, poiChild);
//			}
//			return 0;
//		}
//	}

	public List<Vertice> getFathers() {
		return fathers;
	}

	public void setFathers(List<Vertice> fathers) {
		this.fathers = fathers;
	}

	public void print() {
		String traco = "";
		for(int i = 0; i <= level.getExpression().getOrder(); i++) {
			traco += "-";
		}
		System.out.print(traco + " ");

		if(poi != null)
			System.out.print("[" + poi.getPosition() + " : " + poi.getCategory() + " : " +  poi.getName() + "] ");
		else
			System.out.println("null");

		System.out.println();
		for(Vertice v : children) {
			v.print();
		}
	}

	public boolean isComplete() {//int height) {
		if(children.isEmpty()) {
			return level.getExpression().isFinal();//expression.getOrder() == height;
		}
		for(Iterator<Vertice> it = children.iterator(); it.hasNext();) {
			Vertice v = it.next();
			if(!v.isComplete()) {//height)) {
				it.remove();

				GraphLevel l = v.getLevel();
				l.removeVertice(v);
			}
		}

		if(children.isEmpty()) {
			return level.getExpression().isFinal();
		}
		return true;
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public void listListPoIs(List<LinkedList<PoI>> ll, LinkedList<PoI> lk) {
		for(Vertice v : children) {
			if(children.size() > 1) {
				LinkedList<PoI> clone = (LinkedList<PoI>)(lk.clone());//uma nova lista para cada ramo da árvore
				clone.add(poi);
				v.listListPoIs(ll, clone);
			} else {
				lk.add(poi);
				v.listListPoIs(ll, lk);
			}
		}

		if(children.isEmpty()) {
			lk.add(poi);
			ll.add(lk);
		}
	}

	public void calcSubSequences(Vertice[] subs, List<Vertice[]> result) {
		subs[level.getExpression().getOrder()] = this;
		for(Vertice child : children) {
			if(getChildren().size() > 1) {
				Vertice[] clone = (Vertice[])(subs.clone());//uma nova lista para cada ramo da árvore
				child.calcSubSequences(clone, result);
			} else {
				child.calcSubSequences(subs, result);
			}
		}
		if(children.isEmpty())
			result.add(subs);
	}
	
//	public void calcSubSequences(PoI[] subs, List<PoI[]> result) {
//		subs[level.getExpression().getOrder()] = poi;
//		for(Vertice child : children) {
//			if(getChildren().size() > 1) {
//				PoI[] clone = (PoI[])(subs.clone());//uma nova lista para cada ramo da árvore
//				child.calcSubSequences(clone, result);
//			} else {
//				child.calcSubSequences(subs, result);
//			}
//		}
//		if(children.isEmpty())
//			result.add(subs);
//	}

//	public int getPosition() {
//		return poi.getPosition();
//	}
	
	public int getFirstPosition() {
		return poi.getPosition();
	}
	
	public int getLastPosition() {
		return poi.getPosition();
	}

//	public PoI getPoi() {
//		return poi;
//	}

	public void setPoi(PoI poi) {
		this.poi = poi;
	}

	public boolean poiIsNull() {
		return poi == null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((poi == null) ? 0 : poi.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertice other = (Vertice) obj;
		if (poi == null) {
			if (other.poi != null)
				return false;
		} else if (!poi.equals(other.poi))
			return false;
		return true;
	}

	public boolean addPoi(PoI p) {
		setPoi(p);
		return true;
	}

	@Override
	public int compareTo(Vertice o) {
		if(poi.getPosition() < o.getFirstPosition())
			return -1;
		if(poi.getPosition() == o.getFirstPosition())
			return 0;
		return 1;
	}

	public Map<String, String> getAspects() {
		return poi.getAspects();
	}

	public int poiDistance(Vertice vertice) {
		return getFirstPosition() - vertice.getLastPosition();
	}

	public GraphLevel getLevel() {
		return level;
	}

	public void setLevel(GraphLevel level) {
		this.level = level;
	}

//	public static void main(String[] args) {
//		PoI p1 = new PoI();p1.setPosition(1);
//		PoI p2 = new PoI();p2.setPosition(3);
//		PoI p3 = new PoI();p3.setPosition(5);
//		PoI p4 = new PoI();p4.setPosition(6);
//		PoI p5 = new PoI();p5.setPosition(9);
//		PoI p6 = new PoI();p6.setPosition(12);
//
//		Location l1 = new Location();l1.addPoI(p1);
//		Location l2 = new Location();l2.addPoI(p2);
//		Location l3 = new Location();l3.addPoI(p3);
//		Location l4 = new Location();l4.addPoI(p4);
//		Location l5 = new Location();l5.addPoI(p5);
//		Location l6 = new Location();l6.addPoI(p6);
//
//		Location[] ls1 = {l1, l2, l4};//1 3 6
//		Location[] ls2 = {l1, l2, l3};//1 3 5
////		Location[] ls2 = {l1, l3, l4};//1 5 6
//		Location[] ls3 = {l1, l5, l6};//1 9 12
//
//		SubTrajectory s1 = new SubTrajectory();
//		s1.setLocations(ls1);
//
//		SubTrajectory s2 = new SubTrajectory();
//		s2.setLocations(ls2);
//
//		SubTrajectory s3 = new SubTrajectory();
//		s3.setLocations(ls3);
//
//		List<SubTrajectory> subs = new ArrayList<>();
//		subs.add(s1);subs.add(s2);subs.add(s3);
//		
//		List<SubTrajectory> newSubs = new ArrayList<>();
//
//		//
//		newSubs = createNewSubsequence(subs, 0);
//		for(SubTrajectory s : newSubs) {
//			for(Location l : s.getLocations()) {
//				System.out.print(" (" + l + ")");
//			}
//			System.out.println();
//		}
//
////		int[][] r1 = {{1,2,10},{1,3,10},{1,2,6},{1,4,10}};
////		int[] E1 = {1};
////		int[] E3 = {4};
////		int orderE1 = 0;
////		int orderE2 = 1;
////		int orderE3 = 2;
////		
////		Map<String, Object[]> map = new HashMap<String, Object[]>();
////		
////		for(int[] pois : r1) {
////			int p1 = pois[orderE1];
////			int p3 = pois[orderE3];
////			createNewSubsequence(p1, pois[orderE2] ,p3, orderE2, map);
////		}
////
////		for(Object[] array : map.values()) {
////			System.out.println(array[0] + " " + array[1] + " "+ array[2]);
////		}
//	}
//
//	/**
//	 * [E1+, E2, E3]
//	 * [E1, E2+, E3]
//	 * [E1, E2, E3+]
//	 * @param listSub
//	 * @param order
//	 */
//	private static List<SubTrajectory> createNewSubsequence(
//			List<SubTrajectory> listSub, int order) {
//
//		Map<String, SubTrajectory> map = new HashMap<>();
//		for(SubTrajectory subt : listSub) {
//			int size = subt.getLocations().length;
//			String key = "_";
//			key = order > 0 ? subt.getPoIPosition(order - 1) + key : key;
//			key = order < size-1 ? key + subt.getPoIPosition(order + 1) : key;
//
//			SubTrajectory newSub = map.get(key);
//			if(newSub == null) {
//				newSub = subt;
//				map.put(key, newSub);
//			}
//			newSub.addPoIs(subt.getLocations()[order], order);
//		}
//
//		List<SubTrajectory> result = new ArrayList<>(map.values().size());
//		result.addAll(map.values());
//		return result;
//	}
//
//
////	private static void createNewSubsequence(int p1, int i, int p3, int orderE2, Map<String, Object[]> map) {
////		Object[] array = map.get(p1+"_"+p3);
////		if(array == null) {
////			List<Integer> pontos = new ArrayList<Integer>();
////			array = new Object[3];
////			array[0] = p1;
////			array[1] = pontos;
////			array[2] = p3;
////			map.put(p1+"_"+p3, array);
////		}
////		((List<Integer>)array[orderE2]).add(i);
////	}
}