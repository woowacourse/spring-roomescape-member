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
import roomescape.persistence.entity.ReservationEntity;
import roomescape.presentation.dto.theme.ThemeRequest;
import roomescape.presentation.dto.theme.ThemeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme CASCADE");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation CASCADE");

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS theme
                (
                    id SERIAL,
                    name        VARCHAR(255) NOT NULL,
                    description VARCHAR(255) NOT NULL,
                    thumbnail VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                );
                """);
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS reservation
                (
                    id SERIAL,
                    name VARCHAR(255) NOT NULL,
                    date VARCHAR(255) NOT NULL,
                    time_id BIGINT,
                    theme_id BIGINT,
                    PRIMARY KEY (id)
                );
                """);
    }

    @DisplayName("테마를 생성한다.")
    @Test
    void create() {
        final ThemeRequest request = new ThemeRequest("테마", "소개", "썸네일");

        final ThemeResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/themes")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(ThemeResponse.class);

        assertAll(
                () -> assertThat(response.name()).isEqualTo("테마"),
                () -> assertThat(response.description()).isEqualTo("소개"),
                () -> assertThat(response.thumbnail()).isEqualTo("썸네일")
        );
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void readAll() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '소개1', '썸네일1')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마2', '소개2', '썸네일2')");

        given()
                .when()
                .get("/themes")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("size()", equalTo(2))
                .body("name", contains("테마1", "테마2"));
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void delete() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '소개1', '썸네일1')");

        given()
                .when()
                .delete("/themes/{id}", 1)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("인기 테마를 조회한다.")
    @Test
    void readPopularThemes() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '소개1', '썸네일1')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마2', '소개2', '썸네일2')");

        final String formattedYesterdayDate = ReservationEntity.formatDate(LocalDate.now().minusDays(1));
        jdbcTemplate.update("INSERT INTO RESERVATION (name, date, time_id, theme_id) values ('hotteok', ?, 1, 2)",
                formattedYesterdayDate);

        given()
                .when()
                .get("/themes/popular")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("name[0]", equalTo("테마2"))
                .body("name[1]", equalTo("테마1"));
    }
}
