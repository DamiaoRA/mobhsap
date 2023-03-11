--Query 01
select traj_fk, regexp_matches(values, 'museidipisa(;(\w*))* ((\w*);)*cappelledipisa'), values from tripbuilder.tb_category;
--select traj_fk, regexp_matches(values, '([a-z]*;*)museidipisa((;[a-z]*)*) ([a-z]*;*)cappelledipisa((;[a-z]*)*)'), values from tripbuilder.tb_category;

resultado: 33
tempo: 158 ms

--Query 02
select traj_fk, regexp_matches(values, '((torridipisa)(;(\w*))* ((\w*);)*(cappelledipisa|chiesedipisa)(;(\w*))*)(.)*(((\w*);)*(cappelledipisa|chiesedipisa)(;(\w*))*) ((\w*);)*(museidipisa)'), values 
from tripbuilder.tb_category
resultado: 22
tempo: 162ms

select traj_fk, regexp_matches(values4, '(torridipisa)(;(\w*))* ((\w*);)*(cappelledipisa|chiesedipisa)(;(\w*))* ((\w*);)*(museidipisa)'), values4 from tripbuilder.tb_category
resultado: 18 
sparql: 15 (enrichedby escrito errado) sparql: 22
tempo: 142ms

--Query 03
select traj_fk, regexp_matches(values, '(torridipisa(;(\w*))* )+((\w*);)*museidipisa'), values from tripbuilder.tb_category
resultado: 145 sparql: 46
tempo : 120ms

--Query 04
select traj_fk, regexp_matches(values, 'Torre_del_Leone Torre_pendente_di_Pisa|Torre_pendente_di_Pisa Torre_del_Leone'), values from tripbuilder.tb_poi
resultado: 9 sparql: 3 (corrigido: tinha uma espaço em brando depois do nome do PoI)
tempo: 106ms

--Query 05
select traj_fk, regexp_matches(values, '^((\w*);)*(museidipisa)(;(\w*))* ((\w*);)*(cappelledipisa)(;(\w*))*$'), values from tripbuilder.tb_category
resultado: 5
obs.: fiz essa consulta de forma a ficar parecida com a versão spaql. Ela só retora trajetórias com 2 pontos

select traj_fk, regexp_matches(values4, '^((\w*);)*(museidipisa)(.*)(cappelledipisa)(;(\w*))*$'), values4 from tripbuilder.tb_category
resultado: 8 sparql: 5 (a consulta sparql só busca trajetórias de 2 pontos)
tempo: 90ms

--Query 06
select traj_fk, regexp_matches(values, '(museidipisa)(.*)((cappelledipisa|chiesedipisa)(;(\w*))*$)*'), values from tripbuilder.tb_category
resultado: 607
tempo: 410ms

select traj_fk, regexp_matches(values, '^((\w*);)*(museidipisa)(.*)((cappelledipisa|chiesedipisa)(;(\w*))*$)*'), values from tripbuilder.tb_category
resultado: 108 spaql:607
tempo: 67ms
obs.: consulta mal elaborada, pois tanto faz se a trajetória termina ou não em uma capela ou igreja

select traj_fk, regexp_matches(values4, '^((\w*);)*(museidipisa)(.*)(cappelledipisa|chiesedipisa)(;(\w*))*$'), values4 from tripbuilder.tb_category
resultado: 24 sparql: 607 (muitas trajetórias só tem 1 ponto)
tempo: 74ms

--Query 07
select traj_fk, regexp_matches(values, '^(((\w*);)*(cappelledipisa)(;\w*)* )(?=(((\w*);)*)(cappelledipisa))(.*)(cappelledipisa)$'), values from tripbuilder.tb_category
resultado: 2
tempo: 71ms
obs.: a lógica da consulta está: trajetórias que começam em uma capela, param em pelo menos uma capela e termina em uma capela

select traj_fk, regexp_matches(values4, '^((\w*);)*(cappelledipisa)(.*)(cappelledipisa)*(.*)(cappelledipisa)(;(\w*))*$'), values4 from tripbuilder.tb_category
resultado: 6 sparql: 2 (o primeiro ponto é uma capela, logo em seguida, as próximas paradas tem que ser uma capela e o ponto final é uma capela)
tempo: 92ms

