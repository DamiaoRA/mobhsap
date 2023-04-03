package mobhsap.model;

import mobhsap.util.graph.Vertice;
import mobhsap.util.Constants;
import mobhsap.util.Distance;

public class SubTrajectory implements Comparable<SubTrajectory> {

  private Trajectory trajectory;
  private Vertice[] vertices;
//  private List<PoI> pois;
//  private PoI[] pois;
//  private Location[] pois;
  private Double[] vector;
  private Double[] vectorQuery; //vector of the query
  private Double coefficient;
  private String distanceFunction;

//  private Location[] locations;

  public SubTrajectory() {
//    pois = new ArrayList<PoI>();
    coefficient = 0d;
  }

//  public List<PoI> getPois() {
//    return pois;
//  }
//
//  public void setPois(List<PoI> pois) {
//    this.pois = pois;
//  }

	public Double getCoefficient() {
		return coefficient;
	}

//	public PoI[] getPois() {
//		return pois;
//	}
//
//	public void setPois(PoI[] pois) {
//		this.pois = pois;
//	}

	public void setCoefficient(Double coefficient) {
		this.coefficient = coefficient;
	}

  public Trajectory getTrajectory() {
    return trajectory;
  }

  public void setTrajectory(Trajectory trajectory) {
    this.trajectory = trajectory;
  }

  @Override
  public int compareTo(SubTrajectory o) {
    return this.coefficient.compareTo(o.coefficient);
  }

  public void calcCoefficient(Query query) throws Exception {
    createVector(query);
    
//    for(double d : vector) {
//    	System.out.print(d + ";");
//    }
//    System.out.println("");
    
    coefficient = Distance.similarity(vector, vectorQuery, distanceFunction);
  }

  /**
   * Cria o vetor que representa essa subtrajetória
   * @param filter
   * @return
   */
  
  public Double[] createVector(Query filter) {   
	  
	    vector = new Double[vertices.length * (filter.getNumAspects()) + vertices.length];
	    vectorQuery = new Double[vector.length];
	    int k = 0;
	    for (int indexPoiSub = 0; indexPoiSub < vertices.length; indexPoiSub++) {
//	    	if(filter.checkProximity(indexPoiSub))
//	    		System.out.println("SubTrajectory.createVector()");

	      Vertice p2 = vertices[indexPoiSub];
	      if(p2 != null) {
	    	  Vertice p1 = searchVerticeNotNull(indexPoiSub - 1); //indexPoiSub > 0 ? vertices[indexPoiSub - 1] : null;

		      //calculating aspects coef
		      for (String aspectType : p2.getAspects().keySet()) {

		          String function = filter.searchFunctionName(aspectType);
		          Double limit = filter.searchLimitValue(aspectType);
		          Double weight = filter.searchWeightValue(aspectType);
		          AspectExpression aspect = filter.searchAspectExpression(aspectType, indexPoiSub);

		          vector[k] = Distance.distance(aspect, aspectType, function, 
		    			  limit, weight, p1, p2, 
		    			  trajectory);

		          vectorQuery[k] = filter.weight(aspectType);
		        k++;
		      }

		      //proximity coef
//		      int dist = 1;
		      if(filter.checkProximity(indexPoiSub)) {
//		    	  dist = p2.poiDistance(p1);

		    	  if(indexPoiSub > 0)
		    		  vector[k] = score(filter, Constants.PROXIMITY, indexPoiSub, p1, p2);
		    	  else 
		    		  vector[k] = 1d;
		      } else 
		    	  vector[k] = 1d;

//		      vector[k] = (1d / dist);
	      } else {
	    	  vector[k] = 0d;//1d;
	      }
	      vectorQuery[k] = 1d;
	      k++;
	    }
	    return vector;
	  }

	  private Double score(Query Q, String aspectType, int indexPoiSub, Vertice p1, Vertice p2) {
		  String function = Q.searchFunctionName(aspectType);
	      Double limit = Q.searchLimitValue(aspectType);
	      Double weight = Q.searchWeightValue(aspectType);
	      AspectExpression aspect = Q.searchAspectExpression(aspectType, indexPoiSub);

	      Double score = Distance.distance(aspect, aspectType, function, 
				  limit, weight, p1, p2, 
				  trajectory);
	      return score;
	  }

