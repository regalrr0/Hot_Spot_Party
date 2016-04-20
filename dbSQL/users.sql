drop table users;
create table users (
  
  id       int unsigned not null auto_increment primary key,
  userName varchar(20),
  passHash     varchar(60),
  email        varchar(50),
  fName         varchar(20),
  lName         varchar(20),
  age     tinyint(2),
  gender  char(7)
); 

drop table events;
create table events (
  eventId      int unsigned not null auto_increment primary key,
  name         varchar(150),
  description  varchar(1000),
  dateEvent    date, 
  address      varchar(50),
  specialNotes varchar(150),
  eventTypeId  int

);

drop table hours;
create table hours (
  hoursId     int unsigned not null auto_increment primary key,
  msHours  int(2), 
  meHours  int(2),

  tsHours  int(2),
  teHours  int(2),

  wsHours  int(2),
  weHours  int(2),

  rsHours  int(2),
  reHours  int(2),  

  fsHours  int(2),
  feHours  int(2), 

  sasHours  int(2),
  saeHours  int(2), 

  susHours  int(2),
  sueHours  int(2)  

);
