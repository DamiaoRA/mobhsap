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
-- 134 usuários

select SUM(f.distance)/SUM(f.duration) as speed, f.id_user 
from fato f, tb_poi dimPoi
where f.id_poi = dimPoi.id 
    and f.historic_category like '%Residence,Shop & Service'
group by f.id_user


--2 Qual foi a distância média percorrida por pessoas que usaram transporte público para visitar uma Igreja?
-- College & University = Igreja
-- Resposta = 15.680857793683527 graus
--Resposta = 4265.403038256964 km

select distinct f.num_trajectory --SUM(f.total_distance)/count(distinct id_user)
from fato f
where f.historic_category like '%Travel & Transport,College & University'
	and f.historic_poi ~* '.*(bus(( *)(\w*))*,(( *)(\w+))*)$'
--1217.9881477483373 km
	
select historic_poi, historic_category 
from fato
where 
historic_poi ~* '.*(bus(( *)(\w*))*,(( *)(\w+))*)$'
and 
historic_category like '%Travel & Transport,College & University'
and num_trajectory = '9888'

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
--Resposta = 0.3462758370785988 graus
--Resposta = 97.07307157041234 km

select SUM(f.total_distance)/count(distinct id_user)
from fato f
where f.historic_poi like '%,The City College of New York'
	and f.historic_poi ~* '(bus|subway)(.*),(The City College of New York)'
-- Resultado 32.38675376214307

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
from fato f, tb_aspect asp, tb_poi dimPoi
where f.id_aspect = asp.id and f.id_poi = dimPoi.id
	and f."position" > 1
	and asp.value like 'Rain,%'
	and poi.city = 'New York'
group by f.num_trajectory
having sum(f.distance)/sum(f.duration) > 40


--5 Quais são os usuários que viajaram com velocidade média superior a 40 km/h em tempos de chuva no estado de Nova Iorque?
select f.num_trajectory, sum(f.distance)/sum(f.duration) 
from fato f, tb_aspect asp, tb_poi dimPoi
where f.id_aspect = asp.id and f.id_poi = dimPoi.id
	and f."position" > 1
	and asp.value like 'Rain,%'
	and dimPoi.state = 'New York'
group by f.num_trajectory
having sum(f.distance)/sum(f.duration) > 40

--6 Qual a distância total percorrida por todos os usuários na cidade de Nova Iorque durante o ano de 2022 e que tenham pelo menos uma parada em uma lanchonete?
-- ano 2022 = ano 2012
--Resposta = 1398.3587589340084 graus ~ 15.3819,463482741 km


--322198.22470946243 Km
select sum(f.distance) as distance_ny
from fato f, tb_poi dimPoi, tb_time dimTime
where f.id_poi = dimPoi.id and dimtime.id = f.id_time 
	and dimPoi.city = 'New York'
	and dimTime.year = 2012
	and f.historic_category ~* 'Food'

--Resposta = 393486.3934414246 Km
WITH oneStop AS (
  select num_trajectory, f.id_user
  from fato f, tb_poi dimPoi, tb_time dimTime
  where f.id_poi = dimPoi.id and f.id_time = dimTime.id
  		and f.historic_category ~* 'Food'
  		and dimPoi.city = 'New York'
  		and dimTime.year = 2012
  group by num_trajectory, f.id_user 
)
select sum(f.distance) as distance_ny
from fato f, tb_poi dimPoi, tb_time dimTime, oneStop
where f.id_poi = dimPoi.id and dimtime.id = f.id_time 
	and dimPoi.city = 'New York'
	and dimTime.year = 2012
	and f.num_trajectory = oneStop.num_trajectory
	and f.id_user = oneStop.id_user
	
--	exists (select f2.num_trajectory from fato f2 where f2.num_trajectory=f.num_trajectory and f2.historic_category like '%,Food%')

	
--select f.num_trajectory, f."position" 
--from fato f, tb_poi poi, tb_time dimTime
--where f.id_poi = poi.id and dimtime.id = f.id_time 
--	and poi.city = 'New York'
--	and dimTime.year = 2012
--	and exists (select f2.num_trajectory from fato f2 where f2.num_trajectory=f.num_trajectory and f2.historic_category like '%,Food%')
--group by f.num_trajectory, f."position"
--order by f.num_trajectory, f."position" 

--7 Qual a distância total percorrida por todos os usuários na cidade de Nova Iorque durante os semestres de 2022 e que tenham pelo menos uma parada no McDonalds?
--7 Qual a distância total percorrida por todos os usuários na cidade de Nova Iorque durante os semestres de 2022 e que tenham pelo menos uma parada no Liberty State Park?
--Resposta: 
--49.951929929105		1
--23.551602414318577	2
	
select sum(f.distance) as distance_ny, dimTime.semester
from fato f, tb_poi poi, tb_time dimTime
where f.id_poi = poi.id and dimtime.id = f.id_time
	and poi.city = 'New York'
	and dimTime.year = 2012
	and f.historic_poi ~* 'Liberty State Park'
group by dimTime.semester


