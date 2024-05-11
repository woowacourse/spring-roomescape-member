package roomescape.model;

import io.restassured.RestAssured;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.auth.domain.Token;
import roomescape.auth.provider.CookieProvider;
import roomescape.auth.provider.model.TokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data-test.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class IntegrationTest {

    protected static final LocalDate TODAY = LocalDate.now();

    @LocalServerPort
    private int port;
    @Autowired
    private TokenProvider tokenProvider;

    protected Token token;
    protected ResponseCookie cookie;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        this.token = tokenProvider.getAccessToken(1);
        this.cookie = CookieProvider.setCookieFrom(token);
    }

}


