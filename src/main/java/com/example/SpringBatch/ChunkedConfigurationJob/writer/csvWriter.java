package com.example.SpringBatch.ChunkedConfigurationJob.writer;

import com.example.SpringBatch.ChunkedConfigurationJob.Models.Student;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;


@Component
public class csvWriter implements ItemWriter<Student> {

    public void write(Chunk<? extends Student> chunk) throws Exception {
        for (Student data : chunk) {
            System.out.println("Writing data - " + data);
        }

        System.out.println("Completed writing data.");

    }
}
