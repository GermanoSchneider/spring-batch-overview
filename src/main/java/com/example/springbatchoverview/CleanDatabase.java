package com.example.springbatchoverview;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.sql.DataSource;

public class CleanDatabase extends StepConfig implements Tasklet {

    public CleanDatabase(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        jdbcTemplate.execute("DELETE FROM numbers");

        return RepeatStatus.FINISHED;
    }
}
