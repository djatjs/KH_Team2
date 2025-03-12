SELECT * FROM cafe.income;

insert into income(IN_DATE, IN_MONEY) values
(now(), 3000);


select sum(in_money) from income;

select sum(in_money) from income
where IN_DATE between date_sub(now(), interval 1 day) and now();

select sum(in_money) from income
where IN_DATE between date_sub(now(), interval 1 month) and now();

select sum(in_money) from income
where IN_DATE between date_sub(now(), interval 1 year) and now();
