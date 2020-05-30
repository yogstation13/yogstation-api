package net.yogstation.api.service.impl;

import net.yogstation.api.bean.xenforo.XenforoUser;
import net.yogstation.api.jpa.entity.UserEntity;
import net.yogstation.api.jpa.repository.UserRepository;
import net.yogstation.api.service.PermissionService;
import net.yogstation.api.service.TokenService;
import net.yogstation.api.service.XenforoService;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ProductionLoginServiceTest {
    @Mock
    private XenforoService xenforoService;

    @Mock
    private TokenService tokenService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductionLoginService out;

    @Test
    public void test_login_NewValidUsernamePassword() {
        Mockito.when(xenforoService.login("username", "password", "127.0.0.1"))
                .thenReturn(createXenforoUser(Lists.list(1,2), 3, 4));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(tokenService.generateToken(Mockito.any())).thenReturn("TOKEN");

        Assert.assertEquals("TOKEN", out.login("username", "password", "127.0.0.1"));

        ArgumentCaptor<UserEntity> entityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(userRepository).save(entityArgumentCaptor.capture());

        Assert.assertEquals(1, entityArgumentCaptor.getValue().getId());

    }

    @Test
    public void test_login_ExistingValidUsernamePassword() {
        Mockito.when(xenforoService.login("username", "password", "127.0.0.1"))
                .thenReturn(createXenforoUser(Lists.list(1,2), 3, 4));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(new UserEntity()));
        Mockito.when(tokenService.generateToken(Mockito.any())).thenReturn("TOKEN");

        Assert.assertEquals("TOKEN", out.login("username", "password", "127.0.0.1"));

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void test_getPermissions_Valid() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);

        Mockito.when(tokenService.getUser("TOKEN"))
                .thenReturn(userEntity);
        Mockito.when(xenforoService.getUser(1))
                .thenReturn(createXenforoUser(Lists.list(3, 4),2,1));
        Mockito.when(permissionService.getPermissions(Lists.list(3,4,2), 1))
                .thenReturn(Sets.newSet("permission.one"));

        Assert.assertEquals(Sets.newSet("permission.one"), out.getPermissions("TOKEN"));

        Mockito.verify(permissionService).getPermissions(Lists.list(3,4,2), 1);
    }

    private XenforoUser createXenforoUser(List<Integer> groupIds, int primaryGroup, int userID) {
        XenforoUser xenforoUser = new XenforoUser();
        xenforoUser.setUserId(1);
        xenforoUser.setSecondaryGroupIds(groupIds);
        xenforoUser.setUserGroupId(primaryGroup);

        return xenforoUser;
    }
}
