package roomescape.admin.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class LoginControllerTest {


    @Test
    void 회원가입_테스트() {
        Map<String, String> signupParam = Map.of("username", "signup@username.com", "password", "password");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupParam)
                .when().post("/signup")
                .then()
                .statusCode(201);
    }

    @Test
    void jwt_토큰_로그인_테스트() {
        Map<String, String> signupParam = Map.of("username", "token-login@username.com", "password", "password");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupParam)
                .when().post("/signup")
                .then()
                .statusCode(201);

        Map<String, String> loginParam = Map.of("username", "token-login@username.com", "password", "password");
        String header = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/login")
                .then().log().all()
                .extract().header("Set-Cookie");

        String token = header.split("token=")[1];

        Assertions.assertAll(
                () -> assertThat(header).isNotNull(),
                () -> assertThat(token).isNotEmpty()
        );
    }
}
