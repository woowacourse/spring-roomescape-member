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
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @BeforeEach
    void setUp() {
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
    @DisplayName("/reservations GET 요청에 정상적으로 응답한다")
    void reservations_api() {
        String token = getToken();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("/reservations POST 요청시 사용자의 입장에서 예약을 생성할 수 있다")
    void reservation_post_api() {
        String accessToken = getToken();

        Map<String, Object> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("/reservations POST 요청시 날짜 형식이 올바르지 않을 경우 400을 응답한다")
    void reservation_post_format_not_proper() {
        String accessToken = getToken();

        Map<String, Object> params = new HashMap<>();
        params.put("date", "1월 1일");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("/reservations POST 요청시 과거에 대한 예약을 시도하는 경우 400을 응답한다")
    void reservation_post_past() {
        String accessToken = getToken();

        Map<String, Object> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(-1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("/reservations POST 요청시 중복 예약이 존재할 경우 400을 응답한다")
    void reservation_post_duplication() {
        String accessToken = getToken();
        Map<String, Object> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("/reservations/{id} DELETE 요청에 성공한 경우 204를 응답한다")
    void reservation_delete_api() {
        String accessToken = getToken();

        Map<String, Object> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue());

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("/reservations/{id} DELETE 요청시 id가 존재하지 않는다면 404를 응답한다")
    void reservation_delete_not_exist_api() {
        String token = getToken();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/-1")
                .then().log().all()
                .statusCode(404);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    private String getToken() {
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
