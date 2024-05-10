package roomescape.presentation.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.restassured.RestAssured;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import roomescape.application.member.dto.request.MemberLoginRequest;
import roomescape.application.member.dto.request.MemberRegisterRequest;
import roomescape.application.member.dto.response.MemberResponse;

class AuthAcceptanceTest extends AcceptanceTest {

    @Value("${jwt.secret}")
    private String secret;

    @Test
    @DisplayName("사용자가 로그인한다.")
    void loginTest() {
        MemberRegisterRequest request = new MemberRegisterRequest("aru", "aru@test.com", "12341234");
        long memberId = AcceptanceFixture.registerMember(request)
                .body()
                .as(MemberResponse.class)
                .id();

        String token = RestAssured.given().log().all()
                .contentType("application/json")
                .body(new MemberLoginRequest("aru@test.com", "12341234"))
                .when().post("/login")
                .then().log().all()
                .statusCode(204)
                .extract()
                .cookie("token");

        JwtParser build = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .build();
        Claims claims = build.parseClaimsJws(token).getBody();
        assertThat(claims.get("memberId", Long.class)).isEqualTo(memberId);
    }
}
