package roomescape.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/reservation-time-integration-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 전체_예약_시간을_조회한다() {
        // given & when
        var response = given().log().all()
            .when().get("/times");

        // then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(2));
    }

    @Test
    void 관리자는_예약_시간을_추가한다() {
        // given
        Map<String, Object> request = Map.of("startAt", "10:00");

        // when
        var response = given().log().all()
            .queryParam("role", "admin")
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/times");

        // then
        response.then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", notNullValue());
    }

    @Test
    void 관리자는_예약이_없는_시간을_삭제한다() {
        // given & when (time_id=2는 예약 없음)
        var response = given().log().all()
            .queryParam("role", "admin")
            .when().delete("/times/2");

        // then
        response.then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 오늘_이후_예약이_있는_시간을_삭제하면_예외가_발생한다() {
        // given & when (time_id=1은 내일 예약 있음)
        var response = given().log().all()
            .queryParam("role", "admin")
            .when().delete("/times/1");

        // then
        response.then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 날짜와_테마로_예약_가능한_시간을_조회한다() {
        // given (time_id=1은 내일 예약됨, time_id=2는 비어있음)
        String tomorrow = LocalDate.now().plusDays(1).toString();

        // when
        var response = given().log().all()
            .queryParam("date", tomorrow)
            .queryParam("themeId", 1)
            .when().get("/times/available");

        // then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("times", hasSize(2))
            .body("times.find { it.id == 1 }.available", equalTo(false))
            .body("times.find { it.id == 2 }.available", equalTo(true));
    }

    @Test
    void 관리자가_아닌_경우_시간_추가가_불가하다() {
        // given
        Map<String, Object> request = Map.of("startAt", "10:00");

        // when
        var response = given().log().all()
            .queryParam("role", "user")
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/times");

        // then
        response.then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 관리자가_아닌_경우_시간_삭제가_불가하다() {
        // given & when
        var response = given().log().all()
            .queryParam("role", "user")
            .when().delete("/times/2");

        // then
        response.then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
