package net.yogstation.api.bean.xenforo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class XenforoLoginRequest {
    private String username;
    private String password;
    private String limitIp;
}
