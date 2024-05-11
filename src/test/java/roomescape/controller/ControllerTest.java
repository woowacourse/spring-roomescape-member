package roomescape.controller;

import static roomescape.Fixture.VALID_MEMBER_EMAIL;
import static roomescape.Fixture.VALID_MEMBER_PASSWORD;

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
@Sql("/truncate.sql")
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

    String getMemberToken() {
        return jwtProvider.createToken(
            new TokenAppRequest(VALID_MEMBER_EMAIL.getValue(), VALID_MEMBER_PASSWORD.getValue()));
    }
}
