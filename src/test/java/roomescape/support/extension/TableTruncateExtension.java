package roomescape.support.extension;

import java.util.List;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class TableTruncateExtension implements AfterEachCallback {
    @Override
    public void afterEach(ExtensionContext context) {
        JdbcTemplate jdbcTemplate = SpringExtension.getApplicationContext(context).getBean(JdbcTemplate.class);

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        List<String> tables = jdbcTemplate.query("SHOW TABLES", (rs, rowNum) -> rs.getString(1));
        for (String table : tables) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table + " RESTART IDENTITY");
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
