package org.example.app.services;

import org.example.web.dto.BookPattern;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retrieveAll();

    List<T> retrieveFiltered(BookPattern bookPatternToFilter);

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    boolean removeItemsByPattern(BookPattern bookPatternToRemove);
}
