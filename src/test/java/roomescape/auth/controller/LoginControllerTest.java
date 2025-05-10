package roomescape.auth.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginControllerTest {

    private static String getToken(String email, String password) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", email, "password", password))
                .when().post("/login")
                .then().statusCode(200)
                .extract().response().getDetailedCookies().getValue("token");
    }

    @DisplayName("어드민 계정으로 로그인 한다")
    @Test
    void adminLoginTest() {
        final String token = getToken("admin@email.com", "password");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().statusCode(200);
    }

    @DisplayName("사용자 계정으로 로그인 한다")
    @Test
    void userLoginTest() {
        final String token = getToken("user@email.com", "password");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().statusCode(200);
    }

    @DisplayName("잘못된 비밀 번호로 로그인 하는 경우 예외를 던진다")
    @Test
    void adminLoginTest_WhenPasswordIsWrong() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", "admin@email.com", "password", "wrong"))
                .when().post("/login")
                .then().statusCode(401);
    }

    @DisplayName("잘못된 이메일로 로그인 하는 경우 예외를 던진다")
    @Test
    void adminLoginTest_WhenEmailNotExist() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", "wrong@email.com", "password", "password"))
                .when().post("/login")
                .then().statusCode(401);
    }
}
