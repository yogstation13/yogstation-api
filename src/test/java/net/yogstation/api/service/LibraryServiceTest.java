package net.yogstation.api.service;


import net.yogstation.api.jpa.entity.BookEntity;
import net.yogstation.api.jpa.repository.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@RunWith(MockitoJUnitRunner.class)
public class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LibraryService libraryService;

    private Specification<BookEntity> isDeleted = (book, cq, cb) -> cb.or(
            cb.isNull(book.get("deleted")),
            cb.equal(book.get("deleted"), 0));

    @Test
    public void test_getBooks_Valid() {
        libraryService.getBooks(4, 25, false);

        Mockito.verify(bookRepository).findAll(Mockito.anyObject(), Mockito.eq(PageRequest.of(4, 25)));
    }

    @Test
    public void test_getBooks_ShowDeleted() {
        libraryService.getBooks(4, 25, true);

        Mockito.verify(bookRepository).findAll(Mockito.eq(PageRequest.of(4, 25)));
    }
}
