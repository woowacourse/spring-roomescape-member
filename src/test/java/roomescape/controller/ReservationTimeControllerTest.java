package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.response.ReservationTimeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {

    @Test
    void 시간_조회() {
        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", "09:00");

        int reservationTimeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParams)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getInt("id");

        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "방탈출1");
        themeParams.put("description", "다함께 탈출해요 방탈출.");
        themeParams.put("thumbnail", "https://asdfsdf.sdfs");

        int themeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getInt("id");

        LocalDate date = LocalDate.of(2026, 5, 5);
        Map<String, Object> reservationParams = new HashMap<>();
        reservationParams.put("name", "러키");
        reservationParams.put("date", date);
        reservationParams.put("timeId", reservationTimeId);
        reservationParams.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue());

        List<ReservationTimeResponse> responses = RestAssured.given().log().all()
                .when().get("/times?themeId=" + themeId + "&date=" + date)
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReservationTimeResponse.class);

        assertThat(responses)
                .extracting("startAt", "isNotReserved")
                .contains(
                        tuple(LocalTime.of(9, 0), false)
                );
    }
}
