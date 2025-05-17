package roomescape.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.http.ContentType;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.presentation.dto.playtime.PlayTimeRequest;
import roomescape.presentation.dto.playtime.PlayTimeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class PlayTimeIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time CASCADE");
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS reservation_time
                (
                    id SERIAL,
                    start_at VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                );
                """);
    }

    @DisplayName("방탈출 시간를 생성한다.")
    @Test
    void create() {
        final PlayTimeRequest request = new PlayTimeRequest(LocalTime.of(10, 10));

        final PlayTimeResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/times")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(PlayTimeResponse.class);

        assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 10));
    }

    @DisplayName("모든 방탈출 시간을 조회한다.")
    @Test
    void readAll() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:10')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:20')");

        given()
                .when()
                .get("/times")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("size()", equalTo(2))
                .body("startAt", contains("10:10:00", "10:20:00"));
    }

    @DisplayName("방탈출 시간를 삭제한다.")
    @Test
    void delete() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:10')");

        given()
                .when()
                .delete("/times/{id}", 1)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
