package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.dto.ThemeResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ThemeTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("theme 목록 조회 요청이 올바르게 동작한다.")
    @Test
    void given_when_GetThemes_then_statusCodeIsOk() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("테마 등록 성공 시 201을 응답한다.")
    @Test
    void given_themeRequest_when_saveSuccessful_then_statusCodeIsCreated() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "우테코 레벨 1 탈출");
        params.put("description", "우테코 레벨 1 탈출하는 내용");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마 삭제 성공 시 204를 응답한다.")
    @Test
    void given_when_deleteSuccessful_then_statusCodeIsNoContents() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "우테코 레벨 1 탈출");
        params.put("description", "우테코 레벨 1 탈출하는 내용");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .when().delete("/themes/4")
                .then().log().all()
                .statusCode(204);
    }


    @DisplayName("삭제하고자 하는 테마에 예약이 등록되어 있으면 400 오류를 반환한다.")
    @Test
    void given_when_deleteThemeIdRegisteredReservation_then_statusCodeIsBadRequest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400)
                .body(containsString("예약이 등록된 테마는 제거할 수 없습니다"));
    }

    @DisplayName("테마 등록 시 빈 값이 한 개 이상 포함되어 있을 경우 400 오류를 반환한다.")
    @ParameterizedTest
    @CsvSource({",test,test,name", "test,,test,description", "test,test,,thumbnail"})
    void given_when_saveThemeWithEmptyValues_then_statusCodeIsBadRequest(String name, String description, String thumbNail, String emptyFieldName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", description);
        params.put("thumbnail", thumbNail);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400)
                .body(containsString(emptyFieldName));
    }

    @DisplayName("랭크를 조회하면 현재 날짜 기준 일주일 동안의 인기 테마를 확인할 수 있다.")
    @Test
    void given_startDateEndDateCount_when_rank_then_statusCodeIsOk() {
        //given
        String startDate = "2024-04-30";
        String endDate = "2024-05-02";
        String size = "10";
        final List<ThemeResponse> themeResponses = RestAssured.given().log().all()
                .when().get("/themes/rank?startDate={startDate}&endDate={endDate}&size={count}", startDate, endDate, size)
                .then().extract().body()
                .jsonPath().getList("data", ThemeResponse.class);
        ThemeResponse actual = themeResponses.get(0);
        assertThat(actual.id()).isEqualTo(2L);
    }

    @DisplayName("랭크 조회 시 적절하지 않는 기간을 입력할 경우 400 오류를 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {" ", "", "2999-45-43"})
    void given_invalidStartDate_when_rank_then_statusCodeIsBadRequest(String invalidStartDate) {
        //given
        String endDate = "2024-05-02";
        String count = "10";
        RestAssured.given().log().all()
                .when().get("/themes/rank?startDate={startDate}&endDate={endDate}&size={count}", invalidStartDate, endDate, count)
                .then().log().all()
                .statusCode(400)
                .body(containsString("startDate"));
    }
}
