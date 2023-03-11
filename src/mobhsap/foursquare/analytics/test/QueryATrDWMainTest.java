package sethe.tripbuilder.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QueryATrDWMainTest {

	private static Connection con;
	private static Statement st;
	private static String schema = "dw";

	public static void main(String[] args) throws SQLException {
		try {
			initConnection("localhost", "5432", "postgres", "lsi123");
			List<TimeQ> times = new ArrayList<TimeQ>();
			//TODO renomar as consultas
			times.add(queryQ2());
			times.add(queryQ3());
			times.add(queryQ4());
			times.add(queryQ5());
			times.add(queryQ6());
			times.add(queryQ7());
			times.add(queryQ1());
			times.add(queryQ8());
			times.add(queryQ9());
			times.add(queryQ10());
			times.add(queryQ11());
			times.add(queryQ12());
			System.out.println("Tempo(ms) gasto em cada consulta:");
			for(TimeQ t : times) {
				System.out.println(t.toString());
			}
		} finally {
			if(con != null)
				con.close();
		}
	}

	public static void initConnection(String url1, String port, String user, String pass) throws SQLException {
		String url = "jdbc:postgresql://" + url1 + ":" + port + "/trajetoria";
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pass);
		con = DriverManager.getConnection(url, props);
		con.setSchema(schema);
		st = con.createStatement();
	}

	//--1- Para cada usuário qual a velocidade média viajando do domicílio para um supermercado?
	//-- 193 usuários
	private static TimeQ queryQ1() throws SQLException {
//		String sql = "WITH stopResidence AS (" + 
//				"  select num_trajectory, f.position, f.id_user" + 
//				"  from fato f, tb_poi dimPoi" + 
//				"  where f.id_poi = dimPoi.id" + 
//				"  		and dimPoi.category = 'Residence' " + 
//				")" + 
//				" select SUM(f.distance)/SUM(f.duration) as speed, f.id_user " + 
//				" from fato f, tb_poi dimPoi, stopResidence r" + 
//				" where f.id_poi = dimPoi.id " + 
//				"	and dimPoi.category = 'Shop & Service'" + 
//				"	and f.id_user = r.id_user" + 
//				"	and f.num_trajectory = r.num_trajectory" + 
//				"	and f.position = r.position+1" + 
//				" group by f.id_user";
		
		String sql = "WITH stopResidence AS ( " + 
				"  select num_trajectory, f.position, f.id_user " + 
				"  from fato f, tb_poi dimPoi " + 
				"  where f.id_poi = dimPoi.id " + 
				"  		and dimPoi.category = 'Residence'  " + 
				") " + 
				"select SUM(f.distance)/SUM(f.duration) as speed  " + 
				"from fato f, tb_poi dimPoi, stopResidence r " + 
				"where f.id_poi = dimPoi.id  " + 
				"	and dimPoi.category = 'Shop & Service' " + 
				"	and f.id_user = r.id_user " + 
				"	and f.num_trajectory = r.num_trajectory " + 
				"	and f.position = r.position+1";

		long t1 = System.currentTimeMillis();
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q1", count);
		return time;
	}

	//--2 Qual foi a distância média percorrida por pessoas que usaram transporte público para visitar uma Igreja?
	//-- College & University = Igreja
	//--1194.267625456714 Km
	private static TimeQ queryQ2() throws SQLException {
		long t1 = System.currentTimeMillis();
		String sql = "WITH stopTransport AS (" + 
				"  select f.num_trajectory, f.position, f.id_user" + 
				"  from fato f, tb_poi dimPoi" + 
				"  where f.id_poi = dimPoi.id" + 
				"  		and dimPoi.category = 'Travel & Transport' " + 
				"  		and dimPoi.name ~* '(bus|subway)'" + 
				" )" + 
				" select SUM(f.total_distance)/count(distinct f.id_user)" + 
				" from fato f, tb_poi dimPoi, stopTransport st" + 
				" where f.id_poi = dimPoi.id " + 
				"	and dimPoi.category = 'College & University'" + 
				"	and f.id_user = st.id_user" + 
				"	and f.num_trajectory = st.num_trajectory" + 
				"	and f.position = st.position+1";

		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q2", count);
		return time;
	}
	
