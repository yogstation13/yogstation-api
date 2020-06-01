package net.yogstation.api.controller;


import lombok.AllArgsConstructor;
import net.yogstation.api.bean.AuthorizedSession;
import net.yogstation.api.jpa.entity.BanEntity;
import net.yogstation.api.service.BanService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BanController {
    private BanService banService;
    private AuthorizedSession authorizedSession;

    @GetMapping("/api/v1/bans")
    public Page<BanEntity> getBans(@RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "25") int size) {

        Page<BanEntity> banPage = banService.getBans(page, size);

        banPage.forEach(ban -> {
            if (!authorizedSession.hasPermission("bans.GDPR")) {
                ban.setComputerid("0000000000");
                ban.setIp(0000000000);
            }
        });
        return banPage;
    }
}
