package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
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
import roomescape.dto.web.ThemeWebRequest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class ThemeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void addInitialData() {
        jdbcTemplate.update("INSERT INTO theme(name, description, thumbnail) VALUES (?, ?, ?)", "방탈출1", "설명1",
            "https://url1");
        jdbcTemplate.update("INSERT INTO theme(name, description, thumbnail) VALUES (?, ?, ?)", "방탈출2", "설명2",
            "https://url2");
    }

    @DisplayName("테마를 생성한다 -> 201")
    @Test
    void create() {
        ThemeWebRequest request = new ThemeWebRequest("방탈출", "대충 설명", "https://url.jpg");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/themes")
            .then().log().all()
            .statusCode(201)
            .body("id", is(3));
    }

    @DisplayName("테마를 삭제한다 -> 204")
    @Test
    void delete() {
        RestAssured.given().log().all()
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(204);

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(id) FROM theme", Long.class);

        assertThat(count).isEqualTo(1L);
    }

    @DisplayName("테마를 조회한다 -> 200")
    @Test
    void findAll() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .when().get("/themes")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(2));
    }

    @DisplayName("테마 정보 포맷이 잘못될 경우 -> 400")
    @Test
    void create_IllegalTheme() {
        ThemeWebRequest request = new ThemeWebRequest("방탈출3", "설명3", "ftp://url3");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/themes")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("중복된 데이터를 추가한다 -> 400")
    @Test
    void create_Duplicate() {
        ThemeWebRequest request = new ThemeWebRequest("방탈출1", "설명1", "https://url1");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/themes")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("예약이 존재한 상태에서 테마를 삭제한다 -> 400")
    @Test
    void delete_ReservationExists() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "12:00");
        jdbcTemplate.update("INSERT INTO reservation(name,date,time_id,theme_id) VALUES (?,?,?,?)", "brown",
            "2026-02-01", 1L, 1L);

        RestAssured.given().log().all()
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("상위 10개 인기 테마를 조회 한다. -> 200")
    @Test
    @Sql("/popularTestData.sql")
    void findPopularTheme() {
        RestAssured.given().log().all()
            .when().get("/themes/popular")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(5));
    }
}
