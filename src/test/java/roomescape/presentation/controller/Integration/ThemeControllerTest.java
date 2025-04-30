package roomescape.presentation.controller.Integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ThemeControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        RestAssured.port = port;

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("테마 조회 요청 시 모든 테마를 응답한다")
    @Test
    void getAllThemes() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '테마 1입니다.', '썸네일입니다.')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마2', '테마 2입니다.', '썸네일입니다.')");

        int themesCount = getThemesCount();

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(themesCount));
    }

    @DisplayName("테마 생성 요청 시 생성된 테마 정보를 응답한다")
    @Test
    void createTheme() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "레벨2 탈출입니다.");
        params.put("thumbnail", "썸네일");

        // when & then
        ExtractableResponse<Response> extractedResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201).extract();

        assertAll(
                () -> assertThat(extractedResponse.jsonPath().getString("name")).isEqualTo(params.get("name")),
                () -> assertThat(extractedResponse.jsonPath().getString("description")).isEqualTo(
                        params.get("description")),
                () -> assertThat(extractedResponse.jsonPath().getString("thumbnail")).isEqualTo(params.get("thumbnail"))
        );
    }

    @DisplayName("테마 삭제 요청 시 해당하는 id의 테마를 삭제한다")
    @Test
    void delete() {
        //given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '테마 1입니다.', '썸네일입니다.')");

        int addedCount = getThemesCount();

        assertThat(addedCount).isEqualTo(1);

        //when & then
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        int deletedCount = getThemesCount();
        assertThat(deletedCount).isEqualTo(0);
    }

    private int getThemesCount() {
        int themesCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);
        return themesCount;
    }
}
