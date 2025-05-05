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
    void jwt_토큰_로그인_테스트() {
        Map<String, String> loginParam = Map.of("username", "admin@email.com", "password", "password");
        String header = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/login")
                .then().extract().header("Set-Cookie");

        Assertions.assertAll(
                () -> assertThat(header).isNotNull()
//                () -> assertThat(header.split("token=")).hasSize(1)
        );
    }
}
