package com.example.demo.repo;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class LoginDto {
    private String username;
    private String password;
}
