--drop table fato;
--drop table tb_poi;
--drop table tb_category;
--drop table tb_time;
--drop table tb_trajectory;
--drop table tb_user;
--
--drop SEQUENCE tb_category_id_seq;
--drop SEQUENCE tb_poi_id_seq;
--drop SEQUENCE tb_time_id_seq;
--drop SEQUENCE tb_traj_id_seq;
--drop SEQUENCE tb_user_id_seq;
--
--drop table tb_price;
--drop table tb_rating;
--drop table tb_weather;
--drop SEQUENCE tb_price_id_seq;
--drop SEQUENCE tb_rating_id_seq;
--drop SEQUENCE tb_weather_id_seq;

SET search_path TO public;

/**
 * Script default
 */
CREATE SEQUENCE tb_category_id_seq;
CREATE SEQUENCE tb_poi_id_seq;
CREATE SEQUENCE tb_time_id_seq;
CREATE SEQUENCE tb_traj_id_seq;
CREATE SEQUENCE tb_user_id_seq;

CREATE TABLE tb_category(
	id int default nextval('tb_category_id_seq'),
	name varchar(200),
	CONSTRAINT pk_category PRIMARY KEY (id),
	CONSTRAINT uc_category UNIQUE (name)
);

CREATE TABLE tb_poi(
	id int default nextval('tb_poi_id_seq'),
	x float,
	y float,
	name varchar(200),
	CONSTRAINT pk_poi PRIMARY KEY (id)
);

CREATE TABLE tb_time(
	id int default nextval('tb_time_id_seq'),
	minute int,
	hour int,
	day int,
	month int,
	year int,
	CONSTRAINT pk_time PRIMARY KEY(id),
	CONSTRAINT uc_time UNIQUE (minute, hour, day, month, year)
);

CREATE TABLE tb_user(
	id int default nextval('tb_user_id_seq'),
	value varchar(100),
	CONSTRAINT pk_user PRIMARY KEY(id)
);

CREATE TABLE tb_trajectory(
	id int default nextval('tb_traj_id_seq'),
	id_user int,
	value varchar(100),
	CONSTRAINT pk_trajectory PRIMARY KEY(id),
	CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES tb_user(id)
);

CREATE TABLE Fato(
	id_poi int,
	id_category int,
	id_trajectory int,
	id_time int,
	position int,
    duration float,
	repetition int,
	CONSTRAINT fk_fato_poi FOREIGN KEY (id_poi) REFERENCES tb_poi(id),
	CONSTRAINT fk_fato_cat FOREIGN KEY (id_category) REFERENCES tb_category(id),
	CONSTRAINT fk_fato_traj FOREIGN KEY (id_trajectory) REFERENCES tb_trajectory(id),
	CONSTRAINT fk_fato_time FOREIGN KEY (id_time) REFERENCES tb_time(id)
);
commit;

/**
 * Foursquare Script 
 */
CREATE SEQUENCE tb_price_id_seq;
CREATE SEQUENCE tb_rating_id_seq;
CREATE SEQUENCE tb_weather_id_seq;

CREATE TABLE tb_price(
	id int default nextval('tb_price_id_seq'),
	value float,
	CONSTRAINT pk_price PRIMARY KEY (id)
);

CREATE TABLE tb_rating(
	id int default nextval('tb_rating_id_seq'),
	value float,
	CONSTRAINT pk_rating PRIMARY KEY (id)
);

CREATE TABLE tb_weather(
	id int default nextval('tb_weather_id_seq'),
	value varchar(10),
	CONSTRAINT pk_weather PRIMARY KEY (id)
);

ALTER TABLE Fato ADD COLUMN id_price int;
ALTER TABLE Fato ADD COLUMN id_rating int;
ALTER TABLE Fato ADD COLUMN id_weather int;
ALTER TABLE Fato ADD CONSTRAINT fk_price FOREIGN KEY (id_price) REFERENCES tb_price(id);
ALTER TABLE Fato ADD CONSTRAINT fk_rating FOREIGN KEY (id_rating) REFERENCES tb_rating(id);
ALTER TABLE Fato ADD CONSTRAINT fk_weather FOREIGN KEY (id_weather) REFERENCES tb_weather(id);
