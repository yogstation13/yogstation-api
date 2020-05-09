package net.yogstation.api.controller;

import lombok.AllArgsConstructor;
import net.yogstation.api.bean.AuthorizedSession;
import net.yogstation.api.jpa.entity.BookEntity;
import net.yogstation.api.service.LibraryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LibraryController {
    private LibraryService libraryService;
    private AuthorizedSession authorizedSession;

    @GetMapping("/api/v1/books")
    public Page<BookEntity> getBooks(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "100") int size,
                                     @RequestParam(required = false, defaultValue = "false") boolean showDeleted) {

        showDeleted = showDeleted && authorizedSession.hasPermission("library.books.deleted");

        Page<BookEntity> bookPage = libraryService.getBooks(page, size, showDeleted);

        bookPage.forEach(book -> {
            if (!authorizedSession.hasPermission("library.books.ckey")) {
                book.setCkey(null);
            }
        });

        return bookPage;
    }
}
