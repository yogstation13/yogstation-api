package net.yogstation.api.service;

import net.yogstation.api.bean.xenforo.XenforoUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class XenforoService {

    private String xenforoLoginUrl;
    private String xenforoApiToken;

    public XenforoService(@Value("${xenforo.api.login}") String xenforoLoginUrl, @Value("${xenforo.token}") String xenforoToken) {
        this.xenforoLoginUrl = xenforoLoginUrl;
        this.xenforoApiToken = xenforoToken;
    }

    public XenforoUser login(String username, String password, String ip) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("XF-Api-Key", xenforoApiToken);

        MultiValueMap<String, String> requestMap= new LinkedMultiValueMap<>();
        requestMap.add("login", username);
        requestMap.add("password", password);
        requestMap.add("limit_ip", ip);

        HttpEntity<MultiValueMap> entity = new HttpEntity<>(requestMap, headers);

        try {
            return restTemplate.postForEntity(xenforoLoginUrl, entity, XenforoUser.class).getBody();
        } catch (HttpClientErrorException.BadRequest e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username/Password");
        }
    }
}
