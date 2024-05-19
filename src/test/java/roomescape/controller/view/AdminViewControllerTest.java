package roomescape.controller.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import roomescape.BaseTest;
import roomescape.util.JwtProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminViewControllerTest extends BaseTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    @DisplayName("방탈출 관리 홈페이지를 매핑한다.")
    void index() {
        String token = jwtProvider.createToken("admin@woowa.com");
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
        String token = jwtProvider.createToken("admin@woowa.com");
        Cookie cookie = new Cookie.Builder("token", token).build();
        RestAssured.given().log().all()
                .cookie(cookie)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 테마 관리 페이지를 매핑한다.")
    void theme() {
        String token = jwtProvider.createToken("admin@woowa.com");
        Cookie cookie = new Cookie.Builder("token", token).build();
        RestAssured.given().log().all()
                .cookie(cookie)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
