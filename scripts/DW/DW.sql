--drop table fato;
--drop table tb_poi;
--drop table tb_time;
--drop table tb_user;
--drop table tb_aspect;
--
--drop SEQUENCE tb_poi_id_seq;
--drop SEQUENCE tb_time_id_seq;
--drop SEQUENCE tb_user_id_seq;
--drop SEQUENCE tb_asp_id_seq;
--

CREATE SCHEMA dw_tripbuilder;
SET search_path TO dw_tripbuilder;

/**
 * Script default
 */
CREATE SEQUENCE tb_poi_id_seq;
CREATE SEQUENCE tb_time_id_seq;
CREATE SEQUENCE tb_user_id_seq;
CREATE SEQUENCE tb_asp_id_seq;

CREATE TABLE tb_poi(
	id int default nextval('tb_poi_id_seq'),
	x float,
	y float,
	name varchar(200),
	category varchar(200),
	city varchar(200),
	state varchar(200),
	country varchar(200),
	CONSTRAINT pk_poi PRIMARY KEY (id),
	CONSTRAINT uc_poi UNIQUE (x,y,name, category, city, state, country)
);


CREATE TABLE tb_time(
	id int default nextval('tb_time_id_seq'),
	second int,
	minute int,
	hour int,
	day int,
	month int,
	semester int,
	year int,
	datetime timestamp,
	CONSTRAINT pk_time PRIMARY KEY(id),
	CONSTRAINT uc_time UNIQUE (second, minute, hour, day, month, year)
);

CREATE TABLE tb_user(
	id int default nextval('tb_user_id_seq'),
	name varchar(100),
	CONSTRAINT pk_user PRIMARY KEY(id)
);

CREATE TABLE tb_aspect(
	id int default nextval('tb_asp_id_seq'),
	value text,
	type text,
	CONSTRAINT pk_aspect PRIMARY KEY (id)
);

CREATE TABLE Fato(
	id_poi int,
	id_user int,
	id_aspect int,
	id_time int,
	num_trajectory varchar(100),
	distance float,
	total_distance float,
    duration float,
	total_duration float,
	historic_poi text,
	historic_category text,
	position int,
	CONSTRAINT fk_fato_poi FOREIGN KEY (id_poi) REFERENCES tb_poi(id),
	CONSTRAINT fk_fato_asp FOREIGN KEY (id_aspect) REFERENCES tb_aspect(id),
	CONSTRAINT fk_fato_user FOREIGN KEY (id_user) REFERENCES tb_user(id),
	CONSTRAINT fk_fato_time FOREIGN KEY (id_time) REFERENCES tb_time(id)
);
