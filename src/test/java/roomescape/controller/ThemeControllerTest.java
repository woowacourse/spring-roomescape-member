package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import roomescape.IntegrationTestSupport;
import roomescape.controller.dto.TokenRequest;

class ThemeControllerTest extends IntegrationTestSupport {

    String token;
    String createdId;
    int themeSize;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("테마 생성 조회")
    @TestFactory
    Stream<DynamicTest> dynamicUserTestsFromCollection() {
        return Stream.of(
                dynamicTest("어드민으로 로그인", () -> {
                    token = RestAssured
                            .given().log().all()
                            .body(new TokenRequest(ADMIN_EMAIL, ADMIN_PASSWORD))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/login")
                            .then().log().all().extract().header("Set-Cookie").split("=")[1];
                }),
                dynamicTest("테마 목록을 조회한다.", () -> {
                    themeSize = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().get("/themes")
                            .then().log().all()
                            .statusCode(200).extract()
                            .response().jsonPath().getList("$").size();
                }),
                dynamicTest("테마을 추가한다.", () -> {
                    Map<String, String> param = Map.of("name", "테마_테스트",
                            "description", "설명_테스트",
                            "thumbnail", "섬네일");

                    createdId = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .body(param)
                            .when().post("/themes")
                            .then().log().all()
                            .statusCode(201).extract().header("location").split("/")[2];
                }),
                dynamicTest("테마 목록 개수가 1증가한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().get("/themes")
                            .then().log().all()
                            .statusCode(200).body("size()", is(themeSize + 1));
                }),
                dynamicTest("테마이름이 비어있을 수 없다.", () -> {
                    Map<String, String> param = Map.of("name", "  ",
                            "description", "설명_테스트",
                            "thumbnail", "섬네일");

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .body(param)
                            .when().post("/themes")
                            .then().log().all()
                            .statusCode(400);
                }),
                dynamicTest("테마를 삭제한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().delete("/themes/" + createdId)
                            .then().log().all()
                            .statusCode(204);
                }),
                dynamicTest("테마 목록 개수가 1감소한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().get("/themes")
                            .then().log().all()
                            .statusCode(200).body("size()", is(themeSize));
                })
        );
    }

    @DisplayName("인기 테마 조회")
    @Test
    void themeNameBlank() {
        Map<String, String> params = Map.of("startDate", "2024-05-04",
                "endDate", "2024-05-09",
                "limit", "2");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParams(params)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200);
    }
}
