package roomescape.domain.view;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.ControllerTest;
import roomescape.domain.login.dto.LoginRequest;

public class AdminViewControllerTest extends ControllerTest {

    private static final String EMAIL = "admin@gmail.com";
    private static final String PASSWORD = "123456";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String COOKIE;

    @BeforeEach
    void setUpAll() {
        jdbcTemplate.update(
                "insert into member (name, email, password, role) values ('어드민', 'admin@gmail.com', '123456', 'ADMIN')");
        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);
        COOKIE = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];

    }

    @DisplayName("/admin 으로 시작하는 요청 시 admin권한이 없으면 접근할 수 없다(201 Unauthorized)")
    @Test
    void should_response_401_when_request_admin_prefix_with_not_having_admin_role() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("/admin get 요청 시 응답할 수 있다")
    @Test
    void should_response_200_when_request_admin_page() {
        RestAssured.given().log().all()
                .header("Cookie", COOKIE)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/reservation get 요청 시 응답할 수 있다")
    @Test
    void should_response_200_when_request_reservation_page() {
        RestAssured.given().log().all()
                .header("Cookie", COOKIE)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/theme get 요청 시 응답할 수 있다")
    @Test
    void should_response_200_when_request_theme_page() {
        RestAssured.given().log().all()
                .header("Cookie", COOKIE)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/time get 요청 시 응답할 수 있다")
    @Test
    void should_response_200_when_request_time_page() {
        RestAssured.given().log().all()
                .header("Cookie", COOKIE)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }
}
