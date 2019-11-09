PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE Categories (
    label               varchar(200) not null,
    colour              varchar(20) not null,
    primary key (label, colour)
);
INSERT INTO Categories VALUES('Cooking','0xffcc80ff');
INSERT INTO Categories VALUES('Sleeping','0x6680e6ff');
INSERT INTO Categories VALUES('Studying','0xe64d4dff');
INSERT INTO Categories VALUES('Work','0x80b380ff');
INSERT INTO Categories VALUES('Exercise','0xe6e64dff');
INSERT INTO Categories VALUES('Reading','0xffccffff');
INSERT INTO Categories VALUES('Travel','0xffcc99ff');
INSERT INTO Categories VALUES('Socialising ','0x80b3b3ff');
INSERT INTO Categories VALUES('Social Media ','0x663366ff');
INSERT INTO Categories VALUES('Content Consumption','0xffccccff');
CREATE TABLE Entries (
    category            references Categories(label),
    entry_description   varchar(200) not null,
    startDate           varchar(30) not null,
    endDate             varchar(30) not null,
    total_duration      double not null
);
INSERT INTO Entries VALUES('Cooking','Making pasta ','2019-11-09','2019-11-09',0.75);
INSERT INTO Entries VALUES('Reading','Reading "To Kill a Mockingbird"','2019-11-10','2019-11-10',0.5);
INSERT INTO Entries VALUES('Sleeping','Sleepy','2019-11-10','2019-11-11',8.0);
INSERT INTO Entries VALUES('Studying','STUDY OMG','2019-11-12','2019-11-12',2.8669999999999999928);
INSERT INTO Entries VALUES('Work','I am poor and I need money so I have to go to work','2019-11-13','2019-11-13',8.5);
INSERT INTO Entries VALUES('Studying','This assignment is crazy (good)','2019-11-14','2019-11-14',5.7);
INSERT INTO Entries VALUES('Exercise','Cardio day ','2019-11-17','2019-11-17',0.58299999999999996269);
INSERT INTO Entries VALUES('Social Media ','I might have spent too much time on social media','2019-11-18','2019-11-18',0.6670000000000000373);
INSERT INTO Entries VALUES('Travel','Unfortunately takes an hour to get to work','2019-11-18','2019-11-18',1.0);
INSERT INTO Entries VALUES('Work','Did no actual work today at all','2019-11-18','2019-11-18',6.5);
INSERT INTO Entries VALUES('Content Consumption','An episode of Avatar the Last Airbender ','2019-11-19','2019-11-19',0.5);
INSERT INTO Entries VALUES('Socialising ','Quick tea break with colleague ','2019-11-21','2019-11-21',0.25);
INSERT INTO Entries VALUES('Studying','STUDY OMG','2019-11-21','2019-11-21',2.8);
INSERT INTO Entries VALUES('Sleeping','So damn sleepy','2019-11-21','2019-11-22',7.5);
INSERT INTO Entries VALUES('Work','My boss dislikes me','2019-11-22','2019-11-22',6.1);
CREATE TABLE Tasks (
    task_title          varchar(200) not null,
    task_description    varchar(200) not null,
    doDate              varchar(30) not null,
    dueDate             varchar(30) not null,
    task_priority       int not null
);
INSERT INTO Tasks VALUES('INFS2605 Graphs','Pull data from database to make graphs ','2019-11-09','2019-11-10',100);
INSERT INTO Tasks VALUES('INFS2605 Project Form','Fill in project form for assginment ','2019-11-11','2019-11-12',73);
INSERT INTO Tasks VALUES('INFS2605 Revision','Exam soon! Better revise ','2019-11-23','2020-11-09',99);
INSERT INTO Tasks VALUES('Make Client Appointment ','Make appointment with client to discuss invoicing','2019-11-28','2019-11-30',77);
INSERT INTO Tasks VALUES('Buy groceries','Make sure to buy tomatoes for pasta sauce','2019-11-11','2019-11-11',32);
INSERT INTO Tasks VALUES('Christmas Cleaning','Cleaning the house before Christmas ','2019-12-22','2019-12-23',73);
INSERT INTO Tasks VALUES('Inspect Orders','Check client orders before they go out','2019-11-13','2019-11-13',81);
INSERT INTO Tasks VALUES('Donate clothes','Find clothes to donate before new years','2019-12-29','2019-12-31',51);
COMMIT;
