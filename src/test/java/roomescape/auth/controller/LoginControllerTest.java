package roomescape.auth.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.dto.TokenRequest;
import roomescape.member.MemberFixture;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginControllerTest {

    @Test
    @DisplayName("로그인을 테스트한다.")
    void login() {
        TokenRequest tokenRequest = MemberFixture.tokenRequest("member1@email.com", "1234");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(tokenRequest)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그인이 됐는지 체크한다.")
    void checkLogin() {
        Map<String, String> cookies = MemberFixture.loginUser();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200);
    }
}
