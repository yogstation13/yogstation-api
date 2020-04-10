package net.yogstation.api.bean;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
