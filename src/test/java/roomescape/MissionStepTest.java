package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.controller.ReservationAdminController;
import roomescape.reservation.domain.Reservation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/admin/reservations")
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
        String reservationName = "브라운";
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:40");
        jdbcTemplate.update("INSERT INTO reservation_date (date) VALUES (?)", "2099-01-01");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)", "테마1", "테마1 설명",
                "테마1 썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES (?, ?, ?, ?, ?)",
                "브라운", "2099-01-01", "15:40", 1, "RESERVED");

        List<Reservation> reservations = RestAssured.given().log().all()
                .when().get("/member/reservations/" + reservationName)
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservations)
                .hasSize(count);
    }

    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/admin/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 예약과_시간_연결() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(200);

        Map<String, Object> theme = new HashMap<>();
        theme.put("name", "테마1");
        theme.put("description", "테마1 설명");
        theme.put("thumbnailUrl", "테마1 썸네일");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(200);

        Map<String, Object> date = new HashMap<>();
        date.put("date", "2099-01-01");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(date)
                .when().post("/admin/dates")
                .then().log().all()
                .statusCode(200);

        Map<String, Object> reservation = new HashMap<>();
        String reservationName = "브라운";
        reservation.put("name", reservationName);
        reservation.put("dateId", 1);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/member/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/member/reservations/" + reservationName)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Autowired
    private ReservationAdminController reservationAdminController;

    @Test
    void 계층화_리팩터링() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationAdminController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }
}
