package roomescape.web.support;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@TestConfiguration
public class TestConfig {

    @Bean
    public DatabaseHelper databaseHelper(JdbcTemplate jdbcTemplate) {
        return new DatabaseHelper(jdbcTemplate);
    }

    @Bean
    public Clock clock() {
        return Clock.fixed(
                Instant.parse("2026-05-06T00:00:00Z"),
                ZoneId.of("Asia/Seoul")
        );
    }
}
