package roomescape.controller;

import static roomescape.TestFixture.EMAIL_FIXTURE;
import static roomescape.TestFixture.MEMBER_PARAMETER_SOURCE;
import static roomescape.TestFixture.PASSWORD_FIXTURE;
import static roomescape.TestFixture.createMember;

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
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    void loginSuccessful() {
        // given
        createMember(jdbcTemplate, MEMBER_PARAMETER_SOURCE);
        LoginRequest loginRequest = new LoginRequest("hkim1109@naver.com", "qwer1234");
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("쿠키를 이용한 사용자 정보 조회 테스트")
    @Test
    void findMemberProfileByCookie() {
        // given
        createMember(jdbcTemplate, MEMBER_PARAMETER_SOURCE);
        LoginRequest loginRequest = new LoginRequest(EMAIL_FIXTURE, PASSWORD_FIXTURE);
        String cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];
        // when & then
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/login/check")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }
}
