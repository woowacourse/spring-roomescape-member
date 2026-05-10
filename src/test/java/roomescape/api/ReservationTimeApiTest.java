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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.util.TestDataInitializer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeApiTest {

    @Autowired
    private TestDataInitializer dataInitializer;

    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/times/1")
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
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 예약과_시간_연결() {
        dataInitializer.initializeReservationTime(LocalTime.now());
        dataInitializer.initializeTheme("hello", "world", "/resources/image/...");

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 예약_가능_시간_조회() {
        // given
        ReservationTime ten = dataInitializer.initializeReservationTime(LocalTime.of(10, 0));
        ReservationTime eleven = dataInitializer.initializeReservationTime(LocalTime.of(11, 0));
        Theme theme = dataInitializer.initializeTheme("hello", "world", "/resources/image/...");

        LocalDate date = LocalDate.now().plusDays(1);
        dataInitializer.initializeReservation("라텔", date, ten.getId(), theme.getId());


        Map<String, Object> params = new HashMap<>();
        params.put("date", date.toString());
        params.put("timeId", ten.getId());
        params.put("themeId", theme.getId());
        params.put("available", true);

        // when
        RestAssured.given().log().all()
                .queryParams(params)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("availableTimes.size()", is(1),
                        "availableTimes[0].startAt", equalTo(eleven.getStartAt().toString()));
    }

}
