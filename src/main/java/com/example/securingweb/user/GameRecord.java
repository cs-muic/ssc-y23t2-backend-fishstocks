package com.example.securingweb.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "GameRecord")
public class GameRecord {
    @Id
    private String gameID;

    private String player1;
    private String player2;

//    @OneToMany
//    private List<MoveList> moveList; //
    private String GameHistoryString;



}
