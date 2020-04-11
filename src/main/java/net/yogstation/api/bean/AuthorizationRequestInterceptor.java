package net.yogstation.api.bean;

import lombok.AllArgsConstructor;
import net.yogstation.api.bean.xenforo.XenforoUser;
import net.yogstation.api.service.LoginService;
import net.yogstation.api.service.TokenService;
import net.yogstation.api.service.XenforoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;

@Component
@AllArgsConstructor
public class AuthorizationRequestInterceptor implements HandlerInterceptor {

    private LoginService loginService;
    private AuthorizedSession authorizedSession;
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            String[] authorizationHeaderComponents = authorizationHeader.split(" ");

            if (authorizationHeaderComponents.length != 2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization header");
            }

            if ("Basic".equalsIgnoreCase(authorizationHeaderComponents[0])) {
                doBasicLogin(authorizationHeaderComponents[1], request.getRemoteAddr());
            } else if ("Bearer".equalsIgnoreCase(authorizationHeaderComponents[0])) {
                doBearerLogin(authorizationHeaderComponents[0]);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization header");
            }
        }

        return true;
    }

    public void doBasicLogin(String authorization, String ip) {
        String[] passwordAndUsername = Base64.getEncoder().encodeToString(authorization.getBytes()).split(":");

        if (passwordAndUsername.length != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization header");
        }

        authorizedSession.setPermissions(loginService.nonTokenLogin(passwordAndUsername[0], passwordAndUsername[1], ip));
    }

    public void doBearerLogin(String token) {
        List<String> permissions = tokenService.getPermissions(token);

        authorizedSession.setPermissions(permissions);
    }
}
