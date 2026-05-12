package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.ReservationController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationController reservationController;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("DELETE FROM reservation;");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;");
    }

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
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2099-08-05");
        params.put("timeId", "1");
        params.put("themeId", "1");

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

    @Test
    void 데이터베이스_연동() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void DB_조회_API_전환() {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "브라운",
                "2023-08-05", 1, 1);

        int size = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".").size();

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(size).isEqualTo(count);
    }

    @Test
    void DB_추가_삭제_API_전환() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2099-08-05");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    void 시간_관리_API() {
        jdbcTemplate.update("DELETE FROM reservation_time;");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1;");
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
    void 예약과_시간_연결() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2099-08-05");
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
    void 계층화_리팩터링() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    @Test
    void 테마_관리_API() {
        jdbcTemplate.update("DELETE FROM theme;");
        jdbcTemplate.update("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1;");
        Map<String, String> params = new HashMap<>();
        params.put("name", "테마 이름");
        params.put("description", "테마 설명");
        params.put("thumbnail", "썸네일 주소");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/themes/1/times?date=2026-05-05")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/themes/weekly-top")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void View컨트롤러_테스트() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }


    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void 이름이_빈_경우_400_에러_발생(String input) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", input);
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 날짜가_Null인_경우_400_에러_발생() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", null);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void timeId가_Null인_경우_400_에러_발생() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", null);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void themeId가_Null인_경우_400_에러_발생() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 존재하지_않는_예약_삭제_시_404_에러_발생() {
        RestAssured.given().log().all()
                .when().delete("/reservations/999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 중복_예약_시_409_에러_발생() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2099-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void 존재하지_않는_timeId로_예약_시_404_에러_발생() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2099-08-05");
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
    void 존재하지_않는_themeId로_예약_시_404_에러_발생() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2099-08-05");
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
    void 지난_시간으로의_예약_시_422_에러_발생() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2000-08-05");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422);
    }

    @Test
    void 날짜_형식_오류_400_에러_발생() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2099-081-05");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 예약이_존재하는_시간_삭제_시_409_에러_발생() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2099-08-05");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void 존재하지_않는_시간_삭제_시_404_에러_발생() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 중복된_시간_추가_시_409_에러_발생() {
        jdbcTemplate.update("DELETE FROM reservation_time;");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1;");
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
                .statusCode(409);
    }

    @Test
    void 예약이_존재하는_테마_삭제_시_409_에러_발생() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2099-08-05");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void 존재하지_않는_테마_삭제_시_404_에러_발생() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 중복된_테마_추가_시_409_에러_발생() {
        jdbcTemplate.update("DELETE FROM theme;");
        jdbcTemplate.update("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1;");
        Map<String, String> params = new HashMap<>();
        params.put("name", "테마 이름");
        params.put("description", "테마 설명");
        params.put("thumbnail", "썸네일 주소");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(409);

    }
}
