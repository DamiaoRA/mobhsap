CREATE SCHEMA foursquare_origin;


SET default_tablespace = '';

--
-- TOC entry 243 (class 1259 OID 20455)
-- Name: data_checkin; Type: TABLE; Schema: foursquare_origin; Owner: -
--

CREATE TABLE foursquare_origin.data_checkin (
    checkin_id integer,
    tid integer,
    lat double precision,
    lon double precision,
    date_time timestamp without time zone,
    "time" integer,
    day character varying(50),
    poi_category character varying(200),
    price integer,
    rating double precision,
    weather character varying(50),
    label integer,
    poi_name character varying(200),
    anonymized_user_id integer
);


--
-- TOC entry 284 (class 1259 OID 37187)
-- Name: data_checkin_geom; Type: TABLE; Schema: foursquare_origin; Owner: -
--

CREATE TABLE foursquare_origin.data_checkin_geom (
   checkin_id integer,
    tid integer,
    lat double precision,
    lon double precision,
    date_time timestamp without time zone,
    "time" integer,
    day character varying(50),
    poi_category character varying(200),
    price integer,
    rating double precision,
    weather character varying(50),
    label integer,
    poi_name character varying(200),
    anonymized_user_id integer,
    geom postgis.geometry(Point,4326),
    city character varying(200),
    state character varying(200),
    country character varying(200)
);


