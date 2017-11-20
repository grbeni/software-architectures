drop sequence WORDEXERCISE_SEQ;
drop sequence SENTENCEEXERCISE_SEQ;
drop sequence USER_SEQ;

create sequence WORDEXERCISE_SEQ
start with 1 increment by 1;

create sequence SENTENCEEXERCISE_SEQ
start with 1 increment by 1;

create sequence USER_SEQ
start with 1 increment by 1;

drop table WordExercise;
drop table SentenceExercise;
drop table ApplicationUser;
drop table KnowledgeLevel;

create table KnowledgeLevel
(
  ID int primary key,
  LevelName nvarchar2(10) not null
);

create table ApplicationUser
( 
	ID int primary key,
	UserName nvarchar2(15) not null,
	UserPassword nvarchar2(128) not null,
	UserScore int,
	KnowledgeLevelID int references KnowledgeLevel(ID) not null,
  IsAdmin number(1)
);

create table WordExercise
(
  ID int primary key,
  English nvarchar2(30) not null,
  Hungarian nvarchar2(30) not null,
  UserID int references ApplicationUser(ID),
  KnowledgeLevelID int references KnowledgeLevel(ID) not null
);

create table SentenceExercise
(
  ID int primary key,
  English nvarchar2(200) not null,
  Hungarian nvarchar2(200) not null,
  UserID int references ApplicationUser(ID),
  KnowledgeLevelID int references KnowledgeLevel(ID) not null
);

insert into KnowledgeLevel(ID, LevelName) values (1, 'EASY');
insert into KnowledgeLevel(ID, LevelName) values (2, 'MEDIUM');
insert into KnowledgeLevel(ID, LevelName) values (3, 'HARD');

insert into ApplicationUser(ID, UserName, UserPassword, UserScore, KnowledgeLevelID, IsAdmin) values (USER_SEQ.NEXTVAL,'dummyuser',12345, 0, 1, 0);
insert into ApplicationUser(ID, UserName, UserPassword, UserScore, KnowledgeLevelID, IsAdmin) values (USER_SEQ.NEXTVAL,'adminuser',54321, 0, 2, 1);

insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'cat', 'cica', 1, 1);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'dog', 'kutya', 1, 1);

insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'Good morning', 'J? reggelt', 1, 1);
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'How are you?', 'Hogy vagy?', 1, 1);