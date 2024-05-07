package roomescape.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import roomescape.auth.controller.dto.MemberResponse;
import roomescape.auth.service.AuthService;
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
    void create() {
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
        MemberResponse member = RestAssured
                .given().log().all()
                .cookie("token", token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().as(MemberResponse.class);

        //then
        assertThat(member.name()).isEqualTo("초코칩");
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
}
