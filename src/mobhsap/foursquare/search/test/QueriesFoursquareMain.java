package mobhsap.foursquare.search.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import mobhsap.QuerySETHEMain;
import mobhsap.model.CompositeQuery;
import mobhsap.util.TimeQ;

/**
 * compile exec:java -Dexec.mainClass="mobhsap.foursquare.search.test.QueriesFoursquareMain"
 *
 */
public class QueriesFoursquareMain {

  private static QuerySETHEMain qt = null;

  public static void main(String[] args) throws Exception {
		qt = new QuerySETHEMain("localhost", "5432", "postgres", "postgres", "foursquare2", "trajetoria");

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
   * Trajectories that stop at a Residence and then at a Food.
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ1() throws Exception {
    Properties prop = new Properties();
    prop.setProperty("q1_asp_cat", "Residence ; Food");
    prop.setProperty("q1_proximity", ".* ; ~");

    return queryQ(prop);
  }

  /**
   * Consulta 02: trajetória onde para na categoria "Food", e para em um "Residence" ou "Shop & Service", 
   * depois para em um "Residence" ou "Shop & Service" novamente
   * e finaliza parando em um "Food".
   *
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ2() throws Exception {
    Properties properties = new Properties();
    properties.setProperty(
      "q1_asp_cat",
      "Food ; (Residence|Shop Service) ; (Residence|Shop Service) ; (Food)$"
    );
    properties.setProperty("q1_proximity", ".* ;  ~ ; .* ; ~");

    return queryQ(properties);
  }
  
  /**
   * Trajectories that stop at least once in a College, and then at a Event.
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ3() throws Exception {
	    Properties properties = new Properties();
	    properties.setProperty(
	      "q1_asp_cat",
	      "College( (\\w*))* ; Event"
	    );
	    properties.setProperty("q1_proximity", ".* ; ~");

	    return queryQ(properties);
  }

  /**
   * Trajectories that stop at the Bryant Park and then at the Madison Square, or
   * stop at the Madison Square and then at the Bryant Park.
   */
  private static TimeQ queryQ4() throws Exception {
	  Properties prop = new Properties();

	    prop.setProperty("q1_asp_poi", "Bryant Park ; Madison Square Park");
	    prop.setProperty("q1_proximity", ".*			   ; ~");
	    prop.setProperty("q2_asp_poi", "Madison Square Park ; Bryant Park");
	    prop.setProperty("q2_proximity", ".*                    ; ~");

	    return queryQ(prop);
  }

  /**
   * Trajectories that begin at a Residence and then end at a Shop.
   * @return
   * @throws Exception
   */
  private static TimeQ queryQ5() throws Exception {
	    Properties prop = new Properties();

	    prop.setProperty(
	      "q1_asp_cat",
	      "^Residence ; (Shop( \\w*)*)$"
	    );
	    prop.setProperty("q1_proximity", ".* ; ~");

	    return queryQ(prop);
  }

  /**
   * @return
   * @throws Exception
   */
	private static TimeQ queryQ6() throws Exception {
		Properties prop = new Properties();

		prop.setProperty("q1_asp_cat", "Food ; Residence ; Food ; (Residence)?");
		prop.setProperty("q1_proximity", ".* ; .* ; .* ; .*");

		return queryQ(prop);
	}

	/**
	 * Trajectories that begin at a Food, stop at zero or more Food, and end at a Food.
	 * @return
	 * @throws Exception
	 */
	private static TimeQ queryQ7() throws Exception {
		Properties prop = new Properties();

		prop.setProperty("q1_asp_cat",   "^(Food) ; (Food)* ; (Food)$");
		prop.setProperty("q1_proximity", " .*     ; ~      ; ~");

		return queryQ(prop);
	}

	/**
	 * Trajetórias que param em um Food, logo depois pega uma residencia em um dia claro.
	 * @return
	 * @throws Exception
	 */
	private static TimeQ queryQ8() throws Exception {
		Properties prop = new Properties();

		prop.setProperty("q1_asp_cat",   "(Food) ; (Residence)");
		prop.setProperty("q1_proximity", " .*     ; ~      ");
		prop.setProperty("q1_asp_weather", ".*    ; Clear");
		prop.setProperty("weight_weather", "1");
		prop.setProperty("distance_weather", "equality");
		prop.setProperty("limit_weather", "1");

		return queryQ(prop);
	}

	/**
	 * Trajetórias que começam em um Food ou Residence, sempre está chovendo 
	 * entre as paradas e termina no Central Park.
	 * @return
	 * @throws Exception
	 */
	private static TimeQ queryQ9() throws Exception {
		Properties properties = new Properties();
		properties.setProperty("q1_asp_cat", "^(Food|Residence) ; .*");
		properties.setProperty("q1_asp_poi", "  .*              ; ((Central Park)( \\w*)*)$");
		properties.setProperty("q1_asp_weather", " .*           ; (?-)Rain");
		properties.setProperty("q1_proximity", ".*              ; .*");
		properties.setProperty("weight_weather", "1");
		properties.setProperty("distance_weather", "equality");
		properties.setProperty("limit_weather", "1");

		return queryQ(properties);
	}

	/**
	 * Trajetória que começa em uma torre, caminha para pegar um ônibus para chegar
	 * em um igreja, e termina em um palácio.
	 * 
	 * Trajetória que começa em uma Residence, logo depois para em um lugar de rating alto, 
	 * depois para em um Food de preço alto e termina em um Shop
	 * @return
	 * @throws Exception
	 */
	private static TimeQ queryQ10() throws Exception {
		Properties prop = new Properties();

	    prop.setProperty(
	      "q1_asp_cat",
	      "^(Residence) ; .*  ;  Food ; (Shop Service)$"
	    );
	    prop.setProperty(
	  	  "q1_asp_rating",
	  	  " .*          ; 10   ; .*           ; .*"
	  	);
	    prop.setProperty(
	  	  "q1_asp_price",
	  	  " .*          ; .*   ; 4           ; .*"
	  	);
	    prop.setProperty("weight_rating", "0.7");
	    prop.setProperty("distance_rating", "proportion");
	    prop.setProperty("limit_rating", "10");

	    prop.setProperty("weight_price", "0.3");
	    prop.setProperty("distance_price", "proportion");
	    prop.setProperty("limit_price", "4");
	    return queryQ(prop);
	  }

  /**
   * Consulta 3: trajetória que pare em um POI Hotel qualquer, pare em qualquer POI denominado de Bar e finalize em um
   * POI Hotel.
   *
   * Observação: na categoria do POI não explicita se é um hotel ou não, sendo categorizado como "Travel & Transport".
   *
   * @todo Dúvida: Pensei se seria interessante uma pesquisa onde o usuário inicia e finalize no mesmo hotel, sem
   * necessariamente informar um POI em específico. Seria possível?
   * Exemplos de resultados para a mesma consulta:
   * Hotel Guanabara > Manos Bar > Hotel Guanabara
   * Hotel do Bairro > Bar > Hotel do Bairro
   *
   * @return
   * @throws Exception
   */
//  private static TimeQ queryQ3() throws Exception {
//    Properties properties = new Properties();
//    properties.setProperty(
//      "q1_asp_poi",
//      "Hotel( (\\w*))*;((\\w*) )*Bar;Hotel( (\\w*))*$"
//    );
//    properties.setProperty("q1_proximity", "~;.*;~");
//
//    return queryQ(properties);
//  }

  /**
   * Consulta 4: trajetória que parou num POI Hospital, onde posteriormente passou por um local que pode ser Food ou
   * Shop & Service ou Residence e podendo ou não finalizar em outros locais.
   *
   * @return
   * @throws Exception
   */
//  private static TimeQ queryQ4() throws Exception {
//    Properties properties = new Properties();
//    properties.setProperty("q1_asp_poi", "(\\w*)*Hospital;(\\w*)*;(\\w*)*");
//    properties.setProperty(
//      "q1_asp_cat",
//      "(\\w*)*;(Food|Shop Service|Residence);(\\w*)*"
//    );
//    properties.setProperty("q1_proximity", "~;~;.*");
//
//    return queryQ(properties);
//  }

  /**
   * Consulta 05: trajetória que pare em um Shop e finalize Food, esta trajetória passa pelo o dia Sunday e Monday.
   *
   * @return
   * @throws Exception
   */
//  private static TimeQ queryQ5() throws Exception {
//    Properties properties = new Properties();
//    properties.setProperty("q1_asp_cat", "Shop;(Food)$");
//    properties.setProperty("q1_asp_day", "Sunday;Monday");
//    properties.setProperty("q1_proximity", ".*;~");
//    properties.setProperty("weight_day", "1");
//    properties.setProperty("distance_day", "equality");
//    properties.setProperty("limit_day", "1");
//
//    return queryQ(properties);
//  }

  /**
   * Consulta 6: trajetória que pare um local Arts, passe em outros locais, pare em Event com clima Clear, e
   * posteriormente no Nightlife Spot em clima qualquer.
   *
   * @return
   * @throws Exception
   */
//  private static TimeQ queryQ6() throws Exception {
//    Properties properties = new Properties();
//    properties.setProperty(
//      "q1_asp_cat",
//      "Arts(\\w*)*;(\\w*)*;Event;Nightlife Spot"
//    );
//    properties.setProperty("q1_asp_weather", ".*;Rain;Clear;.*");
//    properties.setProperty("weight_weather", "1");
//    properties.setProperty("distance_weather", "equality");
//    properties.setProperty("limit_weather", "1");
//
//    return queryQ(properties);
//  }

  /**
   * Consulta 7: trajetória pare no POI da categoria de Shop, e finalize no Travel & Transport ou Food ou outros locais.
   *
   * @return
   * @throws Exception
   */
//  private static TimeQ queryQ7() throws Exception {
//    Properties properties = new Properties();
//    properties.setProperty(
//      "q1_asp_cat",
//      "Shop( (\\w*))*;((Travel Transport|Food)( (\\w*))*$)*"
//    );
//    properties.setProperty("q1_proximity", "~;.*");
//
//    return queryQ(properties);
//  }

  /**
   * Consulta 8: trajetória que inicial no Travel & Transport e finaliza no Shop & Service.
   *
   * @return
   * @throws Exception
   */
//  private static TimeQ queryQ8() throws Exception {
//    Properties properties = new Properties();
//    properties.setProperty(
//      "q1_asp_cat",
//      "^(Travel Transport);(\\w*)*;(Shop Service)$"
//    );
//    properties.setProperty("q1_proximity", "~;.*;~");
//
//    return queryQ(properties);
//  }

  /**
   * Consulta 9: trajetória que pare em um College & University ou Outdoors & Recreation, onde a trajetória passa no
   * POI Hamilton e o clima seja Rain.
   *
   * @return
   * @throws Exception
   */
//  private static TimeQ queryQ9() throws Exception {
//    Properties properties = new Properties();
//    properties.setProperty(
//      "q1_asp_cat",
//      "^(((\\w*))*(College University|Outdoors Recreation)); .*"
//    );
//    properties.setProperty("q1_asp_poi", "  .* ;Hamilton");
//    properties.setProperty("q1_proximity", ".* ; .*");
//    properties.setProperty("q1_asp_weather", " .* ; (?-)Rain");
//    properties.setProperty("weight_weather", "1");
//    properties.setProperty("distance_weather", "equality");
//    properties.setProperty("limit_weather", "1");
//
//    return queryQ(properties);
//  }

  /**
   * Consulta 10: trajetória que passa no POI Kennedy Center e passa na categoria Event ou Professional & Other Places.
   *
   * @return
   * @throws Exception
   */
//  private static TimeQ queryQ10() throws Exception {
//    Properties properties = new Properties();
//    properties.setProperty("q1_asp_poi", "Kennedy Center;.*");
//    properties.setProperty(
//      "q1_asp_cat",
//      ".*;(Event|Professional Other Places)"
//    );
//    properties.setProperty("q1_proximity", "~;.*");
//
//    return queryQ(properties);
//  }

  private static TimeQ queryQ(Properties prop) throws Exception {
    prop.setProperty("schema", "foursquare");
    prop.setProperty("dist_func", "jaccard");
    prop.setProperty("pk_column_name", "trajectory_id");
    prop.setProperty("value_column_name", "value");
    prop.setProperty("delimiter", ";");

    CompositeQuery cq = QuerySETHEMain.loadQuery(prop);
    long count = 0l;
    int countSub = 0;

    long t1 = System.currentTimeMillis();
    qt.executeQuery(cq);
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
