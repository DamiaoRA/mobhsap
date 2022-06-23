package sethe;

import sethe.datasource.ETL;
import sethe.foursquare.FoursquareAspectDAO;
import sethe.foursquare.FoursquareInput;
import sethe.tripbuilder.TripBuilderAspectDAO;
import sethe.tripbuilder.TripBuilderInput;

/**
 * O que é necessário para o ETL:
 * - entrada de dados
 * - quais são os aspectos
 * - saída de dados
 *
 */
public class Main_ETL {
	public static void main(String[] args) throws Exception {
		long t1 = System.currentTimeMillis();
		
		ETL etl = new ETL();
		etl.setInput(new FoursquareInput());
		etl.setAspectDAO(new FoursquareAspectDAO());
		etl.start();

//		ETL etl = new ETL();
//		etl.setInput(new TripBuilderInput());
//		etl.setAspectDAO(new TripBuilderAspectDAO());
//		etl.start();

		long t2 = System.currentTimeMillis();
		
		System.out.println("Tempo " + (t2-t1)/1000 + " s");
	}
}
