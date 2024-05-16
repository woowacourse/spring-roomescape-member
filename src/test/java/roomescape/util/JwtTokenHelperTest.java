package roomescape.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import roomescape.fixture.Fixture;
import roomescape.member.model.Member;

class JwtTokenHelperTest {

    private static JwtTokenHelper jwtTokenHelper;

    @BeforeAll
    static void beforeAll() {
        jwtTokenHelper = new JwtTokenHelper();
        ReflectionTestUtils.setField(jwtTokenHelper, "secretKey", "test_secret_key");
        ReflectionTestUtils.setField(jwtTokenHelper, "validityInMilliseconds", 3600000L);
    }

    @Test
    @DisplayName("멤버 객체를 받아 토큰을 생성한다.")
    void createToken() {
        Member member = Fixture.MEMBER_1;

        String token = jwtTokenHelper.createToken(member);

        assertNotNull(token);
    }

    @Test
    @DisplayName("쿠키에서 토큰을 추출한다.")
    void extractTokenFromRequest() {
        final String token = jwtTokenHelper.createToken(Fixture.MEMBER_1); // memberId: 1
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("token", token);

        final String expected = jwtTokenHelper.extractTokenFromCookies(cookies);

        assertEquals(expected, token);
    }

    @Test
    @DisplayName("토큰에서 페이로드 클레임을 추출한다.")
    void getPayloadClaimFromToken() {
        final String token = jwtTokenHelper.createToken(Fixture.MEMBER_1);

        final Long memberId = jwtTokenHelper.getPayloadClaimFromToken(token, "memberId", Long.class);

        assertEquals(Fixture.MEMBER_1.getId(), memberId);
    }
}
