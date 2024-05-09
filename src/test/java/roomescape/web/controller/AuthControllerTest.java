package roomescape.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.service.request.AuthenticationRequest;
import roomescape.support.IntegrationTestSupport;

class AuthControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("로그인을 한다.")
    void login() {
        String email = "admin@woowacourse.com";
        String password = "password";
        AuthenticationRequest request = new AuthenticationRequest(email, password);

        // TODO: 타입 개선
        AuthenticationRequest response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .header("Set-Cookie", startsWith("token="))
                .header("Set-Cookie", endsWith("; Path=/; HttpOnly"))
                .extract().as(AuthenticationRequest.class);

        assertThat(response).isEqualTo(request);
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
                .statusCode(401)
                .body("message", is("로그인에 실패했습니다."));
    }

    @Test
    @DisplayName("로그인 아이디는 이메일 형식이어야 한다.")
    void validateEmailFormat() {
        String invalidEmailFormat = "admin#woowacourse.com";
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
        String email = "admin@woowacourse.com";
        AuthenticationRequest request = new AuthenticationRequest(email, null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("비밀번호는 필수입니다."));
    }

    // TODO: GET /login/check
    @Test
    @DisplayName("사용자 정보를 조회한다.")
    void check() {
        // given

        // when

        // then
    }
}
