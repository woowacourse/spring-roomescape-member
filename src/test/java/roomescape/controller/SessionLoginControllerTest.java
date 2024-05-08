package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionLoginControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

//    @Test
    @DisplayName("유효한 회원의 로그인 요청")
    void login() {
        final Map<String, String> params = Map.of("password", "0000", "email", "redddy@gmail.com");

        //TODO cookie 수정
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzE1MTU2OTE1LCJleHAiOjE3MTUxNjA1MTV9._1KYAthr4G8r-joWTgdnQ6udk8xeZQ3uea5PI7vLiL4")
                .header("Keep-Alive", "timeout=60");
    }
}
