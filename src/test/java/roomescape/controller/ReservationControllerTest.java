package roomescape.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static roomescape.test.util.RoomEscapeTestFixture.AFTERNOON_TIME_ID;
import static roomescape.test.util.RoomEscapeTestFixture.FUTURE_DATE;
import static roomescape.test.util.RoomEscapeTestFixture.MORNING_TIME_ID;
import static roomescape.test.util.RoomEscapeTestFixture.PAST_DATE;
import static roomescape.test.util.RoomEscapeTestFixture.TEST_THEME_ID;
import static roomescape.test.util.RoomEscapeTestFixture.TEST_TIME_ID;
import static roomescape.test.util.RoomEscapeTestFixture.WESTERN_THEME_ID;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import roomescape.test.util.RoomEscapeTestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Import(RoomEscapeTestFixture.class)
class ReservationControllerTest {

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
            params.put("timeId", MORNING_TIME_ID.getValueAsString());
            params.put("themeId", WESTERN_THEME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("id", notNullValue())
                    .body("name", equalTo("브라운"))
                    .body("date", equalTo(FUTURE_DATE.toString()))
                    .body("timeId", equalTo(MORNING_TIME_ID.getValueAsString()))
                    .body("themeId", equalTo(WESTERN_THEME_ID.getValueAsString()));
        }

