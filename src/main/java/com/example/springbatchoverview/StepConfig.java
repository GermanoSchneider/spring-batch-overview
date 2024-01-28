package com.example.springbatchoverview;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public abstract class StepConfig {

    protected final DataSource dataSource;

    protected final JdbcTemplate jdbcTemplate;

    public StepConfig(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
