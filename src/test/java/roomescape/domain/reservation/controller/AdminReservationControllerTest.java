package roomescape.domain.reservation.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
class AdminReservationControllerTest {

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
        @DisplayName("예약 목록 조회 요청이면 200을 반환한다.")
        void 성공() {
            given()
                .when()
                .get("/api/admin/reservations")
                .then()
                .statusCode(200);
        }
    }

    @Nested
    @DisplayName("saveReservation 테스트")
    class SaveReservationTest {

        @Test
        @DisplayName("예약 생성 요청이면 201을 반환한다.")
        void 성공() {
            given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "name", "시오",
                    "date", "2026-12-31",
                    "timeId", 1L,
                    "themeId", 1L
                ))
                .when()
                .post("/api/admin/reservations")
                .then()
                .statusCode(201)
                .body("name", equalTo("시오"));
        }
    }

    @Nested
    @DisplayName("deleteReservation 테스트")
    class DeleteReservationTest {

        @Test
        @DisplayName("예약 삭제 요청이면 204를 반환한다.")
        void 성공() {
            Long id = createReservation();

            given()
                .when()
                .delete("/api/admin/reservations/{id}", id)
                .then()
                .statusCode(204);
        }
    }

    private Long createReservation() {
        return given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "name", "시오",
                "date", "2026-12-31",
                "timeId", 1L,
                "themeId", 1L
            ))
            .when()
            .post("/api/admin/reservations")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getLong("id");
    }
}
