package net.yogstation.api.service;

import lombok.AllArgsConstructor;
import net.yogstation.api.bean.xenforo.XenforoUser;
import net.yogstation.api.jpa.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LoginService {

    private XenforoService xenforoService;
    private TokenService tokenService;
    private PermissionRepository permissionRepository;

    public String login(String username, String password, String ip) {

        XenforoUser xenforoUser = xenforoService.login(username, password, ip);
        xenforoUser.getSecondaryGroupIds().add(xenforoUser.getUserGroupId());

        List<String> permissions = permissionRepository.findByUserGroupInOrUser(xenforoUser.getSecondaryGroupIds(), xenforoUser.getUserId())
                .map(i -> i.getPermission()).collect(Collectors.toList());

        return tokenService.generateToken(xenforoUser.getUsername(), permissions);
    }

    public List<String> nonTokenLogin(String username, String password, String ip) {
        XenforoUser xenforoUser = xenforoService.login(username, password, ip);
        xenforoUser.getSecondaryGroupIds().add(xenforoUser.getUserGroupId());

        return permissionRepository.findByUserGroupInOrUser(xenforoUser.getSecondaryGroupIds(), xenforoUser.getUserId())
                .map(i -> i.getPermission()).collect(Collectors.toList());
    }
}
