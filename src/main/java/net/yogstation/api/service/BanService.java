package net.yogstation.api.service;

import net.yogstation.api.jpa.entity.BanEntity;
import net.yogstation.api.jpa.repository.BanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BanService {
    private BanRepository banRepository;

    public BanService(BanRepository banRepository) { this.banRepository = banRepository;}

    public Page<BanEntity> getBans(int page, int size) {
        return banRepository.findAll(PageRequest.of(page, size));
    }
}
