package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.core.dto.ReservationResponseDto;
import roomescape.web.controller.ReservationController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class MissionStepTest {
    private static final String TOMORROW_DATE = LocalDate.now()
            .plusDays(1)
            .format(DateTimeFormatter.ISO_DATE);

    private String loginTokenCookie;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        final Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        final Map<String, Object> theme = new HashMap<>();
        theme.put("name", "우테코 레벨2");
        theme.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        theme.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        final Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "hong@gmail.com");
        loginParams.put("password", "1234");

        loginTokenCookie = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(loginParams)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];
    }

    @Test
    void 일단계() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(findLastIdOfReservation()));
    }

    @Test
    void 이단계() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(findLastIdOfReservation()));
    }

    @Test
    void 삼단계() {
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW_DATE);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .header("Cookie", loginTokenCookie)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(findLastIdOfReservation()));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(findLastIdOfReservation()));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(countReservation()));
    }

    @Test
    void 사단계() {
        try (final Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(
                    connection.getMetaData()
                            .getTables(null, null, "RESERVATION", null)
                            .next()
            ).isTrue();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 오단계() {
        final List<ReservationResponseDto> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponseDto.class);

        final int actual = countReservation();
        assertThat(reservations).hasSize(actual);
    }

    @Test
    void 육단계() {
        final Map<String, Object> params = new HashMap<>();
        params.put("date", TOMORROW_DATE);
        params.put("timeId", 1);
        params.put("themeId", 1);

        final int reservationSize = countReservation();

        RestAssured.given().log().all()
                .header("Cookie", loginTokenCookie)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/" + findLastIdOfReservation());

        assertThat(countReservation()).isEqualTo(reservationSize + 1);

        RestAssured.given().log().all()
                .when().delete("/reservations/" + findLastIdOfReservation())
                .then().log().all()
                .statusCode(204);

        assertThat(countReservation()).isEqualTo(reservationSize);
    }

    @Test
    void 칠단계() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(countReservationTime()));

        RestAssured.given().log().all()
                .when().delete("/times/" + findLastIdOfReservationTime())
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 팔단계() {
        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", TOMORROW_DATE);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .header("Cookie", loginTokenCookie)
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(countReservation()));
    }

    @Test
    void 구단계() {
        boolean isJdbcTemplateInjected = false;

        for (final Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    private int findLastIdOfReservation() {
        return jdbcTemplate.queryForObject("SELECT max(id) FROM reservation", Integer.class);
    }

    private int findLastIdOfReservationTime() {
        return jdbcTemplate.queryForObject("SELECT max(id) FROM reservation_time", Integer.class);
    }

    private int countReservation() {
        return jdbcTemplate.queryForObject("SELECT count(1) FROM reservation", Integer.class);
    }

    private int countReservationTime() {
        return jdbcTemplate.queryForObject("SELECT count(1) FROM reservation_time", Integer.class);
    }
}
