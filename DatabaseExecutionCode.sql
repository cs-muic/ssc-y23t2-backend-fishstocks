USE Chess;
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