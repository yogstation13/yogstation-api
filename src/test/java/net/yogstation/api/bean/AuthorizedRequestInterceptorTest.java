package net.yogstation.api.bean;

import net.yogstation.api.service.LoginService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizedRequestInterceptorTest {

    @Mock
    private LoginService loginService;

    @Mock
    private AuthorizedSession authorizedSession;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private AuthorizationRequestInterceptor out;

    @Test
    public void test_preHandle_ValidTokenLogin() {
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer TEST_TOKEN");
        Mockito.when(loginService.getPermissions("TEST_TOKEN"))
                .thenReturn(Sets.newSet("permission.one", "permission.two"));

        Assert.assertTrue(out.preHandle(httpServletRequest, null, null));
        Mockito.verify(authorizedSession).setPermissions(Sets.newSet("permission.one", "permission.two"));
    }

    @Test
    public void test_preHandle_ValidBasicLogin() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("Basic dXNlcm5hbWU6cGFzc3dvcmQ=");
        Mockito.when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        Mockito.when(loginService.login("username", "password", "127.0.0.1"))
                .thenReturn("TEST_TOKEN");
        Mockito.when(loginService.getPermissions("TEST_TOKEN"))
                .thenReturn(Sets.newSet("permission.one", "permission.two"));

        Assert.assertTrue(out.preHandle(httpServletRequest, null, null));
        Mockito.verify(authorizedSession).setPermissions(Sets.newSet("permission.one", "permission.two"));
    }

    @Test
    public void test_preHandle_InvalidAuthorizationHeaderType() {
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("INVALID TEST_TOKEN");

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () ->
                Assert.assertTrue(out.preHandle(httpServletRequest, null, null)));

        Assert.assertEquals("Unexpected authorization header, got INVALID expected Basic/Bearer", e.getReason());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void test_preHandle_InvalidAuthorizationHeaderLength() {
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("INVALIDTEST_TOKEN");

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () ->
                Assert.assertTrue(out.preHandle(httpServletRequest, null, null)));

        Assert.assertEquals("Unexpected Authorization header length, got 1 components expected 2", e.getReason());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void test_preHandle_InvalidBasicAuthorizationHeaderLength() {
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Basic dXNlcm5hbWVwYXNzd29yZA==");

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () ->
                Assert.assertTrue(out.preHandle(httpServletRequest, null, null)));

        Assert.assertEquals("Unexpected basic authorization header length, got 1 components expected 2", e.getReason());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }
}