//	--3 Qual foi a distância média percorrida por pessoas que usaram transporte público para visitar a Igreja do Rosário?
//	--St Monica's Church = Igreja do Rosário
//	--Resultado9.163915729043126 Km
	private static TimeQ queryQ3() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "WITH stopTransport AS (" + 
				"  select f.num_trajectory, f.position, f.id_user" + 
				"  from fato f, tb_poi dimPoi" + 
				"  where f.id_poi = dimPoi.id" + 
				"  		and dimPoi.category = 'Travel & Transport' " + 
				"  		and dimPoi.name ~* '(bus|subway)'" + 
				" )" + 
				" select SUM(f.total_distance)/count(distinct f.id_user)" + 
				" from fato f, tb_poi dimPoi, stopTransport st" + 
				" where f.id_poi = dimPoi.id " + 
				"	and dimPoi.name = 'The City College of New York'" + 
				"	and f.id_user = st.id_user" + 
				"	and f.num_trajectory = st.num_trajectory" + 
				"	and f.position = st.position+1";

		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q3", count);
		return time;
	}

//	--4 Quais são as trajetórias onde a velocidade média é superior a 40 km/h em tempos de chuva na cidade de Nova Iorque?
	private static TimeQ queryQ4() throws SQLException {
		long t1 = System.currentTimeMillis();

//		String sql = "select f.num_trajectory, sum(f.distance)/sum(f.duration) " + 
//				" from fato f, tb_aspect asp, tb_poi poi" + 
//				" where f.id_aspect = asp.id and f.id_poi = poi.id" + 
//				"	and f.\"position\" > 1" + 
//				"	and asp.value like 'Rain,%'" + 
//				"	and poi.city = 'New York'" + 
//				" group by f.num_trajectory" + 
//				" having sum(f.distance)/sum(f.duration) > 0.006";

		String sql = "select count(distinct f.num_trajectory) " + 
				"from fato f, tb_aspect asp, tb_poi poi " + 
				"where f.id_aspect = asp.id and f.id_poi = poi.id " + 
				"	and f.\"position\" > 1 " + 
				"	and asp.value like 'Rain,%' " + 
				"	and poi.city = 'New York' " + 
				"	and f.duration > 0 " + 
				"	and ((f.distance)/(f.duration) > 40)";

		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q4", count);
		return time;
	}
	
//	--5 Quais são os usuários que viajaram com velocidade média superior a 40 km/h em tempos de chuva no estado de Nova Iorque?
	private static TimeQ queryQ5() throws SQLException {
		long t1 = System.currentTimeMillis();

//		String sql = "select f.num_trajectory, sum(f.distance)/sum(f.duration) " + 
//				" from fato f, tb_aspect asp, tb_poi poi" + 
//				" where f.id_aspect = asp.id and f.id_poi = poi.id" + 
//				"	and f.\"position\" > 1" + 
//				"	and asp.value like 'Rain,%'" + 
//				"	and poi.state = 'New York'" + 
//				" group by f.num_trajectory" + 
//				" having sum(f.distance)/sum(f.duration) > 40";//0.006";
		
		String sql = "select count(distinct f.id_user) " + 
				"from fato f, tb_aspect asp, tb_poi poi " + 
				"where f.id_aspect = asp.id and f.id_poi = poi.id " + 
				"	and f.\"position\" > 1 " + 
				"	and asp.value like 'Rain,%' " + 
				"	and poi.city = 'New York' " + 
				"	and f.duration > 0 " + 
				"	and ((f.distance)/(f.duration) > 40)";

		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q5", count);
		return time;
	}

