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


--Número de Trajetórias
--3079
select count(*) from tb_category_traj tct 


---CATEGORIAS

--Q1 Trajetórias que param em um museu e depois em uma capela
--Q1 Trajetórias que param em uma Residence e depois em um Shop
--1492 resultados
select count(*) from (
select traj_fk , regexp_matches("values", '(Residence)(.)+(Shop)'), values from tb_category_traj
) as t
 
--Q2 Trajetórias param em uma torre, depois em uma capela ou em uma igreja e depois em um museu
--Q2 Trajetórias que param em uma Residence, depois em um Shop ou Food e depois em um Nightlife Spot
--570 resultado

select count(*) from (
select traj_fk , regexp_matches("values", '(Residence)(.)+(Shop|Food)(.)+(Nightlife Spot)'), values from tb_category_traj order by traj_fk
) as t


--Q3 Trajetórias que passam pelo menos em um torre e depois em um museu
--Q3 Trajetórias que passam pelo menos em um 'Travel & Transport' e depois em um 'College & University'
--238 resultados
select count(*) from (
select traj_fk , regexp_matches("values", '(Travel)+(.)+(College)'), values from tb_category_traj order by traj_fk
) as t

--Q4 Trajetórias que param na 'Lion Tower' e depois na 'Leaning Tower', ou param na 'Leaning Tower' e depois na na 'Lion Tower' (consulta a nível de PoI)
--Q4 Trajetórias que param em um Event e depois em Food, ou param em um Food e depois em um event
--22 resultados
select count(*) from (
select traj_fk , regexp_matches("values", '(Event)(.)+(Food) | (Food)(.)+(Event)'), values from tb_category_traj order by traj_fk
) as t 

--Q5 Trajetórias que começam em um museu e termina em uma capela
--Q5 Trajetórias que começam em uma Residence e termina em uma Residence
--192 resultados

select count(*) from (
select traj_fk , regexp_matches("values", '^(Residence).+(Residence( )*)$'), values from tb_category_traj order by traj_fk
) as t 

--Q6 Trajetórias que pararam em um museu e terminaram em uma capela ou uma igreja (Museidipipisa;stop*;(Cappelledipisa | Chiesedipisa)? A End)
--Q6 Trajetórias que pararam em um Food e terminaram em um 'Shop & Service' ou 'Entertainment'
--591 resultados
select count(*) from (
select traj_fk , regexp_matches("values", '(Food).+((Shop & Service | Entertainment)( )*)$'), values from tb_category_traj order by traj_fk
) as t 

--Q7 Trajetórias que começam em uma capela, para em zero ou mais capelas e termina em uma capela
--Q7 Trajetórias que começam em um 'Food', para em zero ou mais 'Food' e termina em um 'Food'
--190 resultados
select count(*) from (
select traj_fk , regexp_matches("values", '^(Food).+(Food)*.*(Food( )*)$'), values from tb_category_traj order by traj_fk
) as t 

--Q8 Trajetórias que param em um museu e depois pega um ônibus para uma capela
--Q8 Trajetórias que param em uma Residence, depois 'Travel & Transport' e logo depois para em um 'College & University'
--73 resultados
select count(*) from (
select traj_fk , regexp_matches("values", '(Residence).+(Travel & Transport)( )?(College & University)'), values from tb_category_traj order by traj_fk
) as t 

--Q9 Trajetórias que começam em uma capela ou um igreja, sempre se move de ônibus entre as paradas e termina na Torre de Pisa.
--Q9 Trajetórias que começam em uma 'Residence', é intercaldo por 'Travel & Transport' e termina em um 'Professional & Other Places'
--Exemplo: Residence Travel & Transport Food Travel & Transport Residence 
select count(*) from (
select traj_fk , regexp_matches("values", '^(Residence)( (Travel & Transport) (?!(Travel & Transport))([a-zA-Z])+)*( Travel & Transport Residence)( )$'), values 
from tb_category_traj order by traj_fk
) as t 

