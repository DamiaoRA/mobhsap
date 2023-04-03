package mobhsap;

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

import mobhsap.model.CompositeQuery;
import mobhsap.model.Query;
import mobhsap.model.Trajectory;
import mobhsap.util.Constants;
import mobhsap.util.StringUtils;

/**
 * Consulta a trajetória com base nos PoI ou (categoria de PoI) e usa o contexto para ordenar os resultados
 *
 */
public class QuerySETHEMain {

  private Connection con;
  private Statement st;
  private String schema = "trajSem";
  private String pkColumnName = "traj_fk";
  private String valueColumnName = "values";

  public QuerySETHEMain(
    String url1,
    String port,
    String user,
    String pass,
    String schema
  ) throws SQLException {
    String url = "jdbc:postgresql://" + url1 + ":" + port + "/trajetoria";
    this.schema = schema;
    Properties props = new Properties();
    props.setProperty("user", user);
    props.setProperty("password", pass);
    con = DriverManager.getConnection(url, props);
    st = con.createStatement();
  }

  public QuerySETHEMain(
    String host,
    String port,
    String user,
    String pass,
    String schema,
    String database
//    ,
//    String pkColumnName,
//    String valueColumnName
  ) throws SQLException {
    String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
    this.schema = schema;
//    this.pkColumnName = pkColumnName;
//    this.valueColumnName = valueColumnName;
    Properties options = new Properties();
    options.setProperty("user", user);
    options.setProperty("password", pass);
    con = DriverManager.getConnection(url, options);
    st = con.createStatement();
  }

  public void executeQuery(CompositeQuery query) throws Exception {
	this.pkColumnName = query.getPkTrajColumnName();
	this.valueColumnName = query.getValueColumnName();
    for (Query filter : query.getMapFilter().values()) {
      searchTrajectories(filter, query.getDelimiter());
      for (Trajectory t : filter.getMapResultQuery().values()) {
        query.add(t);
      }
    }
  }

  /**
   * Search trajectories that follow the regex described in the query
   * @param filter
   * @return
   * @throws SQLException
   */
  private void searchTrajectories(Query filter, String delimiter)
    throws Exception {
    String sql = filter.createSqlQuery(schema);

    ResultSet rs = st.executeQuery(sql);
    while (rs.next()) {
      Trajectory t = new Trajectory(filter, delimiter);
      t.setId(rs.getString(1));
      t.loadText(rs.getString(2), rs.getString(3)); //Poi values, category values
      t.setQuery(filter);
      searchAspects(t, filter.getAspects(), delimiter);

      //Calculating the subtrajectories
      t.calcSubtrajectory();
      //
      
      if(t.getCoefficient() != 0) {
	      filter.addTrajectory(t);
	      filter.getQuery().add(t);
      }
    }
  }

  /**
   * 
   * @param t
   * @param aspects
   * @param delimiter
   * @throws Exception
   */
  private void searchAspects(Trajectory t, Set<String> aspects, String delimiter)
    throws Exception {
    for (String a : aspects) {
    	if(!Constants.isProximity(a)) {
	      String text = queryAspect(t.getId(), a);
	      if(!StringUtils.isEmpty(text))
	    	  t.addAspect(a, text.split(delimiter));
    	}
    }
  }

  private String queryAspect(String trajectoryId, String aspect)
    throws SQLException {
    String table = schema + ".tb_" + aspect;
    String sql = String.format(
      "SELECT %s FROM %s WHERE %s = '%s'",
      valueColumnName,
      table,
      pkColumnName,
      trajectoryId
    );
    ResultSet rs = st.executeQuery(sql);

    return rs.next() ? rs.getString(1) : "";
  }

  private void close() throws SQLException {
    con.close();
  }

  public static void main(String[] args) throws Exception {
    //Properties file
    InputStream is =
      QuerySETHEMain.class.getResourceAsStream("semantic.properties");
    Properties properties = new Properties();
    properties.load(new InputStreamReader(is, Charset.forName("UTF-8")));

    String schema = properties.getProperty("schema");
    CompositeQuery query = loadQuery(properties);

    QuerySETHEMain qt = null;

    try {
      qt =
        new QuerySETHEMain(
          "localhost",
          "25432",
          "postgres",
          "postgres",
          schema
        );
      long t1 = System.currentTimeMillis();

      qt.executeQuery(query);

      //			for(Trajectory t : query.getFinalResult()) {
      //				t.print();
      //			}
      int count = 0;
      while (!query.getFinalResult().isEmpty()) {
        query.getFinalResult().poll().print();
        count++;
      }
      long t2 = System.currentTimeMillis();

      System.out.println("Total: " + count + "\nTempo total: " + (t2 - t1));
    } finally {
      if (qt != null) qt.close();
    }
  }

