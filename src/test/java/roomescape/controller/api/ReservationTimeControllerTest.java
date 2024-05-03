package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @DisplayName("예약 시간 목록 조회")
    @Test
    void getReservationTimesWhenEmpty() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(7));
    }

    @DisplayName("시간 목록 예약 여부 조회")
    @Test
    void getReservationTimesWithBooked() {
        RestAssured.given().log().all()
                .when().get("/times?date=2024-04-29&themeId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(7));
    }

    @DisplayName("예약 시간 추가 및 삭제")
    @TestFactory
    Stream<DynamicTest> saveAndDeleteReservationTime() {
        return Stream.of(
                dynamicTest("예약 시간을 추가한다", () -> {
                    final Map<String, String> params = Map.of("startAt", "10:10");

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params)
                            .when().post("/times")
                            .then().log().all()
                            .statusCode(201)
                            .header("Location", "/times/8");
                }),
                dynamicTest("예약 시간을 삭제한다", () ->
                        RestAssured.given().log().all()
                                .when().delete("/times/8")
                                .then().log().all()
                                .statusCode(204)
                )
        );
    }

    @DisplayName("중복된 시간 추가 시 BadRequest 반환")
    @Test
    void saveDuplicatedTime() {
        final Map<String, String> params = Map.of("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("존재하지 않는 예약 시간 삭제 시 BadRequest 반환")
    @Test
    void deleteReservationTimeNotFound() {
        RestAssured.given().log().all()
                .when().delete("/times/100")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("유효하지 않은 시간 형식 입력 시 BadRequest 반환")
    @ParameterizedTest
    @ValueSource(strings = {"", "    ", "11:11:11", "25:10"})
    void invalidTimeFormat(final String time) {
        final Map<String, String> params = Map.of("startAt", time);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }
}
