drop table if exists comments;
drop table if exists tickets_categories;
drop table if exists categories ;
drop table if exists tickets;
drop table if exists users;

create table users(
user_id int unsigned not null auto_increment,
username varchar(15) not null unique,
primary key (user_id)
);


create table tickets(
ticket_id int unsigned not null auto_increment,
ticket_title varchar(50) not null,
ticket_body text(1000),
created timestamp(0) default CURRENT_TIMESTAMP,
status enum('PENDING', 'ACTIVE', 'CLOSED', 'RESOLVED') not null default 'PENDING',
user_fk int unsigned not null,
primary key (ticket_id),
foreign key (user_fk) references users (user_id)
);

create table categories(
category_id int unsigned not null auto_increment,
category_name varchar(15) unique,
primary key (category_id)
);

create table tickets_categories(
category_fk int unsigned not null,
ticket_fk int unsigned not null,
foreign key (category_fk) references categories (category_id) on delete cascade,
foreign key (ticket_fk) references tickets (ticket_id) on delete cascade
);

create table comments(
comment_id int unsigned not null auto_increment,
comment_text text(1000) not null,
ticket_fk int unsigned not null,
user_fk int unsigned not null,
comment_order int unsigned not null,
primary key (comment_id),
foreign key (ticket_fk) references tickets (ticket_id) on delete cascade,
foreign key (user_fk) references users (user_id) on delete cascade
);








