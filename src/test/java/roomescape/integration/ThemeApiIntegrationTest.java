package roomescape.integration;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeApiIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpData() {
        String memberSetUp = "insert into member (name, email, password, role) values ('moda', 'moda_email', 'moda_password', 'ADMIN')";
        String themeSetUp = "insert into theme (name, description, thumbnail) values ('theme_name', 'theme_description', 'theme_thumbnail')";
        jdbcTemplate.update(memberSetUp);
        jdbcTemplate.update(themeSetUp);
    }

    @Test
    @DisplayName("사용자가 전체 테마 데이터를 조회한다.")
    void readAllTheme() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("인기 테마 데이터를 조회한다.")
    void readThemeRank() {
        RestAssured.given().log().all()
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