--8 Em média, quanto tempo uma pessoa leva para sair do museu e visitar um restaurante de rating alto (> 6)?
--4.9699745557804755 horas
select sum(f.duration)/count(id_user)
from fato f, tb_aspect dimAspect
where dimAspect.id = f.id_aspect 
	and f.historic_category like '%Arts & Entertainment,Food%'
	and (substring(dimAspect.value from ',([+-]?([0-9]*[.])?[0-9]+),')::numeric) > 6

--9 Aproximadamente, qual a distância total percorrida por pessoas dentro do Estado de Nova Jersey no ano de 2022, 
--satisfazendo o padrão Residência - Trabalho - Entretenimento, não necessariamente consecutivos, 
--com duração de pelo menos 4 horas?
--Resposta: 23248.60808065809 km
--Obs: tenho que usar esse recurso quanto associo o aspecto a alguma outra dimensão, exemplo: cidade. 
--Não é possível responder essa consulta, pois não sei se Residencia e Trabalho são PoI de Nova Jersey
 
WITH Pattern AS (
  select num_trajectory, total_duration
  from fato 
  where historic_category like '%Residence%Professional & Other Places%Arts & Entertainment'
  	and total_duration >= 4
  group by num_trajectory 
)
select SUM(f.distance)
from fato f, tb_poi dimPoi, tb_time dimTime, Pattern pattern
where f.id_poi = dimPoi.id and f.id_time = dimtime.id
	and dimPoi.state = 'New Jersey'
	and dimTime."year" = 2012
	and f.num_trajectory = pattern.num_trajectory
	
--9b Aproximadamente, qual a distância total percorrida no ano de 2022,
--satisfazendo o padrão Residência - Trabalho - Entretenimento, não necessariamente consecutivos, 
--com duração de pelo menos 4 horas?
--Resposta: 6280.0893688255255 KM
select SUM(f.distance)
from fato f, tb_poi dimPoi, tb_time dimTime
where f.id_poi = dimPoi.id and f.id_time = dimtime.id
	and dimTime."year" = 2012
	and f.historic_category like 'Residence%Professional & Other Places%Arts & Entertainment'
	and f.total_duration >= 4


--10 Em média quanto tempo as pessoas levaram para visitar o Central Park e logo em seguida a Times Square em uma tarde ensolarada*?
--   Em média quanto tempo as pessoas levaram para visitar o Central Park e logo em seguida a Times Square em uma manhã clara?
-- Resposta 1.4552777777777777 horas
select SUM(f.duration)/count(id_user)
from fato f, tb_poi dimPoi, tb_aspect dimAspect, tb_time dimTime
where f.id_poi = dimPoi.id and f.id_aspect = dimAspect.id and f.id_time = dimTime.id
	and dimPoi."name" like '%Times Square%'
	and f.historic_poi ~* '(Central Park (\w)*,(\w)*Times Square)'
	and dimAspect.value like 'Clear%'
	and dimTime."hour" >= 5 and dimTime."hour" < 12

-- 11 No ano de 2022, qual a distância total percorrida por pessoas que passaram pela Times Square, 
-- algum tempo depois pararam em uma lanchonete e logo em seguida em algum evento?
-- 11 No ano de 2022, qual a distância total percorrida por pessoas que começaram no New York Sports Clubs, 
-- algum tempo depois usaram algum meio de transporte para chegar no Shopping?
-- Resposta: 527.3173970235181 km
select SUM(f.total_distance) 
from fato f, tb_poi dimPoi, tb_time dimTime
where f.id_poi = dimPoi.id and f.id_time = dimTime.id
	and dimPoi.category like 'Shop%'
	and f.historic_poi like 'New York Sports Clubs%'
	and f.historic_category like '%Transport,Shop & Service'
	and dimTime.year = 2012
	
	
--12 Duração média por mês de trajetórias que passaram em algum local de entretenimento, 
-- depois no McDonalds e terminou em algum lugar de Nova Iorque de preço e rating alto.
-- preço > 2
-- rating > 3
--12 Duração média por mês de trajetórias que começaram em algum local de entretenimento, 
-- algum tempo depois passou no Central Park e terminou em algum lugar de Nova Iorque de preço e rating alto.
-- preço > 1
-- rating > 6
select SUM(f.total_duration), dimTime.month, dimTime.year
from fato f, tb_poi dimPoi, tb_aspect dimAspect, tb_time dimTime
where f.id_poi = dimPoi.id and f.id_aspect = dimAspect.id and f.id_time = dimTime.id 
	and f.historic_category like 'Arts & Entertainment%'
	and f.historic_poi like '%,Central Park%,%'
	and dimPoi.city = 'New York'
	and (substring(dimAspect.value from ',.*,([+-]?([0-9]*[.])?[0-9]+)')::numeric) > 1 --preço
	and (substring(dimAspect.value from ',([+-]?([0-9]*[.])?[0-9]+),')::numeric) > 6 --rating
group by dimTime.year, dimTime.month 

--Resposta
48.431111111111115	10	2012
304.87166666666667	0	2013
127.37527777777777	1	2013


