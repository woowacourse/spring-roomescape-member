package roomescape.controller.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import roomescape.BaseTest;
import roomescape.util.TokenProvider;

class AdminViewControllerTest extends BaseTest {

    @Autowired
    TokenProvider jwtProvider;

    @Test
    @DisplayName("방탈출 관리 홈페이지를 매핑한다.")
    void index() {
        String token = jwtProvider.create("admin@woowa.com");
        Cookie cookie = new Cookie.Builder("token", token).build();
        RestAssured.given().log().all()
                .cookie(cookie)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 예약 관리 페이지를 매핑한다.")
    void reservation() {
        String token = jwtProvider.create("admin@woowa.com");
        Cookie cookie = new Cookie.Builder("token", token).build();
        RestAssured.given().log().all()
                .cookie(cookie)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 시간 관리 페이지를 매핑한다.")
    void time() {
        String token = jwtProvider.create("admin@woowa.com");
        Cookie cookie = new Cookie.Builder("token", token).build();
        RestAssured.given().log().all()
                .cookie(cookie)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 테마 관리 페이지를 매핑한다.")
    void theme() {
        String token = jwtProvider.create("admin@woowa.com");
        Cookie cookie = new Cookie.Builder("token", token).build();
        RestAssured.given().log().all()
                .cookie(cookie)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
