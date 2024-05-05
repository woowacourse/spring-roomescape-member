package roomescape.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

@SpringBootTest
@Sql(scripts = "/reset_test_data.sql")
public abstract class IntegrationTestSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected int countRow(String tableName) {
        return JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
    }

    protected void cleanUp(String tableName) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, tableName);
    }
}
