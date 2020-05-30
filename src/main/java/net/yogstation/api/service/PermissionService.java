package net.yogstation.api.service;

import lombok.AllArgsConstructor;
import net.yogstation.api.jpa.entity.PermissionEntity;
import net.yogstation.api.jpa.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PermissionService {

    private PermissionRepository permissionRepository;

    public Set<String> getPermissions(List<Integer> groups, int userId) {
        return permissionRepository
                .findByUserGroupInOrUser(groups, userId).stream()
                .map(PermissionEntity::getPermission)
                .collect(Collectors.toSet());
    }

}
