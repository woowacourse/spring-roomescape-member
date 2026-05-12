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
class ThemeAcceptanceTest {

    @Nested
    class 테마_추가 {

        @Test
        void 유효한_테마_추가_시_201_반환() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(Map.of(
                            "name", "방탈출1",
                            "description", "방탈출1 설명",
                            "thumbnailUrl", "theme/url.png"
                    ))
                    .when().post("/admin/themes")
                    .then().log().all()
                    .statusCode(201);
        }
    }

    @Nested
    class 테마_조회 {

        @BeforeEach
        void setUp() {
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
        void 전체_테마_조회() {
            RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1));
        }
    }

    @Nested
    class 테마_랭킹_조회 {

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
        void 특정_기간_내_테마_랭킹_조회() {
            RestAssured.given().log().all()
                    .param("start-date", "2026-05-01")
                    .param("end-date", "2026-05-07")
                    .when().get("/themes/ranking")
                    .then().log().all()
                    .statusCode(200)
                    .body("[0].id", is(1));
        }
    }

    @Nested
    class 테마_삭제 {

        private int themeId;

        @BeforeEach
        void setUp() {
            themeId = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of(
                            "name", "방탈출1",
                            "description", "방탈출1 설명",
                            "thumbnailUrl", "theme/url.png"
                    ))
                    .when().post("/admin/themes")
                    .then().extract().path("id");
        }

        @Test
        void 테마_삭제_시_204_반환() {
            RestAssured.given().log().all()
                    .when().delete("/admin/themes/" + themeId)
                    .then().log().all()
                    .statusCode(204);
        }

        @Test
        void 테마_삭제_후_전체_조회_시_목록_비어있음() {
            RestAssured.given()
                    .when().delete("/admin/themes/" + themeId);

            RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }
    }
}
