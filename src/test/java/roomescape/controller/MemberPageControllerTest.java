package roomescape.controller;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MemberPageControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String cookie;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");

        Map<String, String> admin = Map.of(
                "email", "aaa@naver.com",
                "password", "1111"
        );

        cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(admin)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().cookie("token");
    }

    @DisplayName("인기 테마 페이지를 열 수 있다.")
    @Test
    void loadPopularThemePage() {
        RestAssured.given().log().all()
                .cookie("token", cookie)
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("사용자 예약 페이지를 열 수 있다.")
    @Test
    void loadUserReservationPage() {
        RestAssured.given().log().all()
                .cookie("token", cookie)
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("사용자 로그인 페이지를 열 수 있다.")
    @Test
    void loadLoginPage() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("사용자 회원가입 페이지를 열 수 있다.")
    @Test
    void loadSignUpPage() {
        RestAssured.given().log().all()
                .when().get("/signup")
                .then().log().all()
                .statusCode(200);
    }
}
