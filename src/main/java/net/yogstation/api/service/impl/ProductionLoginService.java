package net.yogstation.api.service.impl;

import lombok.AllArgsConstructor;
import net.yogstation.api.bean.xenforo.XenforoUser;
import net.yogstation.api.jpa.entity.UserEntity;
import net.yogstation.api.jpa.repository.UserRepository;
import net.yogstation.api.service.LoginService;
import net.yogstation.api.service.PermissionService;
import net.yogstation.api.service.TokenService;
import net.yogstation.api.service.XenforoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@ConditionalOnMissingBean(DevelopmentLoginService.class)
public class ProductionLoginService implements LoginService {

    private XenforoService xenforoService;
    private TokenService tokenService;
    private PermissionService permissionService;

    private UserRepository userRepository;

    @Override
    public String login(String username, String password, String ip) {
        XenforoUser xenforoUser = xenforoService.login(username, password, ip);

        UserEntity userEntity = userRepository.findById(xenforoUser.getUserId()).orElse(null);

        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setId(xenforoUser.getUserId());

            userEntity = userRepository.save(userEntity);
        }

        return tokenService.generateToken(userEntity);
    }

    @Override
    public Set<String> getPermissions(String token) {
        UserEntity userEntity = tokenService.getUser(token);

        XenforoUser xenforoUser = xenforoService.getUser(userEntity.getId());

        return permissionService.getPermissions(getGroupIds(xenforoUser), xenforoUser.getUserId());
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
