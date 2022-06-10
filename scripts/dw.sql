--drop table tb_poi_category;
--drop table tb_poi;
--drop table tb_category;
--drop table tb_time;
--drop table tb_trajectory;
--drop table tb_user;

CREATE TABLE tb_category(
	id int,
	name varchar(200),
	CONSTRAINT pk_category PRIMARY KEY (id),
	CONSTRAINT uc_category UNIQUE (name)
);
CREATE TABLE tb_poi(
	id int,
	x float,
	y float,
	name varchar(200),
	CONSTRAINT pk_poi PRIMARY KEY (id)
);
commit;

CREATE TABLE tb_poi_category(
	id_poi int,
	id_category int,
	CONSTRAINT fk_poi FOREIGN KEY (id_poi) REFERENCES tb_poi(id),
	CONSTRAINT fk_category FOREIGN KEY (id_category) REFERENCES tb_category(id),
	CONSTRAINT uc_poi_category UNIQUE (id_poi, id_category)
);

CREATE TABLE tb_time(
	id int,
	minute int,
	hour int,
	day int,
	month int,
	year int,
	CONSTRAINT pk_time PRIMARY KEY(id),
	CONSTRAINT uc_time UNIQUE (minute, hour, day, month, year)
);

CREATE TABLE tb_user(
	id int,
	name varchar(200),
	CONSTRAINT pk_user PRIMARY KEY(id)
);

CREATE TABLE tb_trajectory(
	id int,
	size int,
	CONSTRAINT pk_trajectory PRIMARY KEY(id)
);

CREATE TABLE Fato(
	id_poi int,
	id_trajectory int,
	id_time int,
	position int,
      	duration float,
	repetition int,
	CONSTRAINT fk_fato_poi FOREIGN KEY (id_poi) REFERENCES tb_poi(id),
	CONSTRAINT fk_fato_traj FOREIGN KEY (id_trajectory) REFERENCES tb_trajectory(id),
	CONSTRAINT fk_fato_time FOREIGN KEY (id_time) REFERENCES tb_time(id)
);
commit