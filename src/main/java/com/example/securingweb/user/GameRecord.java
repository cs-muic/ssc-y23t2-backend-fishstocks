package com.example.securingweb.user;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
