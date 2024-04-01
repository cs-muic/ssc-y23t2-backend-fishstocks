package com.example.securingweb.repo;

import com.example.securingweb.user.Customer;
import com.example.securingweb.user.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRecordRepository extends JpaRepository<GameRecord, String> {
    GameRecord findByGameID(String gameID);
}
