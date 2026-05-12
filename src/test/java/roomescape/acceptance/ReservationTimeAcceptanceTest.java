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
class ReservationTimeAcceptanceTest {

    @Nested
    class 시간_추가 {

        @Test
        void 유효한_시간_추가_시_201_반환() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(Map.of("startAt", "10:00"))
                    .when().post("/admin/times")
                    .then().log().all()
                    .statusCode(201);
        }
    }

    @Nested
    class 시간_조회 {

        @BeforeEach
        void setUp() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("startAt", "10:00"))
                    .when().post("/admin/times");
        }

        @Test
        void 전체_시간_조회() {
            RestAssured.given().log().all()
                    .when().get("/admin/times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1));
        }
    }

    @Nested
    class 예약_가능한_시간_조회 {

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

        @Test
        void 예약_전_예약_가능한_시간_조회_시_available_true() {
            RestAssured.given().log().all()
                    .queryParam("date", "2026-05-01")
                    .queryParam("themeId", "1")
                    .when().get("/times/available")
                    .then().log().all()
                    .statusCode(200)
                    .body("[0].available", is(true));
        }

        @Test
        void 예약_후_예약_가능한_시간_조회_시_available_false() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of(
                            "name", "예약자",
                            "date", "2026-05-01",
                            "timeId", "1",
                            "themeId", "1"
                    ))
                    .when().post("/reservations");

            RestAssured.given().log().all()
                    .queryParam("date", "2026-05-01")
                    .queryParam("themeId", "1")
                    .when().get("/times/available")
                    .then().log().all()
                    .statusCode(200)
                    .body("[0].available", is(false));
        }
    }

    @Nested
    class 시간_삭제 {

        private int timeId;

        @BeforeEach
        void setUp() {
            timeId = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("startAt", "10:00"))
                    .when().post("/admin/times")
                    .then().extract().path("id");
        }

        @Test
        void 시간_삭제_시_204_반환() {
            RestAssured.given().log().all()
                    .when().delete("/admin/times/" + timeId)
                    .then().log().all()
                    .statusCode(204);
        }

        @Test
        void 시간_삭제_후_전체_조회_시_목록_비어있음() {
            RestAssured.given()
                    .when().delete("/admin/times/" + timeId);

            RestAssured.given().log().all()
                    .when().get("/admin/times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }
    }
}
