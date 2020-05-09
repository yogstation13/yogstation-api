package net.yogstation.api.controller;

import lombok.AllArgsConstructor;
import net.yogstation.api.bean.AuthorizedSession;
import net.yogstation.api.bean.LoginRequest;
import net.yogstation.api.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
public class LoginController {

    private LoginService loginService;
    private AuthorizedSession authorizedSession;

    @PostMapping("/api/v1/login")
    public String login(@RequestBody LoginRequest loginRequest,
                        HttpServletRequest request) {
        if (loginRequest == null ||loginRequest.getPassword() == null || loginRequest.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return loginService.login(loginRequest.getUsername(), loginRequest.getPassword(), request.getRemoteAddr());
    }

    @GetMapping("/api/v1/permissions")
    public List<String> getPermissions() {
        return authorizedSession.getPermissions();
    }
}
