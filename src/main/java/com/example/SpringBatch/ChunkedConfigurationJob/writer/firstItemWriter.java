package com.example.SpringBatch.ChunkedConfigurationJob.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class firstItemWriter implements ItemWriter<String> {

    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {

        for (String data : chunk) {
            System.out.println("Writing data - " + data);
        }

        System.out.println("Completed writing data.");

    }
}
