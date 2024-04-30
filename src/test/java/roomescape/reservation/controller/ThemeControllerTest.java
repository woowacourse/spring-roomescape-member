package roomescape.reservation.controller;

import static org.hamcrest.Matchers.is;

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
import roomescape.reservation.dto.ReservationTimeRequest;
import roomescape.reservation.dto.ThemeRequest;
import roomescape.reservation.service.ThemeService;
import roomescape.util.ControllerTest;

@DisplayName("테마 API 통합 테스트")
class ThemeControllerTest extends ControllerTest {
    @Autowired
    ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeService.create(new ThemeRequest("name", "description", "thumbnail"));
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
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마 조회 시, 200을 반환한다.")
    @Test
    void findAll() {
        //given & when & then
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("테마 삭제 시, 204를 반환한다.")
    @Test
    void delete() {
        //given & when & then
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("테마 생성 시, 잘못된 형식에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "      "})
    void createBadRequest(String name) {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", "Hi, I am Siso");
        params.put("thumbnail", "thumbnail");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }
}
