package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomescapeApplicationTest {

        @LocalServerPort
        int port;

        @BeforeEach
        void setUp() {
                RestAssured.port = port;
        }

        @Test
        @DisplayName("사용자는 조건에 맞는 새로운 예약을 생성할 수 있다.")
        void createReservation() {
                Map<String, Object> request = new HashMap<>();
                request.put("themeId", 1);
                request.put("name", "캐모");
                request.put("date", "2026-05-01");
                request.put("timeId", 2);

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when().post("/reservations")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value());
        }

        @Test
        @DisplayName("이미 예약된 시간에 중복 예약을 시도하면 400 예외가 발생한다.")
        void createReservation_DuplicateException() {
                Map<String, Object> request = new HashMap<>();
                request.put("themeId", 1);
                request.put("name", "캐모");
                request.put("date", "2026-05-01");
                request.put("timeId", 1);

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when().post("/reservations")
                        .then().log().all()
                        .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("예약자의 이름(JSON)을 전송하여 예약을 삭제할 수 있다.")
        void deleteReservation() {
                Map<String, Object> request = new HashMap<>();
                request.put("themeId", 1);
                request.put("name", "캐모");
                request.put("date", "2026-05-01");
                request.put("timeId", 2);

                Long reservationId = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .post("/reservations")
                        .jsonPath().getLong("id");

                Map<String, String> deleteRequest = Map.of("name", "캐모");

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(deleteRequest)
                        .when().delete("/reservations/" + reservationId)
                        .then().log().all()
                        .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("관리자는 새로운 테마를 생성할 수 있다.")
        void createTheme() {
                Map<String, String> request = Map.of(
                        "name", "새로운 테마",
                        "description", "설명입니다.",
                        "thumbnail", "https://example.com/new.png"
                );

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when().post("/admin/themes")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .body("id", notNullValue());
        }

        @Test
        @DisplayName("사용자는 등록된 테마 목록을 조회할 수 있다.")
        void getThemes() {
                RestAssured.given().log().all()
                        .when().get("/themes")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .body("size()", is(4));
        }

        @Test
        @DisplayName("관리자는 특정 테마를 삭제할 수 있다.")
        void deleteTheme() {
                RestAssured.given().log().all()
                        .when().delete("/admin/themes/1")
                        .then().log().all()
                        .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("새로운 예약 시간을 생성할 수 있다.")
        void createTime() {
                Map<String, String> request = Map.of("startAt", "15:00:00");

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when().post("/admin/times")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .body("startAt", is("15:00:00"));
        }

        @Test
        @DisplayName("예약이 없는 시간은 정상적으로 삭제할 수 있다.")
        void deleteTime() {
                long timeId = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(Map.of("startAt", "23:00:00"))
                        .post("/admin/times")
                        .jsonPath().getLong("id");

                RestAssured.given().log().all()
                        .when().delete("/admin/times/" + timeId)
                        .then().log().all()
                        .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("이미 예약이 존재하는 시간을 삭제하려 하면 409 Conflict가 발생한다.")
        void deleteTime_Conflict() {
                RestAssured.given().log().all()
                        .when().delete("/admin/times/1")
                        .then().log().all()
                        .statusCode(HttpStatus.CONFLICT.value());
        }


        @Test
        @DisplayName("특정 날짜와 테마의 스케줄 조회 시 예약 상태(isAvailable)가 정확히 반환된다.")
        void getSchedules() {
                RestAssured.given().log().all()
                        .queryParam("date", "2026-05-01")
                        .when().get("/times/1")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .body("themeId", is(1))
                        .body("date", is("2026-05-01"))
                        .body("schedules.find { it.timeId == 1 }.isAvailable", is(false))
                        .body("schedules.find { it.timeId == 2 }.isAvailable", is(true));
        }

        @Test
        @DisplayName("파라미터 없이 랭킹을 조회하면 최근 7일 기준 상위 10개(기본값)의 테마가 반환된다.")
        void getRankedThemes_Default() {
                RestAssured.given().log().all()
                        .when().get("/themes/rank")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .body("size()", is(4))
                        .body("[0].id", is(1))
                        .body("[1].id", is(2))
                        .body("[2].id", is(3))
                        .body("[3].id", is(4));
        }

        @Test
        @DisplayName("limit 파라미터를 2로 지정하면 상위 2개의 테마만 반환된다.")
        void getRankedThemes_WithLimit() {
                RestAssured.given().log().all()
                        .queryParam("limit", 2)
                        .when().get("/themes/rank")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .body("size()", is(2))
                        .body("[0].id", is(1))
                        .body("[1].id", is(2));
        }

        @Test
        @DisplayName("특정 날짜(startDate, endDate)를 지정하여 랭킹을 조회할 수 있다.")
        void getRankedThemes_WithDates() {
                RestAssured.given().log().all()
                        .queryParam("startDate", "2026-05-05")
                        .queryParam("endDate", "2026-05-06")
                        .when().get("/themes/rank")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .body("[0].id", is(1));
        }
}