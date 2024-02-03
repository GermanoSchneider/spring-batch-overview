package com.example.springbatchoverview;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class BathConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    Job sortNumbersInNaturalOrderJob(
            JobRepository jobRepository,
            Step sortNumbersInNaturalOrderStep,
            Step removeDuplicateDataStep
    ) {

        return new JobBuilder("sortNumbersInNaturalOrderJob", jobRepository)
                .start(sortNumbersInNaturalOrderStep)
                .next(removeDuplicateDataStep)
                .build();
    }

    @Bean
    Step sortNumbersInNaturalOrderStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {

        return new StepBuilder("sortNumbersInNaturalOrderStep", jobRepository)
                .<Map<Integer, Integer>, Map<Integer, Integer>>chunk(10, platformTransactionManager)
                .reader(cursorItemReader())
                .processor(new SortNumberProcessor())
                .writer(new NumberWriter(dataSource))
                .build();
    }

    @Bean
    Step removeDuplicateDataStep(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("removeDuplicateDataStep", jobRepository)
                .tasklet(new RemoveDuplicateData(dataSource), platformTransactionManager)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Map<Integer, Integer>> cursorItemReader(){

        JdbcCursorItemReader<Map<Integer, Integer>> reader = new JdbcCursorItemReader<>();

        reader.setSql("SELECT * FROM numbers");
        reader.setDataSource(dataSource);
        reader.setRowMapper(new DatabaseRowMapper());

        return reader;
    }
}
