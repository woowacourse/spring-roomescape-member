package roomescape.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import roomescape.auth.domain.AuthInfo;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Role;
import roomescape.util.ControllerTest;

@DisplayName("회원 API 통합 테스트")
class AuthControllerTest extends ControllerTest {
    @Autowired
    AuthService authService;

    @DisplayName("로그인 페이지 조회 시, 200을 반환한다.")
    @Test
    void getLoginPage() {
        //given & when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("로그인 시, 200을 반환한다.")
    @Test
    void login() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("email", "dev.chocochip@gmail.com");
        params.put("password", "1234");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("토큰을 통해 회원을 검증할 경우, 200을 반환한다.")
    @Test
    void checkLogin() {
        //given
        String email = "dev.chocochip@gmail.com";
        String password = "1234";
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        //when & then
        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        //when
        AuthInfo authInfo = RestAssured
                .given().log().all()
                .cookie("token", token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().as(AuthInfo.class);

        //then
        assertThat(authInfo.getId()).isEqualTo(1);
        assertThat(authInfo.getName()).isEqualTo("초코칩");
        assertThat(authInfo.getRole()).isEqualTo(Role.USER);
        assertThat(authInfo.getEmail()).isEqualTo(email);
    }

    @DisplayName("로그아웃 시, 200을 반환한다.")
    @Test
    void logout() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("email", "dev.chocochip@gmail.com");
        params.put("password", "1234");

        //when & then
        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        //when
        String cookie = RestAssured
                .given().log().all()
                .cookie("token", token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/logout")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().cookie("token");

        //then
        assertThat(cookie).isBlank();
    }

    @DisplayName("회원 가입 시, 201을 반환한다.")
    @Test
    void signup() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "chocochip2");
        params.put("email", "dev.chocochip2@gmail.com");
        params.put("password", "12345");

        //when & then
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/signup")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("중복된 메일로 회원 생성 시, 409을 반환한다.")
    @Test
    void createDuplicatedEmail() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "chocochip3");
        params.put("email", "dev.chocochip2@gmail.com");
        params.put("password", "1234");

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/signup")
                .then().log().all()
                .statusCode(201);

        //when & then
        params.put("name", "chocchocchop");

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/signup")
                .then().log().all()
                .statusCode(409);

    }

    @DisplayName("메일 형식이 아닐 경우, 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "chocochip", "chocochip@"})
    void signupInvalidMailFormat(String invalidMail) {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "chocochip2");
        params.put("email", invalidMail);
        params.put("password", "12345");

        //when & then
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/signup")
                .then().log().all()
                .statusCode(400);
    }
}
