package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationAcceptanceTest {

    @BeforeEach
    void setUp() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/admin/times");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "방탈출1",
                        "description", "방탈출1 설명",
                        "thumbnailUrl", "theme/url.png"
                ))
                .when().post("/admin/themes");
    }

    @Nested
    class 예약_추가 {

        @Test
        void 유효한_예약_추가_시_201_반환() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(Map.of(
                            "name", "예약자",
                            "date", "2026-05-01",
                            "timeId", "1",
                            "themeId", "1"
                    ))
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(201);
        }

        @Test
        void 중복_예약_추가_시_400_반환() {
            Map<String, Object> reservationBody = Map.of(
                    "name", "예약자",
                    "date", "2026-05-01",
                    "timeId", "1",
                    "themeId", "1"
            );

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(reservationBody)
                    .when().post("/reservations");

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(reservationBody)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(400)
                    .body("message", is("[ERROR] 동일한 예약이 이미 존재합니다."));
        }
    }

    @Nested
    class 예약_조회 {

        @BeforeEach
        void setUp() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of(
                            "name", "예약자",
                            "date", "2026-05-01",
                            "timeId", "1",
                            "themeId", "1"
                    ))
                    .when().post("/reservations");
        }

        @Test
        void 전체_예약_조회() {
            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1));
        }
    }

    @Nested
    class 예약_삭제 {

        private int reservationId;

        @BeforeEach
        void setUp() {
            reservationId = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of(
                            "name", "예약자",
                            "date", "2026-05-01",
                            "timeId", "1",
                            "themeId", "1"
                    ))
                    .when().post("/reservations")
                    .then().extract().path("id");
        }

        @Test
        void 예약_삭제_시_204_반환() {
            RestAssured.given().log().all()
                    .when().delete("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(204);
        }

        @Test
        void 예약_삭제_후_전체_조회_시_목록_비어있음() {
            RestAssured.given()
                    .when().delete("/reservations/" + reservationId);

            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }
    }
}
