package net.yogstation.api.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import net.yogstation.api.bean.AuthorizedSession;
import net.yogstation.api.bean.LoginRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("development")
public class LoginControllerTest {

    @Autowired
    private LoginController loginController;

    @Autowired
    private AuthorizedSession authorizedSession;

    private Algorithm algorithm = Algorithm.HMAC256("secret");

    @Test
    public void test_login() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        LoginRequest loginRequest = new LoginRequest("user", "password");

        String token = loginController.login(loginRequest, request);

        algorithm.verify(JWT.decode(token));
    }

    @Test
    public void test_login_InvalidPassword() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        LoginRequest loginRequest = new LoginRequest("user", "invalid");

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> loginController.login(loginRequest, request));

        Assert.assertEquals("401 UNAUTHORIZED \"Invalid Username/Password\"", exception.getMessage());
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    public void test_login_InvalidRequest() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> loginController.login(null, request));

        Assert.assertEquals("400 BAD_REQUEST", exception.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void test_login_InvalidRequestUsername() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        LoginRequest loginRequest = new LoginRequest(null, "invalid");

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> loginController.login(loginRequest, request));

        Assert.assertEquals("400 BAD_REQUEST", exception.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void test_login_InvalidRequestPassword() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        LoginRequest loginRequest = new LoginRequest("admin", null);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> loginController.login(loginRequest, request));

        Assert.assertEquals("400 BAD_REQUEST", exception.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void test_getPermissions() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        LoginRequest loginRequest = new LoginRequest("user", "password");

        String token = loginController.login(loginRequest, request);

        algorithm.verify(JWT.decode(token));
    }
}
