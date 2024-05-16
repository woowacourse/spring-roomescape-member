package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.servlet.http.Cookie;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.ReservationApiController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/truncate.sql")
public class MissionStepTest {

    private static final String ADMIN_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbi1pZCIsIm5hbWUiOiJBZG1pbiIsInJvbGUiOiJhZG1pbiJ9.rwrMXggxF2IBybjj7M8kC4XBciVOd8LGChohMzS4T_1RJ1BagnTcf-0r0OLqbZeUBT3OdRHcCtYqDmLOzr80AA";
    @LocalServerPort
    private int port;

    @BeforeEach
    public void init() {
        RestAssured.port = port;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationApiController reservationApiController;

    @DisplayName("Admin HTML 호출 테스트")
    @Test
    void admin_html_test() {
        Cookie cookie = makeCookie(ADMIN_TOKEN);

        RestAssured.given().log().all()
                .cookie("token", cookie.getValue())
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .cookie("token", cookie.getValue())
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .cookie("token", cookie.getValue())
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("Home HTML 호출 테스트")
    @Test
    void Home_html_test() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("테이터베이스 연결 테스트")
    @Test
    void database_connect_test() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("예약 시간에 대한 CRD 테스트")
    @Test
    void reservation_time_create_read_update_test() {
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

    @DisplayName("예약에 대한 CRD 테스트")
    @Test
    void reservation_create_read_update_test() {
        initializeTimesData();
        initializeThemeData();

        Cookie cookie = makeCookie(ADMIN_TOKEN);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("memberId", "1");
        reservation.put("date", "2100-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", cookie.getValue())
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    private void initializeThemeData() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "이름");
        params.put("description", "설명");
        params.put("thumbnail", "썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    private static void initializeTimesData() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("컨트롤러에서 영속 계층 의존성이 제거되었는지 테스트")
    @Test
    void layered_architecture_controller_test() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationApiController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    private jakarta.servlet.http.Cookie makeCookie(String token) {
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }
}
