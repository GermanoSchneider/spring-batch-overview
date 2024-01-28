package com.example.springbatchoverview;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import javax.sql.DataSource;
import java.util.Map;

public class NumberWriter extends StepConfig implements ItemWriter<Map<Integer, Integer>> {

    public NumberWriter(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void write(Chunk<? extends Map<Integer, Integer>> chunk) throws Exception {

        for (Map<Integer, Integer> value : chunk.getItems()) {

            for (Map.Entry<Integer, Integer> mapValue : value.entrySet())
                jdbcTemplate.update("UPDATE numbers SET number = (?) WHERE id = (?)", mapValue.getValue(), mapValue.getKey());
        }
    }
}
