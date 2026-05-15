package roomescape.api;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.util.ApiTestSupport;
import roomescape.util.TestDataInitializer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationApiTest extends ApiTestSupport {

    private static final LocalDate TODAY = LocalDate.now();

    @Autowired
    private TestDataInitializer dataInitializer;

    @Test
    void 사용자는_이름으로_본인의_예약_목록을_조회할_수_있다() {
        dataInitializer.createReservationTime(LocalTime.of(10, 0));
        dataInitializer.createReservationTime(LocalTime.of(11, 0));
        dataInitializer.createTheme("귀신의집", "무서워요", "/images/themes/reservation.webp");
        dataInitializer.createReservation("고래", TODAY.plusDays(1), 1L, 1L);
        dataInitializer.createReservation("라텔", TODAY.plusDays(1), 2L, 1L);

        RestAssured.given().log().all()
                .queryParam("name", "고래")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(1))
                .body("reservations[0].name", is("고래"));
    }

    @Test
    void 예약을_생성한다() {
        dataInitializer.createReservationTime(LocalTime.now());
        dataInitializer.createTheme("귀신의집", "무서워요", "/images/themes/reservation.webp");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "고래");
        params.put("date", TODAY.plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

    }

    @ParameterizedTest
    @CsvSource(value = {
            "NULL, 2026-05-16",
            "'', 2026-05-16",
            "'   ', 2026-05-16",
            "123456789012345678901234567890123456789012345678901, 2026-05-16",
            "고래, NULL"
    }, nullValues = "NULL")
    void 예약_생성_요청값이_유효하지_않으면_400을_반환한다(String name, String date) {
        dataInitializer.createReservationTime(LocalTime.of(10, 0));
        dataInitializer.createTheme("귀신의집", "무서워요", "/images/themes/reservation.webp");

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", date);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 존재하지_않는_예약_시간으로_예약을_생성하면_404를_반환한다() {
        dataInitializer.createTheme("귀신의집", "무서워요", "/images/themes/reservation.webp");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "고래");
        params.put("date", TODAY.plusDays(1).toString());
        params.put("timeId", 999);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 존재하지_않는_테마로_예약을_생성하면_404를_반환한다() {
        dataInitializer.createReservationTime(LocalTime.of(10, 0));

        Map<String, Object> params = new HashMap<>();
        params.put("name", "고래");
        params.put("date", TODAY.plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 999);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 같은_날짜_시간_테마로_중복_예약하면_409를_반환한다() {
        dataInitializer.createReservationTime(LocalTime.of(10, 0));
        dataInitializer.createTheme("귀신의집", "무서워요", "/images/themes/reservation.webp");
        dataInitializer.createReservation("고래", TODAY.plusDays(1), 1L, 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "라텔");
        params.put("date", TODAY.plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void 사용자는_본인_예약의_날짜와_시간을_변경할_수_있다() {
        dataInitializer.createReservationTime(LocalTime.of(10, 0));
        dataInitializer.createReservationTime(LocalTime.of(11, 0));
        dataInitializer.createTheme("귀신의집", "무서워요", "/images/themes/reservation.webp");
        dataInitializer.createReservation("고래", TODAY.plusDays(1), 1L, 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "고래");
        params.put("date", TODAY.plusDays(2).toString());
        params.put("timeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/reservations/1/schedule")
                .then().log().all()
                .statusCode(200)
                .body("date", is(TODAY.plusDays(2).toString()))
                .body("time.id", is(2))
                .body("status", is("RESERVED"));
    }

    @Test
    void 사용자는_본인_예약을_취소할_수_있다() {
        dataInitializer.createReservationTime(LocalTime.of(10, 0));
        dataInitializer.createTheme("귀신의집", "무서워요", "/images/themes/reservation.webp");
        dataInitializer.createReservation("고래", TODAY.plusDays(1), 1L, 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "고래");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/reservations/1/cancellation")
                .then().log().all()
                .statusCode(200)
                .body("status", is("CANCELLED"));
    }

    @Test
    void 지나간_날짜와_시간으로_예약하면_400을_반환한다() {
        createReservationPrerequisites(LocalTime.of(15, 0));

        createReservationRequest(TODAY.minusDays(1), 1L, 1L)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 예약_날짜가_오늘이고_현재_서버_시간_이전의_예약_시간이면_400을_반환한다() {
        createReservationPrerequisites(LocalTime.MIN);

        createReservationRequest(TODAY, 1L, 1L)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 오늘_기준_30일을_초과한_날짜로_예약하면_400을_반환한다() {
        createReservationPrerequisites(LocalTime.of(15, 0));

        createReservationRequest(TODAY.plusDays(31), 1L, 1L)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    private void createReservationPrerequisites(LocalTime startAt) {
        dataInitializer.createReservationTime(startAt);
        dataInitializer.createTheme("귀신의집", "무서워요", "/images/themes/reservation.webp");
    }

    private io.restassured.specification.RequestSpecification createReservationRequest(
            LocalDate date,
            Long timeId,
            Long themeId
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "고래");
        params.put("date", date.toString());
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params);
    }

}
