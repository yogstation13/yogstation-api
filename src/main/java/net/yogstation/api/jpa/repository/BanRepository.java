package net.yogstation.api.jpa.repository;

import net.yogstation.api.jpa.entity.BanEntity;
import org.hibernate.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanRepository extends PagingAndSortingRepository<BanEntity, Integer>, JpaSpecificationExecutor<BanEntity> {
    Page<BanEntity> findBy(Pageable pageable, Criteria criteria);
}
