package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ReservationTimeAddRequest;
import roomescape.service.ThemeAddRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into theme (name, description, thumbnail) values('리비', '힘듦', 'url')");
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.update("delete from theme");
    }

    @DisplayName("전체 테마를 조회할 수 있다 (200 OK)")
    @Test
    void should_get_theme_list() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("테마를 추가할 수 있다 (201 created)")
    @Test
    void should_add_theme() {
        ThemeAddRequest themeAddRequest = new ThemeAddRequest("도도", "배고픔", "url");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeAddRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마를 삭제할 수 있다 (204 no content)")
    @Test
    void should_remove_theme() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }
}
