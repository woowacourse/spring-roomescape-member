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
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {

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
    @DisplayName("/times GET 요청에 정상적으로 응답한다")
    void reservation_times_api() {
        String token = getUserToken();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/times")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/times POST 요청에 성공한 경우 201을 응답한다")
    void reservation_time_post_test() {
        String token = getAdminToken();
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", LocalTime.now());

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue());

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/times")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/times POST 요청시 시간 형식이 올바르지 않을 경우 400을 응답한다")
    void reservation_time_post_format_not_proper() {
        String token = getAdminToken();
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "한시 오분");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("/times POST 요청시 해당 시간이 존재하는 경우 400을 응답한다")
    void reservation_time_post_duplication() {
        String token = getAdminToken();
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", LocalTime.of(13, 0));

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("/times/{id} DELETE 요청에 정상적으로 응답한다")
    void reservation_times_delete_when_exist() {
        String token = getAdminToken();
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", LocalTime.of(13, 0));

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue());

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/times")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/times/{id} DELETE 요청시 특정 id에 대한 예약이 존재하면 400을 응답한다")
    void cannot_delete_time_if_reservation_exist() {
        String userToken = getUserToken();
        String adminToken = getAdminToken();

        Map<String, Object> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", userToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("/times/{id} DELETE 요청에 id가 존재하지 않는다면 404를 응답한다")
    void theme_delete_when_not_exist() {
        String token = getAdminToken();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/times/-1")
                .then().log().all()
                .statusCode(404);
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

    private String getUserToken() {
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
        return accessToken;
    }
}
