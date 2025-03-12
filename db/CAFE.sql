SELECT * FROM cafe.income;

insert into income(IN_DATE, IN_MONEY) values
(now(), 1000);


select ifnull(sum(IN_MONEY), 0) ) from income;

#일
/*
select sum(in_money) from income
where IN_DATE between date_sub(now(), interval 1 day) and now();
*/
select ifnull(sum(IN_MONEY), 0) 
from income 
where IN_DATE between date(NOW()) and DATE_ADD(DATE(NOW()), interval 1 day);


#월
/*
select sum(in_money) from income
where IN_DATE between date_sub(now(), interval 1 month) and now();
*/
select ifnull(sum(IN_MONEY), 0) 
from income 
where IN_DATE between date_format(now(), '%Y-%m-01') 
and last_day(now());


#년
/*
select sum(in_money) from income
where IN_DATE between date_sub(now(), interval 1 year) and now();
*/
select ifnull(sum(IN_MONEY), 0) 
from income 
where IN_DATE between date_format(now(), '%Y-01-01') 
and last_day(now());