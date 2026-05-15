package roomescape.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static roomescape.test.util.RoomEscapeTestFixture.FASTER_RESERVATION;
import static roomescape.test.util.RoomEscapeTestFixture.FUTURE_DATE;
import static roomescape.test.util.RoomEscapeTestFixture.INSERTABLE_THEME;
import static roomescape.test.util.RoomEscapeTestFixture.INSERTABLE_TIME;
import static roomescape.test.util.RoomEscapeTestFixture.PAST_DATE;
import static roomescape.test.util.RoomEscapeTestFixture.THEME_IN_USE;
import static roomescape.test.util.RoomEscapeTestFixture.THEME_NOT_IN_USE;
import static roomescape.test.util.RoomEscapeTestFixture.TIME_IN_USE;
import static roomescape.test.util.RoomEscapeTestFixture.TIME_NOT_IN_USE;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
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
class ReservationControllerTest {

    private static final String NOT_EXIST_ID = EntityId.random().getValueAsString();

    @Autowired
    private RoomEscapeTestFixture fixture;

    @BeforeEach
    void initialDatabase() {
        fixture.clearTables();
        fixture.insertInitialData();
    }

    @Nested
    class 새로운_예약을_생성한다 {

        @Test
        void 새로운_예약을_생성하고_정보를_응답한다() {
            Map<String, Object> params = new HashMap<>();
            params.put("name", "브라운");
            params.put("date", FUTURE_DATE.toString());
            params.put("timeId", TIME_IN_USE.id().getValueAsString());
            params.put("themeId", THEME_IN_USE.id().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("id", notNullValue())
                    .body("name", equalTo("브라운"))
                    .body("date", equalTo(FUTURE_DATE.toString()))
                    .body("timeId", equalTo(TIME_IN_USE.id().getValueAsString()))
                    .body("themeId", equalTo(THEME_IN_USE.id().getValueAsString()));
        }

        @Test
        void 이미_지난_시간의_예약이라면_403을_응답한다() {
            Map<String, Object> params = new HashMap<>();
            params.put("name", "브라운");
            params.put("date", PAST_DATE.toString());
            params.put("timeId", TIME_IN_USE.id().getValueAsString());
            params.put("themeId", THEME_IN_USE.id().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(403)
                    .body("errorCode", is(ErrorCode.PAST_RESERVATION.name()));
        }

        @Test
        void 시간을_찾을_수_없다면_404를_응답한다() {
            Map<String, Object> params = new HashMap<>();
            params.put("name", "브라운");
            params.put("date", FUTURE_DATE.toString());
            params.put("timeId", NOT_EXIST_ID);
            params.put("themeId", THEME_IN_USE.id().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(404)
                    .body("errorCode", is(ErrorCode.RESERVATION_TIME_NOT_FOUND.name()));
        }

        @Test
        void 테마를_찾을_수_없다면_404를_응답한다() {
            Map<String, Object> params = new HashMap<>();
            params.put("name", "브라운");
            params.put("date", FUTURE_DATE.toString());
            params.put("timeId", TIME_IN_USE.id().getValueAsString());
            params.put("themeId", NOT_EXIST_ID);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(404)
                    .body("errorCode", is(ErrorCode.THEME_NOT_FOUND.name()));
        }

        @Test
        void 중복_예약이_존재한다면_409를_응답한다() {
            fixture.clearTables();
            fixture.insertTime(INSERTABLE_TIME);
            fixture.insertTheme(INSERTABLE_THEME);
            fixture.insertReservation(
                    "name",
                    FUTURE_DATE,
                    INSERTABLE_TIME.id(),
                    INSERTABLE_THEME.id()
            );

            // 같은 날짜, 시간, 테마로 예약 시도
            Map<String, Object> secondRequest = new HashMap<>();
            secondRequest.put("name", "검프");
            secondRequest.put("date", FUTURE_DATE.toString());
            secondRequest.put("timeId", INSERTABLE_TIME.id().getValueAsString());
            secondRequest.put("themeId", INSERTABLE_THEME.id().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(secondRequest)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(409)
                    .body("errorCode", is(ErrorCode.DUPLICATE_RESERVATION.name()));
        }
    }

    @Nested
    class 이름을_기반으로_예약을_조회한다 {

        @Autowired
        private RoomEscapeTestFixture fixture;

        @Test
        void 이름이_일치하는_예약_정보를_모두_응답한다() {
            RestAssured.given().log().all()
                    .param("name", FASTER_RESERVATION.getName())
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1))
                    .body("[0].name", equalTo(FASTER_RESERVATION.getName()))
                    .body("[0].id", notNullValue());
        }

        @Test
        void 이름이_일치하는_예약이_없다면_빈_리스트를_응답한다() {
            fixture.clearTables();

            RestAssured.given().log().all()
                    .param("name", "존재하지않는이름")
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }
    }

    @Nested
    class 예약의_날짜와_시간을_수정한다 {

        @Autowired
        private RoomEscapeTestFixture fixture;

        @Test
        void 정보를_수정하고_수정된_예약_정보를_응답한다() {
            String reservationName = "브라운";
            LocalDate dateBeforeUpdate = FUTURE_DATE;
            LocalDate dateAfterUpdate = dateBeforeUpdate.plusDays(1);
            String reservationId = fixture.insertReservation(
                    reservationName,
                    dateBeforeUpdate,
                    TIME_NOT_IN_USE.id(),
                    THEME_NOT_IN_USE.id()
            );

            // 다른 날짜로 수정
            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", dateAfterUpdate.toString());
            updateRequest.put("timeId", TIME_NOT_IN_USE.id().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", reservationName)
                    .body(updateRequest)
                    .when().patch("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(200)
                    .body("id", equalTo(reservationId))
                    .body("name", equalTo(reservationName))
                    .body("date", equalTo(dateAfterUpdate.toString()));
        }

        @Test
        void 이미_지난_예약을_수정하려_하면_403을_응답한다() {
            String reservationName = "브라운";
            String reservationId = fixture.insertReservation(
                    reservationName,
                    PAST_DATE,
                    TIME_NOT_IN_USE.id(),
                    THEME_NOT_IN_USE.id()
            );

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FUTURE_DATE.toString());
            updateRequest.put("timeId", TIME_IN_USE.id().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", reservationName)
                    .body(updateRequest)
                    .when().patch("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(403)
                    .body("errorCode", is(ErrorCode.PAST_RESERVATION.name()));
        }

        @Test
        void 이미_지난_시간으로_수정하려_하면_403을_응답한다() {
            String reservationName = "브라운";
            String reservationId = fixture.insertReservation(
                    reservationName,
                    FUTURE_DATE,
                    TIME_NOT_IN_USE.id(),
                    THEME_NOT_IN_USE.id()
            );

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", PAST_DATE.toString());
            updateRequest.put("timeId", TIME_IN_USE.id().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", reservationName)
                    .body(updateRequest)
                    .when().patch("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(403)
                    .body("errorCode", is(ErrorCode.PAST_RESERVATION.name()));
        }

        @Test
        void 취소된_예약이라면_403을_응답한다() {
            String reservationName = "브라운";
            boolean canceled = true;
            String reservationId = fixture.insertReservation(
                    reservationName,
                    FUTURE_DATE,
                    canceled,
                    TIME_NOT_IN_USE.id(),
                    THEME_NOT_IN_USE.id()
            );

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FUTURE_DATE.plusDays(1).toString());
            updateRequest.put("timeId", TIME_NOT_IN_USE.id().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", reservationName)
                    .body(updateRequest)
                    .when().patch("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(403)
                    .body("errorCode", is(ErrorCode.CANCELED_RESERVATION.name()));
        }

        @Test
        void 이름이_다르다면_403을_응답한다() {
            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FUTURE_DATE.toString());
            updateRequest.put("timeId", TIME_IN_USE.id().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", "다른이름")
                    .body(updateRequest)
                    .when().patch("/reservations/" + FASTER_RESERVATION.getId().getValueAsString())
                    .then().log().all()
                    .statusCode(403)
                    .body("errorCode", is(ErrorCode.NOT_RESERVATION_OWNER.name()));
        }

        @Test
        void 예약을_찾을_수_없다면_404를_응답한다() {
            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FUTURE_DATE.toString());
            updateRequest.put("timeId", TIME_IN_USE.id().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", FASTER_RESERVATION.getName())
                    .body(updateRequest)
                    .when().patch("/reservations/" + NOT_EXIST_ID)
                    .then().log().all()
                    .statusCode(404)
                    .body("errorCode", is(ErrorCode.RESERVATION_NOT_FOUND.name()));
        }

        @Test
        void 시간을_찾을_수_없다면_404를_응답한다() {
            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FUTURE_DATE.toString());
            updateRequest.put("timeId", NOT_EXIST_ID);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", FASTER_RESERVATION.getName())
                    .body(updateRequest)
                    .when().patch("/reservations/" + FASTER_RESERVATION.getId().getValueAsString())
                    .then().log().all()
                    .statusCode(404)
                    .body("errorCode", is(ErrorCode.RESERVATION_TIME_NOT_FOUND.name()));
        }

        @Test
        void 이미_예약된_시간으로_수정하려_하면_409를_응답한다() {
            String reservationName = "name";
            String reservationId = fixture.insertReservation(
                    reservationName,
                    FUTURE_DATE,
                    TIME_NOT_IN_USE.id(),
                    FASTER_RESERVATION.getThemeId()
            );

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FASTER_RESERVATION.getDate().toString());
            updateRequest.put("timeId", FASTER_RESERVATION.getTimeId().getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", reservationName)
                    .body(updateRequest)
                    .when().patch("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(409)
                    .body("errorCode", is(ErrorCode.DUPLICATE_RESERVATION.name()));
        }
    }

    @Nested
    class 예약을_취소한다 {

        @Autowired
        private RoomEscapeTestFixture fixture;

        @Test
        void 예약의_취소_상태를_true로_수정한다() {
            String reservationName = "name";
            String reservationId = fixture.insertReservation(
                    reservationName,
                    FUTURE_DATE,
                    TIME_NOT_IN_USE.id(),
                    THEME_NOT_IN_USE.id()
            );

            RestAssured.given().log().all()
                    .param("name", reservationName)
                    .when().delete("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(200);

            RestAssured.given().log().all()
                    .param("name", reservationName)
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1))
                    .body("[0].canceled", is(true));
        }

        @Test
        void 이름이_다르다면_403을_응답한다() {
            String reservationId = fixture.insertReservation(
                    "name",
                    FUTURE_DATE,
                    TIME_NOT_IN_USE.id(),
                    THEME_NOT_IN_USE.id()
            );

            RestAssured.given().log().all()
                    .param("name", "다른이름")
                    .when().delete("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(403)
                    .body("errorCode", is(ErrorCode.NOT_RESERVATION_OWNER.name()));
        }

        @Test
        void 지난_시간의_예약이라면_403을_응답한다() {
            String reservationName = "name";
            String reservationId = fixture.insertReservation(
                    reservationName,
                    PAST_DATE,
                    TIME_NOT_IN_USE.id(),
                    THEME_NOT_IN_USE.id()
            );

            RestAssured.given().log().all()
                    .param("name", reservationName)
                    .when().delete("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(403)
                    .body("errorCode", is(ErrorCode.PAST_RESERVATION.name()));
        }

        @Test
        void 예약을_찾을_수_없다면_404를_응답한다() {
            RestAssured.given().log().all()
                    .param("name", "브라운")
                    .when().delete("/reservations/" + NOT_EXIST_ID)
                    .then().log().all()
                    .statusCode(404)
                    .body("errorCode", is(ErrorCode.RESERVATION_NOT_FOUND.name()));
        }
    }
}
