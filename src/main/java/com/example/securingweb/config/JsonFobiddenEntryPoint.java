package com.example.securingweb.config;


import com.example.securingweb.convert.AjaxUtils;
import com.example.securingweb.dto.SimpleResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonFobiddenEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String ajexJson = AjaxUtils.converttoString(
                SimpleResponseDTO.builder()
                        .success(true)
                        .message("Forbidden")
                        .build()
        );
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(ajexJson);
    }
}
