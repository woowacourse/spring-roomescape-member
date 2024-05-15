package roomescape.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.dto.auth.TokenRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 로그인에_성공한다() {
        memberRepository.save(new Member(new MemberName("레모네"), "lemone@wooteco.com", "lemone1234"));
        TokenRequest tokenRequest = new TokenRequest("lemone1234", "lemone@wooteco.com");

        assertDoesNotThrow(() -> authService.createToken(tokenRequest));
    }

    @Test
    void 존재하지_않는_email로_로그인하면_예외가_발생한다() {
        memberRepository.save(new Member(new MemberName("레모네"), "lemone@wooteco.com", "lemone1234"));
        TokenRequest tokenRequest = new TokenRequest("lemone1234", "lemone@invalid.com");

        assertThatThrownBy(() -> authService.createToken(tokenRequest))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("로그인 정보가 잘못되었습니다.");
    }

    @Test
    void 존재하지_않는_password로_로그인하면_예외가_발생한다() {
        memberRepository.save(new Member(new MemberName("레모네"), "lemone@wooteco.com", "lemone1234"));
        TokenRequest tokenRequest = new TokenRequest("invalid1234", "lemone@invalid.com");

        assertThatThrownBy(() -> authService.createToken(tokenRequest))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("로그인 정보가 잘못되었습니다.");
    }
}
