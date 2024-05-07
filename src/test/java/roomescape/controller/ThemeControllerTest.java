package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.controller.dto.CreateThemeRequest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class ThemeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void addInitialData() {
        jdbcTemplate.update("""
            INSERT INTO theme(name, description, thumbnail)
            VALUES
            ('방탈출1', '설명1', 'https://url1'),
            ('방탈출2', '설명2', 'https://url2')
            """);
    }

    @DisplayName("성공: 테마 생성 -> 201")
    @Test
    void create() {
        CreateThemeRequest request = new CreateThemeRequest("방탈출3", "설명3", "https://url3");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/themes")
            .then().log().all()
            .statusCode(201)
            .body("id", is(3))
            .body("name", is("방탈출3"))
            .body("description", is("설명3"))
            .body("thumbnail", is("https://url3"));
    }

    @DisplayName("성공: 테마 삭제 -> 204")
    @Test
    void delete() {
        RestAssured.given().log().all()
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(204);

        assertThat(
            jdbcTemplate.queryForObject("SELECT COUNT(id) FROM theme", Long.class)
        ).isEqualTo(1L);
    }

    @DisplayName("성공: 테마 조회 -> 200")
    @Test
    void findAll() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .when().get("/themes")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(2))
            .body("id", contains(1, 2))
            .body("name", contains("방탈출1", "방탈출2"))
            .body("description", contains("설명1", "설명2"))
            .body("thumbnail", contains("https://url1", "https://url2"));
    }

    @DisplayName("실패: 잘못된 포맷으로 테마 생성 -> 400")
    @Test
    void create_IllegalTheme() {
        CreateThemeRequest request = new CreateThemeRequest("방탈출3", "설명3", "ftp://url3");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/themes")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("실패: 중복 테마 추가 -> 400")
    @Test
    void create_Duplicate() {
        CreateThemeRequest request = new CreateThemeRequest("방탈출1", "설명1", "https://url1");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/themes")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("실패: 예약에서 사용되는 테마 삭제 -> 400")
    @Test
    void delete_ReservationExists() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time(start_at) VALUES ('12:00');
            INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('brown', '2026-01-01', 1, 1);
            """);

        RestAssured.given().log().all()
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("성공: 상위 10개의 인기 테마 조회 -> 200")
    @Test
    @Sql("/popularTestData.sql")
    void findPopularTheme() {
        RestAssured.given().log().all()
            .when().get("/themes/popular")
            .then().log().all()
            .statusCode(200)
            .body("id", contains(1, 3, 2, 4, 5));
    }
}
