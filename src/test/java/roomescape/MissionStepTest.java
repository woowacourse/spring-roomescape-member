package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.auth.dto.CheckLoginResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.member.dto.SignupRequest;
import roomescape.reservation.controller.ReservationController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.sql.init.schema-locations=classpath:schema.sql",
        "spring.sql.init.data-locations="
})
public class MissionStepTest {

    private static final String NAME = "admin";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "admin@gmail.com";
    private static final String futureDate = LocalDate.now().plusDays(1).toString();

    @Nested
    class AdminTest {

        @Autowired
        private JdbcTemplate jdbcTemplate;
        @Autowired
        private ReservationController reservationController;

        @Test
        void step1_accessAdminPage() {
            RestAssured.given().log().all()
                    .when().get("/admin")
                    .then().log().all()
                    .statusCode(200);
        }

        @Test
        void step2_accessAdminReservationPage() {
            RestAssured.given().log().all()
                    .when().get("/admin/reservation")
                    .then().log().all()
                    .statusCode(200);
        }

        @Test
        void step3_createAndDeleteReservation() {
            createReservationTime();
            createTheme("추리");
            createReservation();

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
        void step4_applyDatabase() {
            try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
                assertThat(connection).isNotNull();
                assertThat(connection.getCatalog()).isEqualTo("TEST");
                assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        void step5_getReservationWithDatabase() {
            createReservationTime();

            jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "브라운", futureDate,
                    "1");

            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200);

            Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
            assertThat(count).isEqualTo(1);
        }

        @Test
        void step6_addReservationWithDatabase() {
            createReservationTime();
            createTheme("추리");
            createReservation();

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
        void step7_timeAPIFeature() {
            createReservationTime();

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
        void step8_schemaModification() {
            createReservationTime();
            createTheme("추리");
            createReservation();

            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1));
        }

        @Test
        void step9_controllerDoesNotHasDatabaseLogic() {
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

    @Nested
    class UserTest {

        @Test
        void step1_exceptionHandle() {
            createReservationTime();
            createTheme("추리");
            createReservation();

            RestAssured.given().log().all()
                    .when().delete("/times/1")
                    .then().log().all()
                    .statusCode(409);
        }

        @Test
        void step2_createAndDeleteTheme() {
            createTheme("추리");
            findThemesBySize(1);

            RestAssured.given().log().all()
                    .when().delete("/themes/1")
                    .then().log().all()
                    .statusCode(204);
            findThemesBySize(0);
        }

        @Test
        void step3_findAvailableReservations() {
            createReservationTime();
            createTheme("추리");

            LocalDate now = LocalDate.now();
            RestAssured.given().log().all()
                    .when().queryParams("date", now.toString(), "themeId", 1L).get("/times/available")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1));
        }

        @Test
        void step3_findPopularTheme() {
            createReservationTime();
            createTheme("추리1");
            createTheme("추리2");
            createTheme("추리3");
            createTheme("추리4");
            createTheme("추리5");
            createTheme("추리6");
            createTheme("추리7");
            createTheme("추리8");
            createTheme("추리9");
            createTheme("추리10");
            createTheme("추리11");
            createTheme("추리12");
            findThemesBySize(12);

            RestAssured.given().log().all()
                    .when().get("/themes/popular")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(10));
        }

        @Test
        void step4_signup() {
            RestAssured.given().log().all()
                    .body(new SignupRequest(EMAIL, PASSWORD, NAME))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/members")
                    .then().log().all()
                    .statusCode(201);
        }

        @Test
        void step4_login() {
            step4_signup();

            RestAssured.given().log().all()
                    .body(new LoginRequest(EMAIL, PASSWORD))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/login")
                    .then().log().all()
                    .statusCode(200);
        }


        @Test
        void step4_logout() {
            step4_signup();
            String authToken = loginAndGetAuthToken();

            RestAssured.given().log().all()
                    .body(new LoginRequest(EMAIL, PASSWORD))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .cookie("token", authToken)
                    .when().post("/logout")
                    .then().log().all()
                    .statusCode(204);
        }

        @Test
        void step4_loginCheck() {
            step4_signup();

            String authToken = loginAndGetAuthToken();

            CheckLoginResponse checkLoginResponse = RestAssured.given().log().all()
                    .body(new LoginRequest(EMAIL, PASSWORD))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .cookie("token", authToken)
                    .when().get("/login/check")
                    .then().log().all()
                    .statusCode(200)
                    .extract()
                    .as(CheckLoginResponse.class);

            assertThat(checkLoginResponse.name()).isEqualTo("admin");
        }

    }

    private void createReservation() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", futureDate);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    private void createTheme(final String name) {
        Map<String, String> theme = new HashMap<>();
        theme.put("name", name);
        theme.put("description", "셜록 with Danny");
        theme.put("thumbnail", "image.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    private void createReservationTime() {
        Map<String, String> reservationTime = new HashMap<>();
        reservationTime.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    private void findThemesBySize(final int size) {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(size));
    }

    private String loginAndGetAuthToken() {
        return RestAssured.given().log().all()
                .body(new LoginRequest(EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .cookie("token");
    }
}
