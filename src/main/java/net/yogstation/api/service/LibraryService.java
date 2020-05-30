package net.yogstation.api.service;

import net.yogstation.api.jpa.entity.BookEntity;
import net.yogstation.api.jpa.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {
    private BookRepository bookRepository;

    private Specification<BookEntity> isDeleted = (book, cq, cb) -> cb.or(
            cb.isNull(book.get("deleted")),
            cb.equal(book.get("deleted"), 0));

    public LibraryService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<BookEntity> getBooks(int page, int size, boolean showDeleted) {
        if(showDeleted) {
            return bookRepository.findAll(PageRequest.of(page, size));
        } else {
            return bookRepository.findAll(isDeleted, PageRequest.of(page, size));
        }
    }

}
