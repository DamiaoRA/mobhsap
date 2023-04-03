package mobhsap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trajectory implements Comparable<Trajectory>{
	private String id;
	private String textPoi;
	private String textCategory;
	private Query query;
	private PoI[] pois;

	private Graph graph;
	private List<SubTrajectory> subTrajectories;
	private Double coefficient;

	public Trajectory() {
		graph = new Graph();
		subTrajectories = new ArrayList<SubTrajectory>();
		coefficient = 0d;
	}

	public void loadText(String valuePoi, String valueCat) throws Exception {
		textPoi = valuePoi;
		textCategory = valueCat;
		String[] places = valuePoi.split(" ");
		String[] categories = valueCat.split(" ");

		if(places.length != categories.length)
			throw new Exception("Trajectory " + id + " num poi size is different from the category size");

		pois = new PoI[places.length];
		for(int i = places.length - 1; i >= 0; i--) {
			PoI p = new PoI();
			p.setName(places[i]);
			p.setPosition(i);
			p.setCategory(categories[i]);
			pois[i] = p;
		}
	}

	public void addAspect(String a, String[] text) throws Exception {
		if(text.length != pois.length) {
			throw new Exception("Trajectory " + id + " num aspect size is different from the trajectory size");
		}

		for(int i=text.length - 1; i >= 0; i--) {
			pois[i].addAspect(a, text[i]);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addSubtrajectory(SubTrajectory subTrajectory) {
		subTrajectories.add(subTrajectory);
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}


	public List<SubTrajectory> getSubTrajectories() {
		return subTrajectories;
	}

	public void setSubTrajectories(List<SubTrajectory> subTrajectories) {
		this.subTrajectories = subTrajectories;
	}

	public Double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Double coefficient) {
		this.coefficient = coefficient;
	}

	@Override
	public boolean equals(Object obj) {
		String id2 = ((Trajectory)obj).getId();
		return id.equals(id2);
	}

	@Override
	public int compareTo(Trajectory o) {
		if(this.coefficient.compareTo(o.coefficient) < 0) {
			return 1;
		} else if(this.coefficient.compareTo(o.coefficient) == 0) {
			return 0;
		}
		return -1;
	}

	public void calcCoefficient() {
		for(SubTrajectory s : subTrajectories) {
			if(s.getCoefficient() > coefficient) {
				coefficient = s.getCoefficient();
			}
		}
	}

	public void print() {
		Collections.reverse(subTrajectories);
//		System.out.println(textCategory);
		System.out.println(id + ";" + coefficient);
//		System.out.println(coefficient);

//		for(PoI p : pois) {
//			System.out.print("[" + p.getPosition() + " " + p.getCategory() + "]");
//		}
//		System.out.println("");
//		for(PoI p : pois) {
//			System.out.print("[" + p.getPosition() + " " + p.getName() + "]");
//		}
//		System.out.println("");
//
//		for(SubTrajectory s : subTrajectories) {
//			s.print();
//		}
//		System.out.println("");
	}

	public void calcSubtrajectory() throws Exception {
		if(id.equals("http://localhost:8080/resource/TP2522"))
			System.out.println("Trajectory.calcSubtrajectory()");
		for(Expression e : query.getArrayExp()) {
			String text = textPoi;
			if(e.isCategory()) {
				text = textCategory;
			}

			Pattern pattern = Pattern.compile(e.getValue());
			Matcher matcher = pattern.matcher(text);
			while(matcher.find()) {
				String m = matcher.group().trim();
				int end = matcher.end();
				PoI poi = findPoIs(m, end, text, e.isCategory());
				addVertice(e, poi);
			}
		}

		graph.fix(query.getArrayExp().length-1); 

		createSubTrajectories();
	}

	private void createSubTrajectories() throws Exception {
		List<LinkedList<PoI>> ll = graph.createListListPoI();
		for(LinkedList<PoI> lk : ll) {
			SubTrajectory st = new SubTrajectory();
			st.setTrajectory(this);
			st.setPois(lk);
			st.setDistanceFunction(query.getDistanceFunction());
			st.calcCoefficient(query);
			subTrajectories.add(st);
		}

		Collections.sort(subTrajectories); //TODO remover esse sort
		if(!subTrajectories.isEmpty())
			this.coefficient = subTrajectories.get(subTrajectories.size() - 1).getCoefficient();
	}

	//TODO talvez não seja mais necessário. Remover essa função pra ganhar desempenho
	private PoI findPoIs(String m, int end, String text, boolean isCategory) throws Exception {
		String subTraj = substring(text, end);//text.substring(0, end).trim();
		String[] arraySubTraj = subTraj.split(" ");

		for(int i = arraySubTraj.length - 1; i >= 0; i--) {
			String poi = arraySubTraj[i];
			if(poi.trim().contains(m)) {
				return pois[i];
			}
		}

		throw new Exception("Expressão " + m + " não encontrada na subtrajetória " + text);
	}

	private String substring(String text, int end) {
		while(end < (text.length()-1) && text.charAt(end) != ' ') {
			end++;
		}
		return text.substring(0, end).trim();
	}

	private void addVertice(Expression e, PoI p) {
		graph.createVertice(e, p);
	}

	public void printGraph() {
		graph.print();
	}

	public String getAspectValuePoI(int i, String aspectType) {
		String aspectValue = pois[i].getAspectValue(aspectType);
		return aspectValue;
	}
}