package roomescape.controller;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.infra.auth.JwtTokenProcessor;
import roomescape.model.user.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {

    @Autowired
    private JwtTokenProcessor jwtTokenProcessor;

    private String adminToken;
    private String userToken;
    private String loginToken = "loginToken";

    @BeforeEach
    void setUp() {
        adminToken = jwtTokenProcessor.createToken("asd@asd.com", Role.ADMIN);
        userToken = jwtTokenProcessor.createToken("vec@vec.com", Role.USER);
    }

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

    @Test
    @DisplayName("시작 시간 형식 검증")
    void test1() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "미친형식");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("시작 시간 null 검증")
    void test2() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] "));
    }

    @Test
    @DisplayName("예약된 시간을 삭제할 수 없음")
    void test3() {
        // given
        Test_ReservationTime_Post();

        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "Ddyong");
        themeParams.put("description", "살인마가 쫓아오는 느낌");
        themeParams.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2025-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie(loginToken, userToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // when

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(loginToken, userToken)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }

}
