package roomescape.domain.reservation.controller;

import static io.restassured.RestAssured.given;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

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
class ReservationControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("getReservations 테스트")
    class GetReservationsTest {

        @Test
        @DisplayName("URL 인코딩된 사용자 이름으로 예약을 조회한다.")
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
    @DisplayName("updateReservation 테스트")
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
    }

    @Nested
    @DisplayName("deleteReservation 테스트")
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
