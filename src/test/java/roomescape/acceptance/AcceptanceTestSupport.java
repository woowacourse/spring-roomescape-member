package roomescape.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AcceptanceTestSupport {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE reservation_history");
        jdbcTemplate.update("TRUNCATE TABLE reservation");
        jdbcTemplate.update("TRUNCATE TABLE reservation_time");
        jdbcTemplate.update("TRUNCATE TABLE theme");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
