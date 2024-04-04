package com.example.securingweb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class PlayerDTO {
    String login;
    List<PieceDTO> capturedPieces;

}
