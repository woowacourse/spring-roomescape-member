package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ThemeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeRestControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void getThemes() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "곰세마리", "공포", "푸우");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(1));
    }

//    @Test // TODO: 수정
    void getRankThemes() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "곰세마리", "공포", "푸우");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "브라운",
                "2024-08-05", 1, 1);

        // when
        List<ThemeResponse> themeResponses = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes/ranking")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ThemeResponse.class);

        assertThat(themeResponses.get(0).name()).isEqualTo("테마이름");
    }

    @Test
    void createTheme() {
        // given
        Map<String, String> params = Map.of(
                "name", "테니",
                "description", "설명",
                "thumbnail", "썸네일"
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    void deleteById() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "곰세마리", "공포", "푸우");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
