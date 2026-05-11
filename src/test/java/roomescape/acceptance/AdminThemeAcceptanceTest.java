package roomescape.acceptance;

import static org.hamcrest.Matchers.matchesPattern;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminThemeAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void POST_admin_themes_테마를_생성한다() {
        Map<String, Object> body = Map.of(
                "name", "공포",
                "description", "무서움",
                "thumbnailImageUrl", "https://thumbnail.url");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", matchesPattern("/themes/\\d+"));
    }

    @Test
    void DELETE_admin_themes_id_테마를_삭제한다() {
        jdbcTemplate.update(
                "INSERT INTO theme(id, name, description, thumbnail_image_url) VALUES (1, '공포', '무서움', 'https://thumbnail.url')");

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(200);
    }
}