package org.example.app.services;

import org.apache.log4j.Logger;
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
    private final Logger logger = Logger.getLogger(FileRepository.class);

    public List<FileToDownload> retrieveAll(String directory) {

        List<FileToDownload> files = new ArrayList<>();
        try {
            List<Path> fileNames = Files.walk(Paths.get(directory)).filter(Files::isRegularFile).collect(Collectors.toList());

            fileNames.forEach(c -> {
                files.add(new FileToDownload());
                files.get(files.size() - 1).setFileName(c.toFile().getName());
            });
        } catch (IOException e) {
            logger.info(e.getMessage());
        }

        return files;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }
}