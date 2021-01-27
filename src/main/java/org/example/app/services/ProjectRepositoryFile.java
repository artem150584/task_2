package org.example.app.services;

import java.util.List;

public interface ProjectRepositoryFile<T> {
    List<T> retrieveAll(String directory);
}
