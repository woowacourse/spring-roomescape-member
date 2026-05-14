package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static roomescape.test.util.RoomEscapeTestFixture.FASTER_RESERVATION;
import static roomescape.test.util.RoomEscapeTestFixture.FUTURE_DATE;
import static roomescape.test.util.RoomEscapeTestFixture.INITIALIZED_THEME_COUNT;
import static roomescape.test.util.RoomEscapeTestFixture.INSERTABLE_THEME;
import static roomescape.test.util.RoomEscapeTestFixture.INSERTABLE_TIME;
import static roomescape.test.util.RoomEscapeTestFixture.LATER_RESERVATION;
import static roomescape.test.util.RoomEscapeTestFixture.THEME_IN_USE;
import static roomescape.test.util.RoomEscapeTestFixture.THEME_NOT_IN_USE;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import roomescape.exception.ErrorCode;
import roomescape.test.util.RoomEscapeTestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Import(RoomEscapeTestFixture.class)
class ThemeControllerTest {

    @Autowired
    private RoomEscapeTestFixture fixture;

    @BeforeEach
    void initialDatabase() {
        fixture.clearTables();
        fixture.insertInitialData();
    }

    @Test
    void 테마를_생성하고_그_정보를_응답한다() {
        String name = "신비로운 숲";
        String description = "신비한 숲을 탈출하는 테마";
        String imageUrl = "https://example.com/image.jpg";

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(Map.of(
                        "name", name,
                        "description", description,
                        "imageUrl", imageUrl
                ))
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is(name))
                .body("description", is(description))
                .body("imageUrl", is(imageUrl));
    }

    @Nested
    class 모든_테마를_조회한다 {

        @Test
        void 모든_테마에_대한_정보를_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(2))
                    .body("[0].id", notNullValue())
                    .body("[0].name", notNullValue())
                    .body("[0].description", notNullValue())
                    .body("[0].imageUrl", notNullValue())
                    .body("[1].id", notNullValue())
                    .body("[1].name", notNullValue())
                    .body("[1].description", notNullValue())
                    .body("[1].imageUrl", notNullValue());
        }

        @Test
        void 테마가_없다면_빈_리스트를_응답한다() {
            fixture.clearTables();

            RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }
    }

    @Nested
    class 테마를_예약이_많은_순으로_조회한다 {

        @Test
        void 기간_내_예약에_대한_테마만_조회한다() {
            fixture.clearTables();
            fixture.insertTheme(INSERTABLE_THEME);
            fixture.insertTime(INSERTABLE_TIME);

            LocalDate startDate = FUTURE_DATE.minusDays(1);
            LocalDate endDate = FUTURE_DATE.plusDays(1);

            fixture.insertReservation(
                    "within range reservation",
                    startDate,
                    INSERTABLE_TIME.id(),
                    INSERTABLE_THEME.id()
            );
            fixture.insertReservation(
                    "without range reservation",
                    endDate.plusYears(1),
                    INSERTABLE_TIME.id(),
                    INSERTABLE_THEME.id()
            );

            RestAssured.given().log().all()
                    .queryParam("limit", 10)
                    .queryParam("startDate", startDate.toString())
                    .queryParam("endDate", endDate.toString())
                    .when().get("/themes/most-reserved")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1));
        }

        @Test
        void 예약_횟수를_같이_조회한다() {
            LocalDate startDate = FASTER_RESERVATION.date();
            LocalDate endDate = LATER_RESERVATION.date();

            RestAssured.given().log().all()
                    .queryParam("limit", 10)
                    .queryParam("startDate", startDate.toString())
                    .queryParam("endDate", endDate.toString())
                    .when().get("/themes/most-reserved")
                    .then().log().all()
                    .statusCode(200)
                    .body("[0].name", is(THEME_IN_USE.name()))
                    .body("[0].reservationCount", is(2));
        }

        @Test
        void 조회된_테마가_없다면_빈_리스트를_응답한다() {
            LocalDate startDate = LocalDate.now().minusYears(2000);
            LocalDate endDate = startDate.plusDays(1);

            RestAssured.given().log().all()
                    .queryParam("limit", 10)
                    .queryParam("startDate", startDate.toString())
                    .queryParam("endDate", endDate.toString())
                    .when().get("/themes/most-reserved")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }

        @Test
        void 시작일이_없다면_400을_응답한다() {
            LocalDate endDate = LATER_RESERVATION.date();

            RestAssured.given().log().all()
                    .queryParam("limit", 10)
                    .queryParam("endDate", endDate.toString())
                    .when().get("/themes/most-reserved")
                    .then().log().all()
                    .statusCode(400)
                    .body("errorCode", is(ErrorCode.INVALID_REQUEST.name()));
        }

        @Test
        void 종료일이_없다면_400을_응답한다() {
            LocalDate startDate = FASTER_RESERVATION.date();

            RestAssured.given().log().all()
                    .queryParam("limit", 10)
                    .queryParam("startDate", startDate.toString())
                    .when().get("/themes/most-reserved")
                    .then().log().all()
                    .statusCode(400)
                    .body("errorCode", is(ErrorCode.INVALID_REQUEST.name()));
        }

        @Test
        void 시작일이_종료일보다_미래라면_400을_응답한다() {
            LocalDate startDate = LATER_RESERVATION.date();
            LocalDate endDate = FASTER_RESERVATION.date();

            RestAssured.given().log().all()
                    .queryParam("limit", 10)
                    .queryParam("startDate", startDate.toString())
                    .queryParam("endDate", endDate.toString())
                    .when().get("/themes/most-reserved")
                    .then().log().all()
                    .statusCode(400)
                    .body("errorCode", is(ErrorCode.INVALID_DURATION.name()));
        }
    }

    @Nested
    class 테마를_제거한다 {

        @Test
        void 제거에_성공하면_200을_응답한다() {
            RestAssured.given().log().all()
                    .when().delete("/themes/" + THEME_NOT_IN_USE.id().getValueAsString())
                    .then().log().all()
                    .statusCode(200);

            RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(INITIALIZED_THEME_COUNT - 1));
        }

        @Test
        void 예약에서_사용_중인_테마라면_400을_응답한다() {
            RestAssured.given().log().all()
                    .when().delete("/themes/" + THEME_IN_USE.id().getValueAsString())
                    .then().log().all()
                    .statusCode(409)
                    .body("errorCode", is(ErrorCode.THEME_IN_USE.name()));
        }

        @Test
        void 존재하지_않는_테마라면_404를_응답한다() {
            String nonExistentTimeId = UUID.randomUUID().toString();

            RestAssured.given().log().all()
                    .when().delete("/themes/" + nonExistentTimeId)
                    .then().log().all()
                    .statusCode(404)
                    .body("errorCode", is(ErrorCode.THEME_NOT_FOUND.name()));
        }
    }
}
