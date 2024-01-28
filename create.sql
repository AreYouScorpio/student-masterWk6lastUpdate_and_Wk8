create sequence special_day_seq start with 1 increment by 50;
create sequence time_table_item_seq start with 1 increment by 50;
create table special_day_aud (id integer not null, rev integer not null, revtype smallint, source_day date, target_day date, primary key (id, rev));
create table special_day (id integer not null, source_day date, target_day date, primary key (id));
create table time_table_item_aud (day_of_week integer, end_lesson time(6), id integer not null, rev integer not null, revtype smallint, start_lesson time(6), course_id bigint, course_name varchar(255), primary key (id, rev));
create table time_table_item (day_of_week integer not null, end_lesson time(6), id integer not null, start_lesson time(6), course_id bigint, course_name varchar(255), primary key (id));
alter table if exists special_day_aud add constraint FKke1jl33xwvveiw7wvsmupf177 foreign key (rev) references revinfo;
alter table if exists time_table_item_aud add constraint FK3u9r9u0niuxdqsasfa3ccbj55 foreign key (rev) references revinfo;
alter table if exists time_table_item add constraint FKoncqcs4k7gesjioopos63b40m foreign key (course_id) references course;
