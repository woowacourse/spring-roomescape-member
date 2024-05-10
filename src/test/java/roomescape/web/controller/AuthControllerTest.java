package roomescape.web.controller;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.infrastructure.authentication.AuthenticationRequest;
import roomescape.support.IntegrationTestSupport;

//TODO: 테스트 개선
class AuthControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("로그인을 한다.")
    void login() {
        String email = "admin@test.com";
        String password = "password";
        AuthenticationRequest request = new AuthenticationRequest(email, password);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .header("Set-Cookie", startsWith("token="))
                .header("Set-Cookie", endsWith("; Path=/; HttpOnly"));
    }

    @Test
    @DisplayName("존재하지 않는 정보로 로그인 할 수 없다.")
    void cantLogin() {
        String email = "unknown@woowacourse.com";
        String password = "password";
        AuthenticationRequest request = new AuthenticationRequest(email, password);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(400)
                .body("message", is("올바른 인증 정보를 입력해주세요."));
    }

    @Test
    @DisplayName("로그인 아이디는 이메일 형식이어야 한다.")
    void validateEmailFormat() {
        String invalidEmailFormat = "admin#test.com";
        String password = "password";
        AuthenticationRequest request = new AuthenticationRequest(invalidEmailFormat, password);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("올바른 형식의 이메일 주소여야 합니다"));
    }

    @Test
    @DisplayName("로그인 아이디는 필수이다.")
    void validateEmail() {
        String password = "password";
        AuthenticationRequest request = new AuthenticationRequest(null, password);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("이메일은 필수입니다."));
    }

    @Test
    @DisplayName("비밀번호는 필수이다.")
    void validatePassword() {
        String email = "admin@test.com";
        AuthenticationRequest request = new AuthenticationRequest(email, null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("비밀번호는 필수입니다."));
    }

    @Test
    @DisplayName("사용자 정보를 조회한다.")
    void check() {
        String email = "admin@test.com";
        String password = "password";
        AuthenticationRequest request = new AuthenticationRequest(email, password);

        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .body("name", is("어드민"));
    }

    @Test
    @DisplayName("유효한 토큰이 존재해야한다.")
    void requiredToken() {
        RestAssured.given().log().all()
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401)
                .body("message", is("유효한 인가 정보를 입력해주세요."));
    }
}
