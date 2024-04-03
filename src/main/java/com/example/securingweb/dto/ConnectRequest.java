package com.example.securingweb.dto;
import lombok.Data;
import com.example.securingweb.model.chess.Player;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;
}
