package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.controller.dto.SignupRequest;
import roomescape.auth.controller.dto.TokenRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthApiTest {

    @Test
    @DisplayName("회원가입 테스트")
    void signupTest() {
        // given
        SignupRequest signupRequest = new SignupRequest(
                "레몬",
                "suwon@naver.com",
                "123"
        );
        // when
        // then
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("/members")
                .then().statusCode(201)
                .extract().response();
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() {
        // given
        SignupRequest signupRequest = new SignupRequest(
                "레몬",
                "suwon@naver.com",
                "123"
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("/members")
                .then().statusCode(201)
                .extract().response();

        TokenRequest tokenRequest = new TokenRequest("suwon@naver.com", "123");
        // when
        // then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(tokenRequest)
                .when().post("/login")
                .then().statusCode(201);
    }

    @Test
    @DisplayName("로그인 체크 테스트")
    void loginCheckTest() {
        // given
        SignupRequest signupRequest = new SignupRequest(
                "레몬",
                "suwon@naver.com",
                "123"
        );
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("/members")
                .then().statusCode(201)
                .extract().response();

        TokenRequest tokenRequest = new TokenRequest("suwon@naver.com", "123");
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(tokenRequest)
                .when().post("/login")
                .then().statusCode(201)
                .extract().response();

        String token = response.getCookie("token");

        //when
        //then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().statusCode(200);

    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logoutTest() {
        // given
        SignupRequest signupRequest = new SignupRequest(
                "레몬",
                "suwon@naver.com",
                "123"
        );
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("/members")
                .then().statusCode(201)
                .extract().response();

        TokenRequest tokenRequest = new TokenRequest("suwon@naver.com", "123");
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(tokenRequest)
                .when().post("/login")
                .then().statusCode(201)
                .extract().response();

        String token = response.getCookie("token");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().statusCode(200);

        // when
        // then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().post("/logout")
                .then().statusCode(200);
    }
}
