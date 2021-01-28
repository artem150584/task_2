package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.example.web.dto.BookPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book, BookPattern> bookRepo;
    private Logger logger = Logger.getLogger(BookService.class);

    @Autowired
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return  bookRepo.retrieveAll();
    }

    public List<Book> getFilteredBooks(BookPattern bookPatternToFilter) {
        return bookRepo.retrieveFiltered(bookPatternToFilter);
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public boolean removeBookByPattern(BookPattern bookPatternToRemove) {
        return bookRepo.removeItemsByPattern(bookPatternToRemove);
    }
}
