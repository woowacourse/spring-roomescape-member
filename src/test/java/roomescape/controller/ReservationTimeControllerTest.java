package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static roomescape.test.util.RoomEscapeTestFixture.FUTURE_DATE;
import static roomescape.test.util.RoomEscapeTestFixture.INITIALIZED_TIME_COUNT;
import static roomescape.test.util.RoomEscapeTestFixture.PAST_DATE;
import static roomescape.test.util.RoomEscapeTestFixture.THEME_IN_USE;
import static roomescape.test.util.RoomEscapeTestFixture.THEME_NOT_IN_USE;
import static roomescape.test.util.RoomEscapeTestFixture.TIME_IN_USE;
import static roomescape.test.util.RoomEscapeTestFixture.TIME_NOT_IN_USE;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import roomescape.domain.EntityId;
import roomescape.exception.ErrorCode;
import roomescape.test.util.RoomEscapeTestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Import(RoomEscapeTestFixture.class)
class ReservationTimeControllerTest {

    private static final LocalTime DEFAULT_START_AT = LocalTime.of(11, 0);

    @Autowired
    private RoomEscapeTestFixture fixture;

    @BeforeEach
    void initialDatabase() {
        fixture.clearTables();
        fixture.insertInitialData();
    }

    @Nested
    class 예약_시간을_생성한다 {

        @Test
        void 생성된_시간_정보를_응답한다() {
            String actualStartAt = RestAssured.given().log().all()
                    .contentType("application/json")
                    .body(Map.of(
                            "startAt", DEFAULT_START_AT.toString()
                    ))
                    .when().post("/times")
                    .then().log().all()
                    .statusCode(200)
                    .body("id", notNullValue())
                    .extract().jsonPath().getString("startAt");

            assertThat(LocalTime.parse(actualStartAt)).isEqualTo(DEFAULT_START_AT);
        }

        @Test
        void 시작_시간_값이_없다면_400을_응답한다() {
            RestAssured.given().log().all()
                    .contentType("application/json")
                    .body(Map.of())
                    .when().post("/times")
                    .then().log().all()
                    .statusCode(400)
                    .body("errorCode", is(ErrorCode.INVALID_RESERVATION_TIME.name()));
        }
    }

    @Nested
    class 모든_예약_시간을_조회한다 {

        @Test
        void 모든_예약_시간_정보를_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(2))
                    .body("[0].id", notNullValue())
                    .body("[0].startAt", notNullValue())
                    .body("[1].id", notNullValue())
                    .body("[1].startAt", notNullValue());
        }

        @Test
        void 응답할_정보가_없다면_빈_리스트를_반환한다() {
            fixture.clearTables();

            RestAssured.given().log().all()
                    .when().get("/times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }
    }

    @Nested
    class 예약_가능한_시간을_조회한다 {

        @Test
        void 이미_예약된_시간은_제외한다() {
            LocalDate reservedDate = FUTURE_DATE;
            EntityId reservedTimeId = TIME_NOT_IN_USE.id();
            EntityId reservedThemeId = THEME_NOT_IN_USE.id();
            fixture.insertReservation(
                    "time name",
                    reservedDate,
                    reservedTimeId,
                    reservedThemeId
            );

            RestAssured.given().log().all()
                    .queryParam("themeId", reservedThemeId.getValueAsString())
                    .queryParam("date", reservedDate.toString())
                    .when().get("/times/available-times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(INITIALIZED_TIME_COUNT - 1));
        }

        @Test
        void 현재보다_과거에_해당하는_시간은_제외한다() {
            RestAssured.given().log().all()
                    .queryParam("themeId", THEME_IN_USE.id().getValueAsString())
                    .queryParam("date", PAST_DATE.toString())
                    .when().get("/times/available-times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }

        @Test
        void 응답할_정보가_없다면_빈_리스트를_반환한다() {
            // 미래 날짜에 모든 시간을 예약
            fixture.insertReservation("test1", FUTURE_DATE, TIME_IN_USE.id(), THEME_IN_USE.id());
            fixture.insertReservation("test2", FUTURE_DATE, TIME_NOT_IN_USE.id(), THEME_IN_USE.id());

            RestAssured.given().log().all()
                    .queryParam("themeId", THEME_IN_USE.id().getValueAsString())
                    .queryParam("date", FUTURE_DATE.toString())
                    .when().get("/times/available-times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }
    }

    @Nested
    class 예약_시간을_제거한다 {

        @Test
        void 제거에_성공하면_200을_응답한다() {
            RestAssured.given().log().all()
                    .when().delete("/times/" + TIME_NOT_IN_USE.id().getValueAsString())
                    .then().log().all()
                    .statusCode(200);

            RestAssured.given().log().all()
                    .when().get("/times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(INITIALIZED_TIME_COUNT - 1));
        }

        @Test
        void 예약에서_사용_중인_시간이라면_409을_응답한다() {
            RestAssured.given().log().all()
                    .when().delete("/times/" + TIME_IN_USE.id().getValueAsString())
                    .then().log().all()
                    .statusCode(409)
                    .body("errorCode", is(ErrorCode.TIME_IN_USE.name()));
        }

        @Test
        void 존재하지_않는_식별자라면_404를_응답한다() {
            String nonExistentTimeId = UUID.randomUUID().toString();

            RestAssured.given().log().all()
                    .when().delete("/times/" + nonExistentTimeId)
                    .then().log().all()
                    .statusCode(404)
                    .body("errorCode", is(ErrorCode.RESERVATION_TIME_NOT_FOUND.name()));
        }
    }
}
