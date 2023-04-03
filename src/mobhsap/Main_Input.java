package mobhsap;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobhsap.datasource.AspectDAOIF;
import mobhsap.datasource.ETL;
import mobhsap.datasource.InputMessageIF;
import mobhsap.foursquare.FoursquareAspectDAO;
import mobhsap.foursquare.FoursquareInput;
import mobhsap.model.Message;
import mobhsap.tripbuilder.TripBuilderAspectDAO;
import mobhsap.tripbuilder.TripBuilderInput;
import mobhsap.util.StringUtils;
import mobhsap.util.Trajectory2Text;

/**
 * O que é necessário para o ETL:
 * - entrada de dados
 * - quais são os aspectos
 * - saída de dados
 *
 */
public class Main_Input {
	private InputMessageIF inputPoi;
	private AspectDAOIF aspectDao;
	private ETL etl;
	private Trajectory2Text traj2text;
	private String separator;

	public Main_Input() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, SQLException {
		etl = new ETL();
		readProperties();
//		traj2text = new Trajectory2Text(separator);
	}
	public Object getInstance(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<?> o = Class.forName(className);
	    Constructor<?> oCon = o.getConstructor();
        Object p = oCon.newInstance();
        return p;
	}
	public void readProperties() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
	    InputStream is =
	      QuerySETHEMain.class.getResourceAsStream("/input.properties");
	    Properties properties = new Properties();
	    properties.load(new InputStreamReader(is, Charset.forName("UTF-8")));
	    String inputClassName = properties.getProperty("input_class");
	    String aspectDaoClassName = properties.getProperty("aspectDao_class");
	    
	    inputPoi = (InputMessageIF)getInstance(inputClassName);
	    aspectDao = (AspectDAOIF)getInstance(aspectDaoClassName);
	    separator = properties.getProperty("separator");
	    if(StringUtils.isEmpty(separator))
	    	separator = " ";

	    etl.setAspectDAO(aspectDao);
	}

	public void start() throws Exception {
		Message m = inputPoi.nextMessage();
		traj2text = new Trajectory2Text(m.getaspectsType(), separator);
		while(m != null) {
//			etl.nextMessage(m);
			traj2text.nextMessage(m);
			m = inputPoi.nextMessage();
		}
		etl.finish();
	}

	public static void main(String[] args) throws Exception {
		long t1 = System.currentTimeMillis();
		
		Main_Input main = new Main_Input();
		main.start();

//		ETL etl = new ETL();
//		etl.setInput(null);
//		etl.setAspectDAO(null);
//		etl.setInput(new FoursquareInput());
//		etl.setAspectDAO(new FoursquareAspectDAO());
//		etl.start();

		/*ETL etl = new ETL();
		etl.setInput(new TripBuilderInput());
		etl.setAspectDAO(new TripBuilderAspectDAO());
		etl.start();*/

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
