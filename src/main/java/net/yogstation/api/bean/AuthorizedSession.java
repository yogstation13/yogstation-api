package net.yogstation.api.bean;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class AuthorizedSession {
    private Set<String> permissions = new HashSet<>();

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
