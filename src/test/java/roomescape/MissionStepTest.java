package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import roomescape.page.controller.AdminController;
import roomescape.reservation.domain.Reservation;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static roomescape.ReservationFixture.ADMIN_TOKEN;
import static roomescape.ReservationFixture.MEMBER_TOKEN;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdminController controller;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
    }

    public String createReservationTime() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation_time (start_at) VALUES (?)",
                    new String[]{"id"});
            ps.setString(1, "15:40");
            return ps;
        }, keyHolder);
        return keyHolder.getKey().toString();
    }

    public String createTheme() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, "이름");
            ps.setString(2, "설명");
            ps.setString(3, "썸네일");
            return ps;
        }, keyHolder);
        return keyHolder.getKey().toString();
    }

    @Test
    void 일단계() {
        RestAssured.given().log().all()
                .cookie("token", ADMIN_TOKEN)
                .when().get("/admin")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void 이단계() {
        RestAssured.given().log().all()
                .cookie("token", ADMIN_TOKEN)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(0));
    }

    @Test
    void 삼단계() {
        String reservationTimeId = createReservationTime();
        String themeId = createTheme();

        Map<String, String> params = Map.of(
                "name", "브라운",
                "date", "2024-08-05",
                "timeId", reservationTimeId,
                "themeId", themeId
        );

        String location = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", MEMBER_TOKEN)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .header("Location");

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(0));
    }

    @Test
    void 리다이렉트() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void 사단계() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("TESTDB");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 오단계() {
        String reservationTimeId = createReservationTime();
        String themeId = createTheme();
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "1", "2024-08-05", reservationTimeId, themeId);

        List<Reservation> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK).extract()
                .jsonPath().getList(".", Reservation.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    void 육단계() {
        String reservationTimeId = createReservationTime();
        String themeId = createTheme();

        Map<String, String> params = Map.of(
                "name", "1",
                "date", "2024-08-05",
                "timeId", reservationTimeId,
                "themeId", themeId
        );

        String location = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", MEMBER_TOKEN)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .header("Location");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    void 칠단계() {
        Map<String, String> params = Map.of(
                "startAt", "10:00"
        );

        String location = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .header("Location");

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void 팔단계() {
        String reservationTimeId = createReservationTime();
        String themeId = createTheme();

        Map<String, Object> reservation = Map.of(
                "name", "1",
                "date", "2024-08-05",
                "timeId", reservationTimeId,
                "themeId", themeId
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", MEMBER_TOKEN)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED);


        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(1));
    }

    @Test
    void 구단계() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : controller.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }
}
