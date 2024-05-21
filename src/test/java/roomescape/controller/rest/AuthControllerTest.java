package roomescape.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.BaseTest;
import roomescape.controller.rest.request.LoginRequest;
import roomescape.controller.rest.response.LoginResponse;
import roomescape.util.TokenProvider;

class AuthControllerTest extends BaseTest {

    @Autowired
    TokenProvider tokenProvider;

    @DisplayName("로그인하면 토큰을 쿠키에 담아 상태코드 200을 응답한다.")
    @Test
    void login() {
        String expected = "zeus@woowa.com";
        LoginRequest request = new LoginRequest(expected, "qwerty");

        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().cookie("token");

        String actual = tokenProvider.extract(token);
        assertEquals(expected, actual);
    }

    @DisplayName("사용자 이름을 body에 담아 상태코드 200을 응답한다.")
    @Test
    void check() {
        String subject = "zeus@woowa.com";
        String token = tokenProvider.create(subject);
        String actual = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract().body()
                .as(LoginResponse.class)
                .name();
        assertEquals("Zeus", actual);
    }

    @DisplayName("토큰 없이 사용자 정보를 요청할 경우 상태코드 401을 응답한다.")
    @Test
    void checkException() {
        RestAssured.given().log().all()
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("사용자가 로그아웃에 성공하면 상태코드 200을 응답한다.")
    @Test
    void logout() {
        RestAssured.given().log().all()
                .when().get("/logout")
                .then().log().all()
                .statusCode(200)
                .cookie("token", Matchers.emptyString());
    }
}
