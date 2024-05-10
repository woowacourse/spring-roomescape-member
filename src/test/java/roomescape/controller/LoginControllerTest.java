package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.UserLoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LoginControllerTest {

    @LocalServerPort
    int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void login() {
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password) VALUES ('사용자1', 'user1@wooteco.com', 'user1')");

        String accessToken = RestAssured.given()
                .body(UserLoginRequest.of("user1", "user1@wooteco.com"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .getHeader("Set-Cookie");

        MemberResponse member = RestAssured
                .given().log().all()
                .header("Cookie", accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().as(MemberResponse.class);

        assertThat(member.name()).isEqualTo("사용자1");
    }
}
