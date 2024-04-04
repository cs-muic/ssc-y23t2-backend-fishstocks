package com.example.securingweb.user;


import jakarta.persistence.Column;
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
@Table(name = "Game")
public class GameRecord {
    @Id
    private String gameID;

    @Column
    private String player1;

    @Column
    private String player2;

//    @OneToMany
//    private List<MoveList> moveList; //
//     private String GameHistoryString;



}
