package net.yogstation.api.controller;


import lombok.AllArgsConstructor;
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

    @GetMapping("/api/v1/publicbans")
    public Page<BanEntity> getBans(@RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "25") int size) {

        return banService.getBans(page, size);
    }
}
