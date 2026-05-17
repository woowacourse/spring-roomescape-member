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
@Sql(scripts = "/reservation-integration-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 전체_예약을_조회한다() {
        // given & when
        var response = given().log().all()
            .when().get("/reservations");

        // then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(2));
    }

    @Test
    void 이름으로_예약을_조회한다() {
        // given & when
        var response = given().log().all()
            .queryParam("name", "코로구")
            .when().get("/reservations");

        // then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(1))
            .body("[0].name", equalTo("코로구"));
    }

    @Test
    void 존재하지_않는_이름으로_조회시_빈_목록을_반환한다() {
        // given & when
        var response = given().log().all()
            .queryParam("name", "없는사람")
            .when().get("/reservations");

        // then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(0));
    }

    @Test
    void 예약을_추가한다() {
        // given
        Map<String, Object> request = Map.of(
            "name", "신규예약자",
            "date", LocalDate.now().plusDays(3).toString(),
            "timeId", 1,
            "themeId", 1
        );

        // when
        var response = given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations");

        // then
        response.then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", notNullValue());
    }

    @Test
    void 예약을_추가하면_전체_조회에서_확인할_수_있다() {
        // given
        Map<String, Object> request = Map.of(
            "name", "신규예약자",
            "date", LocalDate.now().plusDays(3).toString(),
            "timeId", 1,
            "themeId", 1
        );

        // when
        given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations");

        // then
        given()
            .when().get("/reservations")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(3));
    }

    @Test
    void 예약을_삭제한다() {
        // given & when
        given().log().all()
            .queryParam("name", "코로구")
            .when().delete("/reservations/1")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        given()
            .when().get("/reservations")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(1));
    }

    @Test
    void 예약_날짜_및_시간을_변경한다() {
        // given
        Map<String, Object> request = Map.of(
            "date", LocalDate.now().plusDays(5).toString(),
            "timeId", 2
        );

        // when
        var response = given().log().all()
            .queryParam("name", "코로구")
            .contentType(ContentType.JSON)
            .body(request)
            .when().patch("/reservations/1");

        // then
        response.then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 중복_예약_추가시_예외가_발생한다() {
        // given
        Map<String, Object> request = Map.of(
            "name", "다른사람",
            "date", LocalDate.now().plusDays(1).toString(),
            "timeId", 1,
            "themeId", 1
        );

        // when
        var response = given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations");

        // then
        response.then()
            .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void 존재하지_않는_예약을_삭제해도_정상_응답을_반환한다() {
        // given & when
        var response = given().log().all()
            .queryParam("name", "코로구")
            .when().delete("/reservations/999");

        // then
        response.then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 다른_사람의_예약을_삭제하면_예외가_발생한다() {
        // given & when
        var response = given().log().all()
            .queryParam("name", "다른사람")
            .when().delete("/reservations/1");

        // then
        response.then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
