package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.domain.Member;
import roomescape.service.tokenmanager.TokenProvider;
import roomescape.service.dto.TokenRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JwtTokenProviderTest {
    private TokenProvider tokenProvider;

    @LocalServerPort
    int port;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long expireLength;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        tokenProvider = new JwtTokenProvider(secretKey, expireLength);
    }

    @DisplayName("토큰을 생성한다. - 통합 테스트")
    @Test
    void testCreateToken() {
        String token = RestAssured.given().log().all()
                .body(new TokenRequest("password", "admin@email.com"))
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().cookies().extract().cookie("token");

        assertNotNull(token);
    }

    @DisplayName("토큰을 생성한다. - 단위 테스트")
    @Test
    void testCreateTokenWithUnit() {
        Member member = new Member(1, "lily", "lily@email.com", "password", "");
        String token = tokenProvider.createToken(member);

        assertAll(
                () -> assertNotNull(token),
                () -> assertThatCode(() -> tokenProvider.createToken(member)).doesNotThrowAnyException()
        );
    }
}
