package roomescape.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql({"/schema.sql", "/data.sql"})
public class ReservationTimeIntegrationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = this.port;
    }

    @DisplayName("예약 시간을 추가한다.")
    @Test
    void addReservationTimeTest() {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", "00:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("특정 테마, 날짜에 대한 예약 시간 목록을 조회한다.")
    @Test
    void findAllByDateAndThemeTest() {
        RestAssured.given().log().all()
                .param("theme-id", "2")
                .param("date", LocalDate.now().plusDays(1).toString())
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("예약 시간 목록을 조회한다")
    @Test
    void findAllReservationTimeTest() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("예약 시간을 삭제한다.")
    @Test
    void deleteReservationTimeTest() {
        RestAssured.given().log().all()
                .when().delete("/times/3")
                .then().log().all()
                .statusCode(204);
    }
}
