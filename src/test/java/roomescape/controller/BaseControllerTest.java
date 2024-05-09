package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.config.TestConfig;
import roomescape.security.JwtTokenProvider;

@SpringBootTest(
        classes = TestConfig.class,
        webEnvironment = WebEnvironment.RANDOM_PORT
)
@Sql(value = {
        "classpath:truncate.sql",
        "classpath:integration-data.sql"
})
public abstract class BaseControllerTest {

    protected static final Long ADMIN_ID = 1L;
    protected static final String ADMIN_EMAIL = "admin@gmail.com";
    protected static final String ADMIN_PASSWORD = "abc123";

    @LocalServerPort
    private int port;

    @SpyBean
    protected JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void environmentSetUp() {
        RestAssured.port = port;
    }
}
