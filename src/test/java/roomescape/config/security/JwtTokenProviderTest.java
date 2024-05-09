package roomescape.config.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberName;
import roomescape.member.dto.LoginMember;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private Member member;

    @BeforeEach
    void init() {
        member = new Member(1L, new MemberName("카키"), "kaki@email.com", "1234");
    }

    @DisplayName("JWT 토큰을 생성한다.")
    @Test
    void generateToken() {
        String token = jwtTokenProvider.generateToken(member);

        assertThat(token).isNotNull();
    }

    @DisplayName("발급받은 JWT 토큰으로 회원 정보를 반환한다.")
    @Test
    void getMemberId() {
        String token = jwtTokenProvider.generateToken(member);
        LoginMember loginMember = jwtTokenProvider.getMember(token);

        assertThat(member.getId()).isEqualTo(loginMember.id());
    }

    @DisplayName("유효한 토큰인지 확인한다.")
    @Test
    void validateAbleToken() {
        jwtTokenProvider.generateToken(member);
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJtZW1iZXJJZMTU4NzgwfQ.OPANxAz1dQfTo91uX3au03M";

        assertThatThrownBy(() -> jwtTokenProvider.getMember(invalidToken))
                .isInstanceOf(SignatureException.class);
    }
}
