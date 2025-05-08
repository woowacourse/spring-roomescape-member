package roomescape.presentation.controller.rest;

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
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final LocalDate FORMATTED_MAX_LOCAL_DATE = LocalDate.of(9999, 12, 31);

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation CASCADE");
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme CASCADE");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time CASCADE");

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
                    name VARCHAR(255) NOT NULL,
                    date VARCHAR(255) NOT NULL,
                    time_id BIGINT,
                    theme_id BIGINT,
                    FOREIGN KEY (time_id) REFERENCES reservation_time(id),
                    FOREIGN KEY (theme_id) REFERENCES theme(id)
                );
                """);

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:10')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '소개1', '썸네일1')");
    }


    @DisplayName("방탈출 예약을 생성한다.")
    @Test
    void create() {
        final ReservationRequest request = new ReservationRequest(
                "hotteok", FORMATTED_MAX_LOCAL_DATE, 1L, 1L);

        final ReservationResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/reservations")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(ReservationResponse.class);

        assertAll(
                () -> assertThat(response.name()).isEqualTo("hotteok"),
                () -> assertThat(response.date()).isEqualTo(FORMATTED_MAX_LOCAL_DATE)
        );
    }

    @DisplayName("모든 방탈출 예약을 조회한다.")
    @Test
    void readAll() {
        jdbcTemplate.update(
                "INSERT INTO RESERVATION (name, date, time_id, theme_id) values ('hotteok', '2025-01-01', 1, 1)");
        jdbcTemplate.update(
                "INSERT INTO RESERVATION (name, date, time_id, theme_id) values ('saba', '2025-01-01', 1, 1)");

        given()
                .when()
                .get("/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("size()", equalTo(2))
                .body("name", contains("hotteok", "saba"));
    }

    @DisplayName("방탈출 예약을 삭제한다.")
    @Test
    void delete() {
        jdbcTemplate.update(
                "INSERT INTO RESERVATION (name, date, time_id, theme_id) values ('hotteok', '2025-01-01', 1, 1)");

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
                "INSERT INTO RESERVATION (name, date, time_id, theme_id) values ('hotteok', '2025-01-01', 1, 1)");

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
