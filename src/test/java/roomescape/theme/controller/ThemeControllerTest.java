package roomescape.theme.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.theme.dto.ThemeCreateRequest;
import roomescape.theme.dto.ThemeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("테마 목록을 읽을 수 있다.")
    @Test
    void findReservations() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "오리와 호랑이", "오리들과 호랑이들 사이에서 살아남기", "https://image.jpg");

        int size = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getInt("size()");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);

        assertThat(size).isEqualTo(count);
    }

    @DisplayName("인기 테마 목록을 읽을 수 있다.")
    @Test
    void findPopularReservations() {
        jdbcTemplate.update("INSERT INTO member (name, email) VALUES (?, ?)", "브라운", "brown@abc.com");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "https://image.jpg");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마2", "설명2", "https://image.jpg");
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, TIMESTAMPADD(DAY, ?, NOW()), ?, ?)",
                1, -1, 1, 2);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, TIMESTAMPADD(DAY, ?, NOW()), ?, ?)",
                1, -2, 1, 2);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, TIMESTAMPADD(DAY, ?, NOW()), ?, ?)",
                1, -2, 1, 1);

        List<ThemeResponse> expected = List.of(
                new ThemeResponse(2L, "테마2", "설명2", "https://image.jpg"),
                new ThemeResponse(1L, "테마1", "설명1", "https://image.jpg")
        );

        List<ThemeResponse> response = RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ThemeResponse.class);

        assertThat(response).isEqualTo(expected);
    }

    @DisplayName("테마를 DB에 추가할 수 있다.")
    @Test
    void createTime() {
        ThemeCreateRequest params = new ThemeCreateRequest(
                "오리와 호랑이",
                "오리들과 호랑이들 사이에서 살아남기",
                "https://image.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/themes/1");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("삭제할 id를 받아서 DB에서 해당 테마를 삭제 할 수 있다.")
    @Test
    void deleteTime() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "오리와 호랑이", "오리들과 호랑이들 사이에서 살아남기", "https://image.jpg");

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }
}
