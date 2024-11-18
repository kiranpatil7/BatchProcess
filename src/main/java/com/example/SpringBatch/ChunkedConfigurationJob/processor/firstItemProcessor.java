package com.example.SpringBatch.ChunkedConfigurationJob.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class firstItemProcessor implements ItemProcessor<String, String> {

    public firstItemProcessor() {
    }

    public String process(String data) throws Exception {
        System.out.println("Processing data - " + data);
        data = data.toUpperCase();
        return data;
    }
}
