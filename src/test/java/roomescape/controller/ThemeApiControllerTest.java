package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import org.springframework.test.context.ActiveProfiles;
import roomescape.fixture.MemberFixture;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ReservationTimeFixture;
import roomescape.fixture.ThemeFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ThemeApiControllerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("TRUNCATE TABLE reservation");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE reservation_time");
        jdbcTemplate.update("TRUNCATE TABLE theme");
        jdbcTemplate.update("TRUNCATE TABLE member");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("테마 생성에 성공하면 201을 반환한다.")
    @Test
    void return_201_when_theme_create_success() {
        final Map<String, String> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then()
                .statusCode(201);
    }

    @DisplayName("테마 조회에 성공하면 200을 반환한다.")
    @Test
    void return_200_when_get_themes_success() {
        ThemeFixture.createAndReturnId("테마 1");
        ThemeFixture.createAndReturnId("테마 2");

        RestAssured.given()
                .when().get("/themes")
                .then()
                .statusCode(200);
    }

    @DisplayName("인기 테마 조회에 성공하면 200을 반환한다.")
    @Test
    void return_200_when_get_popular_themes_success() {
        final Long timeId1 = ReservationTimeFixture.createAndReturnId("10:00");
        final Long timeId2 = ReservationTimeFixture.createAndReturnId("11:00");
        final Long timeId3 = ReservationTimeFixture.createAndReturnId("12:00");
        final Long memberId = MemberFixture.createAndReturnId();

        final Long themeId1 = ThemeFixture.createAndReturnId("테마 1");
        ReservationFixture.createAndReturnId("2024-06-01", timeId1, themeId1);
        ReservationFixture.createAndReturnId("2024-06-01", timeId2, themeId1);
        ReservationFixture.createAndReturnId("2024-06-01", timeId3, themeId1);

        final Long themeId2 = ThemeFixture.createAndReturnId("테마 2");
        ReservationFixture.createAndReturnId("2024-06-02", timeId1, themeId2);
        ReservationFixture.createAndReturnId("2024-06-02", timeId2, themeId2);

        final Long themeId3 = ThemeFixture.createAndReturnId("테마 3");
        ReservationFixture.createAndReturnId("2024-06-03", timeId1, themeId3);

        RestAssured.given()
                .when().get("/themes/popular?date=2024-06-02")
                .then()
                .statusCode(200);
    }

    @DisplayName("특정 테마가 존재하지 않는데, 그 테마를 삭제하려 하면 400을 반환한다.")
    @Test
    void return_404_when_not_exist_id() {
        RestAssured.given()
                .delete("/themes/-1")
                .then()
                .statusCode(400);
    }

    @DisplayName("특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제하려 하면 400를 반환한다.")
    @Test
    void return_400_when_delete_id_that_exist_reservation() {
        final Long timeId = ReservationTimeFixture.createAndReturnId("10:00");
        final Long themeId = ThemeFixture.createAndReturnId("테마 1");
        final Long memberId = MemberFixture.createAndReturnId();

        ReservationFixture.createAndReturnId("2024-06-01", timeId, themeId);

        RestAssured.given()
                .delete("/themes/" + themeId)
                .then()
                .statusCode(400);
    }
}
