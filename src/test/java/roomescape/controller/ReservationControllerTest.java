package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.response.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ReservationController reservationController;

    Map<String, Object> reservations;

    @BeforeEach
    void setUp() {
        // time
        Map<String, String> times = new HashMap<>();
        times.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(times)
                .when().post("/times");

        // theme
        Map<String, String> themes = new HashMap<>();
        themes.put("name", "테마 이름");
        themes.put("description", "테마 설명");
        themes.put("thumbnail", "테마 썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themes)
                .when().post("/themes");

        // reservation
        reservations = new HashMap<>();
        reservations.put("name", "브라운");
        reservations.put("date", LocalDate.now().plusYears(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
        reservations.put("timeId", 1);
        reservations.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservations)
                .when().post("/reservations")
                .then().statusCode(201);
    }

    @Test
    void getReservationsTest() {
        // when & then
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(reservations).hasSize(count);
    }

    @Test
    void saveReservationTest() {
        // when & then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void deleteReservationTest() {
        // when
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);

        // then
        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isZero();
    }

    @Test
    void doesNotHaveJdbcTemplate() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }
}
