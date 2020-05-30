package net.yogstation.api.service;

import net.yogstation.api.jpa.entity.PermissionEntity;
import net.yogstation.api.jpa.repository.PermissionRepository;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {
    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    @Test
    public void test_getPermissions_Valid() {
        Mockito.when(permissionRepository.findByUserGroupInOrUser(Lists.list(4, 5), 1))
                .thenReturn(getPermissionEntity("permission.one", "permission.two"));

        Assert.assertEquals(Sets.newSet("permission.one", "permission.two"),
                permissionService.getPermissions(Lists.list(4, 5), 1));
    }

    @Test
    public void test_getPermissions_NoResults() {
        Mockito.when(permissionRepository.findByUserGroupInOrUser(Lists.list(4, 5), 1))
                .thenReturn(getPermissionEntity());

        Assert.assertEquals(Sets.newSet(),
                permissionService.getPermissions(Lists.list(4, 5), 1));
    }

    private List<PermissionEntity> getPermissionEntity(String... permissions) {
        return Stream.of(permissions)
                .map(p -> PermissionEntity.builder()
                        .user(1)
                        .userGroup(1)
                        .permission(p)
                        .id(1)
                        .build())
                .collect(Collectors.toList());
    }
}
