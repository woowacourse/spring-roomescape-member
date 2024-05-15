package roomescape.controller.time;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@Sql(value = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimeControllerTest {

    @Autowired
    TimeController timeController;
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("예약 시간 목록을 요청하면 200 과 예약 시간 리스트를 응답한다.")
    void getTimes200AndTimes() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    @DisplayName("해당 날짜와 테마에 예약 가능한 시간 리스트를 요청하면 200 과 예약 시간 리스트를 응답한다.")
    void getTimes200AndTimesWithBooked() {
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);

        RestAssured.given().log().all()
                .contentType(ContentType.URLENC)
                .param("date", tomorrow)
                .param("themeId", 1)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    @DisplayName("날짜는 있지만 테마 없이 예약 가능한 시간 리스트를 요청하면 200 과 필터가 되지 않은 모든 예약 시간 리스트를 응답한다.")
    void getTimesDesireFilter200AndTimes() {
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);

        RestAssured.given().log().all()
                .contentType(ContentType.URLENC)
                .param("date", tomorrow)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    @DisplayName("예약 시간 추가하면 201 을 응답한다.")
    void addTime201AndLocation() {
        TimeRequest request = new TimeRequest("12:40");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", containsString("/times/"))
                .body("startAt", is(request.startAt()));
    }

    @Test
    @DisplayName("존재하는 예약 시간을 추가 하면 409 를 응답한다.")
    void addTime409() {
        TimeRequest request = new TimeRequest("08:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("존재하는 예약 시간을 삭제하면 204 를 응답한다.")
    void deleteTime204Present() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제하면 404 를 응답한다.")
    void deleteTime404NotExist() {
        RestAssured.given().log().all()
                .when().delete("/times/0")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("예약된 시간을 삭제 요청하면 400 을 응답한다.")
    void deleteTim400ExistReservation() {
        RestAssured.given().log().all()
                .when().delete("/times/2")
                .then().log().all()
                .statusCode(400);
    }
}