  /**
   * Read the query expressions
   * @param properties
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static CompositeQuery loadQuery(Properties properties) {
    CompositeQuery cquery = new CompositeQuery();
    String distFun = properties.getProperty("dist_func");
    String split = " ; ";

    String pkColumn = properties.getProperty("pk_column_name");
    String valueColumn = properties.getProperty("value_column_name");
    String delimiter = properties.getProperty("delimiter");

    cquery.setPkTrajColumnName(pkColumn);
    cquery.setValueColumnName(valueColumn);
    cquery.setDelimiter(delimiter);

    int arraySize = 0;

    Map<String, Query> queries = new HashMap<String, Query>();

    //Getting name of the queries
    for (Enumeration e = properties.keys(); e.hasMoreElements();) {
      String key = e.nextElement().toString();
      if (key.contains("_asp")) {
        String queryName = key.substring(0, key.indexOf("_"));
        queries.put(queryName, null);
      }
    }

    for (String fname : queries.keySet()) {
      Query query = new Query(fname, cquery);
      query.setDistanceFunction(distFun);
      //      f.setQuery(query);

      //Getting PoIs
      String p = properties.getProperty(fname + "_asp_poi");
      String[] arrayEPoi = p != null ? p.split(split) : new String[0];
      if (p != null) arraySize = arrayEPoi.length;

      //Getting category
      String c = properties.getProperty(fname + "_asp_cat");
      String[] arrayECat = c != null ? c.split(split) : new String[0];
      if (c != null) arraySize = arrayECat.length;

//      PoI Weight
      String poiw  = properties.getProperty(fname + "_poi_weight");
      String[] arrayPw = poiw != null ? poiw.split(split) : new String[0];

      query.init(arraySize);

      for (int i = 0; i < arraySize; i++) {
        String cat = arrayECat.length > 0 ? arrayECat[i].trim() : null;
        String poi = arrayEPoi.length > 0 ? arrayEPoi[i].trim() : null;
        double weight = arrayPw.length > 0 ? Double.parseDouble(arrayPw[i]) : 1;
        query.addExpression(i, cat, poi, weight);
      }
      query.calcLastExpression();

      //Getting proximity
      String proximity = properties.getProperty(fname + "_proximity");
      if (!StringUtils.isEmpty(proximity)) {
        String[] proximityValues = proximity.split(split);
        for (int i = 0; i < proximityValues.length; i++) {
          query.addProximityExpression(i, proximityValues[i]);
        }
      }

      //Getting aspects
      for (Enumeration e = properties.keys(); e.hasMoreElements();) {
        String key = e.nextElement().toString();
        if (
          key.startsWith(fname + "_asp_") &&
          !key.contains("asp_poi") &&
          !key.contains("asp_cat")
        ) {
          String[] aspValues = properties.getProperty(key).split(";");
          key = key.replace(fname + "_asp_", "");
          for (int i = 0; i < aspValues.length; i++) {
            query.addAspectExpression(i, key, aspValues[i].trim());
          }
        } else if (key.startsWith("weight_")) {
          String asp = key.substring(key.indexOf("_") + 1);
          Double value = Double.parseDouble(properties.getProperty(key));
          query.addWeight(asp, value);
        } else if (key.startsWith("distance_")) {
          String asp = key.substring(key.indexOf("_") + 1);
          String value = properties.getProperty(key);
          query.addDistanceFunction(asp, value.trim());
        } else if (key.startsWith("limit_")) {
          String asp = key.substring(key.indexOf("_") + 1);
          Double value = Double.parseDouble(properties.getProperty(key));
          query.addLimitAspValue(asp, value);
        }
      }

      queries.put(fname, query);
    }

    cquery.setMapFilter(queries);

    return cquery;
  }
}
