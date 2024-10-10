package com.wevioo.fgdb.extract.batch.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryService {



    @Autowired
    public DirectoryRepository directoryRepository;

    public List<Directory> getListDirectory(){
        return directoryRepository.findByStatus(DirectoryStatus.STOP);
    }

    public void updateRepertoryStatus(Long id, DirectoryStatus status) {
        Directory repertory = directoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repertory not found"));
        repertory.setStatus(status);
        directoryRepository.save(repertory);
    }


    public List<String> getFilePathsFromDirectory(String directoryPath) {
        List<String> filePaths = new ArrayList<>();
        File directory = new File(directoryPath);


        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();


            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) { // Check if it's a file
                        filePaths.add(file.getAbsolutePath());
                    }
                }
            }
        } else {

            throw new IllegalArgumentException("The provided path is not a valid directory: " + directoryPath);
        }

        return filePaths;
    }
}
