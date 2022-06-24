--Query 01
--Trajectories that stop at a museum and then at a chapel
SELECT distinct f1.id_trajectory, t.value  FROM fato f1
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory
INNER JOIN tb_category c1 ON c1.id = f1.id_category
INNER JOIN tb_category c2 ON c2.id = f2.id_category
inner join tb_trajectory t on t.id = f1.id_trajectory 
WHERE c1.name = 'museidipisa' AND c2.name = 'cappelledipisa'
AND f1.position = f2.position-1

--Query 02
--Trajectories that stop at a tower, then stop at a chapel or church, and then at a museum
SELECT distinct f1.id_trajectory FROM fato f1
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory
INNER JOIN fato f3 ON f1.id_trajectory = f3.id_trajectory
INNER JOIN fato f4 ON f1.id_trajectory = f4.id_trajectory
INNER JOIN tb_category c1 ON c1.id = f1.id_category
INNER JOIN tb_category c2 ON c2.id = f2.id_category
INNER JOIN tb_category c3 ON c3.id = f3.id_category
INNER JOIN tb_category c4 ON c4.id = f4.id_category
WHERE c1.name = 'torridipisa' 
AND (c2.name = 'cappelledipisa' OR c2.name = 'chiesedipisa')
AND (c3.name = 'cappelledipisa' OR c3.name = 'chiesedipisa')
AND c4.name = 'museidipisa'
AND f1.position = f2.position-1
AND f3.position = f4.position-1

--Query 03
--Trajectories that stop at least once in a tower, and then at a museum
SELECT f1.id_trajectory FROM fato f1
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory
INNER JOIN tb_category c1 ON c1.id = f1.id_category
INNER JOIN tb_category c2 ON c2.id = f2.id_category
WHERE c1.name = 'torridipisa' AND c2.name = 'museidipisa'
AND f1.position = f2.position-1

--Query 04
--Trajectories that stop at the Lion Tower and then at the Leaning Tower, or stop at the Leaning Tower and then at the Lion Tower
SELECT distinct f1.id_trajectory, t.value, p1."name", p2."name" FROM fato f1
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory
INNER JOIN tb_poi p1 ON p1.id = f1.id_poi
INNER JOIN tb_poi p2 ON p2.id = f2.id_poi
inner join tb_trajectory t on t.id = f1.id_trajectory 
WHERE p1.name = 'Torre_del_Leone' AND p2.name = 'Torre_pendente_di_Pisa'
AND (f1.position = f2.position-1 OR f2.position = f1.position-1)

--Query 05
--Trajectories that begin at a museum and then end at a chapel
SELECT distinct f1.id_trajectory FROM fato f1
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory
INNER JOIN tb_trajectory t ON t.id = f1.id_trajectory
INNER JOIN tb_category c1 ON c1.id = f1.id_category
INNER JOIN tb_category c2 ON c2.id = f2.id_category
WHERE c1.name = 'museidipisa' AND c2.name = 'cappelledipisa'
AND f1.position = 1 AND f2.position = f1.position+1 AND f2.position = t.size 

--Query 06
--Trajectories that stop at a museum and, later on, end at a chapel or a church optionally
SELECT f1.id_trajectory FROM fato f1
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory
INNER JOIN tb_trajectory t ON t.id = f1.id_trajectory
INNER JOIN tb_category c1 ON c1.id = f1.id_category
INNER JOIN tb_category c2 ON c2.id = f2.id_category
WHERE c1.name = 'museidipisa' 
AND (c2.name = 'cappelledipisa' OR c2.name = 'chiesedipisa')
AND f2.position = t.size AND f1.position < f2.position

--Query 07
--Trajectories that begin at a chapel, stop at zero or more chapels, and end at a chapel
SELECT distinct f1.id_trajectory FROM fato f1
INNER JOIN tb_trajectory t ON t.id = f1.id_trajectory
INNER JOIN tb_category c1 ON c1.id = f1.id_category
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory AND c1.id = f2.id_category
WHERE c1.name = 'cappelledipisa' 
AND f1.position = 1 AND f2.position = t.size

