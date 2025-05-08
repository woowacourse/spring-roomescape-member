package roomescape;

import org.springframework.test.context.jdbc.Sql;

@Sql(value = "/test-data-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class DataBasedTest {

}
