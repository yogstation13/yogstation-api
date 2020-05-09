package net.yogstation.api.service.impl;

import lombok.AllArgsConstructor;
import net.yogstation.api.service.LoginService;
import net.yogstation.api.service.TokenService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Profile("development")
public class DevelopmentLoginService implements LoginService {
    private TokenService tokenService;

    @Override
    public String login(String username, String password, String ip) {
        List<String> permissions = Arrays.asList(username.split(","));

        if (!"password".equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username/Password");
        }

        return tokenService.generateToken(username, permissions);
    }

    @Override
    public List<String> nonTokenLogin(String username, String password, String ip) {

        List<String> permissions = Arrays.asList(username.split(","));

        if (!"password".equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username/Password");
        }

        return permissions;
    }
}
