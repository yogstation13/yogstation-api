package net.yogstation.api.controller;

import net.yogstation.api.bean.AuthorizedSession;
import net.yogstation.api.jpa.entity.BanEntity;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("development")
public class BanControllerTest {

    @Autowired
    private BanController banController;

    @Autowired
    private AuthorizedSession authorizedSession;

    @Test
    public void test_getBans() {
        Page<BanEntity> bans = banController.getBans(0, 25);

        Assert.assertNotNull(bans);
        Assert.assertEquals(5, bans.getTotalElements());
        Assert.assertEquals(1, bans.getTotalPages());
        Assert.assertNull(bans.getContent().get(0).getIp());
        Assert.assertNull(bans.getContent().get(0).getComputerid());
    }

    @Test
    public void test_getBans_cidAndIPPermissions() {
        Set<String> permissions = Sets.newLinkedHashSet("bans.GDPR");
        authorizedSession.setPermissions(permissions);
        Page<BanEntity> bans = banController.getBans(0, 25);

        Assert.assertNotNull(bans);
        Assert.assertEquals(5, bans.getTotalElements());
        Assert.assertEquals(1, bans.getTotalPages());
        Assert.assertNotNull(bans.getContent().get(0).getIp());
        Assert.assertNotNull(bans.getContent().get(0).getComputerid());
    }
}
