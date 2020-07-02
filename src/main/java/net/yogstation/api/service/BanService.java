package net.yogstation.api.service;

import lombok.AllArgsConstructor;
import net.yogstation.api.jpa.entity.BanEntity;
import net.yogstation.api.jpa.repository.BanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BanService {
    private BanRepository banRepository;

    public Page<BanEntity> getBans(int page, int size) {
        return banRepository.findAll(PageRequest.of(page, size));
    }
}
