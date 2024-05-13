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
class AdminPageControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String cookie;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        jdbcTemplate.update("INSERT INTO member(name, email, password, role) VALUES ('켬미', 'abc@naver.com', '2222', 'ADMIN')");
        Map<String, String> admin = Map.of(
                "email", "abc@naver.com",
                "password", "2222"
        );

        cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(admin)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];
    }

    @DisplayName("관리자 페이지를 열 수 있다.")
    @Test
    void loadAdminPage() {
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("예약 페이지를 열 수 있다.")
    @Test
    void loadReservationPage() {
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("시간 관리 페이지를 열 수 있다.")
    @Test
    void loadTimePage() {
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("테마 관리 페이지를 열 수 있다.")
    @Test
    void loadThemePage() {
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
