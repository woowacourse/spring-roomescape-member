package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    private Map<String, Object> themeParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "이든의 공포 하우스");
        params.put("description", "이든이 귀신으로 나옴");
        params.put("imgUrl", "링크~");
        return params;
    }

    private Map<String, Object> reservationParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);
        return params;
    }

    @Test
    void 테마_추가() {
        Map<String, Object> adminThemeParams = themeParams();
        adminThemeParams.put("userName", "ADMIN");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminThemeParams)
                .when().post("/api/v1/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/v1/themes/1")
                .body("id", is(1));
    }

    @Test
    void 테마_추가시_관리자가_아닌경우_401을_반환한다() {
        Map<String, Object> userThemeParams = themeParams();
        userThemeParams.put("userName", "정콩이");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(userThemeParams)
                .when().post("/api/v1/themes")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    void 테마_조회() {
        Map<String, Object> adminThemeParams = themeParams();
        adminThemeParams.put("userName", "ADMIN");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminThemeParams)
                .when().post("/api/v1/themes");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminThemeParams)
                .when().post("/api/v1/themes");

        RestAssured.given().log().all()
                .when().get("/api/v1/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[1].id", is(2));
    }

    @Test
    void 테마_삭제() {
        Map<String, Object> adminThemeParams = themeParams();
        adminThemeParams.put("userName", "ADMIN");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminThemeParams)
                .when().post("/api/v1/themes");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("userName", "ADMIN"))
                .when().delete("/api/v1/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 테마_삭제시_관리자가_아닌경우_401을_반환한다() {
        Map<String, Object> adminThemeParams = themeParams();
        adminThemeParams.put("userName", "ADMIN");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminThemeParams)
                .when().post("/api/v1/themes");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("userName", "정콩이"))
                .when().delete("/api/v1/themes/1")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    void 예약이_존재하는_테마를_삭제하면_409를_반환한다() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/times");

        Map<String, Object> adminThemeParams = themeParams();
        adminThemeParams.put("userName", "ADMIN");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminThemeParams)
                .when().post("/api/v1/themes");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("userName", "ADMIN"))
                .when().delete("/api/v1/themes/1")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @Sql("/popular-themes.sql")
    void 최근_1주동안_예약이_많았던_테마_상위_10개를_조회한다() {
        RestAssured.given().log().all()
                .when().get("/api/v1/themes?from=2026-05-01&to=2026-05-07")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
                .body("[0].name", is("이든의 공포 하우스"))
                .body("[1].name", is("정콩이의 방탈출"))
                .body("[2].name", is("우주 정거장 탈출"))
                .body("[3].name", is("고대 유적의 비밀"))
                .body("[4].name", is("마법사의 서재"))
                .body("[5].name", is("좀비 연구소"))
                .body("[6].name", is("해적선의 보물"))
                .body("[7].name", is("미스터리 호텔"))
                .body("[8].name", is("시간의 문"))
                .body("[9].name", is("사라진 탐정"));
    }
}