--Q10 Trajetórias que começam em uma torre, vai caminhando para um ônibus para igreja, e depois vai para um 'palace' usando qualquer meio de transporte
--Q10 Trajetórias que começam em uma Residence, depois passa por 'Arts & Entertainment' e logo em seguida 'Food' para chegar  ao 'Shop & Service'
--1 resultado
select traj_fk , regexp_matches("values", '^(Residence).*(Arts & Entertainment Food Shop & Service )$'), values 
from tb_category_traj order by traj_fk


select traj_fk , regexp_matches("values", '^(Residence).+(Shop & Service( )*)$'), values from tb_category_traj order by traj_fk


--------------------------PoIS
select * from public.tb_poi where "name" like '%Popeye%'
select count(*) from public.tb_poi --15402
select * from public.tb_poi_traj order by traj_fk

--Q1 Trajetórias que param em um museu e depois em uma capela
--Q1 Trajetórias que param no 'The Lair Of Modern Strange Cowboy' e depois no 'Popeyes Louisiana Kitchen'
--1 resultado
select count(*) from (
select traj_fk , regexp_matches("values", '.*(The Lair Of Modern Strange Cowboy)(.)+(Popeyes Louisiana Kitchen)'), values from tb_poi_traj
) as t

--Q4 Trajetórias que param na 'Lion Tower' e depois na 'Leaning Tower', ou param na 'Leaning Tower' e depois na na 'Lion Tower' (consulta a nível de PoI)
--Q4 Trajetórias que param na Popeye e depois no Starbucks, ou param no Starbucks e depois no Popeye
--9 resultados
select count(*) from (
select traj_fk , regexp_matches("values", '(Popeye)(.)+(Starbucks) | (Starbucks)(.)+(Popeye)'), values from tb_poi_traj order by traj_fk
) as t 

select traj_fk , regexp_matches("values", '(Popeye''s)'), values from tb_poi_traj order by traj_fk

select traj_fk , regexp_matches("values", '(McDonald)(.)+(Museum)(.)'), values from tb_poi_traj
select traj_fk , regexp_matches("values", '(Museum)(.)+(Starbucks)'), values from tb_poi_traj
select traj_fk , regexp_matches("values", '(McDonald)(.)+(Rubin Residence Hall)'), values from tb_poi_traj


select traj_fk , regexp_matches("values", '(Port Authority Bus Terminal)(.)+(Lincoln Tunnel)(.)+(New York Hilton Midtown)'), values from tb_poi_traj

select traj_fk , regexp_matches("values", '(Times Square)(.)+(McDonald)(.)+(market)'), values from tb_poi_traj

select traj_fk , regexp_matches("values", '(Times Square)(.)+(Planet Fitness)'), values from tb_poi_traj


------------------Weather
select * from public.tb_weather 
--Clear, Clouds, Fog, Rain, Snow, Unknown

--Q2 Trajetórias param em uma torre, depois em uma capela ou em uma igreja e depois em um museu
--Q2 Trajetórias que tem Clear, depois Clouds ou Fog, e depois Snow
--40 resultados

select count(*) from (
select traj_fk , regexp_matches("values", '(Clear)(.)+(Clouds|Fog)(.)+(Snow)'), values from public.tb_weather order by traj_fk
) as t

--Q7 Trajetórias que começam em uma capela, para em zero ou mais capelas e termina em uma capela
--Q7 Trajetórias que começam em 'Fog', passa em zero ou mais 'Rain' e termina em 'Clear'
--36 resultados
select count(*) from (
select traj_fk , regexp_matches("values", '^(Fog).+(Rain)*.*(Clear *)$'), values from tb_weather order by traj_fk
) as t 


-------------
select * from public.tb_rating tr where traj_fk in (26144,26119,2182);
select * from public.tb_rating tr where traj_fk in (14550,28490,5849);

select * from public.tb_poi_traj where traj_fk in (14550,28490,5849);
select * from public.tb_poi_traj where values like '%Astor Row Café%';
select * from public.tb_poi_traj where values like '%Lenox Coffee%';
select * from public.tb_poi_traj where values like '%Joe the Art of Coffee%';
select * from public.tb_poi_traj where values like '%Coffee Shop%';
select * from public.tb_poi_traj where values like '%Saint Thomas Church%' and values like '%Coffee%';
select * from public.tb_poi_traj where values like '%Central Park East I Elementary School%' and values like '%Planet Fitness%' and values like '%Joe the Art of Coffee%';



