package roomescape.testexecutionlistener;

import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class TestDatabaseInitializer extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);

        cleanDatabase(jdbcTemplate);

        resetAutoIncrement(jdbcTemplate);
    }

    private void cleanDatabase(JdbcTemplate jdbcTemplate) {
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY FALSE");

        getTable(jdbcTemplate).forEach(table ->
                execute(jdbcTemplate, "TRUNCATE TABLE " + table + " RESTART IDENTITY")
        );

        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY TRUE");
    }

    private void resetAutoIncrement(JdbcTemplate jdbcTemplate) {
        getTable(jdbcTemplate).forEach(table ->
                execute(jdbcTemplate,
                        "ALTER TABLE " + table + " ALTER COLUMN ID RESTART WITH 1"
                )
        );
    }

    private List<String> getTable(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
                String.class
        );
    }

    private JdbcTemplate getJdbcTemplate(TestContext testContext) {
        ApplicationContext context = testContext.getApplicationContext();
        return context.getBean(JdbcTemplate.class);
    }

    private void execute(JdbcTemplate jdbcTemplate, String sql) {
        jdbcTemplate.execute(sql);
    }

}
