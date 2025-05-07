package roomescape.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    private static final String NAME = "테스트";
    private static final String DESCRIPTION = "테스트 설명";
    private static final String THUMBNAIL = "테스트 섬네일";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM reservation");
    }

    @Test
    @DisplayName("테마 목록을 조회한다")
    void readAllThemes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(11));
    }

    @Test
    @DisplayName("테마를 생성한다")
    void createTheme() {
        Map<String, Object> request = createThemeRequest();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", not(nullValue()),
                        "name", is(NAME),
                        "description", is(DESCRIPTION),
                        "thumbnail", is(THUMBNAIL));
    }

    @Test
    @DisplayName("테마를 삭제한다")
    void deleteTheme() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    private Map<String, Object> createThemeRequest() {
        Map<String, Object> theme = new HashMap<>();
        theme.put("name", NAME);
        theme.put("description", DESCRIPTION);
        theme.put("thumbnail", THUMBNAIL);
        return theme;
    }
}

