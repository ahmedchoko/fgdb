package com.wevioo.fgdb.extract.batch.configuration;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import java.util.HashMap;
import java.util.Map;

public class XMLSecondPartitioner implements Partitioner {

    private final Map<Integer, String> chunkFilePaths;

    public XMLSecondPartitioner(Map<Integer, String> chunkFilePaths) {
        this.chunkFilePaths = chunkFilePaths;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();

        for (Map.Entry<Integer, String> entry : chunkFilePaths.entrySet()) {
            ExecutionContext context = new ExecutionContext();
            context.putString("chunkFilePath", entry.getValue());
            partitions.put(String.valueOf(entry.getKey()), context);
        }
        return partitions;
    }
}



