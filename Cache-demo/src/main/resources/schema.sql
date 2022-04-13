drop  table t_coffee if exists;
drop  table t_order if exists;
drop  table t_coffee_order if exists;

create table t_coffee(
    id bigint auto_increment,
    name varchar(255) not null,
    price bigint not null,
    create_time timestamp,
    update_time timestamp,
    primary key (id)
);

create table t_order(
    id bigint auto_increment,
    customer varchar(255) not null,
    state integer not null,
    create_time timestamp,
    update_time timestamp,
    primary key (id)
);

create table t_coffee_order(
    coffee_order_id bigint not null,
    items_id bigint not null

);

insert into t_coffee (name, price, create_time, update_time) values ('espresso', 2000, now(), now());
insert into t_coffee (name, price, create_time, update_time) values ('latte', 2500, now(), now());
insert into t_coffee (name, price, create_time, update_time) values ('capuccino', 2500, now(), now());
insert into t_coffee (name, price, create_time, update_time) values ('mocha', 3000, now(), now());
insert into t_coffee (name, price, create_time, update_time) values ('macchiato', 3000, now(), now());