--Quero as trajetórias que começam no 'Joe the Art of Coffee', passas pelo 'Central Park East I Elementary School'
select traj_fk , regexp_matches("values", '^(Joe the Art of Coffee)(.)+(Central Park East I Elementary School)(.)*(Shrine World Music Venue )$'), values from tb_poi_traj
select traj_fk , regexp_matches("values", '^(Food)(.)+(Professional & Other Places)(.)*(Arts & Entertainment )$'), values from public.tb_category_traj
select traj_fk , regexp_matches("values", '^(Rain)(.)+(Clouds)(.)*(Clear )$'), values from public.tb_weather

--Quero as trajetórias que começam no 'Central Park East I Elementary School', passas pelo 'Planet Fitness' e depois pelo 'Joe the Art of Coffee'
select traj_fk , regexp_matches("values", '^(Central Park East I Elementary School)(.)+(Planet Fitness)(.)*(Joe the Art of Coffee )'), values from tb_poi_traj
select traj_fk , regexp_matches("values", '^(Professional & Other)(.)+(Places Outdoors & Recreation)(.)*(Food )'), values from public.tb_category_traj where traj_fk in (142,143,145,146,149)
select * from public.tb_weather where traj_fk in (142,143,145,146,149)




select * from tb_poi_traj where traj_fk in (132,290,1822,2627,2709,3253,3852,4315,5064,5105,5175,5179,5187,5217,5846,6921,8213,8231,8926,8930,8955,8963,10580,10891,11988,12452,12527,12569,13019,13382,14535,15147,15314,16600,16601,17495,18397,18400,18450,19200,19206,19649,19724,20168,20382,20754,20998,21371,21543,23223,24358,24430,24452,24453,24724,24974,25339,26998,27203,28246,28915,29153)
order by values



select traj_fk, array_length(regexp_split_to_array(trim(values), E'\\W+'), 1),"values"  from public.tb_rating tr 
order by array_length(regexp_split_to_array(trim(values), E'\\W+'), 1);

select traj_fk, array_length(regexp_split_to_array(trim(values), E'\\W+'), 1),"values"  from public.tb_price tp --where traj_fk in (26144,26119,2182)
order by array_length(regexp_split_to_array(trim(values), E'\\W+'), 1) ;

select tp.traj_fk, array_length(regexp_split_to_array(trim(tr.values), E'\\s+'), 1),
				array_length(regexp_split_to_array(trim(tp.values), E'\\s+'), 1),
	   tr."values" , tp."values"
from tb_rating tr, tb_price tp where tr.traj_fk  = tp.traj_fk --and tp.traj_fk in (14557,19770,4800)
order by array_length(regexp_split_to_array(trim(tr.values), E'\\s+'), 1) desc;

14550	13	13	-1 6.5 -1 9.4 9.1 7 -1 8.9 9.5 -1 6.6 -1 7.6 	-1 2 -1 -1 2 2 -1 3 -1 2 2 -1 2 
28490	13	13	-1 7.4 6.4 -1 6.4 -1 -1 7.2 -1 -1 7.6 -1 7.7 	-1 2 2 -1 2 -1 2 1 1 -1 2 -1 2 
5849	13	13	-1 6.3 -1 -1 9 9.1 9.2 8.1 9 9.4 8.8 8.5 -1 	1 1 -1 3 -1 4 3 1 -1 3 -1 3 2 




select traj_fk, regexp_split_to_array(trim(values), E'\\s+'),"values"  from public.tb_rating tr 

---------------------------------
--MEDIANA
select percentile_cont(0.5) within group(order by rating) from data_checkin where rating <> -1;
--7.3

select percentile_cont(0.5) within group(order by price) from data_checkin where price <> -1;
--1

SELECT traj_fk, regexp_matches("values", '(McDonalds)(.)+(Coffee)(.)+(home)'),values FROM tb_poi_traj

select poi_fk, count(traj_fk) from tb_poi_traj_date
group by poi_fk order by count(traj_fk) desc 


