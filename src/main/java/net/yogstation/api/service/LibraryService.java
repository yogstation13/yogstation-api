package net.yogstation.api.service;

import lombok.AllArgsConstructor;
import net.yogstation.api.jpa.entity.BookEntity;
import net.yogstation.api.jpa.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LibraryService {
    private BookRepository bookRepository;

    public Page<BookEntity> getBooks(int page) {
        return bookRepository.findAll(isDeleted(), PageRequest.of(page, 25));
    }

    private Specification<BookEntity> isDeleted() {
        return (book, cq, cb) ->
                cb.or(
                        cb.isNull(book.get("deleted"))
                        , cb.equal(book.get("deleted"), 0)
                );
    }
}
