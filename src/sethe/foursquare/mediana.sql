--MEDIANA
select percentile_cont(0.5) within group(order by rating) from data_checkin where rating <> -1;
--7.3

select percentile_cont(0.5) within group(order by price) from data_checkin where price <> -1;
--1