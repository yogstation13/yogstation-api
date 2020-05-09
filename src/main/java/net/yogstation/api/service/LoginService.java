package net.yogstation.api.service;

import java.util.List;

public interface LoginService {
    String login(String username, String password, String ip);

    List<String> nonTokenLogin(String username, String password, String ip);
}
