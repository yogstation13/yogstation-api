package net.yogstation.api.service;

import net.yogstation.api.bean.xenforo.XenforoResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class XenforoServiceTest {

    @Mock
    private RestOperations restOperations;

    @InjectMocks
    private XenforoService out;

    @Before
    public void before() {
        ReflectionTestUtils.setField(out, "xenforoLoginUrl", "loginUrl");
        ReflectionTestUtils.setField(out, "xenforoUserUrl", "{{id}}");
        ReflectionTestUtils.setField(out, "xenforoApiToken", "API_KEY");
    }

    @Test
    public void test_login_Request() {
        ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        Mockito.when(restOperations.postForEntity(Mockito.eq("loginUrl"), httpEntityArgumentCaptor.capture(), Mockito.eq(XenforoResponse.class)))
                .thenReturn(ResponseEntity.of(Optional.of(new XenforoResponse())));

        out.login("username", "password", "127.0.0.1");

        HttpEntity httpEntity = httpEntityArgumentCaptor.getValue();
        MultiValueMap<String, String> body = (MultiValueMap) httpEntity.getBody();

        Assert.assertEquals("username", body.getFirst("login"));
        Assert.assertEquals("password", body.getFirst("password"));
        Assert.assertEquals("127.0.0.1", body.getFirst("limit_ip"));
        Assert.assertEquals("API_KEY", httpEntity.getHeaders().getFirst("XF-Api-Key"));
        Assert.assertEquals(MediaType.APPLICATION_FORM_URLENCODED, httpEntity.getHeaders().getContentType());
    }

    @Test
    public void test_login_BadRequest() {
        ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        Mockito.when(restOperations.postForEntity(Mockito.eq("loginUrl"), httpEntityArgumentCaptor.capture(), Mockito.eq(XenforoResponse.class)))
                .thenThrow(Mockito.mock(HttpClientErrorException.BadRequest.class));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> out.login("username", "password", "127.0.0.1"));

        Assert.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        Assert.assertEquals("Invalid Username/Password", exception.getReason());
    }

    @Test
    public void test_getUser_Request() {
        ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        Mockito.when(restOperations.exchange(Mockito.eq("1"), Mockito.eq(HttpMethod.GET), httpEntityArgumentCaptor.capture(), Mockito.eq(XenforoResponse.class)))
                .thenReturn(ResponseEntity.of(Optional.of(new XenforoResponse())));

        out.getUser(1);

        HttpEntity httpEntity = httpEntityArgumentCaptor.getValue();

        Assert.assertEquals("API_KEY", httpEntity.getHeaders().getFirst("XF-Api-Key"));
        Assert.assertEquals(MediaType.APPLICATION_FORM_URLENCODED, httpEntity.getHeaders().getContentType());
    }

    @Test
    public void test_getUser_BadRequest() {
        ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        Mockito.when(restOperations.exchange(Mockito.eq("1"), Mockito.eq(HttpMethod.GET), httpEntityArgumentCaptor.capture(), Mockito.eq(XenforoResponse.class)))
                .thenThrow(Mockito.mock(HttpClientErrorException.BadRequest.class));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> out.getUser(1));

        Assert.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        Assert.assertEquals("Invalid User", exception.getReason());
    }
}
