package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static roomescape.test.util.RoomEscapeTestFixture.AFTERNOON_TIME_ID;
import static roomescape.test.util.RoomEscapeTestFixture.FUTURE_DATE;
import static roomescape.test.util.RoomEscapeTestFixture.INITIALIZED_TIME_COUNT;
import static roomescape.test.util.RoomEscapeTestFixture.MORNING_TIME_ID;
import static roomescape.test.util.RoomEscapeTestFixture.PAST_DATE;
import static roomescape.test.util.RoomEscapeTestFixture.WESTERN_THEME_ID;

import io.restassured.RestAssured;
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
                    .statusCode(400);
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
            // 미래 날짜에 MORNING_TIME으로 예약 추가
            fixture.insertReservation("test", FUTURE_DATE, MORNING_TIME_ID, WESTERN_THEME_ID);

            RestAssured.given().log().all()
                    .queryParam("themeId", WESTERN_THEME_ID.getValueAsString())
                    .queryParam("date", FUTURE_DATE.toString())
                    .when().get("/times/available-times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1))
                    .body("[0].id", is(AFTERNOON_TIME_ID.getValueAsString()));
        }

        @Test
        void 현재보다_과거에_해당하는_시간은_제외한다() {
            RestAssured.given().log().all()
                    .queryParam("themeId", WESTERN_THEME_ID.getValueAsString())
                    .queryParam("date", PAST_DATE.toString())
                    .when().get("/times/available-times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }

        @Test
        void 응답할_정보가_없다면_빈_리스트를_반환한다() {
            // 미래 날짜에 모든 시간을 예약
            fixture.insertReservation("test1", FUTURE_DATE, MORNING_TIME_ID, WESTERN_THEME_ID);
            fixture.insertReservation("test2", FUTURE_DATE, AFTERNOON_TIME_ID, WESTERN_THEME_ID);

            RestAssured.given().log().all()
                    .queryParam("themeId", WESTERN_THEME_ID.getValueAsString())
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
            EntityId unreservedTimeId = EntityId.random();
            fixture.insertTime(unreservedTimeId, LocalTime.of(12, 0));

            RestAssured.given().log().all()
                    .when().delete("/times/" + unreservedTimeId.getValueAsString())
                    .then().log().all()
                    .statusCode(200);

            RestAssured.given().log().all()
                    .when().get("/times")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(INITIALIZED_TIME_COUNT));
        }

        @Test
        void 예약에서_사용_중인_시간이라면_400을_응답한다() {
            RestAssured.given().log().all()
                    .when().delete("/times/" + MORNING_TIME_ID.getValueAsString())
                    .then().log().all()
                    .statusCode(400);
        }

        @Test
        void 존재하지_않는_식별자라면_404를_응답한다() {
            String nonExistentTimeId = UUID.randomUUID().toString();

            RestAssured.given().log().all()
                    .when().delete("/times/" + nonExistentTimeId)
                    .then().log().all()
                    .statusCode(404);
        }
    }
}
