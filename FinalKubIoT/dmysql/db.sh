
mysql -uroot -pdlcjf2779! -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'dlcjf2779!';"
mysql -uroot -pdlcjf2779! -e "create database kubiot";
mysql -uroot -pdlcjf2779! kubiot -e "create table admin(id varchar(100), pass varchar(100));"
mysql -uroot -pdlcjf2779! kubiot -e "create table control_device(deviceid varchar(100) primary key, min double, max double);"
mysql -uroot -pdlcjf2779! kubiot -e "create table control_log(id int not null auto_increment primary key, deviceid varchar(500), request varchar(5000));"
mysql -uroot -pdlcjf2779! kubiot -e "create table device(id int not null auto_increment primary key, name varchar(100), location varchar(100), memo varchar(5000));"
mysql -uroot -pdlcjf2779! kubiot -e "create table fcmtoken(fcmtoken varchar(1000));"
mysql -uroot -pdlcjf2779! kubiot -e "create table groupinfo(id int not null primary key auto_increment, name varchar(500));"
mysql -uroot -pdlcjf2779! kubiot -e "create table groupregi ( id int not null auto_increment, deviceid int, groupid int, primary key(id), foreign key(deviceid) references device(id), foreign key(groupid) references groupinfo(id));"
mysql -uroot -pdlcjf2779! kubiot -e "create table projectinfo(projectname varchar(200))"
mysql -uroot -pdlcjf2779! kubiot -e "create table push_data ( id int auto_increment primary key, time timestamp default now(), device int, ischecked int default 0, foreign key (device) references device(id));"

