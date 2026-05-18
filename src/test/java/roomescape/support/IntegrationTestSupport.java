package roomescape.support;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.sql.init.data-locations=",
                "spring.datasource.url=jdbc:h2:mem:integrationtestdb"
        })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class IntegrationTestSupport {

    @LocalServerPort
    private int port;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.port = port;
    }

    protected Long createTime(String startAt) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", startAt))
                .when().post("/admin/times")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    protected Long createTheme(String name) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", name,
                        "description", name + " 설명",
                        "thumbnailUrl", name + " 썸네일"
                ))
                .when().post("/admin/themes")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    protected Long createActiveTheme(String name) {
        Long themeId = createTheme(name);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("isActive", true))
                .when().patch("/admin/themes/{id}", themeId)
                .then()
                .statusCode(200);

        return themeId;
    }

    protected Long createReservation(String name, LocalDate date, Long timeId, Long themeId) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", name,
                        "date", date.toString(),
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    protected Long savePastReservation(String name, LocalDate date, String startAt, Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES (?, ?, ?, ?, ?)",
                name,
                date,
                startAt,
                themeId,
                "RESERVED"
        );

        return jdbcTemplate.queryForObject("SELECT MAX(id) FROM reservation", Long.class);
    }
}
