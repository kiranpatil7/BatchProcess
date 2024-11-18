package com.example.SpringBatch.ChunkedConfigurationJob.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
public class firstItemReader implements ItemReader<String> {

    private String[] tokens = new String[]{"Java", "Spring", "SpringBoot", "Hibernate", "SpringBootBatch", "Advanced Java"};
    private int index = 0;

    public firstItemReader() {
    }

    public String read() throws Exception {
        if (this.index >= this.tokens.length) {
            return null;
        } else {
            String data = this.index + " " + this.tokens[this.index];
            ++this.index;
            System.out.println("Reading data - " + data);
            return data;
        }
    }
}