//	--6 Qual a distância total percorrida por todos os usuários na cidade de Nova Iorque durante o ano de 2022 e que tenham pelo menos uma parada em uma lanchonete?
//	--307695.7062324146 Km
	private static TimeQ queryQ6() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "WITH oneStop AS (" + 
				"  select num_trajectory, f.id_user, min(f.position) as position" + 
				"  from fato f, tb_poi dimPoi, tb_time dimTime" + 
				"  where f.id_poi = dimPoi.id and f.id_time = dimTime.id" + 
				"  		and dimPoi.category = 'Food'" + 
				"  		and dimTime.year = 2012" + 
				"  group by num_trajectory, f.id_user" + 
				" )" + 
				" select sum(f.distance) as distance_ny" + 
				" from fato f, tb_poi dimPoi, tb_time dimTime, oneStop" + 
				" where f.id_poi = dimPoi.id and dimtime.id = f.id_time " + 
				"	and dimPoi.city = 'New York'" + 
				"	and dimTime.year = 2012" + 
				"	and f.num_trajectory = oneStop.num_trajectory" + 
				"	and f.id_user = oneStop.id_user" + 
				"	and f.position > oneStop.position";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q6", count);
		return time;
	}

//	--7 Qual a distância total percorrida por todos os usuários na cidade de Nova Iorque durante os semestres de 2022 e que tenham pelo menos uma parada no Liberty State Park?
//			--Resposta: 
//			      	 49.951929929105	1
//				     23.551602414318577	2
	private static TimeQ queryQ7() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "WITH oneStop AS (" + 
				"  select num_trajectory, f.id_user, min(f.position) as position" + 
				"  from fato f, tb_poi dimPoi, tb_time dimTime" + 
				"  where f.id_poi = dimPoi.id and f.id_time = dimTime.id" + 
				"  		and dimPoi.name ~* 'Liberty State Park'" + 
				"  group by num_trajectory, f.id_user" + 
				" )" + 
				" select sum(f.distance) as distance_ny, dimTime.semester" + 
				" from fato f, tb_poi poi, tb_time dimTime, oneStop" + 
				" where f.id_poi = poi.id and dimtime.id = f.id_time" + 
				"	and poi.city = 'New York'" + 
				"	and dimTime.year = 2012" + 
				"	and f.num_trajectory = oneStop.num_trajectory" + 
				"	and f.id_user = oneStop.id_user" + 
				" group by dimTime.semester";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q7", count);
		return time;
	}
	
//	--8 Em média, quanto tempo uma pessoa leva para sair do museu e visitar um restaurante de rating alto (> 6)?
//	-- Resposta: 7.751445843325336 horas
	private static TimeQ queryQ8() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "WITH oneStop AS (" + 
				"  select num_trajectory, f.id_user, f.position" + 
				"  from fato f, tb_poi dimPoi" + 
				"  where f.id_poi = dimPoi.id" + 
				"  		and dimPoi.category = 'Arts & Entertainment'" + 
				"  group by num_trajectory, f.id_user, f.position" + 
				" )" + 
				" select  sum(f.duration)/count(f.id_user)" + 
				" from fato f, tb_poi dimPoi, tb_aspect dimAspect, oneStop" + 
				" where f.id_poi = dimPoi.id and dimAspect.id = f.id_aspect " + 
				"	and dimPoi.category = 'Food'" + 
				"	and f.num_trajectory = oneStop.num_trajectory" + 
				"	and f.id_user = oneStop.id_user" + 
				"	and f.position = oneStop.position+1" + 
				"	and (substring(dimAspect.value from ',([+-]?([0-9]*[.])?[0-9]+),')::numeric) > 6";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q8", count);
		return time;
	}
	