  	  private Vertice searchVerticeNotNull(int i) {
  			while (i >= 0) {
  				Vertice v = vertices[i];
  				if(v != null)
  					return v;
  				i--;
  			}
  			return null;
  		}
  
//  public Double[] createVector(Query filter) {
//	    vector = new Double[pois.length * (filter.getNumAspects()) + pois.length];
//	    vectorQuery = new Double[vector.length];
//	    int k = 0;
//	    for (int indexPoiSub = 0; indexPoiSub < pois.length; indexPoiSub++) {
//	      PoI p2 = pois[indexPoiSub];
//	      if(p2 != null) {
//	    	  PoI p1 = indexPoiSub > 0 ? pois[indexPoiSub - 1] : null;
//
//		      //calculating aspects coef
//		      for (String aspectType : p2.getAspects().keySet()) {
////		        String aspect = p2.getAspects().get(aspectType);
////		        vector[k] =
////		          filter.distance(aspectType, indexPoiSub, p1, p2, trajectory); //calcula a distancia entre o aspecto e o valor fornecido na consulta
//
//		          String function = filter.searchFunctionName(aspectType);
//		          Double limit = filter.searchLimitValue(aspectType);
//		          Double weight = filter.searchWeightValue(aspectType);
//		          AspectExpression aspect = filter.searchAspectExpression(aspectType, indexPoiSub);
//
//		          vector[k] = Distance.distance(aspect, aspectType, function, 
//		    			  limit, weight, p1, p2, 
//		    			  trajectory);
//
//		          vectorQuery[k] = filter.weight(aspectType);
//		        k++;
//		      }
//
//		      //proximity coef
//		      int dist = 1;
//		      if(filter.checkProximity(indexPoiSub) && indexPoiSub > 0)
//		    	  dist = p2.getPosition() - pois[indexPoiSub - 1].getPosition();
//
////		      int dist = filter.checkProximity(indexPoiSub) && indexPoiSub > 0
////		        ? (p2.getPosition() - pois[indexPoiSub - 1].getPosition())
////		        : 1;
//
//		      vector[k] = (1d / dist);
//	      } else {
//	    	  vector[k] = 0d;
//	      }
//	      vectorQuery[k] = 1d;
//	      k++;
//	    }
//	    return vector;
//	  }

//  public Double[] createVector(Query filter) {
//	    vector = new Double[pois.length * (filter.getNumAspects()) + pois.length];
//	    vectorQuery = new Double[vector.length];
//	    int k = 0;
//	    for (int indexPoiSub = 0; indexPoiSub < pois.length; indexPoiSub++) {
//	      PoI p2 = pois[indexPoiSub];
//	      if(p2 != null) {
//		      PoI p1 = indexPoiSub > 0 ? pois[indexPoiSub - 1] : null;
//	
//		      //calculating aspects coef
//		      for (String aspectType : p2.getAspects().keySet()) {
//		        String aspect = p2.getAspects().get(aspectType);
//		        vector[k] =
//		          filter.distance(aspectType, aspect, indexPoiSub, p1, p2, trajectory); //calcula a distancia entre o aspecto e o valor fornecido na consulta
//		        vectorQuery[k] = filter.weight(aspectType);
//		        k++;
//		      }
//	
//		      //proximity coef
//		      int dist = filter.checkProximity(indexPoiSub) && indexPoiSub > 0
//		        ? (p2.getPosition() - pois[indexPoiSub - 1].getPosition())
//		        : 1;
//	
//		      vector[k] = (1d / dist);
//	      } else {
//	    	  vector[k] = 0d;
//	      }
//	      vectorQuery[k] = 1d;
//	      k++;
//	    }
//	    return vector;
//	  }
  
//  public Double[] createVector(Query filter) {
//    vector = new Double[pois.size() * (filter.getNumAspects()) + pois.size()];
//    vectorQuery = new Double[vector.length];
//    int k = 0;
//    for (int indexPoiSub = 0; indexPoiSub < pois.size(); indexPoiSub++) {
//      PoI p2 = pois.get(indexPoiSub);
//      PoI p1 = indexPoiSub > 0 ? pois.get(indexPoiSub - 1) : null;
//
//      //calculating aspects coef
//      for (String aspectType : p2.getAspects().keySet()) {
//        String aspect = p2.getAspects().get(aspectType);
//        vector[k] =
//          filter.distance(aspectType, aspect, indexPoiSub, p1, p2, trajectory); //calcula a distancia entre o aspecto e o valor fornecido na consulta
//        vectorQuery[k] = filter.weight(aspectType);
//        k++;
//      }
//
//      //proximity coef
//      int dist = filter.checkProximity(indexPoiSub) && indexPoiSub > 0
//        ? (p2.getPosition() - pois.get(indexPoiSub - 1).getPosition())
//        : 1;
//
//      vector[k] = (1d / dist);
//      vectorQuery[k] = 1d;
//      k++;
//    }
//    return vector;
//  }

  public void calcCoefficient(double[] vectorCoef) {
    double sumMin = 0;
    double sumMax = 0;
    for (int i = 0; i < vector.length; i++) {
      sumMin += vector[i] <= vectorCoef[i] ? vector[i] : vectorCoef[i];
      sumMax += vector[i] <= vectorCoef[i] ? vectorCoef[i] : vector[i];
    }
    coefficient = sumMin / sumMax;
  }

  public String getDistanceFunction() {
    return distanceFunction;
  }

  public void setDistanceFunction(String distanceFunction) {
    this.distanceFunction = distanceFunction;
  }

//  public Location[] getLocations() {
//	  return locations;
//  }
//
//  public void setLocations(Location[] locations) {
//	  this.locations = locations;
//  }

	public Vertice[] getVertices() {
	return vertices;
}

public void setVertices(Vertice[] vertices) {
	this.vertices = vertices;
}

	public void print() {
		System.out.println("PoI:");
		for (Vertice v : vertices) {
			System.out.print(v.toStringPoI());
		}
	
		System.out.println("\nCat:");
		for (Vertice p : vertices) {
			System.out.print(p.toStringCategory());
		}
	
		System.out.println("\n" + coefficient);
		for (double v : vector) {
			System.out.print(v + " ");
		}
		System.out.println();
	}

//	public void print() {
//		System.out.println("PoI:");
//		for (PoI p : pois) {
//			System.out.print(p.toStringPoI());
//		}
//
//		System.out.println("\nCat:");
//		for (PoI p : pois) {
//			System.out.print(p.toStringCategory());
//		}
//
//		System.out.println("\n" + coefficient);
//		for (double v : vector) {
//			System.out.print(v + " ");
//		}
//		System.out.println();
//	}

//	public void addPoIs(Location loc, int order) {
//		(getLocations()[order]).addAllPoIs(loc.getPois());
//	}
//
//	public int getPoIPosition(int order) {
//		return (getLocations()[order]).getPois().get(0).getPosition();
//	}

//	public void setPois(PoI[] pois) {
//		this.pois = pois;
//	}
}