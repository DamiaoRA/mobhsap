package mobhsap.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobhsap.util.graph.Graph;
import mobhsap.util.graph.Vertice;

public class Trajectory implements Comparable<Trajectory> {

  private String id;
  private String textPoi;
  private String textCategory;
  private Query query;
  private PoI[] pois;

  private Graph graph;
  private List<SubTrajectory> subTrajectories;
  private Double coefficient;

  private String delimiter;

	public Trajectory(Query query, String delimiter) {
		this.query = query;
		graph = new Graph(query.getArrayExp());
		subTrajectories = new ArrayList<SubTrajectory>();
		coefficient = 0d;
		this.delimiter = delimiter == null ? " " : delimiter;
	}

  public void loadText(String valuePoi, String valueCat) throws Exception {
    textPoi = valuePoi;
    textCategory = valueCat;
    String[] places = valuePoi.split(delimiter);
    String[] categories = valueCat.split(delimiter);

    if (places.length != categories.length) throw new Exception(
      "Trajectory " + id + " num poi size is different from the category size"
    );

    pois = new PoI[places.length];
    for (int i = places.length - 1; i >= 0; i--) {
      PoI p = new PoI();
      p.setName(places[i]);
      p.setPosition(i);
      p.setCategory(categories[i]);
      pois[i] = p;
    }
  }

  public void addAspect(String a, String[] text) throws Exception {
    if (text.length != pois.length) {
      throw new Exception(
        "Trajectory " +
        id +
        " num aspect size is different from the trajectory size"
      );
    }

    for (int i = text.length - 1; i >= 0; i--) {
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
    String id2 = ((Trajectory) obj).getId();
    return id.equals(id2);
  }

  @Override
  public int compareTo(Trajectory o) {
    if (this.coefficient.compareTo(o.coefficient) < 0) {
      return 1;
    } else if (this.coefficient.compareTo(o.coefficient) == 0) {
      return 0;
    }
    return -1;
  }

  public void calcCoefficient() {
    for (SubTrajectory s : subTrajectories) {
      if (s.getCoefficient() > coefficient) {
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
//		for (Expression e : query.getArrayExp()) {
//			for(int i = 0; i < pois.length; i++) {
//				String text = "";
//				if (e.isCategory()) {
//					text = pois[i].getCategory();
//				} else {
//					text = pois[i].getName();
//				}
//
//				Pattern pattern = Pattern.compile(e.getCleanValue());
//				Matcher matcher = pattern.matcher(text);
//				if (matcher.find()) {
//					graph.createVertice(e, pois[i]);
//				}
//			}			
//		}
//
//		graph.fix();
//
//		createSubTrajectories();
		
		if(getId().equals("http://localhost:8080/resource/TP1882"))
			System.out.println("Trajectory.calcSubtrajectory()");
			
		for (Expression e : query.getArrayExp()) {
			String text = "";
			if (e.isCategory()) {
				text = textCategory;
			} else {
				text = textPoi;
			}

			if(e.isAnyValue()) {
				for(PoI p : pois) {
					graph.createVertice(e, p);
				}
			} else {
				Pattern pattern = Pattern.compile(e.getCleanValue());
				Matcher matcher = pattern.matcher(text);
				while (matcher.find()) {
					String m = matcher.group().trim();
					int end = matcher.end();
					PoI poi = findPoIs(m, end, text, e.isCategory());
					graph.createVertice(e, poi);
				}
			}
		}

		graph.fix();

		createSubTrajectories();
	}

//  public void calcSubtrajectory() throws Exception {
//    for (Expression e : query.getArrayExp()) {
//      String text = textPoi;
//      if (e.isCategory()) {
//        text = textCategory;
//      }
//
//      Pattern pattern = Pattern.compile(e.getCleanValue());
//      Matcher matcher = pattern.matcher(text);
//      while (matcher.find()) {
//        String m = matcher.group().trim();
//        int end = matcher.end();
//        PoI poi = findPoIs(m, end, text, e.isCategory());
//        graph.createVertice(e, poi);
//      }
//    }
//
//    graph.fix();
//
//    createSubTrajectories();
//  }
  
  /**
   * 
   * @param m
   * @param end
   * @param text
   * @param isCategory
   * @return
   * @throws Exception
   */
  private PoI findPoIs(String m, int end, String text, boolean isCategory)
    throws Exception {
    String subTraj = substring(text, end); //text.substring(0, end).trim();
    String[] arraySubTraj = subTraj.split(delimiter);

    String lastPoi = arraySubTraj[arraySubTraj.length - 1];
    if (lastPoi.trim().contains(m))
    	return pois[arraySubTraj.length - 1];

    throw new Exception(
      "Expressão " + m + " não encontrada na subtrajetória " + text
    );
  }

  private void createSubTrajectories() throws Exception {	  
	  List<Vertice[]> result = graph.extractPoiSubSequences();

	  for (Vertice[] subs : result) {
	      SubTrajectory st = new SubTrajectory();
	      st.setTrajectory(this);
	      st.setVertices(subs);
	      st.setDistanceFunction(query.getDistanceFunction());
	      st.calcCoefficient(query);
	      subTrajectories.add(st);
	    }

	    Collections.sort(subTrajectories);
	    if (!subTrajectories.isEmpty()) 
	    	this.coefficient = subTrajectories.get(subTrajectories.size() - 1).getCoefficient();
  }
  
//  private void createSubTrajectories() throws Exception {
//	  List<PoI[]> result = graph.extractPoiSubSequences();
//
//	  for (PoI[] subs : result) {
//	      SubTrajectory st = new SubTrajectory();
//	      st.setTrajectory(this);
//	      st.setPois(subs);
//	      st.setDistanceFunction(query.getDistanceFunction());
//	      st.calcCoefficient(query);
//	      subTrajectories.add(st);
//	    }
//
//	    Collections.sort(subTrajectories);
//	    if (!subTrajectories.isEmpty()) 
//	    	this.coefficient = subTrajectories.get(subTrajectories.size() - 1).getCoefficient();
//  }

//  private void createSubTrajectories() throws Exception {
//    List<LinkedList<PoI>> ll = graph.createListListPoI();
//    for (LinkedList<PoI> lk : ll) {
//      SubTrajectory st = new SubTrajectory();
//      st.setTrajectory(this);
//      st.setPois(lk);
//      st.setDistanceFunction(query.getDistanceFunction());
//      st.calcCoefficient(query);
//      subTrajectories.add(st);
//    }
//
//    Collections.sort(subTrajectories);
//    if (!subTrajectories.isEmpty()) 
//    	this.coefficient = subTrajectories.get(subTrajectories.size() - 1).getCoefficient();
//  }

  private String substring(String text, int end) {
//    while (end < (text.length() - 1) && text.charAt(end) != ' ') {
//      end++;
//    }
//    return text.substring(0, end).trim();
	  return text.substring(0, end);
  }

//  private void addVertice(Expression e, PoI p) {
//    graph.createVertice(e, p);
//  }

  public void printGraph() {
    graph.print();
  }

  public String getAspectValuePoI(int i, String aspectType) {
    String aspectValue = pois[i].getAspectValue(aspectType);
    return aspectValue;
  }
}