--Query 08
select traj_fk, regexp_matches(values, '(museidipisa)(;(\w)+)* Bus ((\w)+;)*(cappelledipisa)') as rc
from tripbuilder.tb_category_move tcm

resultado: 10
tempo: 140ms

select * from (
select c.traj_fk, regexp_matches(c.values4, '(museidipisa)(.*)(cappelledipisa)') as rc,
			    regexp_matches(m.values, '(Bus)') as rm, c.values4 
from tripbuilder.tb_category c, tripbuilder.tb_move m
WHERE c.traj_fk = m.traj_fk
) as t
where rc is not null and rm is not null

resultado: 75 sparql: 10 obs: a consulta usa meio de transporte
tempo: 130 ms


--Query 09
select  traj_fk, cat, pois, moves from 
(
select
c.traj_fk,
regexp_matches(c.values, '^(((\w*);)*(cappelledipisa|chiesedipisa))') as rmc,
c.values as cat,
regexp_matches(p.values, 'Torre_pendente_di_Pisa$') as rmp, 
p.values as pois,
regexp_matches(m.values, '^(N\/A) (Bus )*(Bus)$') as rmm,
--regexp_matches(m.values, '') as rmm,
m.values as moves
from tripbuilder.tb_category c, tripbuilder.tb_move m, tripbuilder.tb_poi p
where p.traj_fk = c.traj_fk and p.traj_fk = m.traj_fk
) as t
where rmm is not null  and rmp is not null and rmc is not null 

resultado: 6 sparql:27 (a consulta sparl tá errada.)


select * from 
(
select c.traj_fk, regexp_matches(p.values, 'Torre_pendente_di_Pisa$') as r1, 
regexp_matches(c.values, '^(([a-z]*;*)(cappelledipisa|chiesedipisa)(;[a-z]*)*) ') as r2, 
regexp_matches(m.values, '^(Bus)') as r3, 
c.values, p.values
from tripbuilder.tb_category c, tripbuilder.tb_poi p, tripbuilder.tb_move m
where p.traj_fk = c.traj_fk and p.traj_fk = m.traj_fk
) as t where r1 is not null and r2 is not null and r3 is not null

resultado: 13 sparql: 27 obs.: a consulta envolve meio de transporte
tempo: 195ms


asp_cat = ^cappelledipisa|chiesedipisa
asp_poi = Torre_pendente_di_Pisa$
asp_move = ^(Bus) (Bus )*(Bus)$

select traj_fk , regexp_matches("values", '^cappelledipisa|chiesedipisa'), values from tripbuilder.tb_category 
where traj_fk in (
select traj_fk from
(
select traj_fk, regexp_matches("values", '( Walk Bus )'), values 
from tripbuilder.tb_move
) as tm
)


#Query 10

select traj_fk , 
regexp_matches("values", '^((\w*);)*(torridipisa)(;(\w*))*(.)* Walk (.)* Bus ((\w*);)*(chiesedipisa)(;(\w*))* (.)*(palazzidipisa)(;(\w*))*$'), values 
from tripbuilder.tb_category_move tcm


select traj_fk , regexp_matches("values", '^([a-z]*;*torridipisa[a-z]*;*) (.*chiesedipisa.*) ([a-z]*;*palazzidipisa[a-z]*;*)$'), values from tripbuilder.tb_category 
where traj_fk in (
select traj_fk from
(
select traj_fk, regexp_matches("values", '( Walk Bus )'), values 
from tripbuilder.tb_move
) as tm
)

resultado: 2 sparql: 2 obs.: a consulta envolve meio de transporte
tempo: 88ms

------------------------------------------

Consultando trajetória
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
prefix owl: <http://www.w3.org/2002/07/owl#>

SELECT ?object ?transport ?move_number
WHERE {
  ?object <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://localhost:8080/vocab/Move> .
  ?object <http://localhost:8080/vocab/enrichedBy> ?transport .
  ?object <http://localhost:8080/vocab/move_number> ?move_number .
  ?traj <http://localhost:8080/vocab/has> ?object .
  FILTER (str(?traj) = "http://localhost:8080/resource/TP79")
}
ORDER BY ?object