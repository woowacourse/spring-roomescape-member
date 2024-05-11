package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class ReservationTimeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("정상적인 시간 추가 요청 시 201으로 응답한다.")
    @Test
    void insertTest() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all().statusCode(201);
    }

    @DisplayName("시간 조회 요청 시 200으로 응답한다.")
    @Test
    void findAllTest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("시간 삭제 요청 시 204로 응답한다.")
    @Test
    void deleteTest() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        int id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().path("id");

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(id));

        RestAssured.given().log().all()
                .when().delete("/times/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("빈 시간을 입력하면 400으로 응답한다.")
    @Test
    void invalidTimeTest() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all().statusCode(400);
    }

    @DisplayName("예약이 존재하는 시간은 삭제할 수 없다.")
    @Test
    void invalidDeleteTimeTest() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400)
                .body(is("예약이 존재하는 시간은 삭제할 수 없습니다."));
    }

    @DisplayName("예약이 존재하지 않는 시간은 예약이 가능하다.")
    @Test
    void getAvailableForReservationTimes() {
        RestAssured.given().log().all()
                .param("date", "2024-05-01")
                .param("themeId", "1")
                .when().get("/times/filter")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].startAt", is("01:00"))
                .body("[0].booked", is(false));
    }

    @DisplayName("예약이 존재하는 시간은 예약할 수 없다.")
    @Test
    void getUnavailableForReservationTimes() {
        ZoneId kst = ZoneId.of("Asia/Seoul");

        RestAssured.given().log().all()
                .param("date", LocalDate.now(kst).minusDays(1).toString())
                .param("themeId", "1")
                .when().get("/times/filter")
                .then().log().all()
                .statusCode(200)
                .body("[0].startAt", is("01:00"))
                .body("[0].booked", is(true));
    }
}