        @Test
        void 이미_지난_시간의_예약이라면_403을_응답한다() {
            Map<String, Object> params = new HashMap<>();
            params.put("name", "브라운");
            params.put("date", PAST_DATE.toString());
            params.put("timeId", MORNING_TIME_ID.getValueAsString());
            params.put("themeId", WESTERN_THEME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(403);
        }

        @Test
        void 시간을_찾을_수_없다면_404를_응답한다() {
            Map<String, Object> params = new HashMap<>();
            params.put("name", "브라운");
            params.put("date", FUTURE_DATE.toString());
            params.put("timeId", UUID.randomUUID().toString());
            params.put("themeId", WESTERN_THEME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(404);
        }

        @Test
        void 테마를_찾을_수_없다면_404를_응답한다() {
            Map<String, Object> params = new HashMap<>();
            params.put("name", "브라운");
            params.put("date", FUTURE_DATE.toString());
            params.put("timeId", MORNING_TIME_ID.getValueAsString());
            params.put("themeId", UUID.randomUUID().toString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(404);
        }

        @Test
        void 중복_예약이_존재한다면_409를_응답한다() {
            fixture.clearTables();
            fixture.insertTestTimeAndTheme();

            Map<String, Object> firstRequest = new HashMap<>();
            firstRequest.put("name", "브라운");
            firstRequest.put("date", FUTURE_DATE.toString());
            firstRequest.put("timeId", TEST_TIME_ID.getValueAsString());
            firstRequest.put("themeId", TEST_THEME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(firstRequest)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(200);

            // 같은 날짜, 시간, 테마로 예약 시도
            Map<String, Object> secondRequest = new HashMap<>();
            secondRequest.put("name", "검프");
            secondRequest.put("date", FUTURE_DATE.toString());
            secondRequest.put("timeId", TEST_TIME_ID.getValueAsString());
            secondRequest.put("themeId", TEST_THEME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(secondRequest)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(409);
        }
    }

    @Nested
    class 이름을_기반으로_예약을_조회한다 {

        @Autowired
        private RoomEscapeTestFixture fixture;

        @Test
        void 이름이_일치하는_예약_정보를_모두_응답한다() {
            RestAssured.given().log().all()
                    .param("name", "브라운")
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1))
                    .body("[0].name", equalTo("브라운"))
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
            fixture.clearTables();
            fixture.insertTestTimeAndTheme();
            fixture.insertAnotherTime();

            // 먼저 예약 생성
            String reservationId = fixture.insertReservation("브라운", FUTURE_DATE, TEST_TIME_ID, TEST_THEME_ID);

            // 다른 시간으로 수정
            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FUTURE_DATE.toString());
            updateRequest.put("timeId", AFTERNOON_TIME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", "브라운")
                    .body(updateRequest)
                    .when().patch("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(200)
                    .body("id", equalTo(reservationId))
                    .body("name", equalTo("브라운"))
                    .body("date", equalTo(FUTURE_DATE.toString()));
        }

        @Test
        void 이미_지난_예약을_수정하려_하면_403을_응답한다() {
            fixture.clearTables();
            fixture.insertTestTimeAndTheme();

            String reservationId = fixture.insertReservation("브라운", PAST_DATE, TEST_TIME_ID, TEST_THEME_ID);

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FUTURE_DATE.toString());
            updateRequest.put("timeId", TEST_TIME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", "브라운")
                    .body(updateRequest)
                    .when().patch("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(403);
        }

        @Test
        void 이미_지난_시간으로_수정하려_하면_403을_응답한다() {
            fixture.clearTables();
            fixture.insertTestTimeAndTheme();

            String reservationId = fixture.insertReservation("브라운", FUTURE_DATE, TEST_TIME_ID, TEST_THEME_ID);

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", PAST_DATE.toString());
            updateRequest.put("timeId", TEST_TIME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", "브라운")
                    .body(updateRequest)
                    .when().patch("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(403);
        }

        @Test
        void 이름이_다르다면_403을_응답한다() {
            fixture.clearTables();
            fixture.insertTestTimeAndTheme();

            String reservationId = fixture.insertReservation("브라운", FUTURE_DATE, TEST_TIME_ID, TEST_THEME_ID);

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FUTURE_DATE.toString());
            updateRequest.put("timeId", TEST_TIME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", "다른이름")
                    .body(updateRequest)
                    .when().patch("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(403);
        }

        @Test
        void 예약을_찾을_수_없다면_404를_응답한다() {
            fixture.clearTables();

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FUTURE_DATE.toString());
            updateRequest.put("timeId", MORNING_TIME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", "브라운")
                    .body(updateRequest)
                    .when().patch("/reservations/" + UUID.randomUUID())
                    .then().log().all()
                    .statusCode(404);
        }

        @Test
        void 시간을_찾을_수_없다면_404를_응답한다() {
            fixture.clearTables();
            fixture.insertTestTimeAndTheme();

            String reservationId = fixture.insertReservation("브라운", FUTURE_DATE, TEST_TIME_ID, TEST_THEME_ID);

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", FUTURE_DATE.toString());
            updateRequest.put("timeId", UUID.randomUUID().toString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", "브라운")
                    .body(updateRequest)
                    .when().patch("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(404);
        }

        @Test
        void 이미_예약된_시간으로_수정하려_하면_409를_응답한다() {
            fixture.clearTables();
            fixture.insertTestTimeAndTheme();

            LocalDate firstReservationDate = FUTURE_DATE;
            LocalDate secondReservationDate = firstReservationDate.plusDays(1);

            String firstReservationId = fixture.insertReservation("브라운", firstReservationDate, TEST_TIME_ID, TEST_THEME_ID);
            fixture.insertReservation("검프", secondReservationDate, TEST_TIME_ID.getValueAsString(), TEST_THEME_ID.getValueAsString());

            Map<String, Object> updateRequest = new HashMap<>();
            updateRequest.put("date", secondReservationDate.toString());
            updateRequest.put("timeId", TEST_TIME_ID.getValueAsString());

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .param("name", "브라운")
                    .body(updateRequest)
                    .when().patch("/reservations/" + firstReservationId)
                    .then().log().all()
                    .statusCode(409);
        }
    }

    @Nested
    class 예약을_취소한다 {

        @Autowired
        private RoomEscapeTestFixture fixture;

        @Test
        void 예약_정보를_삭제한다() {
            fixture.clearTables();
            fixture.insertTestTimeAndTheme();

            String reservationId = fixture.insertReservation("브라운", FUTURE_DATE, TEST_TIME_ID, TEST_THEME_ID);

            RestAssured.given().log().all()
                    .param("name", "브라운")
                    .when().delete("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(204);

            RestAssured.given().log().all()
                    .param("name", "브라운")
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }

        @Test
        void 이름이_다르다면_403을_응답한다() {
            fixture.clearTables();
            fixture.insertTestTimeAndTheme();

            String reservationId = fixture.insertReservation("브라운", FUTURE_DATE, TEST_TIME_ID, TEST_THEME_ID);

            RestAssured.given().log().all()
                    .param("name", "다른이름")
                    .when().delete("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(403);
        }

        @Test
        void 지난_시간의_예약이라면_403을_응답한다() {
            fixture.clearTables();
            fixture.insertTestTimeAndTheme();

            String reservationId = fixture.insertReservation("브라운", PAST_DATE, TEST_TIME_ID, TEST_THEME_ID);

            RestAssured.given().log().all()
                    .param("name", "브라운")
                    .when().delete("/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(403);
        }

        @Test
        void 예약을_찾을_수_없다면_404를_응답한다() {
            RestAssured.given().log().all()
                    .param("name", "브라운")
                    .when().delete("/reservations/" + UUID.randomUUID())
                    .then().log().all()
                    .statusCode(404);
        }
    }
}
