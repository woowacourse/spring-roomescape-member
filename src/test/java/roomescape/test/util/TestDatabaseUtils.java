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

        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
