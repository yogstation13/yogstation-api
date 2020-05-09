package net.yogstation.api.controller;

import net.yogstation.api.bean.AuthorizedSession;
import net.yogstation.api.jpa.entity.BookEntity;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("development")
public class LibraryControllerTest {

    @Autowired
    private LibraryController libraryController;

    @Autowired
    private AuthorizedSession authorizedSession;

    @Test
    public void test_getBooks() {
        Page<BookEntity> books = libraryController.getBooks(0, 25, false);

        Assert.assertNotNull(books);
        Assert.assertEquals(6, books.getTotalElements());
        Assert.assertEquals(1, books.getTotalPages());
        Assert.assertNull(books.getContent().get(0).getCkey());
    }

    @Test
    public void test_getBooks_NoDeletedPermissions() {
        Page<BookEntity> books = libraryController.getBooks(0, 25, true);

        Assert.assertNotNull(books);
        Assert.assertEquals(6, books.getTotalElements());
        Assert.assertEquals(1, books.getTotalPages());
        Assert.assertNull(books.getContent().get(0).getCkey());
    }

    @Test
    public void test_getBooks_DeletedPermissions() {
        List<String> permissions = Lists.list("library.books.deleted");
        authorizedSession.setPermissions(permissions);

        Page<BookEntity> books = libraryController.getBooks(0, 25, true);

        Assert.assertNotNull(books);
        Assert.assertEquals(10, books.getTotalElements());
        Assert.assertEquals(1, books.getTotalPages());
        Assert.assertNull(books.getContent().get(0).getCkey());
    }

    @Test
    public void test_getBooks_CkeyPermissions() {
        List<String> permissions = Lists.list("library.books.ckey");
        authorizedSession.setPermissions(permissions);

        Page<BookEntity> books = libraryController.getBooks(0, 25, true);

        Assert.assertNotNull(books);
        Assert.assertEquals(6, books.getTotalElements());
        Assert.assertEquals(1, books.getTotalPages());
        Assert.assertNotNull(books.getContent().get(0).getCkey());
    }

}