INSERT INTO foursquare_origin.data_checkin VALUES (285481, 126, 40.7116349040813006, -74.0102935014650996, '2012-11-16 16:45:38', 1005, 'Friday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA - Church St & Barclay St (BM1/BM2/BM3/BM4)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (285722, 126, 40.9123450467867968, -73.9027547836303995, '2012-11-16 21:04:59', 1264, 'Friday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA MaBSTOA & Bee-Line Bus at Riverdale Ave & W. 263rd St: (Bx7, Bx10, BxM1, BxM2, BxM18 & BL 8)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (285772, 126, 40.7463791194240983, -73.9818906784058044, '2012-11-16 22:05:06', 1325, 'Friday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - 33rd St (6)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (285815, 126, 40.6904333657826029, -73.9852809906006001, '2012-11-16 23:02:30', 1382, 'Friday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - Hoyt St (2/3)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (285896, 126, 40.7994853414516996, -73.9683072897887968, '2012-11-17 00:58:11', 58, 'Saturday', 'Food', 2, 8, 'Clear', 6, 'Yakitori Sun-Chan', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (285897, 126, 40.7946757855249018, -73.9717626571654989, '2012-11-17 01:01:00', 61, 'Saturday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - 96th St (1/2/3)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (285940, 126, 40.8331652006224033, -73.9418603427692034, '2012-11-17 02:35:32', 155, 'Saturday', 'Residence', -1, -1, 'Clear', 6, 'The Lair Of Modern Strange Cowboy', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (286262, 126, 40.7571644769378025, -73.9144165607694958, '2012-11-17 14:27:31', 867, 'Saturday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - 46th St (M/R)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (286435, 126, 40.7491426612534013, -73.9378380775451944, '2012-11-17 17:20:38', 1040, 'Saturday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - Queens Plaza (E/M/R)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (287234, 127, 40.834097804107202, -73.9452672225881003, '2012-11-19 13:08:12', 788, 'Monday', 'Food', 1, 8.19999999999999929, 'Clouds', 6, 'Galaxy Gourmet Deli', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (287467, 127, 40.5671960000000027, -73.8825760000000002, '2012-11-19 19:35:16', 1175, 'Monday', 'Travel & Transport', -1, -1, 'Clouds', 6, 'MTA Bus - Beach 169 St & Rockaway Point Bl (Q22/Q35)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (287603, 127, 40.6899127194574035, -73.981504440307603, '2012-11-19 23:01:09', 1381, 'Monday', 'Travel & Transport', -1, -1, 'Clouds', 6, 'MTA Subway - DeKalb Ave (B/Q/R)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (287614, 127, 40.7085883614823985, -73.9910316467285014, '2012-11-19 23:24:32', 1404, 'Monday', 'Travel & Transport', -1, -1, 'Clouds', 6, 'MTA Subway - Manhattan Bridge (B/D/N/Q)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (288060, 127, 40.8331652006224033, -73.9418603427692034, '2012-11-20 14:05:34', 845, 'Tuesday', 'Residence', -1, -1, 'Clear', 6, 'The Grinnell', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (288164, 127, 40.7085883614823985, -73.9910316467285014, '2012-11-20 17:01:18', 1021, 'Tuesday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - Manhattan Bridge (B/D/N/Q)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (288223, 127, 40.7590904609108975, -73.8300561904906942, '2012-11-20 18:17:15', 1097, 'Tuesday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Bus Stop - Q25/Q34/Q65', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (288399, 127, 40.7594786665510966, -73.8300985117754038, '2012-11-20 21:23:44', 1283, 'Tuesday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - Flushing/Main St (7)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (288450, 127, 40.6899127194574035, -73.981504440307603, '2012-11-20 23:06:42', 1386, 'Tuesday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - DeKalb Ave (B/Q/R)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (289214, 127, 40.8487589952160022, -73.9382779598235942, '2012-11-22 00:40:59', 40, 'Thursday', 'Travel & Transport', -1, 7.40000000000000036, 'Clear', 6, 'MTA New York City Bus - W. 178th Street & Ft. Washington Avenue (Bx3/Bx7/Bx11/Bx13/Bx35/Bx36/Bx36LTD/M4/M4LTD/M5/M5LTD/M98LTD/M100)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (289218, 127, 40.8488458333229971, -73.938417434692397, '2012-11-22 00:48:04', 48, 'Thursday', 'Travel & Transport', -1, 5.59999999999999964, 'Clear', 6, 'George Washington Bridge Bus Station', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (289227, 127, 40.8821407898917997, -74.0422647901029052, '2012-11-22 01:15:13', 75, 'Thursday', 'Travel & Transport', -1, 6.29999999999999982, 'Clear', 6, 'Hackensack Bus Terminal', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (289251, 127, 40.9141779975906985, -74.1776352146309961, '2012-11-22 01:58:04', 118, 'Thursday', 'Professional & Other Places', -1, -1, 'Clear', 6, 'NJT Market St Garage', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (289269, 127, 40.7572212983983988, -73.9915466308594034, '2012-11-22 03:24:00', 204, 'Thursday', 'Travel & Transport', -1, 5.5, 'Clear', 6, 'Port Authority Bus Terminal', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (289273, 127, 40.7617816261867034, -73.9850546682068, '2012-11-22 03:51:08', 231, 'Thursday', 'Food', 3, 7.90000000000000036, 'Clear', 6, 'Natsumi', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (289274, 127, 40.815540684618, -73.9584234721819058, '2012-11-22 03:51:30', 231, 'Thursday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - 125th St (1)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (290238, 127, 40.8331652006224033, -73.9418603427692034, '2012-11-23 18:34:15', 1114, 'Friday', 'Residence', -1, -1, 'Clear', 6, 'Broadway/Nightmare Drive', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (290369, 127, 40.8341815824087035, -73.9451873302460001, '2012-11-23 22:13:54', 1333, 'Friday', 'Travel & Transport', -1, -1, 'Clouds', 6, 'MTA MaBSTOA Bus - M4 / M5 / Bx6 at 157th Street / Broadway & Riverside Drive', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (291051, 127, 40.834657653567902, -73.9451868588010939, '2012-11-24 23:24:20', 1404, 'Saturday', 'Shop & Service', -1, -1, 'Clear', 6, 'La Stella Cleaners', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (291598, 127, 40.8331652006224033, -73.9418603427692034, '2012-11-25 20:54:55', 1254, 'Sunday', 'Residence', -1, -1, 'Clouds', 6, 'Broadway/Nightmare Drive', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (291616, 127, 40.8341610206876027, -73.9450376116796946, '2012-11-25 21:54:03', 1314, 'Sunday', 'Food', 1, -1, 'Clouds', 6, 'Twin Donut', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (291651, 127, 40.8331652006224033, -73.9418603427692034, '2012-11-25 23:39:06', 1419, 'Sunday', 'Residence', -1, -1, 'Clouds', 6, 'The Lair Of Modern Strange Cowboy', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (292162, 128, 40.6604738351669965, -73.830291089186403, '2012-11-26 17:22:13', 1042, 'Monday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - Howard Beach/JFK Airport (A)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (292271, 128, 40.6086420833784985, -73.8190376758575013, '2012-11-26 19:39:13', 1179, 'Monday', 'Outdoors & Recreation', -1, -1, 'Clear', 6, 'MTA Bus - Q53', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (292291, 128, 40.734055576476301, -73.8708472251891948, '2012-11-26 20:08:35', 1208, 'Monday', 'Shop & Service', -1, 7.5, 'Clear', 6, 'Queens Center Mall', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (252734, 29563, 40.6946728967502978, -73.9940820360804992, '2012-08-09 12:48:36', 768, 'Thursday', 'Food', 1, 7, 'Clouds', 1070, 'Starbucks', 1070);
INSERT INTO foursquare_origin.data_checkin VALUES (292295, 128, 40.7333724746836978, -73.8711404741537052, '2012-11-26 20:10:12', 1210, 'Monday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Bus - Q11/Q21/Q29/Q52LTD/Q53LTD/Q59/Q60 - Queens Blvd & 59th Av', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (292339, 128, 40.763133791032601, -73.8752118314645969, '2012-11-26 21:13:02', 1273, 'Monday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTABus Q19, Q49 (Astoria Blvd/94th St)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (293479, 128, 40.7572212983983988, -73.9915466308594034, '2012-11-29 02:17:24', 137, 'Thursday', 'Travel & Transport', -1, 5.5, 'Clear', 6, 'Port Authority Bus Terminal', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (293480, 128, 40.7564898726737965, -73.9862680435180948, '2012-11-29 02:27:22', 147, 'Thursday', 'Outdoors & Recreation', -1, 9, 'Clear', 6, 'Times Square', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (293488, 128, 40.815540684618, -73.9584234721819058, '2012-11-29 02:51:19', 171, 'Thursday', 'Travel & Transport', -1, -1, 'Clear', 6, 'MTA Subway - 125th St (1)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (294521, 128, 40.8331652006224033, -73.9418603427692034, '2012-12-01 14:39:46', 879, 'Saturday', 'Residence', -1, -1, 'Clouds', 6, 'The Grinnell', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (294587, 128, 40.8312828264783008, -73.941392502021003, '2012-12-01 15:54:25', 954, 'Saturday', 'Travel & Transport', -1, -1, 'Clouds', 6, 'MTA Subway - 155th St (C)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (294662, 128, 40.6900872257332011, -73.9817776229190969, '2012-12-01 17:35:07', 1055, 'Saturday', 'Professional & Other Places', -1, -1, 'Clouds', 6, 'NYCT Transit Survey Unit', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (294704, 128, 40.8278390825734974, -73.9257681369781068, '2012-12-01 18:47:17', 1127, 'Saturday', 'Travel & Transport', -1, 6.20000000000000018, 'Clouds', 6, 'MTA Bus - E 161 St & River Av (Bx6/Bx13)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (294926, 128, 40.8331652006224033, -73.9418603427692034, '2012-12-02 00:11:22', 11, 'Sunday', 'Residence', -1, -1, 'Clouds', 6, 'The Lair Of Modern Strange Cowboy', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (295195, 128, 40.7085883614823985, -73.9910316467285014, '2012-12-02 12:31:33', 751, 'Sunday', 'Travel & Transport', -1, -1, 'Fog', 6, 'MTA Subway - Manhattan Bridge (B/D/N/Q)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (295265, 128, 40.6914746462291035, -73.9873838424683044, '2012-12-02 13:58:45', 838, 'Sunday', 'Travel & Transport', -1, -1, 'Fog', 6, 'MTA Subway - Jay St/MetroTech (A/C/F/R)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (295295, 128, 40.7306323510852977, -73.9996371281807939, '2012-12-02 14:50:20', 890, 'Sunday', 'College & University', -1, -1, 'Fog', 6, 'NYU Law | Vanderbilt Hall', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (295512, 128, 40.8002312249140999, -73.8288116455077983, '2012-12-02 20:10:19', 1210, 'Sunday', 'Outdoors & Recreation', -1, -1, 'Clouds', 6, 'Bronx-Whitestone Bridge', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (295549, 128, 40.8604664913412989, -73.9255428314208984, '2012-12-02 21:19:17', 1279, 'Sunday', 'Travel & Transport', -1, -1, 'Clouds', 6, 'MTA Subway - Dyckman St (1)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (295898, 129, 40.8213441496323028, -73.954038619995103, '2012-12-03 12:15:52', 735, 'Monday', 'Travel & Transport', -1, -1, 'Fog', 6, 'MTA Subway - 137th St/City College (1)', 6);
INSERT INTO foursquare_origin.data_checkin VALUES (295933, 129, 40.6900872257332011, -73.9817776229190969, '2012-12-03 13:02:43', 782, 'Monday', 'Professional & Other Places', -1, -1, 'Clear', 6, 'NYCT Transit Survey Unit', 6);



