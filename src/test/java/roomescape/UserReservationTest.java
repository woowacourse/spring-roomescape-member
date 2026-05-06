package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserReservationTest {

    @Test
    void 사용자_예약__API() {
        Map<String, String> themes = new HashMap<>();
        themes.put("name", "무서운 이야기");
        themes.put("description", "공포");
        themes.put("url", "http://example.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themes)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);

        Map<String, String> times = new HashMap<>();
        times.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(times)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201);

        Map<String, String> reservations = new HashMap<>();
        reservations.put("name", "브라운");
        reservations.put("date", "2025-05-04");
        reservations.put("timeId", "1");
        reservations.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservations)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

}
