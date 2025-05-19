package roomescape.infrastructure;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtProvider.class)
@TestPropertySource(properties = {
        "security.jwt.token.secret-key=dGVzdGluZ0tleUhleGFEcml2ZW5WYWx1ZTIzNDU2Nzg5MCE=",
        "security.jwt.token.expire-length=300000"
})
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("사용자 아이디로 토큰을 생성한다")
    void createToken() {
        // given
        Long memberId = 1L;

        // when
        String token = jwtProvider.createToken(memberId.toString());

        // then
        Assertions.assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("토큰의 값을 꺼낸다.")
    void getPayload() {
        // given
        Long memberId = 1L;
        String token = jwtProvider.createToken(memberId.toString());

        // when
        Long payload = Long.valueOf(jwtProvider.getPayload(token));

        // then
        Assertions.assertThat(payload).isEqualTo(memberId);
    }

    @Test
    @DisplayName("유효한 토큰이면 검증에 성공한다.")
    void validateToken() {
        // given
        Long memberId = 1L;
        String token = jwtProvider.createToken(memberId.toString());

        // when
        boolean expectedTrue = jwtProvider.validateToken(token);

        // then
        Assertions.assertThat(expectedTrue).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 토큰이면 검증에 실패한다.")
    void invalidToken() {
        // given
        String token = "1234";

        // when
        boolean expectedFalse = jwtProvider.validateToken(token);

        // then
        Assertions.assertThat(expectedFalse).isFalse();
    }
}