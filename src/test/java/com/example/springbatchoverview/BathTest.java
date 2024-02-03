package com.example.springbatchoverview;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.lang.String.join;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
@EnableAutoConfiguration
class BathTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    Job sortNumbersInNaturalOrderJob;

    private static JdbcTemplate JDBC_TEMPLATE;

    private final Set<Integer> expectedNumbers = new HashSet<>();

    @BeforeAll
    static void before(@Autowired DataSource dataSource) {

        JDBC_TEMPLATE = new JdbcTemplate(dataSource);
    }

    @Test
    void shouldRunSortNumbersInNaturalOrderJobWithSuccess() throws Exception {

        fillDatabase();

        sortExpectedNumbersInNaturalOrder(getNumbers());

        jobLauncherTestUtils.setJob(sortNumbersInNaturalOrderJob);

        jobLauncherTestUtils.launchJob();

        assertThat(expectedNumbers).hasSameElementsAs(getNumbers());
    }

    private List<Integer> getNumbers() {

        return JDBC_TEMPLATE.queryForList("SELECT number FROM numbers", Integer.class);
    }

    private void fillDatabase() {

        for (int index = 0; index < 1000; index++) {

            Random random = new Random();

            int randomNumber = random.nextInt(0, 10000);

            JDBC_TEMPLATE.execute("INSERT INTO numbers (number) VALUES " + randomNumber);
        }
    }

    private void sortExpectedNumbersInNaturalOrder(List<Integer> numbers) {

        for (Integer number : numbers) {

            List<String> orderedNumber = new ArrayList<>(stream(number.toString()
                    .split(""))
                    .toList());

            orderedNumber.sort(Comparator.naturalOrder());

            Integer newValue = parseInt(join("", orderedNumber));

            expectedNumbers.add(newValue);
        }
    }
}
