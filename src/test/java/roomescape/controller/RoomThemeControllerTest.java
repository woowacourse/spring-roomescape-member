package roomescape.controller;

import static roomescape.TestFixture.ROOM_THEME_PARAMETER_SOURCE;
import static roomescape.TestFixture.THEME_DESCRIPTION_FIXTURE;
import static roomescape.TestFixture.THEME_NAME_FIXTURE;
import static roomescape.TestFixture.THEME_THUMBNAIL_FIXTURE;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.dto.request.RoomThemeCreateRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomThemeControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id");
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");
    }

    @DisplayName("테마 전체 조회 테스트")
    @Test
    void findAllThemes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("테마 랭킹순으로 조회 테스트")
    @Test
    void findAllThemesRanking() {
        RestAssured.given().log().all()
                .when().get("/themes/ranking")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("테마 추가 테스트")
    @Test
    void createTheme() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new RoomThemeCreateRequest(THEME_NAME_FIXTURE,
                        THEME_DESCRIPTION_FIXTURE, THEME_THUMBNAIL_FIXTURE))
                .when().post("/themes")
                .then().log().all().assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("비어있거나 null인 이름이 존재하는 경우 400을 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeWithNullOrEmptyName(String value) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new RoomThemeCreateRequest(value, "desc", "th.jpg"))
                .when().post("/themes")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("비어있거나 null인 설명 존재하는 경우 400을 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeWithNullOrEmptyDescription(String value) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new RoomThemeCreateRequest("name", value, "th.jpg"))
                .when().post("/themes")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("비어있거나 null인 이름이 존재하는 경우 400을 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeWithNullOrEmptyThumbnail(String value) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new RoomThemeCreateRequest("name", "desc", value))
                .when().post("/themes")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테마 삭제 성공 테스트")
    @Test
    void deleteTheme() {
        // given
        long id = simpleJdbcInsert.withTableName("theme")
                .executeAndReturnKey(ROOM_THEME_PARAMETER_SOURCE)
                .longValue();
        // when & then
        RestAssured.given().log().all()
                .when().delete("/themes/" + id)
                .then().log().all().assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("예약 시간 삭제 실패 테스트")
    @Test
    void deleteReservationTimeFail() {
        // given
        long invalidId = 0;
        // when & then
        RestAssured.given().log().all()
                .when().delete("/themes/" + invalidId)
                .then().log().all().assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
