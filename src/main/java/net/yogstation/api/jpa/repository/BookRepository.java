package net.yogstation.api.jpa.repository;

import net.yogstation.api.jpa.entity.BookEntity;
import org.hibernate.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends PagingAndSortingRepository<BookEntity, Integer>, JpaSpecificationExecutor<BookEntity> {
    Page<BookEntity> findBy(Pageable pageable, Criteria criteria);
}
