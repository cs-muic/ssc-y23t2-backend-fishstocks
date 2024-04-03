package com.example.securingweb.repo;

import com.example.securingweb.user.GameMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameMatchRepository extends JpaRepository<GameMatch, Long> {
    GameMatch findByPlayer1(String player1);
    GameMatch findByPlayer2(String player2);
}
