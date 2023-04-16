package mobhsap.tripbuilder.search.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import mobhsap.QuerySETHEMain;
import mobhsap.model.CompositeQuery;
import mobhsap.util.TimeQ;

/**
 * SETHE Queries
 *
 */
public class QueriesTripBuilderMain {

  private static QuerySETHEMain qt = null;

  public static void main(String[] args) throws Exception {
	  
	  qt =
		      new QuerySETHEMain(
		        "localhost",
		        "5432",
		        "postgres",
		        "postgres",
		        "tripbuilder2"
//		        "trajetoria,
//		        "trajectory_id",
//		        "value"
//		        "tripbuilder"
		      );

    List<TimeQ> times = new ArrayList<TimeQ>();
    times.add(queryQ1());
    times.add(queryQ2());
    times.add(queryQ3());
    times.add(queryQ4());
    times.add(queryQ5());
    times.add(queryQ6());
    times.add(queryQ7());
    times.add(queryQ8());
    times.add(queryQ9());
    times.add(queryQ10());
    System.out.println();
    for (TimeQ t : times) {
      System.out.println(t.toString());
    }
  }

  private static TimeQ queryQ1() throws Exception {
    Properties prop = new Properties();
    prop.setProperty(
      "q1_asp_cat",
      "museidipisa(;(\\w*))* ; ((\\w*);)*cappelledipisa"
    );
    prop.setProperty("q1_asp_proximity", ".* ; ~");

    return queryQ(prop);
  }

  private static TimeQ queryQ2() throws Exception {
    Properties prop = new Properties();
    prop.setProperty(
      "q1_asp_cat",
      "(torridipisa)(;(\\w*))* ; ((\\w*);)*(cappelledipisa|chiesedipisa)(;(\\w*))* ; (((\\w*);)*(cappelledipisa|chiesedipisa)(;(\\w*))*) ; ((\\w*);)*(museidipisa)"
    );
    prop.setProperty(
      "q1_proximity",
      ".*                     ; ~                                                 ; .*                                                ; ~"
    );

    return queryQ(prop);
  }

  private static TimeQ queryQ3() throws Exception {
    Properties prop = new Properties();
    prop.setProperty(
      "q1_asp_cat",
      "(torridipisa(;(\\w*))*)+ ; ((\\w*);)*museidipisa"
    );
    prop.setProperty("q1_proximity", " .*                  ; ~");

    return queryQ(prop);
  }

  private static TimeQ queryQ4() throws Exception {
    Properties prop = new Properties();

    prop.setProperty("q1_asp_poi", "Torre_del_Leone ; Torre_pendente_di_Pisa");
    prop.setProperty("q1_proximity", ".*			   ; ~");
    prop.setProperty("q2_asp_poi", "Torre_pendente_di_Pisa ; Torre_del_Leone");
    prop.setProperty("q2_proximity", ".*                    ; ~");

    return queryQ(prop);
  }

  private static TimeQ queryQ5() throws Exception {
    Properties prop = new Properties();

    prop.setProperty(
      "q1_asp_cat",
      "^((\\w*);)*(museidipisa)(;(\\w*))* ; ((\\w*);)*(cappelledipisa)(;(\\w*))*$"
    );
    prop.setProperty("q1_proximity", ".*			                     ; ~");

    return queryQ(prop);
  }

  private static TimeQ queryQ6() throws Exception {
    Properties prop = new Properties();

    prop.setProperty(
    	      "q1_asp_cat",
    	      "museidipisa(;(\\w*))* ; ((cappelledipisa|chiesedipisa)(;(\\w*))*$)?"
    );

    return queryQ(prop);
  }

  private static TimeQ queryQ7() throws Exception {
    Properties prop = new Properties();
    
    prop.setProperty(
    	      "q1_asp_cat",
    	      "^(((\\w*);)*(cappelledipisa)) ; (((\\w*);)*(cappelledipisa)(;(\\w*))*)* ; (((\\w*);)*(cappelledipisa))$"
    	    );
	prop.setProperty("q1_proximity", " .*     ; ~      ; ~");

    return queryQ(prop);
  }

  private static TimeQ queryQ8() throws Exception {
    Properties prop = new Properties();

    prop.setProperty(
      "q1_asp_cat",
      "museidipisa(;(\\w*))* ; ((\\w*);)*cappelledipisa"
    );
    prop.setProperty("q1_asp_move", ".*                  ; Bus");
    prop.setProperty("q1_proximity", ".*			      ; ~");
    prop.setProperty("weight_move", "1");
    prop.setProperty("distance_move", "equality");
    prop.setProperty("limit_move", "1");

    return queryQ(prop);
  }

  private static TimeQ queryQ9() throws Exception {
    Properties prop = new Properties();

    prop.setProperty(
      "q1_asp_cat",
      "^(((\\w*);)*(cappelledipisa|chiesedipisa)) ; .*"
    );
    prop.setProperty(
      "q1_asp_poi",
      "  .*              						   ; (Torre_pendente_di_Pisa)$"
    );
    prop.setProperty("q1_asp_move", " .*  				  ; (?-)Bus");
    prop.setProperty("q1_proximity", ".*			      ; .*");
    prop.setProperty("weight_move", "1");
    prop.setProperty("distance_move", "equality");
    prop.setProperty("limit_move", "1");
    return queryQ(prop);
  }

  private static TimeQ queryQ10() throws Exception {
    Properties prop = new Properties();
   
    prop.setProperty(
    	      "q1_asp_cat",
    	      "^((\\w*);)*(torridipisa) ; .+ ;  ((\\w*);)*(chiesedipisa)(;(\\w*))* ; (palazzidipisa)(;(\\w*))*$"
    	    );
    prop.setProperty(
      "q1_asp_move",
      " .*              ; Walk          ; Bus                                 ; .*"
    );
    prop.setProperty("weight_move", "1");
    prop.setProperty("distance_move", "equality");
    prop.setProperty("limit_move", "1");
    return queryQ(prop);
  }

  private static TimeQ queryQ(Properties prop) throws Exception {
    prop.setProperty("schema", "tripbuilder");
    prop.setProperty("dist_func", "jaccard");
    prop.setProperty("delimiter", " ");
    prop.setProperty("split", " ; ");
    prop.setProperty("pk_column_name", "trajectory_id");
    prop.setProperty("value_column_name", "value");

    CompositeQuery cq = QuerySETHEMain.loadQuery(prop);
    long count = 0l;
    int countSub = 0;

    long t1 = System.currentTimeMillis();
    qt.executeQuery(cq);
    while (!cq.getFinalResult().isEmpty()) {
      mobhsap.model.Trajectory t = cq.getFinalResult().poll(); 
      t.print();
      count++;
      countSub += t.getSubTrajectories().size(); 
    }

    long t2 = System.currentTimeMillis();

    System.out.println("count: " + count);
    TimeQ time = new TimeQ(t1, t2, "Q1", count);
    time.setCountSubsequece(countSub);

    return time;
  }
}
