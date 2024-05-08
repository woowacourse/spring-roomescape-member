package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dto.request.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    void loginSuccessful() {
        // given
        jdbcTemplate.update("""
                insert into member(name, email, password)
                values ('명오', 'hkim1109@naver.com', 'qwer1234')
                """);
        LoginRequest loginRequest = new LoginRequest("hkim1109@naver.com", "qwer1234");
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all().statusCode(HttpStatus.OK.value());
    }
}
