create sequence pk_roles_seq start with 4 increment by 1;
create sequence pk_users_seq start with 6 increment by 1;

create table users (
    id integer not null,
    user_id uuid not null,
    type varchar(10) not null,
    username varchar(50) not null,
    password varchar(60) not null,
    name varchar (255),
    last_name varchar (255),
    email varchar(255) not null,
    age integer,
    f_user_id varchar(255),
    f_access_token varchar(1024),
    f_refresh_token varchar(1024),
    created timestamp not null default current_timestamp,
    updated timestamp not null default current_timestamp,
    primary key (id)
);

create table roles (
    id integer not null,
    role_id uuid not null,
    name varchar(50) not null,
    primary key (id)
);

create table user_roles (
    user_entity_id integer not null,
    role_entity_id integer not null,
    primary key (user_entity_id, role_entity_id)
);

alter table if exists users
   add constraint user_id_and_username_unique unique (user_id, username);

alter table if exists roles
   add constraint role_id_and_name_unique unique (role_id, name);

alter table if exists user_roles
   add constraint user_entity_id_fk_user_id
   foreign key (user_entity_id)
   references users;

alter table if exists user_roles
   add constraint role_entity_id_fk_role_id
   foreign key (role_entity_id)
   references roles;