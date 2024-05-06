package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.dto.ThemeResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ClientRankTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("랭크를 조회하면 현재 날짜 기준 일주일 동안의 인기 테마를 확인할 수 있다.")
    @Test
    void given_startDateEndDateCount_when_rank_then_statusCodeIsOk() {
        //given
        String startDate = "2024-04-30";
        String endDate = "2024-05-02";
        String count = "10";
        final List<ThemeResponse> themeResponses = RestAssured.given().log().all()
                .when().get("/rank?startDate={startDate}&endDate={endDate}&count={count}", startDate, endDate, count)
                .then().extract().body()
                .jsonPath().getList("", ThemeResponse.class);
        ThemeResponse actual = themeResponses.get(0);
        assertThat(actual.id()).isEqualTo(2L);
    }

    @DisplayName("적절하지 않는 기간을 입력할 경우 400 오류를 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {" ", "", "2999-45-43"})
    void given_invalidStartDate_when_rank_then_statusCodeIsBadRequest(String invalidStartDate) {
        //given
        String endDate = "2024-05-02";
        String count = "10";
        RestAssured.given().log().all()
                .when().get("/rank?startDate={startDate}&endDate={endDate}&count={count}", invalidStartDate, endDate, count)
                .then().log().all()
                .statusCode(400)
                .body(containsString("startDate"));
    }
}
