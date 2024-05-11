package roomescape.controller;

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
@Sql(scripts = "/truncate.sql")
public class ControllerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JwtProvider jwtProvider;

    static final String MEMBER_NAME = "test";
    static final String EMAIL = "test@test.com";
    static final String PASSWORD = "test123";
    static final String COOKIE_NAME = "token";
    @LocalServerPort
    int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    String getMemberToken() {
        return jwtProvider.createToken(new TokenAppRequest(EMAIL, PASSWORD));
    }
}
