package sethe.tripbuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import sethe.Query;
import sethe.CompositeQuery;
import sethe.Trajectory;
import sethe.util.StringUtils;

/**
 * Consulta a trajetória com base nos PoI ou (categoria de PoI) e usa o contexto para ordenar os resultados
 *
 */
public class QuerySETHEMain {
	private Connection con;
	private Statement st;
	private String schema = "trajSem";

	public QuerySETHEMain(String url1, String port, String user, String pass, String schema) throws SQLException {
		String url = "jdbc:postgresql://" + url1 + ":" + port + "/trajetoria";
		this.schema = schema;
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pass);
		con = DriverManager.getConnection(url, props);
		st = con.createStatement();
	}

	public void executeQuery(CompositeQuery query) throws Exception {
		for(Query filter : query.getMapFilter().values()) {
			searchTrajectories(filter);
			for(Trajectory t : filter.getMapResultQuery().values()) {
//				t.printGraph();
				query.add(t);
			}
		}
	}

	/**
	 * Pesquisa as trajetórias que obedecem o regex descrito na query
	 * @param filter
	 * @return
	 * @throws SQLException
	 */
	private void searchTrajectories(Query filter) throws Exception {
		String sql = filter.createSqlQuery(schema);

		ResultSet rs = st.executeQuery(sql); //TODO pensar em como fazer paginação
		while(rs.next()) {
			Trajectory t = new Trajectory();
			t.setId(rs.getString(1));
			t.loadText(rs.getString(2), rs.getString(3));//Poi values, category values
			t.setQuery(filter);
			searchAspects(t, filter.getAspects());

			//Calculando as subtrajetórias
			t.calcSubtrajectory();
			//

			filter.addTrajectory(t);
			filter.getQuery().add(t);
		}
	}

	private void searchAspects(Trajectory t, Set<String> aspects) throws Exception {
		for(String a : aspects) {
			String text = queryAspect(t.getId(), a);
			t.addAspect(a, text.split(" "));
		}
	}

	private String queryAspect(String trajId, String a) throws SQLException {
		String table = schema + ".tb_" + a;
		String sql = "SELECT values FROM " + table + " WHERE traj_fk = '" + trajId + "'";
		ResultSet rs = st.executeQuery(sql);
		if(rs.next()) {
			return rs.getString(1);
		}
		return "";
	}

	private void close() throws SQLException {
		con.close();
	}

	public static void main(String[] args) throws Exception {

		//Properties file
		InputStream is = QuerySETHEMain.class.getResourceAsStream("semantic.properties");
		Properties properties = new Properties();
		properties.load(new InputStreamReader(is, Charset.forName("UTF-8")));

		String schema = properties.getProperty("schema");
		CompositeQuery query = loadQuery(properties);

		QuerySETHEMain qt = null;

		try {
			qt = new QuerySETHEMain("localhost", "25432", "postgres", "postgres", schema);
			long t1 = System.currentTimeMillis();

			qt.executeQuery(query);

//			for(Trajectory t : query.getFinalResult()) {
//				t.print();
//			}
			int count = 0;
			while(!query.getFinalResult().isEmpty()) {
				query.getFinalResult().poll().print();
				count++;
			}
			long t2 = System.currentTimeMillis();

			System.out.println("Total: " + count + "\nTempo total: " +  (t2 - t1));
		} finally{
			if(qt != null)
				qt.close();
		}
	}

	@SuppressWarnings("rawtypes")
	public static CompositeQuery loadQuery(Properties properties) {
		CompositeQuery query = new CompositeQuery();
		String distFun = properties.getProperty("dist_func");
		String split = properties.getProperty("split");
		int arraySize = 0;

		Map<String, Query> filters = new HashMap<String, Query>();

		//Getting filters name
		for(Enumeration e = properties.keys();e.hasMoreElements();) {
			String key = e.nextElement().toString();
			if(key.contains("_asp")) {
				String filterName = key.substring(0, key.indexOf("_"));
				filters.put(filterName, null);
			}
		}

		for(String fname : filters.keySet()) {
			Query f = new Query(fname);
			f.setDistanceFunction(distFun);
			f.setQuery(query);

			//Getting PoIs
			String p = properties.getProperty(fname + "_asp_poi");
			String[] arrayPoi = p != null ? p.split(split) : new String[0];
			if(p != null ) arraySize = arrayPoi.length;

			//Getting category
			String c = properties.getProperty(fname + "_asp_cat");
			String[] arrayCat = c != null? c.split(split) : new String[0];
			if(c != null ) arraySize = arrayCat.length;
			
			f.init(arraySize);

			for(int i = 0; i < arraySize; i++) {
				String cat = arrayCat.length > 0 ? arrayCat[i].trim() : null;
				String poi = arrayPoi.length > 0 ? arrayPoi[i].trim() : null;
				f.addExpression(i, cat, poi);
			}

			//Getting proximity
			String proximity = properties.getProperty(fname + "_proximity");
			if(!StringUtils.isEmpty(proximity)) {
				String[] proximityValues = proximity.split(split);
				for(int i = 0; i < proximityValues.length; i++) {
					f.addProximityExpression(i, proximityValues[i]);
				}
			}

			//Getting aspects
			for(Enumeration e = properties.keys();e.hasMoreElements();) {
				String key = e.nextElement().toString();
				if(key.startsWith(fname + "_asp_") && !key.contains("asp_poi") && !key.contains("asp_cat")) {
					String[] aspValues = properties.getProperty(key).split(";");
					key = key.replace(fname + "_asp_", "");
					for(int i = 0; i < aspValues.length; i++) {
						f.addAspectExpression(i, key, aspValues[i].trim());
					}
				} else if (key.startsWith("weight_")) {
					String asp = key.substring(key.indexOf("_") + 1);
					Double value = Double.parseDouble(properties.getProperty(key));
					f.addWeight(asp, value);
				} else if (key.startsWith("distance_")) {
					String asp = key.substring(key.indexOf("_") + 1);
					String value = properties.getProperty(key);
					f.addDistanceFunction(asp, value.trim());
				} else if (key.startsWith("limit_")) {
					String asp = key.substring(key.indexOf("_") + 1);
					Double value = Double.parseDouble(properties.getProperty(key));
					f.addLimitAspValue(asp, value);
				}
			}

			filters.put(fname, f);
		}

		query.setMapFilter(filters);

		return query;
	}
	

