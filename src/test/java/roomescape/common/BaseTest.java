package roomescape.common;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ClockConfig.class)
public class BaseTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @BeforeEach
    void truncate() {
        String sql = """
                select TABLE_NAME
                from INFORMATION_SCHEMA.TABLES
                where TABLE_SCHEMA = 'PUBLIC';
                """;
        List<String> tableNames = jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("TABLE_NAME"));
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE;");
        tableNames.forEach(tableName -> {
            jdbcTemplate.update(String.format("TRUNCATE TABLE %s RESTART IDENTITY;", tableName));
        });
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE;");
    }

}
