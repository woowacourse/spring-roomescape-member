package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 테마를_생성한다() {
        Map<String, String> params = Map.of(
                "name", "공포",
                "description", "무서운 테마",
                "thumbnailUrl", "https://example.com/img.jpg"
        );

        Map<String, Object> result = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getMap(".");

        assertThat(result.get("name")).isEqualTo("공포");
        assertThat(result.get("description")).isEqualTo("무서운 테마");
    }

    @Test
    void 전체_테마를_조회한다() {
        themeRepository.save(Theme.of("공포", "무서운 테마", "https://example.com/img.jpg"));
        themeRepository.save(Theme.of("추리", "머리 쓰는 테마", "https://example.com/img2.jpg"));

        List<Map<String, Object>> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".");

        assertThat(themes).hasSize(2);
        assertThat(themes.get(0).get("name")).isEqualTo("공포");
    }

    @Test
    void id로_테마를_조회한다() {
        Theme saved = themeRepository.save(Theme.of("공포", "무서운 테마", "https://example.com/img.jpg"));

        Map<String, Object> result = RestAssured.given().log().all()
                .when().get("/themes/" + saved.getId())
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getMap(".");

        assertThat(result.get("name")).isEqualTo("공포");
    }

    @Test
    void 테마를_삭제한다() {
        Theme saved = themeRepository.save(Theme.of("공포", "무서운 테마", "https://example.com/img.jpg"));

        RestAssured.given().log().all()
                .when().delete("/themes/" + saved.getId())
                .then().log().all()
                .statusCode(204);

        assertThat(themeRepository.findAll()).isEmpty();
    }

    @Test
    void 인기_테마를_조회한다() {
        Theme theme1 = themeRepository.save(Theme.of("공포", "desc", "url"));
        Theme theme2 = themeRepository.save(Theme.of("추리", "desc", "url"));
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));

        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "유저1", java.time.LocalDate.now().minusDays(1), time.getId(), theme1.getId());
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "유저2", java.time.LocalDate.now().minusDays(1), time.getId(), theme2.getId());

        List<Map<String, Object>> result = RestAssured.given().log().all()
                .when().get("/themes/famous")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".");

        assertThat(result).hasSize(2);
    }
}
