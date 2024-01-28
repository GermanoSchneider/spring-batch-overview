package com.example.springbatchoverview;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@SpringBatchTest
@SpringBootTest
@EnableAutoConfiguration
@TestMethodOrder(OrderAnnotation.class)
class BathTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    Job orderNumbersJob;

    @Autowired
    Job cleanDatabaseJob;

    private static JdbcTemplate JDBC_TEMPLATE;

    @BeforeAll
    static void before(@Autowired DataSource dataSource) {

        JDBC_TEMPLATE = new JdbcTemplate(dataSource);
        fillDatabase();
    }

    @Test
    @Order(1)
    void shouldRunOrderNumberJobWithSuccess() throws Exception {

        jobLauncherTestUtils.setJob(orderNumbersJob);

        jobLauncherTestUtils.launchJob();

        assertEquals(100, getNumbers().size());
    }

    @Test
    @Order(2)
    void shouldRunCleanDatabaseJobWithSuccess() throws Exception {

        jobLauncherTestUtils.setJob(cleanDatabaseJob);

        jobLauncherTestUtils.launchJob();

        assertEquals(0, getNumbers().size());
    }

    private List<Integer> getNumbers() {

        return JDBC_TEMPLATE.queryForList("SELECT number FROM numbers", Integer.class);
    }

    private static void fillDatabase() {

        for (int index = 0; index < 100; index++) {

            Random random = new Random();

            int randomNumber = random.nextInt(1000, 10000);

            JDBC_TEMPLATE.update("INSERT INTO numbers (number) VALUES (?)", randomNumber);
        }
    }
}
