package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import roomescape.domain.MemberRole;
import roomescape.exception.UnAuthorizedException;
import roomescape.service.result.MemberResult;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private static final String secretKey = "test1234test1234test1234test1234";
    private final int validityInMilliseconds = 3600000; //1h
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", validityInMilliseconds);
    }

    @Test
    void 로그인_결과를_통해_토큰을_생성할_수_있다() {
        //given
        MemberResult memberResult = new MemberResult(1L, "Eve", MemberRole.USER, "eve@example.com");

        //when
        String token = jwtTokenProvider.createToken(memberResult);

        //then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void 토큰으로부터_id를_추출할_수_있다() {
        //given
        Long memberId = 1L;
        String token = generateValidToken(memberId);

        //when
        Long id = jwtTokenProvider.extractIdFromToken(token);

        //then
        assertThat(id).isEqualTo(memberId);
    }

    @Test
    void 토큰이_만료됐을_경우_예외를_던진다() {
        //given
        Long memberId = 1L;
        String token = generateExpiredToken(memberId);

        //when & then
        assertThatThrownBy(() -> jwtTokenProvider.extractIdFromToken(token))
                .isInstanceOf(UnAuthorizedException.class);
    }

    private String generateValidToken(Long id) {
        Date expirationDate = new Date(System.currentTimeMillis() + validityInMilliseconds);

        return Jwts.builder()
                .subject(id.toString())
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    private String generateExpiredToken(Long id) {
        Date expirationDate = new Date(System.currentTimeMillis() - 1000);

        return Jwts.builder()
                .subject(id.toString())
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}