//	@SuppressWarnings("rawtypes")
//	public static void loadQuery_old(Query query, Properties properties) {
//		String split = properties.getProperty("split");
//		int arraySize = 0;
//		
//		//Getting PoIs
//		String p = properties.getProperty("asp_poi");
//		String[] arrayPoi = p != null ? p.split(split) : new String[0];
//		if(p != null ) arraySize = arrayPoi.length;
//
//		//Getting category
//		String c = properties.getProperty("asp_cat");
//		String[] arrayCat = c != null? c.split(split) : new String[0];
//		if(c != null ) arraySize = arrayCat.length;
//
//		query.init(arraySize);
//
//		for(int i = 0; i < arraySize; i++) {
//			String cat = arrayCat.length > 0 ? arrayCat[i].trim() : null;
//			String poi = arrayPoi.length > 0 ? arrayPoi[i].trim() : null;
//			query.addExpression(i, cat, poi);
//		}
//
//		//Getting proximity
//		String proximity = properties.getProperty("proximity");
//		String[] proximityValues = proximity.split(split);
//		for(int i = 0; i < proximityValues.length; i++) {
//			query.addProximityExpression(i, proximityValues[i]);
//		}
//
//		//Getting aspects
//		for(Enumeration e = properties.keys();e.hasMoreElements();) {
//			String key = e.nextElement().toString();
//			if(key.startsWith("asp_") && !key.equals("asp_poi") && !key.equals("asp_cat")) {
//				String[] aspValues = properties.getProperty(key).split(";");
//				key = key.replace("asp_", "");
//				for(int i = 0; i < aspValues.length; i++) {
//					query.addAspectExpression(i, key, aspValues[i]);
//				}
//			} else if (key.startsWith("weight_")) {
//				String asp = key.substring(key.indexOf("_") + 1);
//				Double value = Double.parseDouble(properties.getProperty(key));
//				query.addWeight(asp, value);
//			} else if (key.startsWith("distance_")) {
//				String asp = key.substring(key.indexOf("_") + 1);
//				String value = properties.getProperty(key);
//				query.addDistanceFunction(asp, value);
//			} else if (key.startsWith("limit_")) {
//				String asp = key.substring(key.indexOf("_") + 1);
//				Double value = Double.parseDouble(properties.getProperty(key));
//				query.addLimitAspValue(asp, value);
//			}
//		}
//	}
}