package roomescape.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.BasicAcceptanceTest;
import roomescape.TestFixtures;
import roomescape.dto.response.ThemeResponse;

class ThemeAcceptanceTest extends BasicAcceptanceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @TestFactory
    @DisplayName("2개의 테마를 추가한다")
    Stream<DynamicTest> themePostTest() {
        return Stream.of(
                dynamicTest("테마를 추가한다", () -> postTheme(201)),
                dynamicTest("테마를 추가한다", () -> postTheme(201)),
                dynamicTest("모든 테마를 조회한다 (총 2개)", () -> getThemes(200, 2))
        );
    }

    @TestFactory
    @DisplayName("테마를 추가하고 삭제한다")
    Stream<DynamicTest> themePostAndDeleteTest() {
        AtomicLong themeId = new AtomicLong();

        return Stream.of(
                dynamicTest("테마를 추가한다 (10:00)", () -> themeId.set(postTheme(201))),
                dynamicTest("테마를 삭제한다 (10:00)", () -> deleteTheme(themeId.longValue(), 204)),
                dynamicTest("테마를 추가한다 (10:00)", () -> postTheme(201)),
                dynamicTest("모든 테마를 조회한다 (총 1개)", () -> getThemes(200, 1))
        );
    }

    @TestFactory
    @Sql("/init-for-top-theme.sql")
    @DisplayName("예약 횟수 상위 10개의 테마를 조회한다")
    Stream<DynamicTest> top10Theme() {
        LocalDate today = LocalDate.now();
        return Stream.of(
                dynamicTest("인기 테마를 조회한다", () -> getTopTheme(200, TestFixtures.THEME_RESPONSES_1)),
                dynamicTest("과거 예약을 추가한다", () -> postPastReservation(today.minusDays(5).toString(), "1", "1", "10")),
                dynamicTest("인기 테마를 조회한다", () -> getTopTheme(200, TestFixtures.THEME_RESPONSES_2)),
                dynamicTest("과거 예약을 추가한다", () -> postPastReservation(today.minusDays(4).toString(), "1", "1", "11")),
                dynamicTest("과거 예약을 추가한다", () -> postPastReservation(today.minusDays(4).toString(), "1", "2", "11")),
                dynamicTest("인기 테마를 조회한다", () -> getTopTheme(200, TestFixtures.THEME_RESPONSES_3))
        );
    }

    private Long postTheme(int expectedHttpCode) {
        Map<?, ?> requestBody = Map.of("name", "테마", "description", "설명서", "thumbnail", "썸네일");

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/themes")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        if (expectedHttpCode == 201) {
            return response.jsonPath().getLong("id");
        }

        return null;
    }

    private void getThemes(int expectedHttpCode, int expectedthemesSize) {
        Response response = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        List<?> themeResponses = response.as(List.class);

        assertThat(themeResponses).hasSize(expectedthemesSize);
    }

    private void deleteTheme(Long themeId, int expectedHttpCode) {
        RestAssured.given().log().all()
                .when().delete("/themes/" + themeId)
                .then().log().all()
                .statusCode(expectedHttpCode);
    }

    private void getTopTheme(int expectedHttpCode, List<ThemeResponse> expectedThemeResponses) {
        Response response = RestAssured.given().log().all()
                .when().get("/themes/tops")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        List<ThemeResponse> themeResponses = response.jsonPath().getList(".", ThemeResponse.class);

        assertThat(themeResponses).isEqualTo(expectedThemeResponses);
    }

    private void postPastReservation(String date, String member_id, String timeId, String themeId) {
        jdbcTemplate.update(
                "insert into reservation (date, member_id, time_id, theme_id) values (?, ?, ?, ?)",
                date, member_id, timeId, themeId);
    }
}
