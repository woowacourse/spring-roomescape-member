package roomescape.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.business.service.AuthService;
import roomescape.presentation.dto.LoginRequest;
import roomescape.presentation.dto.reservation.ReservationRequest;
import roomescape.presentation.dto.reservation.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationIntegrationTest {

    private static final LocalDate FORMATTED_MAX_LOCAL_DATE = LocalDate.of(9999, 12, 31);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthService authService;

    private String userToken;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation CASCADE");
        jdbcTemplate.execute("DROP TABLE IF EXISTS users CASCADE");
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme CASCADE");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time CASCADE");

        jdbcTemplate.execute("""
                CREATE TABLE users
                  (
                      id   SERIAL,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      role VARCHAR(255) NOT NULL,
                      PRIMARY KEY (id)
                  );
                """);

        jdbcTemplate.execute("""
                CREATE TABLE reservation_time (
                    id SERIAL PRIMARY KEY,
                    start_at VARCHAR(255) NOT NULL
                );
                """);

        jdbcTemplate.execute("""
                CREATE TABLE theme (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    description VARCHAR(255) NOT NULL,
                    thumbnail VARCHAR(255) NOT NULL
                );
                """);

        jdbcTemplate.execute("""
                CREATE TABLE reservation (
                    id SERIAL PRIMARY KEY,
                    user_id BIGINT,
                    date VARCHAR(255) NOT NULL,
                    time_id BIGINT,
                    theme_id BIGINT,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (time_id) REFERENCES reservation_time(id),
                    FOREIGN KEY (theme_id) REFERENCES theme(id)
                );
                """);

        jdbcTemplate.update("INSERT INTO USERS (name, email, password, role) values ('hotteok', 'hoho', 'qwe123', 'USER')");
        jdbcTemplate.update("INSERT INTO USERS (name, email, password, role) values ('saba', 'saba', 'qwe123', 'USER')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:10')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '소개1', '썸네일1')");

        userToken = authService.createToken(new LoginRequest("hoho", "qwe123"));
    }

    @DisplayName("방탈출 예약을 생성한다.")
    @Test
    void create() {
        final ReservationRequest request = new ReservationRequest(
                FORMATTED_MAX_LOCAL_DATE, 1L, 1L);

        final ReservationResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .cookie("token", userToken)
                        .when()
                        .post("/reservations")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(ReservationResponse.class);

        assertAll(
                () -> assertThat(response.user().name()).isEqualTo("hotteok"),
                () -> assertThat(response.date()).isEqualTo(FORMATTED_MAX_LOCAL_DATE)
        );
    }

    @DisplayName("모든 방탈출 예약을 조회한다.")
    @Test
    void readAll() {
        jdbcTemplate.update(
                "INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (1, '2025-01-01', 1, 1)");
        jdbcTemplate.update(
                "INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (2, '2025-01-01', 1, 1)");

        given()
                .when()
                .get("/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("size()", equalTo(2))
                .body("user.name", contains("hotteok", "saba"));
    }

    @DisplayName("방탈출 예약을 삭제한다.")
    @Test
    void delete() {
        jdbcTemplate.update(
                "INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (1, '2025-01-01', 1, 1)");

        given()
                .when()
                .delete("/reservations/{id}", 1)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("해당 날짜와 테마의 방탈출 시간을 예약 여부와 함께 조회한다.")
    @Test
    void readAvailableTimes() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:20')");

        jdbcTemplate.update(
                "INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (1, '2025-01-01', 1, 1)");

        given()
                .queryParam("date", "2025-01-01")
                .queryParam("themeId", 1)
                .when()
                .get("/reservations/available-times")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("[0].startAt", equalTo("10:10"))
                .body("[0].alreadyBooked", equalTo(true))
                .body("[1].startAt", equalTo("10:20"))
                .body("[1].alreadyBooked", equalTo(false));
    }
}