//	--9 Aproximadamente, qual a distância total percorrida por pessoas dentro do Estado de Nova Jersey no ano de 2022, 
//	--satisfazendo o padrão Residência - Trabalho - Entretenimento, não necessariamente consecutivos, 
//	--com duração de pelo menos 4 horas?
//	--Resposta: 13132.999819012302 km
	private static TimeQ queryQ9() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "WITH stopResidence AS (" + 
				"  select num_trajectory, f.id_user, f.position" + 
				"  from fato f, tb_poi dimPoi" + 
				"  where f.id_poi = dimPoi.id" + 
				"  		and dimPoi.category = 'Residence'" + 
				"		and dimPoi.state = 'New Jersey'" + 
				"  group by num_trajectory, f.id_user, f.position" + 
				" )," + 
				" stopWork AS (" + 
				"  select num_trajectory, f.id_user, f.position" + 
				"  from fato f, tb_poi dimPoi" + 
				"  where f.id_poi = dimPoi.id" + 
				"  		and dimPoi.category = 'Professional & Other Places'" + 
				"		and dimPoi.state = 'New Jersey'" + 
				"  group by num_trajectory, f.id_user, f.position" + 
				" )" + 
				" select SUM(f.distance)" + 
				" from fato f, tb_poi dimPoi, tb_time dimTime, stopResidence, stopWork" + 
				" where f.id_poi = dimPoi.id and f.id_time = dimtime.id" + 
				"	and dimPoi.state = 'New Jersey'" + 
				"	and dimTime.\"year\" = 2012" + 
				"	and dimPoi.category = 'Arts & Entertainment'" + 
				"	and f.num_trajectory=stopResidence.num_trajectory" + 
				"	and f.num_trajectory=stopWork.num_trajectory" + 
				"	and f.id_user = stopResidence.id_user" + 
				"	and f.id_user = stopWork.id_user" + 
				"	and f.position > stopWork.position" + 
				"	and stopWork.position > stopResidence.position" + 
				"	and f.total_duration >= 4";

		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q9", count);
		return time;
	}

//	--10 Em média quanto tempo as pessoas levaram para visitar o Central Park e logo em seguida a Times Square em uma tarde ensolarada*?
//	--   Em média quanto tempo as pessoas levaram para visitar o Central Park e logo em seguida a Times Square em uma manhã clara?
//	- Resposta 1.4552777777777777 horas
	private static TimeQ queryQ10() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "WITH stopCP AS (" + 
				"  select num_trajectory, f.id_user, f.position" + 
				"  from fato f, tb_poi dimPoi" + 
				"  where f.id_poi = dimPoi.id" + 
				"		and dimPoi.name ~* '(Central Park)'" + 
				"  group by num_trajectory, f.id_user, f.position" + 
				" )" + 
				" select SUM(f.duration)/count(f.id_user)" + 
				" from fato f, tb_poi dimPoi, tb_aspect dimAspect, tb_time dimTime, stopCP" + 
				" where f.id_poi = dimPoi.id and f.id_aspect = dimAspect.id and f.id_time = dimTime.id" + 
				"	and dimPoi.name like '%Times Square%'" + 
				"	and dimAspect.value like 'Clear%'" + 
				"	and dimTime.hour >= 5 and dimTime.hour < 12" + 
				"	and f.num_trajectory = stopCP.num_trajectory" + 
				"	and f.id_user = stopCP.id_user" + 
				"	and f.position = stopCP.position+1";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q10", count);
		return time;
	}

