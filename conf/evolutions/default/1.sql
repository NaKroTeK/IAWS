# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table transport (
  id                        bigint not null,
  label                     varchar(255),
  constraint pk_transport primary key (id))
;

create sequence transport_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists transport;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists transport_seq;

