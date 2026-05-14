package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Test
    void 테마_조회() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "이든의 공포 하우스");
        params.put("description", "이든이 귀신으로 나옴");
        params.put("imgUrl", "링크~");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/v1/admin/themes");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/v1/admin/themes");

        RestAssured.given().log().all()
                .when().get("/api/v1/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[1].id", is(2));
    }

    @Test
    @Sql("/popular-themes.sql")
    void 최근_1주동안_예약이_많았던_테마_상위_10개를_조회한다() {
        RestAssured.given().log().all()
                .when().get("/api/v1/themes/popular?from=2026-05-01&to=2026-05-07")
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

    @Test
    void 인기_테마_조회_시_조건을_만족하지_않으면_400을_반환한다() {
        RestAssured.given().log().all()
                .when().get("/api/v1/themes/popular?from=2026-05-01")
                .then().log().all()
                .statusCode(400)
                .body("status", is(400))
                .body("errorCode", is("UNSATISFIED_PARAMETERS"))
                .body("message", containsString("요청 파라미터 조건이 맞지 않습니다"));
    }
}
