package roomescape.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.application.member.dto.request.MemberLoginRequest;
import roomescape.application.member.dto.request.MemberRegisterRequest;
import roomescape.application.member.dto.response.MemberResponse;

class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("사용자가 로그인한다.")
    void loginTest() {
        MemberRegisterRequest request = new MemberRegisterRequest("aru", "aru@test.com", "12341234");
        long memberId = AcceptanceFixture.registerMember(request)
                .body()
                .as(MemberResponse.class)
                .id();

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(new MemberLoginRequest("aru@test.com", "12341234"))
                .when().post("/login")
                .then().log().all()
                .statusCode(204);
    }
}
