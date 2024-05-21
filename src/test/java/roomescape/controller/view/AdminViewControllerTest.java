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
        RestAssured.given().log().all()
                .cookie(adminCookie())
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 예약 관리 페이지를 매핑한다.")
    void reservation() {
        RestAssured.given().log().all()
                .cookie(adminCookie())
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 시간 관리 페이지를 매핑한다.")
    void time() {
        RestAssured.given().log().all()
                .cookie(adminCookie())
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 테마 관리 페이지를 매핑한다.")
    void theme() {
        RestAssured.given().log().all()
                .cookie(adminCookie())
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자가 아닌 사용자가 관리자 페이지를 요청할 경우 상태코드 401을 응답한다.")
    void adminException() {
        RestAssured.given().log().all()
                .cookie(userCookie())
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(401);
    }

    Cookie adminCookie() {
        return cookie("admin@woowa.com");
    }

    Cookie userCookie() {
        return cookie("zeus@woowa.com");
    }

    Cookie cookie(String value) {
        String token = jwtProvider.create(value);
        return new Cookie.Builder("token", token).build();
    }
}
