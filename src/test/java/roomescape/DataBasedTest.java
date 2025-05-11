package roomescape;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(value = "/test-data-h2.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class DataBasedTest {

}
