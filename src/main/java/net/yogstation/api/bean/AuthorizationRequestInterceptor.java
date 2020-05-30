package net.yogstation.api.bean;

import lombok.AllArgsConstructor;
import net.yogstation.api.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Set;

@Component
@AllArgsConstructor
public class AuthorizationRequestInterceptor implements HandlerInterceptor {

    private LoginService loginService;
    private AuthorizedSession authorizedSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            String[] authorizationHeaderComponents = authorizationHeader.split(" ");

            if (authorizationHeaderComponents.length != 2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unexpected Authorization header length, got " + authorizationHeaderComponents.length + " components expected 2");
            }

            if ("Basic".equalsIgnoreCase(authorizationHeaderComponents[0])) {
                doBasicLogin(authorizationHeaderComponents[1], request.getRemoteAddr());
            } else if ("Bearer".equalsIgnoreCase(authorizationHeaderComponents[0])) {
                doBearerLogin(authorizationHeaderComponents[1]);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unexpected authorization header, got " + authorizationHeaderComponents[0] + " expected Basic/Bearer");
            }
        }

        return true;
    }

    public void doBasicLogin(String authorization, String ip) {
        String[] passwordAndUsername = new String(Base64.getDecoder().decode(authorization)).split(":");

        if (passwordAndUsername.length != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unexpected basic authorization header length, got " + passwordAndUsername.length + " components expected 2");
        }

        String token = loginService.login(passwordAndUsername[0], passwordAndUsername[1], ip);

        authorizedSession.setPermissions(loginService.getPermissions(token));
    }

    public void doBearerLogin(String token) {
        Set<String> permissions = loginService.getPermissions(token);

        authorizedSession.setPermissions(permissions);
    }
}
