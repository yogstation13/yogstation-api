package net.yogstation.api.service;

import net.yogstation.api.jpa.entity.TokenEntity;
import net.yogstation.api.jpa.entity.UserEntity;
import net.yogstation.api.jpa.repository.TokenRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private Random random;

    @InjectMocks
    private TokenService out;

    @Test
    public void test_generateToken_ValidToken() {
        Mockito.when(random.nextInt(61)).thenReturn(0);
        out.setRandom(random);

        Assert.assertEquals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", out.generateToken(null));
    }

    @Test
    public void test_generateToken_FailedAfterRetry() {
        Mockito.when(random.nextInt(61)).thenReturn(0);
        out.setRandom(random);
        Mockito.when(tokenRepository.findById("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))
                .thenReturn(Optional.of(new TokenEntity()));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> out.generateToken(null));

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        Assert.assertEquals("Failed to generate token, tried 5 times.", exception.getReason());
        Mockito.verify(tokenRepository, Mockito.times(5))
                .findById("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }

    @Test
    public void test_getUser_ValidToken() {
        UserEntity userEntity = new UserEntity();
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setRevoked(false);
        tokenEntity.setExpires(LocalDateTime.now().plusYears(1));
        tokenEntity.setToken("TOKEN");

        Mockito.when(tokenRepository.findById("TOKEN")).thenReturn(Optional.of(tokenEntity));

        Assert.assertEquals(userEntity, out.getUser("TOKEN"));
    }

    @Test
    public void test_getUser_ExpiredToken() {
        UserEntity userEntity = new UserEntity();
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setRevoked(false);
        tokenEntity.setExpires(LocalDateTime.now().minusYears(1));
        tokenEntity.setToken("TOKEN");

        Mockito.when(tokenRepository.findById("TOKEN")).thenReturn(Optional.of(tokenEntity));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> out.getUser("TOKEN"));

        Assert.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        Assert.assertEquals("Token expired.", exception.getReason());
    }

    @Test
    public void test_getUser_RevokedToken() {
        UserEntity userEntity = new UserEntity();
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setRevoked(true);
        tokenEntity.setExpires(LocalDateTime.now().plusYears(1));
        tokenEntity.setToken("TOKEN");

        Mockito.when(tokenRepository.findById("TOKEN")).thenReturn(Optional.of(tokenEntity));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> out.getUser("TOKEN"));

        Assert.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        Assert.assertEquals("Token revoked.", exception.getReason());
    }

    @Test
    public void test_getUser_NonExistingToken() {
        Mockito.when(tokenRepository.findById("TOKEN")).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> out.getUser("TOKEN"));

        Assert.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        Assert.assertEquals("Invalid token.", exception.getReason());
    }
}
