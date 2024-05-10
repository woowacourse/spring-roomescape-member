package roomescape.reservation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.dto.SaveThemeRequest;
import roomescape.reservation.dto.ThemeResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class ThemeControllerTest {

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    public void initReservation() {
        RestAssured.port = randomServerPort;
    }

    @DisplayName("전체 테마 정보를 조회한다.")
    @Test
    void getThemesTest() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(15));
    }

    @DisplayName("테마 정보를 저장한다.")
    @Test
    void saveThemeTest() {
        final SaveThemeRequest saveThemeRequest = new SaveThemeRequest(
                "즐거운 방방탈출~",
                "방방방! 탈탈탈!",
                "방방 사진"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(saveThemeRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(16));
    }

    @DisplayName("테마 정보를 삭제한다.")
    @Test
    void deleteThemeTest() {
        // 예약 시간 정보 삭제
        RestAssured.given().log().all()
                .when().delete("/themes/7")
                .then().log().all()
                .statusCode(204);

        // 예약 시간 정보 조회
        final List<ThemeResponse> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ThemeResponse.class);

        assertThat(themes.size()).isEqualTo(14);
    }
}
