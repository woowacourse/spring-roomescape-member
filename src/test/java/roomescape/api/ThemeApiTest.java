package roomescape.api;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.contains;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Theme;
import roomescape.domain.ReservationTime;
import roomescape.util.TestDataInitializer;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테마 API 요구사항 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeApiTest {

    @Autowired
    private TestDataInitializer dataInitializer;

    @Test
    @DisplayName("테마를 추가한다.")
    void createTheme() {
        // given
        Map<String, String> request = new HashMap<>();
        request.put("name", "귀신의 집");
        request.put("description", "무서워요");
        request.put("imageUrl", "/resources/image/ghost.png");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNull();
        assertThat(response.jsonPath().getObject("id", Integer.class)).isNotNull();
    }

    @Test
    @DisplayName("전체 테마 목록을 조회한다.")
    void getThemes() {
        // given
        createThemeHelper("귀신의 집", "무서워요", "/resources/image/1");
        createThemeHelper("물고기", "어푸", "/resources/image/2");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("themes")).hasSizeGreaterThanOrEqualTo(2);

        // JSON 응답 구조 검증
        assertThat(response.jsonPath().getList("themes.id")).doesNotContainNull();
        assertThat(response.jsonPath().getList("themes.name")).contains("귀신의 집", "물고기");
        assertThat(response.jsonPath().getList("themes.description")).contains("무서워요", "어푸");
        assertThat(response.jsonPath().getList("themes.imageUrl")).doesNotContainNull();
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteTheme() {
        // given
        int themaId = createThemeHelper("삭제할 테마", "삭제될 예정입니다", "/resources/image/delete");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("id", themaId)
                .when().delete("/themes/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private int createThemeHelper(String name, String description, String imageUrl) {
        Map<String, String> request = new HashMap<>();
        request.put("name", name);
        request.put("description", description);
        request.put("imageUrl", imageUrl);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .jsonPath().getInt("id");
    }

    @Test
    @DisplayName("인기 테마를 예약 많은 순, 예약 수가 같다면 이름 순으로 조회한다.")
    void getPopularThemes() {
        Theme themeA = dataInitializer.createTheme("A 테마", "설명A", "urlA");
        Theme themeB = dataInitializer.createTheme("B 테마", "설명B", "urlB");
        Theme themeC = dataInitializer.createTheme("C 테마", "설명C", "urlC");

        ReservationTime timeA = dataInitializer.createReservationTime(LocalTime.of(10, 0));
        ReservationTime timeB = dataInitializer.createReservationTime(LocalTime.of(11, 0));
        ReservationTime timeC = dataInitializer.createReservationTime(LocalTime.of(12, 0));
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // 테마 A에 예약 3개
        dataInitializer.createReservation("사용자1", yesterday, timeA.getId(), themeA.getId());
        dataInitializer.createReservation("사용자2", yesterday, timeB.getId(), themeA.getId());
        dataInitializer.createReservation("사용자3", yesterday, timeC.getId(), themeA.getId());

        // 테마 B에 예약 1개
        dataInitializer.createReservation("사용자4", yesterday, timeA.getId(), themeB.getId());

        // 테마 C에 예약 3개
        dataInitializer.createReservation("사용자5", yesterday, timeA.getId(), themeC.getId());
        dataInitializer.createReservation("사용자6", yesterday, timeB.getId(), themeC.getId());
        dataInitializer.createReservation("사용자7", yesterday, timeC.getId(), themeC.getId());

        RestAssured.given().log().all()
                .queryParam("days", 7)
                .queryParam("limit", 10)
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(200)
                .body("themeRankings.theme.name", contains("A 테마", "C 테마", "B 테마"))
                .body("themeRankings.size()", is(3));
    }

    @Test
    @DisplayName("인기 테마 조회는 days와 limit를 생략하면 기본값을 사용한다.")
    void getPopularThemesWithDefaultQueryParams() {
        Theme theme = dataInitializer.createTheme("A 테마", "설명A", "urlA");
        ReservationTime time = dataInitializer.createReservationTime(LocalTime.of(15, 0));
        dataInitializer.createReservation("사용자1", LocalDate.now().minusDays(1), time.getId(), theme.getId());

        RestAssured.given().log().all()
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(200)
                .body("themeRankings.theme.name", contains("A 테마"))
                .body("themeRankings.size()", is(1));
    }

    @Test
    @DisplayName("인기 테마 조회는 오늘을 제외하고 days일 범위만 조회한다.")
    void getPopularThemesExcludesTodayAndIncludesExactDaysBeforeToday() {
        Theme includedStartTheme = dataInitializer.createTheme("시작일 포함 테마", "설명", "url");
        Theme includedEndTheme = dataInitializer.createTheme("종료일 포함 테마", "설명", "url");
        Theme excludedTodayTheme = dataInitializer.createTheme("오늘 제외 테마", "설명", "url");
        Theme excludedBeforeRangeTheme = dataInitializer.createTheme("범위 이전 제외 테마", "설명", "url");
        ReservationTime time = dataInitializer.createReservationTime(LocalTime.of(15, 0));
        LocalDate today = LocalDate.now();

        dataInitializer.createReservation("사용자1", today.minusDays(7), time.getId(), includedStartTheme.getId());
        dataInitializer.createReservation("사용자2", today.minusDays(1), time.getId(), includedEndTheme.getId());
        dataInitializer.createReservation("사용자3", today, time.getId(), excludedTodayTheme.getId());
        dataInitializer.createReservation("사용자4", today.minusDays(8), time.getId(), excludedBeforeRangeTheme.getId());

        RestAssured.given().log().all()
                .queryParam("days", 7)
                .queryParam("limit", 10)
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(200)
                .body("themeRankings.theme.name", contains("시작일 포함 테마", "종료일 포함 테마"))
                .body("themeRankings.size()", is(2));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 10, 200",
            "0, 10, 400",
            "180, 10, 200",
            "181, 10, 400",
            "7, 1, 200",
            "7, 0, 400",
            "7, 50, 200",
            "7, 51, 400"
    })
    @DisplayName("인기 테마 조회 조건의 경계값을 검증한다.")
    void validatePopularThemeRankingQueryRange(int days, int limit, int statusCode) {
        RestAssured.given().log().all()
                .queryParam("days", days)
                .queryParam("limit", limit)
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(statusCode);
    }
}
