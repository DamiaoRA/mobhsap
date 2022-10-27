id|name                       |
--+---------------------------+
 1|Residence                  |
 2|Food                       |
 3|Travel & Transport         |
 4|Professional & Other Places|
 5|Shop & Service             |
 6|Outdoors & Recreation      |
 7|College & University       |
 8|Arts & Entertainment       |
 9|Nightlife Spot             |
10|Event                      |


--TODO: criar um novo DW sem as colunas de histórico. Comparar com as consultas: tempo e memória 

--1- Para cada usuário qual a velocidade média viajando do domicílio para um supermercado?
-- 193 usuários

select SUM(f.distance)/SUM(f.duration) as speed, f.id_user 
from fato f, tb_poi poi
where f.id_poi = poi.id 
    and f.historic_category like '%Residence,Shop & Service'
group by f.id_user


--2 Qual foi a distância média percorrida por pessoas que usaram transporte público para visitar uma Igreja?
-- Professional & Other Places = Igreja
-- rating = -1
-- Resposta = 15.680857793683527 graus

select SUM(f.total_distance)/count(distinct id_user)
from fato f, tb_aspect asp, tb_poi poi
where f.id_aspect =asp.id and f.id_poi = poi.id 
	  and poi.category = 'Professional & Other Places'
	  and asp.value like '%,-1.0,%' --weather,rating,price

select SUM(f.total_distance)/count(distinct id_user)
from fato f, tb_aspect asp
where f.id_aspect =asp.id 
	  and f.historic_category like '%Professional & Other Places' 
	  and asp.value like '%,-1.0,%' --weather,rating,price

--3 Qual foi a distância média percorrida por pessoas que usaram transporte público para visitar a Igreja do Rosário?
--St Monica's Church = Igreja do Rosário
--Resposta = 0.3462758370785988
select SUM(f.total_distance)/count(distinct id_user)
from fato f, tb_aspect asp, tb_poi poi
where f.id_aspect =asp.id and f.id_poi = poi.id
	  and poi."name" = 'St Monica''s Church' 
	  and asp.value like '%,-1.0,%' --weather,rating,price

--4 Quais são as trajetórias onde a velocidade média é superior a 40 km/h em tempos de chuva na cidade de Nova Iorque?
--1 grau      = 110 km 
--0,363636364 =  40 km
--40km/h = 0,363636364 graus/h = 0,006060606 graus/min

select f.num_trajectory, sum(f.distance)/sum(f.duration) 
from fato f, tb_aspect asp, tb_poi poi
where f.id_aspect = asp.id and f.id_poi = poi.id
	and f."position" > 1
	and asp.value like 'Rain,%'
	and poi.city = 'New York'
group by f.num_trajectory
having sum(f.distance)/sum(f.duration) > 0.006


--5 Quais são os usuários que viajaram com velocidade média superior a 40 km/h em tempos de chuva no estado de Nova Iorque?
--select f.num_trajectory, sum(f.distance)/sum(f.duration) 
--from fato f, tb_aspect asp, tb_poi poi
--where f.id_aspect = asp.id and f.id_poi = poi.id
--	and f."position" > 1
--	and asp.value like 'Rain,%'
--	and poi.state = 'New York'
--group by f.num_trajectory
--having sum(f.distance)/sum(f.duration) > 0.006

--6 Qual a distância total percorrida por todos os usuários na cidade de Nova Iorque durante o ano de 2022 e que tenham pelo menos uma parada em uma lanchonete?
-- ano 2022 = ano 2012
--Resposta = 1398.3587589340084 graus ~ 15.3819,463482741 km
select sum(f.distance) as distance_ny
from fato f, tb_poi poi, tb_time dimTime
where f.id_poi = poi.id and dimtime.id = f.id_time 
	and poi.city = 'New York'
	and dimTime.year = 2012
	and exists (select f2.num_trajectory from fato f2 where f2.num_trajectory=f.num_trajectory and f2.historic_category like '%,Food%')

	
--select f.num_trajectory, f."position" 
--from fato f, tb_poi poi, tb_time dimTime
--where f.id_poi = poi.id and dimtime.id = f.id_time 
--	and poi.city = 'New York'
--	and dimTime.year = 2012
--	and exists (select f2.num_trajectory from fato f2 where f2.num_trajectory=f.num_trajectory and f2.historic_category like '%,Food%')
--group by f.num_trajectory, f."position"
--order by f.num_trajectory, f."position" 

--7 Qual a distância total percorrida por todos os usuários na cidade de Nova Iorque durante os semestres de 2022 e que tenham pelo menos uma parada no McDonalds?
-- Resposta = 221.24653608096062
select sum(f.distance) as distance_ny
from fato f, tb_poi poi, tb_time dimTime
where f.id_poi = poi.id and dimtime.id = f.id_time 
	and poi.city = 'New York'
	and dimTime.year = 2012
	and exists (select f2.num_trajectory from fato f2 where f2.num_trajectory=f.num_trajectory and f2.historic_poi like '%,McDonald''s%')
    
