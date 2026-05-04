package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.controller.ReservationController;
import roomescape.reservation.dto.ResponseReservation;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
    }

    @Test
    void 예약이_없을_때_예약_목록을_조회하면_빈_목록을_반환한다() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 예약을_생성한_뒤_삭제하면_예약_목록이_비어있다() {
        Long timeId = createTime("10:00");
        Long themeId = createTheme();

        Map<String, String> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", String.valueOf(timeId));
        reservation.put("themeId", String.valueOf(themeId));

        Long reservationId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");
        assertThat(reservationId).isPositive();

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 존재하지_않는_시간_id로_예약을_생성하면_400을_응답한다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 999);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 애플리케이션이_H2_데이터베이스와_연결되고_예약_테이블이_생성된다() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void DB에_저장된_예약을_API로_조회하면_DB_건수와_응답_건수가_같다() {
        Long timeId = createTime("10:00");
        Long themeId = createTheme();

        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "브라운", "2023-08-05", timeId, themeId);

        List<ResponseReservation> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ResponseReservation.class);
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    void 예약_생성과_삭제_API를_호출하면_DB_예약_건수가_변경된다() {
        Long timeId = createTime("10:00");
        Long themeId = createTheme();

        Map<String, String> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", String.valueOf(timeId));
        reservation.put("themeId", String.valueOf(themeId));


        Long reservationId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(200);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    void 시간_생성_조회_삭제_API를_순서대로_호출하면_정상_응답한다() {
        Long timeId = createTime("10:00");

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/admin/times/" + timeId)
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 시간_id로_예약을_생성하면_예약_목록에서_조회된다() {
        Long timeId = createTime("10:00");
        Long themeId = createTheme();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 예약_컨트롤러는_JdbcTemplate에_직접_의존하지_않는다() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }
        assertThat(isJdbcTemplateInjected).isFalse();
    }

    private Long createTime(String startAt) {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", startAt);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");
    }

    private Long createTheme() {
        Map<String, String> theme = new HashMap<>();
        theme.put("name", "공포방");
        theme.put("description", "공포도 5");
        theme.put("thumbnail", "image-url");

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");
    }
}
