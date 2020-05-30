package net.yogstation.api.service;

import net.yogstation.api.bean.xenforo.XenforoResponse;
import net.yogstation.api.bean.xenforo.XenforoUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.server.ResponseStatusException;

@Service
public class XenforoService {

    private String xenforoLoginUrl;
    private String xenforoUserUrl;
    private String xenforoApiToken;
    private RestOperations restOperations;

    public XenforoService(@Value("${xenforo.api.login}") String xenforoLoginUrl,
                          @Value("${xenforo.api.user}") String xenforoUserUrl,
                          @Value("${xenforo.token:default}") String xenforoToken,
                          RestOperations restOperations) {
        this.xenforoLoginUrl = xenforoLoginUrl;
        this.xenforoUserUrl = xenforoUserUrl;
        this.xenforoApiToken = xenforoToken;
        this.restOperations = restOperations;
    }

    public XenforoUser login(String username, String password, String ip) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("XF-Api-Key", xenforoApiToken);

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("login", username);
        requestMap.add("password", password);
        requestMap.add("limit_ip", ip);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestMap, headers);

        try {
            XenforoResponse response = restOperations.postForEntity(xenforoLoginUrl, entity, XenforoResponse.class).getBody();
            return response.getUser();
        } catch (HttpClientErrorException.BadRequest e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username/Password");
        }
    }

    public XenforoUser getUser(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("XF-Api-Key", xenforoApiToken);

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestMap, headers);

        try {
            XenforoResponse response = restOperations.exchange(xenforoUserUrl.replace("{{id}}", String.valueOf(id)),
                    HttpMethod.GET,
                    entity,
                    XenforoResponse.class).getBody();
            return response.getUser();
        } catch (HttpClientErrorException.BadRequest e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid User");
        }
    }
}
