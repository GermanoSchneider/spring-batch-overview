package com.example.springbatchoverview;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.sql.DataSource;

public class RemoveDuplicateData extends StepConfig implements Tasklet {

    public RemoveDuplicateData(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        String sql = "DELETE FROM numbers t1 WHERE t1.id > (SELECT MIN(t2.id) FROM numbers t2 WHERE t1.number = t2.number)";

        jdbcTemplate.execute(sql);

        return RepeatStatus.FINISHED;
    }
}
