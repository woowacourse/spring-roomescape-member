package roomescape.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.controller.request.UserLoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {

    @DisplayName("로그인 요청시 쿠키를 응답한다.")
    @Test
    void should_response_cookie_when_login() {
        UserLoginRequest request = new UserLoginRequest("1234", "sun@email.com");

        String cookie = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().statusCode(200)
                .extract().header("Set-Cookie");

        assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(cookie).isNotBlank();
            assertSoftly.assertThat(cookie).containsPattern("^token=");
        });
    }
}