--Query 08
--Trajectories that stop at a museum and then take a bus to a chapel
SELECT distinct f1.id_trajectory FROM fato f1
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory
INNER JOIN tb_transport_mean tm ON tm.id = f2.id_transport_mean
INNER JOIN tb_category c1 ON c1.id = f1.id_category
INNER JOIN tb_category c2 ON c2.id = f2.id_category
WHERE c1.name = 'museidipisa' AND c2.name = 'cappelledipisa'
AND f1.position = f2.position-1 AND tm.value = 'Bus'

--Query 09
--Trajectories that begin at a chapel or a church, always move by bus between stops, and end at the Leaning Tower
SELECT distinct f1.id_trajectory, tt.value FROM fato f1
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory
inner join tb_trajectory tt on tt.id = f1.id_trajectory
INNER JOIN tb_trajectory t ON t.id = f1.id_trajectory
INNER JOIN tb_category c1 ON c1.id = f1.id_category
INNER JOIN tb_poi p ON p.id = f2.id_poi
WHERE f1.position = 1 AND f2.position=t.size
AND (c1.name = 'cappelledipisa' OR c1.name = 'chiesedipisa')
AND p.name = 'Torre_pendente_di_Pisa'
AND NOT EXISTS (
	SELECT id FROM tb_transport_mean tm
	INNER JOIN fato f3 ON f3.id_trajectory = f1.id_trajectory and tm.id=f3.id_transport_mean
	WHERE f3.position > 1 and tm.value <> 'Bus')
	
--Query 10
--Trajectories that begin at a tower, then walk to take a bus to a church, and then using any transportation means end at a palace
SELECT distinct f1.id_trajectory, tt.value
FROM fato f1
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory
INNER JOIN fato f3 ON f1.id_trajectory = f3.id_trajectory
INNER JOIN fato f4 ON f1.id_trajectory = f4.id_trajectory
inner join tb_trajectory tt on tt.id = f1.id_trajectory
INNER JOIN tb_transport_mean tm2 ON tm2.id = f2.id_transport_mean--walk
INNER JOIN tb_transport_mean tm3 ON tm3.id = f3.id_transport_mean--bus church
INNER JOIN tb_category c1 ON c1.id = f1.id_category
INNER JOIN tb_category c3 ON c3.id = f3.id_category
INNER JOIN tb_category c4 ON c4.id = f4.id_category
WHERE c1.name = 'torridipisa' 
AND c3.name = 'chiesedipisa' and c4.name = 'palazzidipisa'
AND f1.position = 1
AND f2.position > f1.position
AND f3.position > f2.position
AND f4.position = tt.size
and tm2.value = 'Walk'
AND tm3.value = 'Bus'

--debug version
SELECT distinct f1.id_trajectory, tt.value, c1.name as c1, f2.id_category as c2, tm2.value as tm2, 
c3.name as c3, tm3.value as tm3, c4.name as c4,
f1.position, f2.position,f3.position,f4.position
FROM fato f1
INNER JOIN fato f2 ON f1.id_trajectory = f2.id_trajectory
INNER JOIN fato f3 ON f1.id_trajectory = f3.id_trajectory
INNER JOIN fato f4 ON f1.id_trajectory = f4.id_trajectory
inner join tb_trajectory tt on tt.id = f1.id_trajectory
INNER JOIN tb_transport_mean tm2 ON tm2.id = f2.id_transport_mean--walk
INNER JOIN tb_transport_mean tm3 ON tm3.id = f3.id_transport_mean--bus church
INNER JOIN tb_category c1 ON c1.id = f1.id_category
INNER JOIN tb_category c3 ON c3.id = f3.id_category
INNER JOIN tb_category c4 ON c4.id = f4.id_category
WHERE c1.name = 'torridipisa' 
AND c3.name = 'chiesedipisa' and c4.name = 'palazzidipisa'
AND f1.position = 1
AND f2.position > f1.position
AND f3.position > f2.position
AND f4.position = tt.size
and tm2.value = 'Walk'
AND tm3.value = 'Bus'

--Desvantagem
  -- quanto mais pontos de parada, mais autojoin sobre a tabela de fato
  -- não faz consulta por aproximação