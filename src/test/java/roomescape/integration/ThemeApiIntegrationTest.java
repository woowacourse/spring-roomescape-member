package roomescape.integration;

import static org.hamcrest.CoreMatchers.is;

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
public class ThemeApiIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpData() {
        String themeSetUp = "insert into theme (name, description, thumbnail) values ('theme_name', 'theme_description', 'theme_thumbnail')";
        jdbcTemplate.update(themeSetUp);
    }

    @Test
    @DisplayName("전체 테마 데이터를 조회한다.")
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

    @Test
    @DisplayName("테마 데이터를 추가한다.")
    void createTheme() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "레벨2 탈출");
        reservation.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        reservation.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2),
                        "name", is("레벨2 탈출"),
                        "description", is("우테코 레벨2를 탈출하는 내용입니다."),
                        "thumbnail", is("https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
                );
    }

    @Test
    @DisplayName("테마 데이터를 삭제한다.")
    void deleteTheme() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }
}
