package roomescape.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.member.MemberLoginRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ViewIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("루트 화면을 요청하면 200 OK를 응답한다.")
    void popularThemePageTest() throws Exception {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 페이지를 요청하면 200 OK를 응답한다.")
    @Sql(value = "classpath:clean_data.sql")
    void reservationPageTest() throws Exception {
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES ('사용자1', 'user1@wooteco.com', 'user1', 'USER')");

        String token =  RestAssured.given()
                .body(MemberLoginRequest.of("user1", "user1@wooteco.com"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .getHeader("Set-Cookie");

        RestAssured.given().log().all()
                .header("Cookie", token)
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그인 페이지를 요청하면 200 OK를 응답한다.")
    void loginPageTest() throws Exception {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("회원가입 페이지를 요청하면 200 OK를 응답한다.")
    void signupPageTest() throws Exception {
        RestAssured.given().log().all()
                .when().get("/signup")
                .then().log().all()
                .statusCode(200);
    }
}
