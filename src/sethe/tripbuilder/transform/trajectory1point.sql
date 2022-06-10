select count (t) from (select t from tripbuilder.trajonepoint group by t) as temp --36.353 trajetória com 1 ponto

select count(id) from tripbuilder.tb_trajectory --19.121 trajetória com mais de 1 ponto 

select count(distinct "object") from tripbuilder.trajectory --55.474 --todas as trajetórias

select count(id) from teste.tb_trajectory --36.353

select * from teste.tb_trajectory t1, tripbuilder.tb_trajectory t2 where t1.id = t2.id -- 0

insert into tripbuilder.tb_trajectory (id, values)  
select id, values from teste.tb_trajectory;

insert into tripbuilder.tb_stop (traj_fk, values)  
select traj_fk, values from teste.tb_stop;

insert into tripbuilder.tb_poi (traj_fk, values)  
select traj_fk, values from teste.tb_poi;

insert into tripbuilder.tb_category (traj_fk, values)  
select traj_fk, values from teste.tb_category;

insert into tripbuilder.tb_locatedin (traj_fk, values)  
select traj_fk, values from teste.tb_locatedin;
