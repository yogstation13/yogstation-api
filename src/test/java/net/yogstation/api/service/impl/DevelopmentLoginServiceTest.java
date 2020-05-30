package net.yogstation.api.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.internal.util.collections.Sets;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DevelopmentLoginServiceTest {
    private DevelopmentLoginService out = new DevelopmentLoginService();

    @Test
    public void test_login_ValidUsernamePassword() {
        Assert.assertEquals("username", out.login("username", "password", "127.0.0.1"));
    }

    @Test
    public void test_login_InvalidUsernamePassword() {
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> out.login("username", "invalid", "127.0.0.1"));

        Assert.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        Assert.assertEquals("Invalid Username/Password", exception.getReason());
    }

    @Test
    public void test_getPermissions_SplitUsername() {
        Assert.assertEquals(Sets.newSet("permission.one", "permission.two"),
                out.getPermissions("permission.one,permission.two"));
    }
}
