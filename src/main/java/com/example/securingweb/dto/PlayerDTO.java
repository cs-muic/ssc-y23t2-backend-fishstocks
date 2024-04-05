package com.example.securingweb.dto;

import com.example.securingweb.model.chess.Piece;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class PlayerDTO {
    String login;
    Boolean isWhite;
    List<Piece> capturedPieces;

}
