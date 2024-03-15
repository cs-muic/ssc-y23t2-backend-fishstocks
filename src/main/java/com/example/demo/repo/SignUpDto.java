package com.example.demo.repo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class SignUpDto {
    private String name;
    private String username;
    private String password;
}