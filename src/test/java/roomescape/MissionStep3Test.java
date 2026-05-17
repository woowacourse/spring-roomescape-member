package roomescape;

import static org.hamcrest.core.Is.is;
import static roomescape.config.TestFixture.reservationRequestBody;
import static roomescape.config.TestFixture.reservationTimeRequestBody;
import static roomescape.config.TestFixture.themeRequestBody;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStep3Test {

    private static final String ADMIN_TIME_START_AT = "10:00";
    private static final String RESERVATION_TIME_START_AT = "12:00";
    private static final String THEME_NAME = "테마A";
    private static final String THEME_DESCRIPTION = "테마A란...";
    private static final String THEME_THUMBNAIL_URL = "https://example.com/themes/theme-1.png";
    private static final String RESERVATION_NAME = "브라운";
    private static final LocalDate RESERVATION_DATE = LocalDate.of(2026, 5, 10);

    @Test
    void 시간_관리_API() {
        Map<String, Object> params = reservationTimeRequestBody(ADMIN_TIME_START_AT);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 예약과_시간_연결() {
        Map<String, Object> reservationTime = reservationTimeRequestBody(RESERVATION_TIME_START_AT);

        Integer reservationTimeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        Map<String, Object> theme = themeRequestBody(THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL_URL);

        Integer themeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        Map<String, Object> reservation = reservationRequestBody(
                RESERVATION_NAME,
                RESERVATION_DATE,
                reservationTimeId.longValue(),
                themeId.longValue()
        );

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
                .body("size()", is(1));
    }

}