//	-- 11 No ano de 2022, qual a distância total percorrida por pessoas que passaram pela Times Square, 
//	-- algum tempo depois pararam em uma lanchonete e logo em seguida em algum evento?
//	-- 11 No ano de 2022, qual a distância total percorrida por pessoas que começaram no New York Sports Clubs, 
//	-- algum tempo depois usaram algum meio de transporte para chegar no Shopping?
//	-- Resposta: 55417.521091532486 km
	private static TimeQ queryQ11() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "WITH startNY AS (" + 
				"  select num_trajectory, f.id_user, f.position" + 
				"  from fato f, tb_poi dimPoi" + 
				"  where f.id_poi = dimPoi.id" + 
				"		and dimPoi.name ~* 'New York Sports Clubs'" + 
				"  group by num_trajectory, f.id_user, f.position" + 
				" )," + 
				" transport AS (" + 
				"  select num_trajectory, f.id_user, f.position" + 
				"  from fato f, tb_poi dimPoi" + 
				"  where f.id_poi = dimPoi.id" + 
				"		and dimPoi.category = 'Travel & Transport'" + 
				"  group by num_trajectory, f.id_user, f.position" + 
				" )" + 
				" select SUM(f.total_distance) " + 
				" from fato f, tb_poi dimPoi, tb_time dimTime, startNY, transport" + 
				" where f.id_poi = dimPoi.id and f.id_time = dimTime.id" + 
				"	and dimPoi.category = 'Shop & Service'" + 
				"	and f.num_trajectory = transport.num_trajectory" + 
				"	and f.id_user =transport.id_user" + 
				"	and f.num_trajectory = startNY.num_trajectory" + 
				"	and f.id_user =startNY.id_user" + 
				"	and f.position > transport.position" + 
				"	and transport.position > startNY.position" + 
				"	and dimTime.year = 2012	";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q11", count);
		return time;
	}
	
//	--12 Duração média por mês de trajetórias que passaram em algum local de entretenimento, 
//	-- depois no McDonalds e terminou em algum lugar de Nova Iorque de preço e rating alto.
//	-- preço > 2
//	-- rating > 3
//	--12 Duração média por mês de trajetórias que começaram em algum local de entretenimento, 
//	-- algum tempo depois passou no Central Park e terminou em algum lugar de Nova Iorque de preço e rating alto.
//	-- preço > 1
//	-- rating > 6
	private static TimeQ queryQ12() throws SQLException {
		long t1 = System.currentTimeMillis();

		String sql = "WITH stopEnt AS (" + 
				"  select num_trajectory, f.id_user, f.position" + 
				"  from fato f, tb_poi dimPoi" + 
				"  where f.id_poi = dimPoi.id" + 
				"		and dimPoi.category = 'Arts & Entertainment'" + 
				"  group by num_trajectory, f.id_user, f.position" + 
				" )," + 
				" stopCP AS (" + 
				"  select num_trajectory, f.id_user, f.position" + 
				"  from fato f, tb_poi dimPoi" + 
				"  where f.id_poi = dimPoi.id" + 
				"		and dimPoi.name ~* 'Central Park'" + 
				"  group by num_trajectory, f.id_user, f.position" + 
				" )" + 
				" select SUM(f.total_duration), dimTime.month, dimTime.year" + 
				" from fato f, tb_poi dimPoi, tb_aspect dimAspect, tb_time dimTime,stopEnt,stopCP" + 
				" where f.id_poi = dimPoi.id and f.id_aspect = dimAspect.id and f.id_time = dimTime.id " + 
				"	and f.num_trajectory = stopEnt.num_trajectory" + 
				"	and f.id_user = stopEnt.id_user" + 
				"	and f.num_trajectory = stopCP.num_trajectory" + 
				"	and f.id_user = stopCP.id_user" + 
				"	and f.position > stopCP.position" + 
				"	and stopCP.position > stopEnt.position" + 
				"	and dimPoi.city = 'New York'" + 
				"	and (substring(dimAspect.value from ',.*,([+-]?([0-9]*[.])?[0-9]+)')::numeric) > 1 " + 
				"	and (substring(dimAspect.value from ',([+-]?([0-9]*[.])?[0-9]+),')::numeric) > 6 " + 
				" group by dimTime.year, dimTime.month ";
		ResultSet rs = st.executeQuery(sql);
		long count = 0;
		while (rs.next()) {
			System.out.println(rs.getString(1));
			count++;
		}

		long t2 = System.currentTimeMillis();
		TimeQ time = new TimeQ(t1, t2, "Q12", count);
		return time;
	}
}