package com.wevioo.fgdb.extract.batch.configuration;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class XMLPartitioner implements Partitioner {

    private final ChunkFilePathRepository chunkFilePathRepository;
    private final List<XmlFileSplitter.Chunk> chunks;


    public XMLPartitioner(ChunkFilePathRepository chunkFilePathRepository, List<XmlFileSplitter.Chunk> chunks) {
        this.chunkFilePathRepository = chunkFilePathRepository;
        this.chunks = chunks;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();
        List<ChunkFilePath> chunkFilePaths = new ArrayList<>();

        for (XmlFileSplitter.Chunk chunk : chunks) {
            try {
                // Create a temporary file for each chunk
                File tempFile = File.createTempFile("chunk_" + chunk.getId(), ".xml");
                tempFile.deleteOnExit();

                // Write chunk data to temp file
                try (FileWriter writer = new FileWriter(tempFile)) {
                    for (String element : chunk.getElements()) {
                        writer.write(element);
                    }
                }

                // Store the temp file path in ExecutionContext
                ExecutionContext context = new ExecutionContext();
                context.putInt("chunkId", chunk.getId());
                context.putString("chunkFilePath", tempFile.getAbsolutePath());
                partitions.put("partition" + chunk.getId(), context);

                // Save chunk file paths to the database
                ChunkFilePath chunkFilePath = new ChunkFilePath();
                chunkFilePath.setChunkId(chunk.getId());
                chunkFilePath.setFilePath(tempFile.getAbsolutePath());
                chunkFilePaths.add(chunkFilePath);
            } catch (IOException e) {
                throw new RuntimeException("Error creating temp file for chunk " + chunk.getId(), e);
            }
        }

        chunkFilePathRepository.saveAll(chunkFilePaths);
        return partitions;
    }
}


