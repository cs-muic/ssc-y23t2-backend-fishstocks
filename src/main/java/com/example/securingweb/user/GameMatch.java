package com.example.securingweb.user;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@Entity
@Setter
@Getter
@Table(name = "GameMatch")

//
public class GameMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String player1;
    private String player2;

    @OneToMany
    private List<GameRecord> gameRecordList;
    public GameMatch() {

    }
}
