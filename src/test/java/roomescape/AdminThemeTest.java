package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminThemeTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    void 테마_추가() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "테마명");
        params.put("description", "테스트 테마입니다.");
        params.put("thumbnailImageUrl", "http://www.test.com/testImageUrl");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 테마_삭제() {
        insertTheme();

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 테마_DB_추가() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "테마명");
        params.put("description", "테스트 테마입니다.");
        params.put("thumbnailImageUrl", "http://www.test.com/testImageUrl");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void 테마_DB_삭제() {
        insertTheme();

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(204);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);
        assertThat(count).isEqualTo(0);
    }


    private void insertTheme() {
        jdbcTemplate.execute(
                "INSERT INTO theme(name, description, thumbnail_image_url) VALUES ('테마명', '테마설명', 'https://thumbnail.url')");
    }
}
