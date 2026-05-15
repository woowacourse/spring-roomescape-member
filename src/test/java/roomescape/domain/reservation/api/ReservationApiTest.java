package roomescape.domain.reservation.api;

import static io.restassured.RestAssured.given;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.net.URLEncoder;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("사용자 예약의")
class ReservationApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("예약 조회 api는")
    class GetReservationsTest {

        @Test
        @DisplayName("예약을 조회한다.")
        void 성공() {
            createReservation("브라운", "2026-12-31", 1L, 1L);

            given()
                .header("Reservation-Name", URLEncoder.encode("브라운", UTF_8))
                .when()
                .get("/api/reservations")
                .then()
                .statusCode(200)
                .body("name", hasItem("브라운"));
        }

        @Test
        @DisplayName("헤더가 없으면 400을 반환한다.")
        void 실패1() {
            given()
                .when()
                .get("/api/reservations")
                .then()
                .statusCode(400);
        }
    }

    @Nested
    @DisplayName("예약 생성 api는")
    class SaveReservationTest {

        @Test
        @DisplayName("정상 요청이면 201을 반환한다.")
        void 성공() {
            given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "name", "브라운",
                    "date", "2026-12-31",
                    "timeId", 1L,
                    "themeId", 1L
                ))
                .when()
                .post("/api/reservations")
                .then()
                .statusCode(201)
                .body("name", equalTo("브라운"));
        }

        @Test
        @DisplayName("이름이 빈 문자열이면 400을 반환한다.")
        void 실패1() {
            given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "name", "",
                    "date", "2026-12-31",
                    "timeId", 1L,
                    "themeId", 1L
                ))
                .when()
                .post("/api/reservations")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("name"))
                .body("errors.find { it.field == 'name' }.value", equalTo(""))
                .body("errors.find { it.field == 'name' }.message", notNullValue());
        }

        @Test
        @DisplayName("필수 필드가 누락되면 400을 반환한다.")
        void 실패2() {
            given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "name", "브라운",
                    "date", "2026-12-31",
                    "timeId", 1L
                ))
                .when()
                .post("/api/reservations")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("themeId"))
                .body("errors.find { it.field == 'themeId' }.value", nullValue())
                .body("errors.find { it.field == 'themeId' }.message", equalTo("널이어서는 안됩니다"));
        }

        @Test
        @DisplayName("날짜 형식이 잘못되면 400을 반환한다.")
        void 실패3() {
            String wrongDate = "2026/12/31";

            given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "name", "브라운",
                    "date", wrongDate,
                    "timeId", 1L,
                    "themeId", 1L
                ))
                .when()
                .post("/api/reservations")
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("date"))
                .body("errors.find { it.field == 'date' }.value", equalTo(wrongDate))
                .body("errors.find { it.field == 'date' }.message", equalTo("형식이 올바르지 않습니다."));
        }
    }

    @Nested
    @DisplayName("예약 수정 api는")
    class UpdateReservationTest {

        @Test
        @DisplayName("정상 요청이면 204를 반환한다.")
        void 성공() {
            Long id = createReservation("브라운", "2026-12-30", 1L, 1L);

            given()
                .header("Reservation-Name", URLEncoder.encode("브라운", UTF_8))
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "date", "2026-12-31",
                    "timeId", 2L
                ))
                .when()
                .patch("/api/reservations/{id}", id)
                .then()
                .statusCode(204);
        }

        @Test
        @DisplayName("헤더가 없으면 400을 반환한다.")
        void 실패1() {
            Long id = createReservation("브라운", "2026-12-30", 1L, 1L);

            given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "date", "2026-12-31",
                    "timeId", 2L
                ))
                .when()
                .patch("/api/reservations/{id}", id)
                .then()
                .statusCode(400);
        }

        @Test
        @DisplayName("요청 경로 변수가 잘못되면 400을 반환한다.")
        void 실패2() {
            Object wrongId = "a";

            given()
                .header("Reservation-Name", URLEncoder.encode("브라운", UTF_8))
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "timeId", 2L
                ))
                .when()
                .patch("/api/reservations/{id}", wrongId)
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("id"))
                .body("errors.find { it.field == 'id' }.value", equalTo(wrongId))
                .body("errors.find { it.field == 'id' }.message", equalTo("Long 타입이어야 합니다."));
        }

        @Test
        @DisplayName("요청 본문 필드 중 하나가 누락되면 400을 반환한다.")
        void 실패3() {
            Long id = createReservation("브라운", "2026-12-30", 1L, 1L);

            given()
                .header("Reservation-Name", URLEncoder.encode("브라운", UTF_8))
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "timeId", 2L
                ))
                .when()
                .patch("/api/reservations/{id}", id)
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("date"))
                .body("errors.find { it.field == 'date' }.value", nullValue())
                .body("errors.find { it.field == 'date' }.message", equalTo("널이어서는 안됩니다"));
        }

        @Test
        @DisplayName("날짜 형식이 잘못되면 400을 반환한다.")
        void 실패4() {
            Long id = createReservation("브라운", "2026-12-30", 1L, 1L);
            String wrongDate = "2026/12/31";

            given()
                .header("Reservation-Name", URLEncoder.encode("브라운", UTF_8))
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "date", wrongDate,
                    "timeId", 2L
                ))
                .when()
                .patch("/api/reservations/{id}", id)
                .then()
                .statusCode(400)
                .body("errors.field", hasItem("date"))
                .body("errors.find { it.field == 'date' }.value", equalTo(wrongDate))
                .body("errors.find { it.field == 'date' }.message", equalTo("형식이 올바르지 않습니다."));
        }
    }

    @Nested
    @DisplayName("예약 삭제 api는")
    class DeleteReservationTest {

        @Test
        @DisplayName("정상 요청이면 204를 반환한다.")
        void 성공() {
            Long id = createReservation("브라운", "2026-12-31", 1L, 1L);

            given()
                .header("Reservation-Name", URLEncoder.encode("브라운", UTF_8))
                .when()
                .delete("/api/reservations/{id}", id)
                .then()
                .statusCode(204);
        }

        @Test
        @DisplayName("헤더가 없으면 400을 반환한다.")
        void 실패1() {
            Long id = createReservation("브라운", "2026-12-31", 1L, 1L);

            given()
                .when()
                .delete("/api/reservations/{id}", id)
                .then()
                .statusCode(400);
        }
    }

    private Long createReservation(String name, String date, Long timeId, Long themeId) {
        return given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "name", name,
                "date", date,
                "timeId", timeId,
                "themeId", themeId
            ))
            .when()
            .post("/api/reservations")
            .then()
            .statusCode(201)
            .body("name", equalTo(name))
            .extract()
            .jsonPath()
            .getLong("id");
    }
}
