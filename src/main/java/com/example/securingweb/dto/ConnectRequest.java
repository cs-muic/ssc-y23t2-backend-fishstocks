package com.example.securingweb.dto;

import com.example.securingweb.model.chess.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private PlayerDTO player;
    private String gameId;
}
