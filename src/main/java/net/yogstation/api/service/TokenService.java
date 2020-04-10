package net.yogstation.api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TokenService {

    private Algorithm jwtAlgorithm;

    public TokenService(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtAlgorithm = Algorithm.HMAC256(jwtSecret);
    }

    public String generateToken(String username, List<String> permissions) {
        return JWT.create()
                .withIssuer("yogstation")
                .withClaim("username", username)
                .withClaim("permissions", permissions)
                .sign(jwtAlgorithm);
    }

    public List<String> getPermissions(String token) {
        try {
        jwtAlgorithm.verify(JWT.decode(token));
        } catch (JWTVerificationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token.");
        }

        return JWT.decode(token).getClaim("permissions").asList(String.class);
    }
}
