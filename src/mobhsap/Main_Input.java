package sethe;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

//		ETL etl = new ETL();
//		etl.setInput(new FoursquareInput());
//		etl.setAspectDAO(new FoursquareAspectDAO());
//		etl.start();

		ETL etl = new ETL();
		etl.setInput(new TripBuilderInput());
		etl.setAspectDAO(new TripBuilderAspectDAO());
		etl.start();

		long t2 = System.currentTimeMillis();
		
		System.out.println("Tempo " + (t2-t1)/1000 + " s");
		
//		String s = "Bab , '!@#$&*()[]-/\\:;.?| Alhara Restaurant | مطعم باب الحارة (Bab Alhara Restaurant) Market 昌旺市場";
//		
//		String f = "";
//		int countSpace = 0;
//		for(int i = 0 ; i < s.length(); i++) {
//			Character c = s.charAt(i);
//			if(Charset.forName("US-ASCII").newEncoder().canEncode(c)) {
//				String t = (c+"").replaceAll( "[+,'!@#$&\\*()\\[\\]\\-/\\\\:;\\.\\?|]", "" );
//				if(t.isEmpty())
//					continue;
//				if(Character.isWhitespace(c)) {
//					countSpace++;
//				} else {
//					countSpace = 0;
//				}
//				if(countSpace <= 1) {
//					f += c+"";
//				}
//					
//			}
//		}
//		f = f.trim();
//		System.out.println(f);
	}
}
