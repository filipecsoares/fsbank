create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);

insert into users(username,password,enabled) values ('user','{noop}password',true);
insert into users(username,password,enabled) values ('admin','{bcrypt}$2a$12$B9Lo5MQs6VA0aHKifZXmRe1sho71o/7ul6ahVoQUCe2gM9M.P8JCu',true);
insert into authorities(username,authority) values ('user','ROLE_USER');
insert into authorities(username,authority) values ('admin','ROLE_ADMIN');