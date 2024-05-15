package roomescape.reservation.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.util.JwtTokenProvider.TOKEN;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import roomescape.member.dto.LoginRequest;
import roomescape.reservation.dto.ThemeRequest;
import roomescape.reservation.service.ThemeService;
import roomescape.util.ControllerTest;

@DisplayName("테마 API 통합 테스트")
class ThemeControllerTest extends ControllerTest {
    private String adminToken;

    @BeforeEach
    void setDate() {
        adminToken = RestAssured
                .given().log().all()
                .body(new LoginRequest("admin@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract()
                .cookie("token");
    }

    @DisplayName("테마 생성 시, 201을 반환한다.")
    @Test
    void create() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "siso");
        params.put("description", "Hi, I am Siso");
        params.put("thumbnail", "thumbnail");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, adminToken)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마 생성 시, 잘못된 이름 형식에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "      "})
    void createBadNameRequest(String name) {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", "Hi, I am Siso");
        params.put("thumbnail", "thumbnail");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, adminToken)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("테마 생성 시, 잘못된 설명 형식에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "      "})
    void createBadDescriptionRequest(String description) {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "siso");
        params.put("description", description);
        params.put("thumbnail", "thumbnail");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, adminToken)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("테마 생성 시, 잘못된 썸네일 형식에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "      "})
    void createBadThumbnailRequest(String description) {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "siso");
        params.put("description", "Hi, I am Siso");
        params.put("thumbnail", description);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, adminToken)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("테마 조회 시, 200을 반환한다.")
    @Test
    void findAll() {
        //given & when & then
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));
    }

    @DisplayName("테마 삭제 시, 204를 반환한다.")
    @Test
    void delete() {
        //given & when & then
        RestAssured.given().log().all()
                .cookie(TOKEN, adminToken)
                .when().delete("/themes/4")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("인기 테마 조회 시, 200를 반환한다.")
    @Test
    void findPopular() {
        //given & when & then
        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200);
    }
}
