package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        Map<String, Object> adminParams = new HashMap<>();
        adminParams.put("name", "어드민");
        adminParams.put("email", "admin@gmail.com");
        adminParams.put("password", "1234");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(adminParams)
                .post("/signup/admin");

        Map<String, Object> normalParams = new HashMap<>();
        normalParams.put("name", "일반");
        normalParams.put("email", "user@gmail.com");
        normalParams.put("password", "1234");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(normalParams)
                .post("/signup");
    }

    @Test
    @DisplayName("/themes GET 요청에 정상적으로 응답한다")
    void themes_api() {
        String token = getUserToken();
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/themes POST 요청에 정상적으로 응답한다")
    void theme_post_test() {
        String token = getAdminToken();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue());

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/themes POST 요청시 중복된 테마 이름이 존재하는 경우 400을 응답한다")
    void theme_post_name_duplication() {
        String token = getAdminToken();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("/themes/{id} DELETE 요청에 정상적으로 응답한다")
    void theme_delete_when_exist() {
        String token = getAdminToken();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue());

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/themes/{id} DELETE 요청에 id가 존재하지 않는다면 404를 반환한다")
    void theme_delete_when_not_exist() {
        String token = getAdminToken();
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/themes/-1")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("/themes/{id} DELETE 요청시 특정 id 대한 예약이 존재하면 400를 반환한다")
    void cannot_delete_theme_if_reservation_exist() {
        String token = getAdminToken();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("/themes/popular GET 요청을 통해 인기 테마 상위 10개를 조회할 수 있다.")
    void theme_top_10() {
        String token = getUserToken();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L,
                yesterday, 1L, 1L);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L,
                yesterday, 2L, 1L);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L,
                yesterday, 3L, 1L);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L,
                yesterday, 1L, 2L);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L,
                yesterday, 2L, 2L);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L,
                yesterday, 3L, 2L);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L,
                yesterday, 4L, 2L);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L,
                yesterday, 1L, 3L);

        List<Integer> ids = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/themes/popular")
                .andReturn().body().jsonPath().getList("id");
        assertAll(
                () -> assertThat(ids.get(0)).isEqualTo(2),
                () -> assertThat(ids.get(1)).isEqualTo(1),
                () -> assertThat(ids.get(2)).isEqualTo(3),
                () -> assertThat(ids.get(3)).isEqualTo(4)
        );
    }

    private String getAdminToken() {
        Map<String, Object> memberParams = new HashMap<>();
        memberParams.put("email", "admin@gmail.com");
        memberParams.put("password", "1234");

        String accessToken = RestAssured
                .given().log().all()
                .body(memberParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");
        return accessToken;
    }

    private String getUserToken() {
        Map<String, Object> memberParams = new HashMap<>();
        memberParams.put("email", "user@gmail.com");
        memberParams.put("password", "1234");

        String accessToken = RestAssured
                .given().log().all()
                .body(memberParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");
        return accessToken;
    }
}
