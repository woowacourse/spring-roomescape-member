package roomescape.controller.viewcontroller;

import static roomescape.TestFixture.ADMIN_PARAMETER_SOURCE;
import static roomescape.TestFixture.EMAIL_FIXTURE;
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
class AdminViewControllerTest {
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

    @DisplayName("Admin Page 홈화면 접근 성공 테스트")
    @Test
    void responseAdminPage() {
        createMember(jdbcTemplate, ADMIN_PARAMETER_SOURCE);
        String cookie = getCookie();
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/admin")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Admin Reservation Page 접근 성공 테스트")
    @Test
    void responseAdminReservationPage() {
        createMember(jdbcTemplate, ADMIN_PARAMETER_SOURCE);
        String cookie = getCookie();
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/admin/reservation")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Admin Time Page 접근 성공 테스트")
    @Test
    void responseAdminTimePage() {
        createMember(jdbcTemplate, ADMIN_PARAMETER_SOURCE);
        String cookie = getCookie();
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/admin/time")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Admin Theme Page 접근 성공 테스트")
    @Test
    void responseAdminThemePage() {
        createMember(jdbcTemplate, ADMIN_PARAMETER_SOURCE);
        String cookie = getCookie();
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/admin/theme")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    private String getCookie() {
        LoginRequest loginRequest = new LoginRequest(EMAIL_FIXTURE, PASSWORD_FIXTURE);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];
    }
}
