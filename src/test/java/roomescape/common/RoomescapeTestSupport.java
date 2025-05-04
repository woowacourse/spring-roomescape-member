package roomescape.common;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomescapeTestSupport {

    @Autowired
    JdbcTemplate jdbcTemplate;

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
