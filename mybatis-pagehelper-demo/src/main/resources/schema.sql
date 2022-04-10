create table t_coffee(
    id bigint not null auto_increment,
    name varchar(255),
    price bigint not null,
    create_time timestamp,
    UPDATE_TIME timestamp,
    primary key (id)
);