package roomescape.test.util;

import org.springframework.jdbc.core.JdbcTemplate;

public class TestDatabaseUtils {

    private TestDatabaseUtils() {
    }

    public static void clearTables(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");

        jdbcTemplate.update("TRUNCATE TABLE reservation");
        jdbcTemplate.update("TRUNCATE TABLE reservation_time");
        jdbcTemplate.update("TRUNCATE TABLE theme");

        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
