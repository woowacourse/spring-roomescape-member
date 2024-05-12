package roomescape.member.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.config.IntegrationTest;
import roomescape.member.dto.MemberSignUpRequest;
import roomescape.util.CookieUtils;

class MemberApiControllerTest extends IntegrationTest {

    @DisplayName("회원가입에 성공하면 201 응답과 Location 헤더에 리소스 저장 경로를 받는다.")
    @Test
    void signup() {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("카키", "kaki@email.com", "1234");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(memberSignUpRequest)
                .when()
                .post("/members")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/members/1");
    }

    @DisplayName("회원 목록 조회에 성공하면 200 응답을 받는다.")
    @Test
    void findAll() {
        RestAssured.given().log().all()
                .cookie(CookieUtils.TOKEN_KEY, "cookieValue")
                .when()
                .get("/members")
                .then().log().all()
                .statusCode(200);
    }
}
