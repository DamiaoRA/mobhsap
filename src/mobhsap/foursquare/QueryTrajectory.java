package sethe.foursquare;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import sethe.Query;
import sethe.PoI;
import sethe.Trajectory;

public class QueryTrajectory {
//	private Connection con;
//	private Statement st;
//	private String schema = "trajSem";
//	private PreparedStatement psPoIid = null;
//
//	public QueryTrajectory(String url1, String port, String user, String pass) throws SQLException {
//		String url = "jdbc:postgresql://" + url1 + ":" + port + "/trajetoria";
//		Properties props = new Properties();
//		props.setProperty("user", user);
//		props.setProperty("password", pass);
//		con = DriverManager.getConnection(url, props);
//		st = con.createStatement();
//	}
//
//	private List<Trajectory> executeQuery(Filter query) throws SQLException {
//		List<Trajectory> listTraj = searchTrajectories(query);
//		searchPoIs(listTraj);
//		for(Trajectory t : listTraj) {
//			t.calcSubtrajectory();
//			t.printGraph();
//		}
//		Collections.sort(listTraj);
//		return listTraj;
//	}
//
//	private List<Trajectory> searchTrajectories(Filter query) throws SQLException {
//		List<Trajectory> result = new ArrayList<Trajectory>();
//		//regex sql
//		String regexSql = "regexp_matches(\"values\", '" + query.regex();
//		regexSql += "')";
//
//		//query
//		String sql = "SELECT traj_fk, " + regexSql + ",values FROM " + schema + ".tb_poi_traj";
//		System.out.println("Consulta PoI: " + sql);
//
//		ResultSet rs = st.executeQuery(sql);
//		while(rs.next()) {
//			Trajectory t = new Trajectory();
//			t.setId(rs.getInt(1));
//			t.setText(rs.getString(3));
//			t.setQuery(query);
//			searchAspects(t, query.getAspects());
//			result.add(t);
//		}
//
//		if(result.isEmpty()) {
//			sql = "SELECT traj_fk, " + regexSql + ",values FROM " + schema + ".tb_category";
//			System.out.println("Consulta Category: " + sql);
//			rs = st.executeQuery(sql);
//			while(rs.next()) {
//				Trajectory t = new Trajectory();
//				t.setId(rs.getInt(1));
//				t.setText(rs.getString(3));
//				t.setQuery(query);
//				t.setIsCategory(true);
//				searchAspects(t, query.getAspects());
//				result.add(t);
//			}
//		}
//
//		return result;
//	}
//
//	private void searchPoIs(List<Trajectory> list) throws SQLException {
//		if(psPoIid == null) {
//			String sql = "select p.id, p.name, d.position,c.name "
//				+ "from " + schema + ".tb_poi_traj_date d, " + schema + ".tb_poi p "
//				+ ", " + schema + ".tb_category c " 
//				+ "where d.traj_fk = ?" 
//				+ " and d.poi_fk = p.id and c.id = p.category_fk"
//				+ " order by d.position";
//			psPoIid = con.prepareStatement(sql);
//		}
//
//		for(Trajectory t : list) {
//			psPoIid.setInt(1, t.getId());
//			ResultSet rs = psPoIid.executeQuery();
//			while(rs.next()) {
//				PoI p = new PoI();
//				p.setId(rs.getInt(1));
//				p.setName(rs.getString(2));
//				p.setPosition(rs.getInt(3));
//				p.setCategory(rs.getString(4));
//				t.addPoI(p);
//			}
//		}
//	}
//	
//	private void searchAspects(Trajectory t, Set<String> aspects) throws SQLException {
//		for(String a : aspects) {
//			String text = queryAspect(t.getId(), a);
//			t.addAspect(a, text.split(" "));
//		}
//	}
//
//	private String queryAspect(Integer trajId, String a) throws SQLException {
//		String table = schema + ".tb_" + a;
//		String sql = "SELECT values FROM " + table + " WHERE traj_fk = " + trajId;
//		ResultSet rs = st.executeQuery(sql);
//		if(rs.next()) {
//			return rs.getString(1);
//		}
//		return "";
//	}
//
//	private void close() throws SQLException {
//		con.close();
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static void main(String[] args) throws SQLException, IOException {
//		//Properties file
//		InputStream is = QueryTrajectory.class.getResourceAsStream("semantic.properties");
//		Properties properties = new Properties();
//		properties.load(new InputStreamReader(is, Charset.forName("UTF-8")));
//
//		Filter query = new Filter();
//
//		String p = properties.getProperty("asp_poi");						//Getting PoIs
//		String[] arrayPoi = p.split(";");
//		double poiWeight = 0;//Double.parseDouble(properties.getProperty("weight_poi")); //Getting weight of poi
//		for(String exp : arrayPoi) {
//			query.addExpression(exp, poiWeight); //TODO: a pensar, peso por PoI e não por categoria
//		}
//
//		for(Enumeration e = properties.keys();e.hasMoreElements();) { 		//Getting Aspects
//			String key = e.nextElement().toString();
//			if(key.startsWith("asp_") && !key.equals("asp_poi")) {
//				String[] aspValues = properties.getProperty(key).split(";");
//				key = key.replace("asp_", "");
//				for(int i = 0; i < aspValues.length; i++) {
//					//double weight = Double.parseDouble(properties.getProperty("weight_" + key));
//					//query.addSubExpression(i, key, aspValues[i], weight);
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
//
//		QueryTrajectory qt = null;
//		try {
//			qt = new QueryTrajectory("localhost", "25432", "postgres", "postgres");
//			List<Trajectory> result = qt.executeQuery(query);
//			System.out.println(query);
//			for(Trajectory t : result) {
//				t.print();
//			}
//		} finally{
//			if(qt != null)
//				qt.close();
//		}
//	}
}