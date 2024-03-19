package com.example.securingweb.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SimpleResponseDTO {
    private boolean success;
    private String message;
}
