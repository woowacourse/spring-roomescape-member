package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.AuthInformationResponse;
import roomescape.member.domain.Member;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class AuthAcceptanceTest extends ApiAcceptanceTest {
    @Test
    @DisplayName("[2 - Step4] 사용자가 로그인한다.")
    void login() {
        // given
        Member member = createTestMember();
        LoginRequest request = new LoginRequest(member.getEmail(), member.getPassword());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .extract();
        String token = response.response().getCookie("token");

        // then
        assertSoftly(softly -> {
            checkHttpStatusOk(softly, response);
            softly.assertThat(token.split("\\.")).hasSize(3);
        });
    }

    @Test
    @DisplayName("[2 - Step4] 사용자 인증 정보를 조회한다.")
    void checkAuthInformation() {
        // given
        Member member = createTestMember();
        String token = createTestToken(member);
        Cookie cookie = new Cookie.Builder("token", token).build();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .when().get("/login/check")
                .then().log().all()
                .extract();
        AuthInformationResponse authInfoResponse = response.as(AuthInformationResponse.class);

        // then
        assertSoftly(softly -> {
            checkHttpStatusOk(softly, response);
            softly.assertThat(authInfoResponse.name()).isEqualTo(member.getName());
        });
    }
}
