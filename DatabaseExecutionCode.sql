--Everything here is done in Mariadb, just remember to give the permissions
--like
#Please run line by line
CREATE DATABASE Chess;

GRANT ALL PRIVILEGES ON Chess.* TO 'root'@'%';
FLUSH PRIVILEGES;

#STOP HERE, before you run the follow commands, look above, there is something called schema
#now we are going to change it to the database Chess which we just created and then run the code

create table User
(
    ID       int auto_increment,
    Username varchar(255)     not null,
    Ratings  int default 1000 not null,
    Password varchar(255) not null,
    constraint User_pk_3
        primary key (ID),
    constraint User_pk
        unique (ID),
    constraint User_pk_2
        unique (Username)
);

create table Game
(
    GameID  int auto_increment,
    WhiteID int not null,
    BlackID int not null,
    constraint Game_pk_2
        primary key (GameID),
    constraint Game_pk
        unique (GameID)
);

create table Moves
(
    MoveID int auto_increment,
    GameID int not null,
    GameMove varchar(10) not null,
    constraint Moves_pk
        primary key (MoveID),
    constraint Moves_pk_2
        unique (MoveID)
);

# YOU are not done yet, then go to application.yml and click on the database icon next to the datasource
# And then go to game record, connect to chess database

--Used to find all ID
SELECT *
FROM User;

--Choose Game from the Player's UserID
SELECT *
FROM Game
WHERE WhiteID = 'UserID' or BlackID = 'UserID';

--Select the moves for the player
SELECT *
FROM Moves
WHERE GameID = (SELECT GameID
               From Game
               WHERE WhiteID = 'UserID' or BlackID = 'UserID');