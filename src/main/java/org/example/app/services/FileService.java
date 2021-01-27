package org.example.app.services;

import org.example.web.dto.FileToDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final ProjectRepositoryFile<FileToDownload> fileRepo;

    @Autowired
    public FileService(FileRepository fileRepo) {
        this.fileRepo = fileRepo;
    }

    public List<FileToDownload> getAllFiles(String directory) {
        return fileRepo.retrieveAll(directory);
    }
}
