--create sequence image_seq start with 1 increment by 50;
--create table image (id bigint not null, file_name varchar(255), data bytea, primary key (id));
--create table image_aud (rev integer not null, revtype smallint, id bigint not null, file_name varchar(255), data bytea, primary key (rev, id));
--alter table if exists image_aud add constraint FKetc5y2t13bkdk5yuj4eswagd4 foreign key (rev) references revinfo;

