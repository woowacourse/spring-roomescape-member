package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserReservationControllerTest {
    private static String token;

    void Test_ReservationTime_Post() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    private static void Test_Theme_Post() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "Ddyong");
        params.put("description", "살인마가 쫓아오는 느낌");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    private static void Test_Login_Cookie() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "abc");
        params.put("password", "def");
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/auth/login")
                .then().log().all()
                .statusCode(200)
                .extract().response();
        String setCookie = response.getHeader("Set-Cookie");
        token = response.getCookie("token");
    }

    @Test
    @DisplayName("유저가 예약을 생성한다")
    void test1() {
        Test_ReservationTime_Post();
        Test_Theme_Post();
        Test_Login_Cookie();
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2025-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/user/reservations")
                .then().log().all()
                .statusCode(201)
        ;

    }


}
