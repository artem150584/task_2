package org.example.app.services;

import java.util.List;

public interface ProjectRepository<T, S> {
    List<T> retrieveAll();

    List<T> retrieveFiltered(S bookPatternToFilter);

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    boolean removeItemsByPattern(S bookPatternToRemove);
}
