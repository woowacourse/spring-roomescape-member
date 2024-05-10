package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("회원 가입 요청 성공 시 200을 응답한다.")
    @Test
    void given_when_signupSuccess_then_statusCodeIsOk() {
        Map<String, Object> params = new HashMap<>();
        params.put("email", "wedge@test.com");
        params.put("password", "1q2w3e4r!");
        params.put("name", "웨지");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/signup")
                .then().log().all()
                .statusCode(200);
    }


    @DisplayName("회원 가입 요청 실패 시 400을 응답한다.")
    @Test
    void given_when_signupFail_then_statusCodeIsBadRequest() {
        Map<String, Object> params = new HashMap<>();
        params.put("email", "poke@test.com");
        params.put("password", "1q2w3e4r!");
        params.put("name", "poke");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/signup")
                .then().log().all()
                .statusCode(400);
    }
}
