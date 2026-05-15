package roomescape.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Theme;
import roomescape.domain.ReservationTime;
import roomescape.util.ApiTestSupport;
import roomescape.util.TestDataInitializer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeApiTest extends ApiTestSupport {

    @Autowired
    private TestDataInitializer dataInitializer;

    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservation-times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservation-times")
                .then().log().all()
                .statusCode(200)
                .body("reservationTimes.size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservation-times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 같은_예약_시간은_등록할_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservation-times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservation-times")
                .then().log().all()
                .statusCode(409);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "1000", "10:0", "10-00", "25:00"})
    void 예약_시간_요청값이_유효하지_않으면_400을_반환한다(String startAt) {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", startAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservation-times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 예약과_시간_연결() {
        dataInitializer.createReservationTime(LocalTime.now());
        dataInitializer.createTheme("hello", "world", "/images/themes/hello.webp");

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", LocalDate.now().plusDays(1).toString());
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(1));
    }

    @Test
    void 예약_가능_시간_조회() {
        // given
        ReservationTime ten = dataInitializer.createReservationTime(LocalTime.of(10, 0));
        ReservationTime eleven = dataInitializer.createReservationTime(LocalTime.of(11, 0));
        Theme theme = dataInitializer.createTheme("hello", "world", "/images/themes/hello.webp");

        LocalDate date = LocalDate.now().plusDays(1);
        dataInitializer.createReservation("라텔", date, ten.getId(), theme.getId());

        Map<String, Object> params = new HashMap<>();
        params.put("date", date.toString());
        params.put("timeId", ten.getId());
        params.put("themeId", theme.getId());
        params.put("available", true);

        // when
        RestAssured.given().log().all()
                .queryParams(params)
                .when().get("/reservation-times/available")
                .then().log().all()
                .statusCode(200)
                .body("availableTimes.size()", is(1),
                        "availableTimes[0].startAt", equalTo(eleven.getStartAt().toString()));
    }

    @Test
    void 예약된_시간만_조회한다() {
        ReservationTime ten = dataInitializer.createReservationTime(LocalTime.of(10, 0));
        dataInitializer.createReservationTime(LocalTime.of(11, 0));
        Theme theme = dataInitializer.createTheme("hello", "world", "/images/themes/hello.webp");

        LocalDate date = LocalDate.now().plusDays(1);
        dataInitializer.createReservation("라텔", date, ten.getId(), theme.getId());

        Map<String, Object> params = new HashMap<>();
        params.put("date", date.toString());
        params.put("themeId", theme.getId());
        params.put("available", false);

        RestAssured.given().log().all()
                .queryParams(params)
                .when().get("/reservation-times/available")
                .then().log().all()
                .statusCode(200)
                .body("availableTimes.size()", is(1),
                        "availableTimes[0].startAt", equalTo(ten.getStartAt().toString()),
                        "availableTimes[0].available", is(false));
    }

    @Test
    void 존재하지_않는_테마의_예약_가능_시간을_조회하면_404를_반환한다() {
        dataInitializer.createReservationTime(LocalTime.of(10, 0));

        RestAssured.given().log().all()
                .queryParam("date", LocalDate.now().plusDays(1).toString())
                .queryParam("themeId", 999)
                .when().get("/reservation-times/available")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_404를_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/reservation-times/{id}", 999L)
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 예약이_존재하는_시간을_삭제하면_409를_반환한다() {
        ReservationTime time = dataInitializer.createReservationTime(LocalTime.of(10, 0));
        Theme theme = dataInitializer.createTheme("hello", "world", "/images/themes/hello.webp");
        LocalDate date = LocalDate.now().plusDays(1);
        dataInitializer.createReservation("라텔", date, time.getId(), theme.getId());

        RestAssured.given().log().all()
                .when().delete("/reservation-times/{id}", time.getId())
                .then().log().all()
                .statusCode(409);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026/05/20", "invalid-date"})
    void 예약_가능_시간_조회_시_날짜_형식이_잘못되면_400을_반환한다(String date) {
        Theme theme = dataInitializer.createTheme("hello", "world", "/images/themes/hello.webp");

        RestAssured.given().log().all()
                .queryParam("date", date)
                .queryParam("themeId", theme.getId())
                .when().get("/reservation-times/available")
                .then().log().all()
                .statusCode(400);
    }
}
