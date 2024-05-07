package roomescape.member.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.util.ControllerTest;

@DisplayName("사용자 API 테스트")
class MemberControllerTest extends ControllerTest {
    @DisplayName("로그인 폼 페이지 조회에 성공한다.")
    @Test
    void popularPage() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200)
                .body(containsString("Login"));
    }
}
