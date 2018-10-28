insert into USERS (name, password, phone, address, regdate, token, active) values ('user1', '11111', '0690444555676', 'Balatonfüred', '2016-1-9', 52301, TRUE)
insert into USERS (name, password, phone, address, regdate, token, active) values ('user2', '11111', '069053595023', 'Paks', '2017-12-25', 33216, TRUE)
insert into USERS (name, password, phone, address, regdate, token, active) values ('user3', '11111', '069054565464', 'Dunaújváros', '2018-5-5', 45622, TRUE)
insert into USERS (name, password, phone, address, regdate, token, active) values ('user4', '11111', '0690535567657', 'Kiskunlacháza', '2018-7-9', 61677, TRUE)
insert into USERS (name, password, phone, address, regdate, token, active) values ('admin', 'pass', '0690535565555', 'Budapest', '2018-7-9', 61677, TRUE)

insert into ROLES (role) values ('USER')
insert into ROLES (role) values ('EDITOR')
insert into ROLES (role) values ('ADMIN')

insert into users_roles (user_id, role_id) values (5,3)
insert into users_roles (user_id, role_id) values (1,1)

insert into TICKETS (task, startdate, client_id) values ('Nincs internet kapcsolat a számítógépen! SOS!', '2017-10-6', (select id from USERS where name='user1'))
insert into TICKETS (task, startdate, client_id) values ('Központi szerver nem indul. Sürgős!', '2018-5-11', (select id from USERS where name='user2'))
insert into TICKETS (task, startdate, client_id) values ('VLC média player nem indul. ', '2018-8-19', (select id from USERS where name='user3'))
