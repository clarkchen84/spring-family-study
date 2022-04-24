drop table t_coffee if exists;
drop table t_order if exists;
drop table t_order_coffee if exists;

create table t_coffee (
    id bigint auto_increment,
    name varchar(255),
    price bigint,
    create_time timestamp,
    update_time timestamp,
    primary key (id)
);

create table t_order(
    id bigint auto_increment,
    customer varchar(255),
    state integer not null,
    create_time timestamp,
    update_time timestamp,
    primary key (id)

);

create table t_order_coffee(
    coffee_order_id bigint,
    items_id bigint
);
