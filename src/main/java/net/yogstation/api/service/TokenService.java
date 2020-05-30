package net.yogstation.api.service;

import net.yogstation.api.jpa.entity.TokenEntity;
import net.yogstation.api.jpa.entity.UserEntity;
import net.yogstation.api.jpa.repository.TokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TokenService {

    private static final char[] ALPHA_NUMERIC_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();
    private static final int TOKEN_LENGTH = 64;
    private static final int TOKEN_GENERATE_RETRY_TIMES = 5;

    private Random random = new Random();

    private TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(UserEntity user) {

        for (int i = 0; i < TOKEN_GENERATE_RETRY_TIMES; i++) {
            String token = generateRandomString();

            if (tokenRepository.findById(token).isPresent()) {
                continue;
            }

            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setToken(token);
            tokenEntity.setExpires(LocalDateTime.now().plusDays(1));
            tokenEntity.setRevoked(false);
            tokenEntity.setUser(user);

            // Theres a very miniscule chance that between checking that a token already exists and saving a new one that another thread might create an identical one.
            // This chance is astronomically small though, so chances are it wont be a problem.
            tokenRepository.save(tokenEntity);

            return token;
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate token, tried " + TOKEN_GENERATE_RETRY_TIMES + " times.");
    }

    public UserEntity getUser(String token) {
        TokenEntity tokenEntity = tokenRepository.findById(token).orElse(null);

        if(tokenEntity == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token.");
        }

        if(tokenEntity.getExpires().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired.");
        }

        if(tokenEntity.isRevoked()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token revoked.");
        }

        return tokenEntity.getUser();
    }

    private String generateRandomString() {
        char[] characterArray = new char[TOKEN_LENGTH]; // length is bounded by 7

        for (int i = 0; i < characterArray.length; i++) {
            characterArray[i] = ALPHA_NUMERIC_CHARACTERS[random.nextInt(ALPHA_NUMERIC_CHARACTERS.length)];
        }

        return String.valueOf(characterArray);
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
