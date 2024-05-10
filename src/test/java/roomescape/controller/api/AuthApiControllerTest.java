package roomescape.controller.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import roomescape.service.dto.request.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthApiControllerTest {

    @Test
    @DisplayName("올바른 로그인 정보를 입력할 시, 로그인에 성공한다.")
    void authenticatedMemberLogin_Success() {
        RestAssured.given().log().all()
                .body(new LoginRequest("user@naver.com", "1234"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("잘못된 로그인 정보를 입력할 시, 로그인에 실패한다.")
    void authenticatedMemberLogin_Failure() {
        RestAssured.given().log().all()
                .body(new LoginRequest("wrond@naver.com", "1234"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }
}
