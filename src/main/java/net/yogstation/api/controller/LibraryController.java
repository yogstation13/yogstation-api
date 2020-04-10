package net.yogstation.api.controller;

import lombok.AllArgsConstructor;
import net.yogstation.api.jpa.entity.BookEntity;
import net.yogstation.api.service.LibraryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LibraryController {
    private LibraryService libraryService;

    @GetMapping("/api/v1/books")
    public Page<BookEntity> getBooks() {
        return  libraryService.getBooks(1);
    }
}
