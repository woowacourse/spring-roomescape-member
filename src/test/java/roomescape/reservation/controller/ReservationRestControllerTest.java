package roomescape.reservation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.controller.dto.AvailableReservationTimeResponse;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationRestControllerTest {

    @Autowired
    private ReservationRestController reservationRestController;

    @BeforeEach
    void setUp() {
        final String TIME1 = "10:00";
        final String TIME2 = "11:00";
        final List<String> times = List.of(TIME1, TIME2);

        for (String time : times) {
            final Map<String, String> params = new HashMap<>();
            params.put("startAt", time);
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/times")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());
        }

        final Map<String, String> params = new HashMap<>();
        params.put("name", "우가우가");
        params.put("description", "우가우가 설명");
        params.put("thumbnail", "따봉우가.jpg");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 요청_형식이_맞지_않아_예약_정보_저장에_실패하는_경우_bad_request를_반환한다() {
        final Map<String, String> params
                = createReservationRequestJsonMap("헤일러", "2025 04 15", "10 00", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 예약_정보를_저장한다() {
        final Map<String, String> params
                = createReservationRequestJsonMap("헤일러", "2023-08-05", "1", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 예약_정보를_삭제한다() {
        final Map<String, String> params
                = createReservationRequestJsonMap("포스티", "2023-08-05", "1", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 삭제할_예약_정보가_없는_경우_not_found를_반환한다() {

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void 예약_정보_목록을_조회한다() {
        final Map<String, String> params
                = createReservationRequestJsonMap("헤일러", "2023-08-05", "1", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }

    @Test
    void 예약_가능한_시간_목록을_조회한다() {
        final Map<String, String> params
                = createReservationRequestJsonMap("헤일러", "2023-08-05", "1", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        final Map<String, String> availableParams = new HashMap<>();
        availableParams.put("date", "2023-08-05");
        availableParams.put("themeId", "1");

        final List<AvailableReservationTimeResponse> availableReservationTimeResponses = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(availableParams)
                .when().post("/reservations/available-times")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath()
                .getList(".", AvailableReservationTimeResponse.class);

        final long count = availableReservationTimeResponses.stream()
                .filter(AvailableReservationTimeResponse::alreadyBooked)
                .count();

        assertThat(count).isEqualTo(1);
    }

    @Test
    void 컨트롤러는_JdbcTemplate_타입의_필드를_갖고_있지_않다() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationRestController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    private Map<String, String> createReservationRequestJsonMap(String name, String date, String timeId,
                                                                String themeId) {
        return Map.ofEntries(
                Map.entry("name", name),
                Map.entry("date", date),
                Map.entry("timeId", timeId),
                Map.entry("themeId", themeId)
        );
    }
}
