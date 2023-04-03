package mobhsap.foursquare.search.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import mobhsap.QuerySETHEMain;
import mobhsap.model.CompositeQuery;
import mobhsap.util.TimeQ;

/**
 * compile exec:java -Dexec.mainClass="mobhsap.foursquare.search.test.NewQueriesFoursquareMain"
 *
 */
public class NewQueriesFoursquareMain {

  private static QuerySETHEMain query = null;

  public static void main(String[] args) throws Exception {

//	  query = new QuerySETHEMain("localhost", "5432", "postgres", "postgres", "foursquare", "trajetoria", "trajectory_id",
//				"trajectory_value");
	  query = new QuerySETHEMain("localhost", "5432", "postgres", "postgres", "foursquare3", "trajetoria");

    List<TimeQ> times = new ArrayList<>();
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

    for (TimeQ time : times) {
      System.out.println(time.toString());
    }
  }

  /**
   * Trajetórias que param na Bryant Park e logo depois na Madison Square e o dia estava claro.
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ1() throws Exception {
    Properties properties = new Properties();
    properties.setProperty("q1_asp_poi", "Bryant Park ; Madison Square");

    properties.setProperty("q1_asp_weather", ".* ; Clear");
    properties.setProperty("weight_weather", "0.6");
    properties.setProperty("limit_weather", "1");
    properties.setProperty("distance_weather", "equality");

    properties.setProperty("q1_asp_proximity", ".* ; ~");
    properties.setProperty("weight_proximity", "0.4");
    properties.setProperty("limit_proximity", "20");
    properties.setProperty("distance_proximity", "proportion");

    return executeQuerySethe(properties);
  }

  /**
   * Trajetórias que pararam em uma residência e o clima estava nublado, logo depois pararam em um restaurante ou
   * shopping bem avaliado e depois em um colégio.
   * Bem avaliado = rating > 7
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ2() throws Exception {
    Properties properties = new Properties();
    properties.setProperty(
      "q1_asp_cat",
      "Residence ; (Food|Shop Service) ; College University"
    );

    properties.setProperty("q1_asp_weather", "Clouds ; .* ; .*");
    properties.setProperty("weight_weather", "0.5");
    properties.setProperty("limit_weather", "1");
    properties.setProperty("distance_weather", "equality");

    properties.setProperty("q1_asp_rating", ".* ; 10 ; .* ");
    properties.setProperty("weight_rating", "0.5");
    properties.setProperty("limit_rating", "10");
    properties.setProperty("distance_rating", "proportion");

    properties.setProperty("q1_proximity", ".* ; ~ ; .*");

    return executeQuerySethe(properties);
  }

  /**
   *     --> poi + cat + algum tempo depois
   * Trajetórias que pararam em um colégio e depois em um lugar de recreação bem avaliado ou vice-versa.
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ3() throws Exception {

		Properties properties = new Properties();
		properties.setProperty("q1_asp_cat", "College University ; Outdoors Recreation");
		properties.setProperty("q1_proximity", ".* ; ~ ");
		properties.setProperty("q1_asp_rating", ".* ; 10 ");
		
		properties.setProperty("q2_asp_cat", "Outdoors Recreation ; College University");
		properties.setProperty("q2_proximity", ".* ; ~ ");
		properties.setProperty("q2_asp_rating", "10 ; .* ");

		properties.setProperty("limit_rating", "10");
		properties.setProperty("distance_rating", "proportion");
		properties.setProperty("weight_rating", "0.8");

		////////////////////
	  
//    Properties properties = new Properties();
//    properties.setProperty(
//      "q1_asp_cat",
//      "College University ; Outdoors Recreation"
//    );
//
//    properties.setProperty("q1_asp_rating", ".* ; 8 ");
//    properties.setProperty("weight_rating", "0.8");
//    properties.setProperty("distance_rating", "proportion");
//    properties.setProperty("limit_rating", "10");
//
//    properties.setProperty("q1_proximity", ".* ; ~ ");
//
//    properties.setProperty(
//      "q2_asp_cat",
//      "Outdoors Recreation ; College University"
//    );
//
//    properties.setProperty("q2_asp_rating", "8 ; .* ");
//    properties.setProperty("q2_proximity", ".* ; ~ ");

    return executeQuerySethe(properties);
  }

  /**
   * --> poi + cat + 3 aspectos
   * Trajetórias que pararam no Central Park em uma sexta-feira nublada, logo depois usaram metrô para uma festa
   * noturna bem avaliada.
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ4() throws Exception {
	Properties properties = new Properties();
	
	properties.setProperty("q1_asp_poi", "((\\w*) )*(Central Park)(( \\w*))* ; ((\\w*) )*(Subway)(( \\w*))* ; .*");
//	properties.setProperty("q1_asp_poi", "Central Park ; ((\\w*) )*(Subway)(( \\w*))* ; .*");
	properties.setProperty("q1_asp_cat", ".* ; Travel Transport ; Nightlife Spot");
	
	properties.setProperty("q1_asp_day", "Friday ; .*; .* ");
	properties.setProperty("weight_day", "0.4");
	properties.setProperty("limit_day", "1");
	properties.setProperty("distance_day", "equality");
	
	properties.setProperty("q1_asp_weather", "Clouds ; .* ; .*");
	properties.setProperty("weight_weather", "0.2");
	properties.setProperty("limit_weather", "1");
	properties.setProperty("distance_weather", "equality");
	
	properties.setProperty("q1_asp_rating", ".* ; .*; 10 ");
	properties.setProperty("weight_rating", "0.4");
	properties.setProperty("distance_rating", "proportion");
	properties.setProperty("limit_rating", "10");
	
	properties.setProperty("q1_proximity", ".* ; ~ ; ~ ");
	
	return executeQuerySethe(properties);
	  
	  
//    Properties properties = new Properties();
//
//    properties.setProperty("q1_asp_poi", "Central Park ; .* ; .*");
//    properties.setProperty(
//      "q1_asp_cat",
//      ".* ; Travel Transport ; Nightlife Spot"
//    );
//
//    properties.setProperty("q1_asp_day", "Friday ; .*; .* ");
//    properties.setProperty("weight_day", "1");
//    properties.setProperty("limit_day", "1");
//    properties.setProperty("distance_day", "equality");
//
//    properties.setProperty("q1_asp_weather", "Clouds ; .* ; .*");
//    properties.setProperty("weight_weather", "1");
//    properties.setProperty("limit_weather", "1");
//    properties.setProperty("distance_weather", "equality");
//
//    properties.setProperty("q1_asp_rating", ".* ; .*; 7 ");
//    properties.setProperty("weight_rating", "0.7");
//    properties.setProperty("distance_rating", "proportion");
//    properties.setProperty("limit_rating", "10");
//
//    properties.setProperty("q1_proximity", ".* ; ~ ; ~ ");
//
//    return executeQuerySethe(properties);
  }

  /**
   * Trajetórias que passaram pela Madison Square em um sábado ensolarado e terminaram na Times square.
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ5() throws Exception {
	  Properties properties = new Properties();

	    properties.setProperty(
	      "q1_asp_poi",
	      "((\\w*) )*(Madison Square)( \\w*)* ; ((Times Square)( \\w*)*)$"
	    );

	    properties.setProperty("q1_asp_day", "Saturday ; .* ");
	    properties.setProperty("weight_day", "0.6");
	    properties.setProperty("limit_day", "1");
	    properties.setProperty("distance_day", "equality");

	    properties.setProperty("q1_asp_weather", "Clear ; .* ");
	    properties.setProperty("weight_weather", "0.4");
	    properties.setProperty("limit_weather", "1");
	    properties.setProperty("distance_weather", "equality");

	    properties.setProperty("q1_proximity", ".* ; .* ");

	    return executeQuerySethe(properties);
	  
	  
//    Properties properties = new Properties();
//
//    properties.setProperty(
//      "q1_asp_poi",
//      "Madison Square ; (Times Square( \\w*)*)$"
//    );
//
//    properties.setProperty("q1_asp_day", "Saturday ; .* ");
//    properties.setProperty("weight_day", "1");
//    properties.setProperty("limit_day", "1");
//    properties.setProperty("distance_day", "equality");
//
//    properties.setProperty("q1_asp_weather", "Clear ; .* ");
//    properties.setProperty("weight_weather", "1");
//    properties.setProperty("limit_weather", "1");
//    properties.setProperty("distance_weather", "equality");
//
//    properties.setProperty("q1_proximity", ".* ; .* ");
//
//    return executeQuerySethe(properties);
  }

  /**
   * Trajetórias que pararam na Midtown Comics e terminam em um restaurante caro ou em um shopping.
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ6() throws Exception {
	  	Properties properties = new Properties();

	    properties.setProperty("q1_asp_poi", "Midtown Comics ; .*");
	    properties.setProperty("q1_asp_cat", ".* ; (Food|(Shop Service))$");

	    properties.setProperty("q1_asp_price", " .* ; 4 ");
	    properties.setProperty("weight_price", "1");
	    properties.setProperty("distance_price", "proportion");
	    properties.setProperty("limit_price", "4");

	    properties.setProperty("q1_proximity", ".* ; .* ");

	    return executeQuerySethe(properties);
	  
//    Properties properties = new Properties();
//
//    properties.setProperty("q1_asp_poi", "Midtown Comics ; .*");
//    properties.setProperty("q1_asp_cat", ".* ; (Food|Shop & Service)$");
//
//    properties.setProperty("q1_asp_price", " .* ; 4 ");
//    properties.setProperty("weight_price", "0.3");
//    properties.setProperty("distance_price", "proportion");
//    properties.setProperty("limit_price", "4");
//
//    properties.setProperty("q1_proximity", ".* ; .* ");
//
//    return executeQuerySethe(properties);
  }

  /**
   * Trajetórias que começam na residencia em uma segunda-feira chuvosa, e logo em seguida param em um Colégio,
   * Universidade ou trabalho.
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ7() throws Exception {
	  Properties properties = new Properties();

	    properties.setProperty(
	      "q1_asp_cat",
	      "^(Residence) ; (College University|Professional Other Places)"
	    );

	    properties.setProperty("q1_asp_day", "Monday ; .*");
	    properties.setProperty("weight_day", "0.7");
	    properties.setProperty("limit_day", "1");
	    properties.setProperty("distance_day", "equality");

	    properties.setProperty("q1_asp_weather", "Rain ; .*");
	    properties.setProperty("weight_weather", "0.3");
	    properties.setProperty("limit_weather", "1");
	    properties.setProperty("distance_weather", "equality");

	    properties.setProperty("q1_proximity", ".* ; ~");

	    return executeQuerySethe(properties);
	  
//    Properties properties = new Properties();
//
//    properties.setProperty(
//      "q1_asp_cat",
//      "^(Residence) ; (College University|Professional Other Places)"
//    );
//
//    properties.setProperty("q1_asp_day", "Monday ; .*");
//    properties.setProperty("weight_day", "1");
//    properties.setProperty("limit_day", "1");
//    properties.setProperty("distance_day", "equality");
//
//    properties.setProperty("q1_asp_weather", "Rain ; .*");
//    properties.setProperty("weight_weather", "1");
//    properties.setProperty("limit_weather", "1");
//    properties.setProperty("distance_weather", "equality");
//
//    properties.setProperty("q1_proximity", ".* ; ~");
//
//    return executeQuerySethe(properties);
  }

  /**
   * Trajetórias que começam no trabalho ou universidade, termina em uma residência e o clima está sempre claro
   * durante toda a trajetória.
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ8() throws Exception {
	  Properties properties = new Properties();

	    properties.setProperty(
	      "q1_asp_cat",
	      "^((College University)|(Professional Other Places)) ; (Residence)$"
	    );

	    properties.setProperty("q1_asp_weather", "Clear ; (?-)Clear");
	    properties.setProperty("weight_weather", "1");
	    properties.setProperty("limit_weather", "1");
	    properties.setProperty("distance_weather", "equality");

	    properties.setProperty("q1_proximity", ".* ; .*");

	    return executeQuerySethe(properties);
	    
//    Properties properties = new Properties();
//
//    properties.setProperty(
//      "q1_asp_cat",
//      "^(College University|Professional Other Places) ; (Residence)$"
//    );
//
//    properties.setProperty("q1_asp_weather", "Clear ; Clear");
//    properties.setProperty("weight_weather", "1");
//    properties.setProperty("limit_weather", "1");
//    properties.setProperty("distance_weather", "equality");
//
//    properties.setProperty("q1_proximity", ".* ; .*");
//
//    return executeQuerySethe(properties);
  }

  /**
   * Trajetória que começa no shopping ou residência, logo em seguida para em um local de preço e rating alto e
   * termina no Chase Bank.
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ9() throws Exception {
	  Properties properties = new Properties();

	    properties.setProperty("q1_asp_cat", "^(Shop Service|Residence) ; .* ; .*");
	    properties.setProperty("q1_asp_poi", ".* ; .* ; (Chase Bank)( \\w*)*$");

	    properties.setProperty("q1_asp_price", ".* ; 4 ; .*");
	    properties.setProperty("weight_price", "0.3");
	    properties.setProperty("distance_price", "proportion");
	    properties.setProperty("limit_price", "4");

	    properties.setProperty("q1_asp_rating", ".* ; 10 ; .* ");
	    properties.setProperty("weight_rating", "0.7");
	    properties.setProperty("distance_rating", "proportion");
	    properties.setProperty("limit_rating", "10");

	    properties.setProperty("q1_proximity", ".* ; ~; .*");

	    return executeQuerySethe(properties);

//    Properties properties = new Properties();
//
//    properties.setProperty("q1_asp_cat", "^(Shop Service|Residence) ; .* ; .*");
//    properties.setProperty("q1_asp_poi", ".* ; .* ; (Chase Bank)$");
//
//    properties.setProperty("q1_asp_price", ".* ; 4 ; .*");
//    properties.setProperty("weight_price", "0.3");
//    properties.setProperty("distance_price", "proportion");
//    properties.setProperty("limit_price", "4");
//
//    properties.setProperty("q1_asp_rating", ".* ; 10 ; .* ");
//    properties.setProperty("weight_rating", "0.7");
//    properties.setProperty("distance_rating", "proportion");
//    properties.setProperty("limit_rating", "10");
//
//    properties.setProperty("q1_proximity", ".* ; ~");
//
//    return executeQuerySethe(properties);
  }

  /**
   * Trajetórias que começam em um sábado na Times Square, algum tempo depois para em um local bem avaliado e logo em
   * seguida termina em uma residencia ou restaurante.
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ10() throws Exception {
	  Properties properties = new Properties();

	    properties.setProperty("q1_asp_cat", ".* ; .* ; (Residence|Food)$");
	    properties.setProperty("q1_asp_poi", "^(((\\w*) )*(Times Square)(( \\w*))*) ; .* ; .*");

	    properties.setProperty("q1_asp_day", "(Saturday) ; .* ; .*");
	    properties.setProperty("weight_day", "0.3");
	    properties.setProperty("limit_day", "1");
	    properties.setProperty("distance_day", "equality");

	    properties.setProperty("q1_asp_rating", ".* ; 10 ; .* ");
	    properties.setProperty("weight_rating", "0.7");
	    properties.setProperty("distance_rating", "proportion");
	    properties.setProperty("limit_rating", "10");

	    properties.setProperty("q1_proximity", ".* ; .* ; ~");

	    return executeQuerySethe(properties);
	  
	  
//    Properties properties = new Properties();
//
//    properties.setProperty("q1_asp_cat", ".* ; .* ; (Residence|Food)$");
//    properties.setProperty("q1_asp_poi", "^(Times Square( \\w*)*) ; .* ; .*");
//
//    properties.setProperty("q1_asp_day", "^(Saturday) ; .* ; .*");
//    properties.setProperty("weight_day", "1");
//    properties.setProperty("limit_day", "1");
//    properties.setProperty("distance_day", "equality");
//
//    properties.setProperty("q1_asp_price", ".* ; 4 ; .*");
//    properties.setProperty("weight_price", "0.3");
//    properties.setProperty("distance_price", "proportion");
//    properties.setProperty("limit_price", "4");
//
//    properties.setProperty("q1_asp_rating", ".* ; 10 ; .* ");
//    properties.setProperty("weight_rating", "0.7");
//    properties.setProperty("distance_rating", "proportion");
//    properties.setProperty("limit_rating", "10");
//
//    properties.setProperty("q1_proximity", ".* ; .* ; ~");
//
//    return executeQuerySethe(properties);
  }

  private static TimeQ executeQuerySethe(Properties prop) throws Exception {
    prop.setProperty("schema", "foursquare");
    prop.setProperty("dist_func", "jaccard");
    prop.setProperty("pk_column_name", "trajectory_id");
    prop.setProperty("value_column_name", "trajectory_value");
    prop.setProperty("delimiter", ";");
    
    prop.setProperty("pk_column_name", "trajectory_id");
    prop.setProperty("value_column_name", "value");

    CompositeQuery cq = QuerySETHEMain.loadQuery(prop);
    long count = 0l;
    int countSub = 0;

    long t1 = System.currentTimeMillis();
    query.executeQuery(cq);
    while (!cq.getFinalResult().isEmpty()) {
//      cq.getFinalResult().poll().print();
    	mobhsap.model.Trajectory t = cq.getFinalResult().poll(); 
        t.print();
      count++;
      countSub += t.getSubTrajectories().size(); 
    }

    long t2 = System.currentTimeMillis();

    System.out.println("count: " + count);
    System.out.println("========");

    TimeQ time = new TimeQ(t1, t2, "Q1", count);
    time.setCountSubsequece(countSub);

    return time;
  }
}
