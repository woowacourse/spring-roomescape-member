package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminControllerTest {

    @BeforeEach
    void setUp() {
        Map<String, Object> adminParams = new HashMap<>();
        adminParams.put("name", "어드민");
        adminParams.put("email", "admin@gmail.com");
        adminParams.put("password", "1234");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(adminParams)
                .post("/signup/admin");

        Map<String, Object> normalParams = new HashMap<>();
        normalParams.put("name", "일반");
        normalParams.put("email", "user@gmail.com");
        normalParams.put("password", "1234");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(normalParams)
                .post("/signup");
    }

    @Test
    @DisplayName("/admin api에 접근 시, 관리자가 아닌 경우 403을 응답한다")
    void admin_not_admin() {
        Map<String, Object> memberParams = new HashMap<>();
        memberParams.put("email", "user@gmail.com");
        memberParams.put("password", "1234");

        String accessToken = RestAssured
                .given().log().all()
                .body(memberParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", "1월 1일");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(403);
    }

    @Test
    @DisplayName("/admin/reservations POST 요청시 관리자의 입장에서 예약을 생성할 수 있다")
    void admin_reservation_post_api() {
        String token = getAdminToken();

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("/admin/reservations POST 요청시 날짜 형식이 올바르지 않을 경우 400을 응답한다")
    void admin_reservation_post_format_not_proper() {
        String token = getAdminToken();

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", "1월 1일");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("/admin/reservations POST 요청시 과거에 대한 예약을 시도하는 경우 400을 응답한다")
    void admin_reservation_post_past() {
        String token = getAdminToken();

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", LocalDate.now().plusDays(-1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("/admin/reservations POST 요청시 중복 예약이 존재할 경우 400을 응답한다")
    void admin_reservation_post_duplication() {
        String token = getAdminToken();

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }

    private String getAdminToken() {
        Map<String, Object> memberParams = new HashMap<>();
        memberParams.put("email", "admin@gmail.com");
        memberParams.put("password", "1234");

        String accessToken = RestAssured
                .given().log().all()
                .body(memberParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        return accessToken;
    }
}
