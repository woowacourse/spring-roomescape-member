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
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(0)); // 아직 생성 요청이 없으니 0개
    }

    @Test
    void 예약_추가_및_삭제() {
        dataInitializer.createReservationTime(LocalTime.now());
        dataInitializer.createTheme("귀신의집", "무서워요", "/images/themes/reservation.webp");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
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

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(0));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "NULL, 2026-05-16",
            "'', 2026-05-16",
            "'   ', 2026-05-16",
            "123456789012345678901234567890123456789012345678901, 2026-05-16",
            "브라운, NULL"
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
        params.put("name", "브라운");
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
        params.put("name", "브라운");
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
        dataInitializer.createReservation("브라운", TODAY.plusDays(1), 1L, 1L);

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

    @ParameterizedTest
    @CsvSource({
            "0, 1, 200",
            "0, 0, 400",
            "0, 100, 200",
            "0, 101, 400",
            "-1, 20, 400"
    })
    void 예약_목록_페이징_조건의_경계값을_검증한다(int page, int size, int statusCode) {
        RestAssured.given().log().all()
                .queryParam("page", page)
                .queryParam("size", size)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(statusCode);
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
        params.put("name", "브라운");
        params.put("date", date.toString());
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params);
    }

}
