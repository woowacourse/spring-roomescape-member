package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.FilterConditionDto;
import roomescape.dto.LoginRequestDto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class AdminReservationControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("예약 목록 조회 시 200으로 응답한다.")
    @Test
    void reservationsTest() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("예약 삭제 요청 시 204로 응답한다.")
    @Test
    void deleteByIdTest() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("날짜가 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidDateTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "");
        params.put("timeId", 1);

        String accessToken = RestAssured
                .given().log().all()
                .body(new LoginRequestDto("email@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().statusCode(200)
                .extract().header("Set-Cookie").split("=")[1];

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", accessToken)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("시간이 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidTimeIdTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-04-30");
        params.put("timeId", "시간 입력");

        String accessToken = RestAssured
                .given().log().all()
                .body(new LoginRequestDto("email@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().statusCode(200)
                .extract().header("Set-Cookie").split("=")[1];

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", accessToken)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("정상적인 어드민 예약 추가 요청 시 201으로 응답한다.")
    @Test
    void insertAdminTest() {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        Map<String, Object> params = new HashMap<>();

        params.put("date", LocalDate.now(kst).plusDays(2).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);
        params.put("memberId", 1);

        String accessToken = RestAssured
                .given().log().all()
                .body(new LoginRequestDto("admin@email.com", "admin"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().statusCode(200)
                .extract().header("Set-Cookie").split("=")[1];

        RestAssured.given().contentType("application/json").body(params).log().all()
                .cookie("token", accessToken)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("어드민 Role 이 없을 때에 어드민 예약 추가 요청 시 401으로 응답한다.")
    @Test
    void insertAdminWithNoAdminRoleTest() {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        Map<String, Object> params = new HashMap<>();

        params.put("date", LocalDate.now(kst).plusDays(2).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);
        params.put("memberId", 1);

        String accessToken = RestAssured
                .given().log().all()
                .body(new LoginRequestDto("email@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().statusCode(200)
                .extract().header("Set-Cookie").split("=")[1];

        RestAssured.given().contentType("application/json").body(params).log().all()
                .cookie("token", accessToken)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("정상적인 조건으로 요청한 경우 200으로 응답한다.")
    @Test
    void getFilteredReservationsTest() {
        ZoneId kst = ZoneId.of("Asia/Seoul");

        LocalDate dateFrom = LocalDate.now(kst).minusWeeks(1);
        LocalDate dateTo = LocalDate.now(kst).minusDays(1);

        FilterConditionDto filterConditionDto = new FilterConditionDto(1L, 1L, dateFrom, dateTo);

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(filterConditionDto)
                .when().get("/reservations/filter")
                .then().log().all()
                .statusCode(200);
    }
}
