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

-- word easy
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'cat', 'cica', 1, 1);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'dog', 'kutya', 1, 1);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'morning', 'reggel', 1, 1);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'good', 'jó', 1, 1);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'bad', 'rossz', 1, 1);

-- word intermediate
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'bed', 'ágy', 1, 2);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'time', 'idő', 1, 2);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'team', 'csapat', 1, 2);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'fork', 'villa', 1, 2);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'spoon', 'kanál', 1, 2);

-- word expert
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'brain', 'agy', 1, 3);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'wise', 'bölcs', 1, 3);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'beautiful', 'gyönyörű', 1, 3);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'poison', 'méreg', 1, 3);
insert into WordExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (WORDEXERCISE_SEQ.NEXTVAL, 'guilty', 'bűnös', 1, 3);


-- sentence easy
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'Good morning!', 'Jó reggelt!', 1, 1);
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'How are you?', 'Hogy vagy?', 1, 1);
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'What is your name?', 'Mi a neved?', 1, 1);
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'Everything is alright!', 'Minden rendben!', 1, 1);

-- sentence intermediate
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'What is your favourite color?', 'Mi a kedvenc színed?', 1, 2);
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'I love listening to music.', 'Imádok zenét hallgatni.', 1, 2);
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'Did you see the movie yesterday?', 'Láttad tegnap a filmet?', 1, 2);
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'John has never been to Italy.', 'John soha nem járt Olaszországban.', 1, 2);

-- sentence expert
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'We like to talk about religion.', 'Szeretünk vallásról beszélgetni.', 1, 3);
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'The car was stolen yesterday.', 'Az autó el lett lopva tegnap.', 1, 3);
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'The suspect was sentenced to death.', 'A gyanúsítottat halálra ítélték.', 1, 3);
insert into SentenceExercise(ID, English, Hungarian, UserID, KnowledgeLevelID) values (SENTENCEEXERCISE_SEQ.NEXTVAL, 'Lucy is going to be a dancer when she grows up.', 'Lucy táncos lesz, ha felnő.', 1, 3);