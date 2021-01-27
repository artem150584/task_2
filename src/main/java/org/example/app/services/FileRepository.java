package org.example.app.services;

import org.example.web.dto.FileToDownload;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FileRepository implements ProjectRepositoryFile<FileToDownload>, ApplicationContextAware {
    private ApplicationContext context;

    public List<FileToDownload> retrieveAll(String directory){

        List<FileToDownload> files = new ArrayList<>();
        List<Path> fileNames = null;
        try {
            fileNames = Files.walk(Paths.get(directory)).filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileNames.forEach(c -> {
            files.add(new FileToDownload());
            files.get(files.size() - 1).setFileName(c.toFile().getName());
        });

        return files;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}