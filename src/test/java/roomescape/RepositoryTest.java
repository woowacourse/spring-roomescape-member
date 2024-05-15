package roomescape;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/truncate.sql")
@Sql("/testdata.sql")
@ExtendWith(MockitoExtension.class)
public class RepositoryTest {
}
