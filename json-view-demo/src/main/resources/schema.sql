drop table t_coffee if exists;
drop table t_order if exists;
drop table t_order_coffee if exists;

create table t_coffee(
    id bigint auto_increment,
    name varchar(255),
    create_Time timestamp,
    update_Time timestamp,
    price bigint
);

create table t_order(
    id bigint auto_increment,
    customer varchar(255),
    create_Time timestamp,
    update_Time timestamp,
    state integer not null
);

create table t_order_coffee(
    coffee_order_id bigint not null,
    items_id bigint not null
);