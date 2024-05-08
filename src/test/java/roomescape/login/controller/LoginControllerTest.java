package roomescape.login.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import roomescape.ControllerTest;
import roomescape.domain.login.dto.LoginRequest;

class LoginControllerTest extends ControllerTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "1234";

    @DisplayName("로그인 요청을 하면 쿠키와 함께 응답한다.(200 OK)")
    @Test
    void should_response_with_cookie_when_request_login() {
        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);
        String cookie = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];

        assertThat(cookie).isNotNull();
    }

}
