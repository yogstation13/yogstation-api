package net.yogstation.api.service;

import java.util.Set;

public interface LoginService {
    String login(String username, String password, String ip);

    Set<String> getPermissions(String token);
}
