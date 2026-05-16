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
class AdminReservationApiTest extends ApiTestSupport {

    @Autowired
    private TestDataInitializer dataInitializer;

    @Test
    void 전체_예약_목록을_조회한다() {
        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(0));
    }

    @Test
    void 예약을_하드_삭제한다() {
        dataInitializer.createReservationTime(LocalTime.now());
        dataInitializer.createTheme("귀신의집", "무서워요", "/images/themes/reservation.webp");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "고래");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(0));
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
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(statusCode);
    }
}
