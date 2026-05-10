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
import roomescape.util.TestDataInitializer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationApiTest {

    @Autowired
    private TestDataInitializer dataInitializer;

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0)); // 아직 생성 요청이 없으니 0개
    }

    @Test
    void 예약_추가_및_삭제() {
        dataInitializer.createReservationTime(LocalTime.now());
        dataInitializer.createTheme("귀신의집", "무서워요", "/resources/image/...");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
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
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "NULL, 2023-08-05",
            "'', 2023-08-05",
            "'   ', 2023-08-05",
            "123456789012345678901234567890123456789012345678901, 2023-08-05",
            "브라운, NULL"
    }, nullValues = "NULL")
    void 예약_생성_요청값이_유효하지_않으면_400을_반환한다(String name, String date) {
        dataInitializer.createReservationTime(LocalTime.of(10, 0));
        dataInitializer.createTheme("귀신의집", "무서워요", "/resources/image/...");

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
        dataInitializer.createTheme("귀신의집", "무서워요", "/resources/image/...");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
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
        params.put("date", "2023-08-05");
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
        dataInitializer.createTheme("귀신의집", "무서워요", "/resources/image/...");
        dataInitializer.createReservation("브라운", LocalDate.of(2023, 8, 5), 1L, 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "라텔");
        params.put("date", "2023-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
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
}
