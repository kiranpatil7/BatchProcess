package com.example.SpringBatch.ChunkedConfigurationJob.Configuration;

import com.example.SpringBatch.ChunkedConfigurationJob.Models.Student;
import com.example.SpringBatch.ChunkedConfigurationJob.processor.firstItemProcessor;
import com.example.SpringBatch.ChunkedConfigurationJob.reader.firstItemReader;
import com.example.SpringBatch.ChunkedConfigurationJob.writer.csvWriter;
import com.example.SpringBatch.ChunkedConfigurationJob.writer.firstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

@Configuration
public class csvConfiguration {

    private csvWriter cWriter;

    @Autowired
    firstItemReader firstReader;

    @Autowired
    firstItemWriter firstWriter;

    @Autowired
    firstItemProcessor firstProcessor;
    @Autowired
    DataSource source;

    public csvConfiguration(csvWriter cWriter) {
        this.cWriter = cWriter;
    }

    @Bean
    Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("studentJob", jobRepository).start(step).build();
    }

    @Bean
    Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("studentStep", jobRepository)
                .<Student,Student>chunk(3, transactionManager).
              //  reader(flatFileItemReader())
                 reader(jdbcCursorItemReader())
             //   .processor(firstProcessor)
             //   .writer(firstWriter)
                  .writer(jsonFileItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Student> flatFileItemReader() {
        FlatFileItemReader<Student> flatFileItemReader = new FlatFileItemReader();
        flatFileItemReader.setResource(new FileSystemResource(new File("F:\\MicroService\\SpringBatch\\SpringBatch\\inputfiles\\200KB.csv")));
        flatFileItemReader.setLinesToSkip(1);
        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames(new String[]{"id", "name", "gender", "age", "date", "country"});
        lineMapper.setLineTokenizer(tokenizer);
        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(Student.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        flatFileItemReader.setLineMapper(lineMapper);
        return flatFileItemReader;
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Student> jdbcCursorItemReader() {
        JdbcCursorItemReader<Student> jdbcCursorItemReader = new JdbcCursorItemReader();
        jdbcCursorItemReader.setDataSource(this.source);
        jdbcCursorItemReader.setSql("select id,name,age,gender,date,country from student");
        jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<Student>() {
            {
                this.setMappedClass(Student.class);
            }
        });
        return jdbcCursorItemReader;
    }

    @Bean
    @StepScope
    public JsonItemReader<Student> jsonItemReader() {
        JsonItemReader<Student> jsonItemReader = new JsonItemReader();
        jsonItemReader.setResource(new FileSystemResource(new File("F:\\MicroService\\Batch\\springBatch\\inputfiles\\200KB.json")));
        jsonItemReader.setJsonObjectReader(new JacksonJsonObjectReader(Student.class));
        return jsonItemReader;
    }

    @Bean
    @StepScope
    public ItemReaderAdapter<Student> itemReaderAdapter() {
        ItemReaderAdapter<Student> itemReaderAdapter = new ItemReaderAdapter();
        itemReaderAdapter.setTargetObject("studentService");
        itemReaderAdapter.setTargetMethod("method name from studentService class where restAPI called");
        return itemReaderAdapter;
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Student> flatFileItemWriter() {
        FlatFileItemWriter<Student> flatFileItemWriter = new FlatFileItemWriter();
        flatFileItemWriter.setResource(new FileSystemResource(new File("F:\\MicroService\\SpringBatch\\SpringBatch\\outputfiles")));
        flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
            public void writeHeader(Writer writer) throws IOException {
                writer.write("Id, name,gender, age,date country");
            }
        });
        flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<Student>() {
            {
                this.setFieldExtractor(new BeanWrapperFieldExtractor<Student>() {
                    {
                        this.setNames(new String[]{"id", "name", "gender", "age", "date", "country"});
                    }
                });
            }
        });
        flatFileItemWriter.setFooterCallback(new FlatFileFooterCallback() {
            public void writeFooter(Writer writer) throws IOException {
                Date var10001 = new Date();
                writer.write("File_Created@_" + var10001);
            }
        });
        return flatFileItemWriter;
    }

    @Bean
    @StepScope
    public JsonFileItemWriter<Student> jsonFileItemWriter() {
        File file = new File("F:/MicroService/SpringBatch/SpringBatch/outputfiles");
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        JacksonJsonObjectMarshaller<Student> jsonObjectMarshaller = new JacksonJsonObjectMarshaller();
        JsonFileItemWriter<Student> jsonFileItemWriter = new JsonFileItemWriter(fileSystemResource, jsonObjectMarshaller);

        return jsonFileItemWriter;
    }
}
