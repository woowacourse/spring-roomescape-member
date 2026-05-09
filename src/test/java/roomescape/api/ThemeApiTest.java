package roomescape.api;

import static org.hamcrest.Matchers.equalTo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
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
        assertThat(response.header("Location")).isNull();assertThat(response.jsonPath().getObject("id", Integer.class)).isNotNull();
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
        assertThat(response.jsonPath().getList("")).hasSizeGreaterThanOrEqualTo(2);

        // JSON 응답 구조 검증
        assertThat(response.jsonPath().getList("id")).doesNotContainNull();
        assertThat(response.jsonPath().getList("name")).contains("귀신의 집", "물고기");
        assertThat(response.jsonPath().getList("description")).contains("무서워요", "어푸");
        assertThat(response.jsonPath().getList("imageUrl")).doesNotContainNull();
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
        Theme themeA = dataInitializer.initializeTheme("A 테마", "설명A", "urlA");
        Theme themeB = dataInitializer.initializeTheme("B 테마", "설명B", "urlB");
        Theme themeC = dataInitializer.initializeTheme("C 테마", "설명C", "urlC");

        ReservationTime time = dataInitializer.initializeReservationTime(LocalTime.of(15, 0));
        LocalDate today = LocalDate.now();

        // 테마 A에 예약 3개
        dataInitializer.initializeReservation("사용자1", today, time.getId(), themeA.getId());
        dataInitializer.initializeReservation("사용자2", today, time.getId(), themeA.getId());
        dataInitializer.initializeReservation("사용자3", today, time.getId(), themeA.getId());

        // 테마 B에 예약 1개
        dataInitializer.initializeReservation("사용자4", today, time.getId(), themeB.getId());

        // 테마 C에 예약 3개
        dataInitializer.initializeReservation("사용자5", today, time.getId(), themeC.getId());
        dataInitializer.initializeReservation("사용자6", today, time.getId(), themeC.getId());
        dataInitializer.initializeReservation("사용자7", today, time.getId(), themeC.getId());

        RestAssured.given().log().all()
                .queryParam("days", 7)
                .queryParam("limit", 10)
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(200)
                .body("theme.name", contains("A 테마", "C 테마", "B 테마"))
                .body("size()", is(3));
    }

    @Test
    void 예약_가능_시간_조회() {
        // given
        ReservationTime ten = dataInitializer.initializeReservationTime(LocalTime.of(10, 0));
        ReservationTime eleven = dataInitializer.initializeReservationTime(LocalTime.of(11, 0));
        Theme theme = dataInitializer.initializeTheme("hello", "world", "/resources/image/...");

        LocalDate date = LocalDate.now().plusDays(1);
        dataInitializer.initializeReservation("라텔", date, ten.getId(), theme.getId());


        Map<String, Object> params = new HashMap<>();
        params.put("date", date.toString());
        params.put("timeId", ten.getId());
        params.put("themeId", theme.getId());
        params.put("available", true);

        // when
        RestAssured.given().log().all()
                .queryParams(params)
                .when().get("/themes/1/times")
                .then().log().all()
                .statusCode(200)
                .body("availableTimes.size()", is(1),
                        "availableTimes[0].startAt", equalTo(eleven.getStartAt().toString()));
    }
}
