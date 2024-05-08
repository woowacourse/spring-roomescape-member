package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.context.jdbc.Sql;
import roomescape.web.dto.ThemeListResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ThemeAcceptanceTest extends BasicAcceptanceTest {
    @DisplayName("2개의 테마를 추가한다")
    @TestFactory
    Stream<DynamicTest> themePostTest() {
        return Stream.of(
                dynamicTest("테마를 추가한다", () -> postTheme(201)),
                dynamicTest("테마를 추가한다", () -> postTheme(201)),
                dynamicTest("모든 테마를 조회한다 (총 2개)", () -> getThemes(200, 2))
        );
    }

    @DisplayName("테마를 추가하고 삭제한다")
    @TestFactory
    Stream<DynamicTest> themePostAndDeleteTest() {
        AtomicLong themeId = new AtomicLong();

        return Stream.of(
                dynamicTest("테마를 추가한다 (10:00)", () -> {
                    long id = postTheme(201);
                    themeId.set(id);
                }),
                dynamicTest("테마를 삭제한다 (10:00)", () -> deleteTheme(themeId.longValue(), 204)),
                dynamicTest("테마를 추가한다 (10:00)", () -> postTheme(201)),
                dynamicTest("모든 테마를 조회한다 (총 1개)", () -> getThemes(200, 1))
        );
    }

    @DisplayName("상위 10개 테마를 조회한다")
    @Sql("/test-data/theme-tops.sql")
    @TestFactory
    Stream<DynamicTest> themeGetTop10Test() {
        return Stream.of(
                dynamicTest("상위 10개 테마를 조회한다", () -> getTopThemes(200, 10))
        );
    }

    private long postTheme(int expectedHttpCode) {
        Map<?, ?> requestBody = Map.of("name", "테마", "description", "설명서", "thumbnail",
                "https://i.postimg.cc/cLqW2JLB/theme-SOS-SOS.jpg");

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

        return 0L;
    }

    private void getThemes(int expectedHttpCode, int expectedthemesSize) {
        Response response = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        ThemeListResponse themeResponses = response.as(ThemeListResponse.class);

        assertThat(themeResponses.getThemes()).hasSize(expectedthemesSize);
    }

    private void deleteTheme(long themeId, int expectedHttpCode) {
        RestAssured.given().log().all()
                .when().delete("/themes/" + themeId)
                .then().log().all()
                .statusCode(expectedHttpCode);
    }

    private void getTopThemes(int expectedHttpCode, int expectedthemesSize) {
        Response response = RestAssured.given().log().all()
                .when().get("/themes/tops")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        List<?> themeResponses = response.as(List.class);

        assertThat(themeResponses).hasSize(expectedthemesSize);
    }
}
