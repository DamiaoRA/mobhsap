CREATE TABLE tripbuilder.tb_trajectory(
	id varchar(200),
	"values" text
);

ALTER TABLE tripbuilder.tb_trajectory ADD CONSTRAINT tripbuilder_tb_trajectory_pk PRIMARY KEY (id);

CREATE TABLE tripbuilder.tb_stop(
	traj_fk varchar(200),
	values text
);

ALTER TABLE tripbuilder.tb_stop ADD CONSTRAINT tripbuilder_tb_stop_fk FOREIGN KEY (traj_fk) REFERENCES tripbuilder.tb_trajectory(id);


CREATE TABLE tripbuilder.tb_poi(
	traj_fk varchar(200),
	values text
);

ALTER TABLE tripbuilder.tb_poi ADD CONSTRAINT tripbuilder_tb_poi_fk FOREIGN KEY (traj_fk) REFERENCES tripbuilder.tb_trajectory(id);

CREATE TABLE tripbuilder.tb_category(
	traj_fk varchar(200),
	values text
);

ALTER TABLE tripbuilder.tb_category ADD CONSTRAINT tripbuilder_tb_category_fk FOREIGN KEY (traj_fk) REFERENCES tripbuilder.tb_trajectory(id);

CREATE TABLE tripbuilder.tb_locatedin(
	traj_fk varchar(200),
	values text
);

ALTER TABLE tripbuilder.tb_locatedin ADD CONSTRAINT tripbuilder_tb_locatedin_fk FOREIGN KEY (traj_fk) REFERENCES tripbuilder.tb_trajectory(id);

CREATE TABLE tripbuilder.tb_move(
	traj_fk varchar(200),
	values text
);

ALTER TABLE tripbuilder.tb_move ADD CONSTRAINT tripbuilder_tb_move_fk FOREIGN KEY (traj_fk) REFERENCES tripbuilder.tb_trajectory(id);

CREATE TABLE tripbuilder.tb_poi_move(
	traj_fk varchar(200),
	values text
);

ALTER TABLE tripbuilder.tb_poi_move ADD CONSTRAINT tripbuilder_tb_poimove_fk FOREIGN KEY (traj_fk) REFERENCES tripbuilder.tb_trajectory(id);

CREATE TABLE tripbuilder.tb_category_move(
	traj_fk varchar(200),
	values text
);

ALTER TABLE tripbuilder.tb_category_move ADD CONSTRAINT tripbuilder_tb_catmove_fk FOREIGN KEY (traj_fk) REFERENCES tripbuilder.tb_trajectory(id);

--------------
delete from tripbuilder.tb_move;
delete from tripbuilder.tb_locatedin;
delete from tripbuilder.tb_category;
delete from tripbuilder.tb_poi;
delete from tripbuilder.tb_trajectory;
delete from tripbuilder.tb_category_move;
delete from tripbuilder.tb_poi_move;