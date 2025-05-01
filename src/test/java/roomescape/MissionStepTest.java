package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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
public class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setupDatabase() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
    }

    @Test
    @DisplayName("/ GET 요청에 응답한다")
    void welcome_page() {
        RestAssured.given().log().all()
            .when().get("/")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    @DisplayName("/admin GET 요청에 응답한다")
    void admin_page() {
        RestAssured.given().log().all()
            .when().get("/admin")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    @DisplayName("/admin/reservation GET 요청에 응답한다")
    void admin_reservation_page() {
        RestAssured.given().log().all()
            .when().get("/admin/reservation")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    @DisplayName("/reservations GET 요청에 정상적으로 응답한다")
    void reservations_api() {
        RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200)
            .body("size()",
                is(0)); // 아직 생성 요청이 없으니 Controller에서 임의로 넣어준 Reservation 갯수 만큼 검증하거나 0개임을 확인하세요.
    }

    @Test
    @DisplayName("/reservations POST 요청에 정상적으로 응답한다")
    void reservation_post_api() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(201)
            .body("id", is(1));

        RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    @DisplayName("/reservations DELETE 요청에 정상적으로 응답한다")
    void reservation_delete_api() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(201)
            .body("id", notNullValue());

        RestAssured.given().log().all()
            .when().delete("/reservations/1")
            .then().log().all()
            .statusCode(204);

        RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    @DisplayName("/reservations DELETE id가 존재하지 않는다면 404를 반환한다")
    void reservation_delete_not_exist_api() {
        RestAssured.given().log().all()
            .when().delete("/reservations/1")
            .then().log().all()
            .statusCode(404);

        RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(0));
    }

    @Test
    @DisplayName("/reservations POST 요청시 날짜 형식이 올바르지 않을 경우 400을 반환한다")
    void reservation_post_format_not_proper() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "1월 1일");
        params.put("timeId", 1);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(400);
    }

    @Test
    @DisplayName("/times POST 요청시 시간 형식이 올바르지 않을 경우 400을 반환한다")
    void reservation_time_post_format_not_proper() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "한시 오분");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/times")
            .then().log().all()
            .statusCode(400);
    }

    @Test
    @DisplayName("특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때 400 코드를 반환한다")
    void cannot_delete_time_if_reservation_exist() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(201);

        RestAssured.given().log().all()
            .when().delete("/times/1")
            .then().log().all()
            .statusCode(400);
    }

    @Test
    @DisplayName("/themes POST 요청에 정상적으로 응답한다")
    void theme_post_test() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/themes")
            .then().log().all()
            .statusCode(201)
            .body("id", notNullValue());

        RestAssured.given().log().all()
            .when().get("/themes")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    @DisplayName("/themes GET 요청에 정상적으로 응답한다")
    void theme_get_test() {
        RestAssured.given().log().all()
            .when().get("/themes")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    @DisplayName("/themes DELETE 요청에 정상적으로 응답한다")
    void theme_delete_when_exist() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/themes")
            .then().log().all()
            .statusCode(201)
            .body("id", notNullValue());

        RestAssured.given().log().all()
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(204);

        RestAssured.given().log().all()
            .when().get("/themes")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    @DisplayName("/themes DELETE 요청에 id가 존재하지 않는다면 404를 반환한다")
    void theme_delete_when_not_exist() {
        RestAssured.given().log().all()
            .when().delete("/themes/100")
            .then().log().all()
            .statusCode(404);
    }

    @Test
    @DisplayName("특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제하려 할 때 400 코드를 반환한다")
    void cannot_delete_theme_if_reservation_exist() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(201);

        RestAssured.given().log().all()
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(400);
    }

    @Test
    @DisplayName("/themes/top10 GET 요청을 통해 인기 테마 상위 10개를 조회할 수 있다.")
    void theme_top_10() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "두리",
            yesterday, 1L, 1L);
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "두리",
            yesterday, 2L, 1L);
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "두리",
            yesterday, 3L, 1L);
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "두리",
            yesterday, 1L, 2L);
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "두리",
            yesterday, 2L, 2L);
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "두리",
            yesterday, 3L, 2L);
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "두리",
            yesterday, 4L, 2L);
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "두리",
            yesterday, 1L, 3L);

        List<Integer> ids = RestAssured.given().log().all()
            .when().get("/themes/top10")
            .andReturn().body().jsonPath().getList("id");
        assertAll(
            () -> assertThat(ids.get(0)).isEqualTo(2),
            () -> assertThat(ids.get(1)).isEqualTo(1),
            () -> assertThat(ids.get(2)).isEqualTo(3),
            () -> assertThat(ids.get(3)).isEqualTo(4)
        );
    }
}
