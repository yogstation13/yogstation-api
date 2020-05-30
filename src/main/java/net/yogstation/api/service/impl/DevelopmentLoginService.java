package net.yogstation.api.service.impl;

import lombok.AllArgsConstructor;
import net.yogstation.api.service.LoginService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Profile("development")
public class DevelopmentLoginService implements LoginService {
    @Override
    public String login(String username, String password, String ip) {
        if (!"password".equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username/Password");
        }

        return username;
    }

    @Override
    public Set<String> getPermissions(String token) {
        return new HashSet<>(Arrays.asList(token.split(",")));
    }
}
