package roomescape.controller;

import static roomescape.Fixture.VALID_ADMIN_EMAIL;
import static roomescape.Fixture.VALID_ADMIN_PASSWORD;
import static roomescape.Fixture.VALID_USER_EMAIL;
import static roomescape.Fixture.VALID_USER_PASSWORD;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.service.JwtProvider;
import roomescape.service.request.TokenAppRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ControllerTest {
    public static final String COOKIE_NAME = "token";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JwtProvider jwtProvider;
    @LocalServerPort
    int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    String getUserToken() {
        return jwtProvider.createToken(
            new TokenAppRequest(VALID_USER_EMAIL.getValue(), VALID_USER_PASSWORD.getValue()));
    }

    String getAdminToken() {
        return jwtProvider.createToken(
            new TokenAppRequest(VALID_ADMIN_EMAIL.getValue(), VALID_ADMIN_PASSWORD.getValue()));
    }
}
