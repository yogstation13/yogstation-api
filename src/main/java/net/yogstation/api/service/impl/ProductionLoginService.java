package net.yogstation.api.service.impl;

import lombok.AllArgsConstructor;
import net.yogstation.api.bean.xenforo.XenforoUser;
import net.yogstation.api.jpa.entity.PermissionEntity;
import net.yogstation.api.jpa.repository.PermissionRepository;
import net.yogstation.api.service.LoginService;
import net.yogstation.api.service.TokenService;
import net.yogstation.api.service.XenforoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@ConditionalOnMissingBean(DevelopmentLoginService.class)
public class ProductionLoginService implements LoginService {

    private XenforoService xenforoService;
    private TokenService tokenService;
    private PermissionRepository permissionRepository;

    @Override
    public String login(String username, String password, String ip) {
        XenforoUser xenforoUser = xenforoService.login(username, password, ip);

        List<String> permissions = permissionRepository.findByUserGroupInOrUser(getGroupIds(xenforoUser), xenforoUser.getUserId())
                .stream().map(PermissionEntity::getPermission).collect(Collectors.toList());

        return tokenService.generateToken(xenforoUser.getUsername(), permissions);
    }

    @Override
    public List<String> nonTokenLogin(String username, String password, String ip) {
        XenforoUser xenforoUser = xenforoService.login(username, password, ip);

        return permissionRepository.findByUserGroupInOrUser(getGroupIds(xenforoUser), xenforoUser.getUserId())
                .stream().map(PermissionEntity::getPermission).collect(Collectors.toList());
    }

    private List<Integer> getGroupIds(XenforoUser user) {
        List<Integer> groupIds = new ArrayList<>();

        if(user.getSecondaryGroupIds() != null) {
            groupIds.addAll(user.getSecondaryGroupIds());
        }
        if(user.getUserGroupId() != null) {
            groupIds.add(user.getUserGroupId());
        }

        return groupIds;
    }
}
