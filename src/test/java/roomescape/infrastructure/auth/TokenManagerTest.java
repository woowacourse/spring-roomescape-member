package roomescape.infrastructure.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.member.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenManagerTest {

    @Autowired
    private TokenManager tokenManager;

    @Test
    @DisplayName("토큰을 만들 수 있다")
    void generate() {
        //given
        Member member = new Member(1L, "콜리", "email@naver.com", "password", "USER");

        //when-then
        assertThat(tokenManager.generate(member)).isInstanceOf(Token.class);
    }

    @Test
    @DisplayName("토큰을 기반으로 memberId를 복호화할 수 있다")
    void getMemberId() {
        //given
        Member member = new Member(1L, "콜리", "email@naver.com", "password", "USER");
        Token token = tokenManager.generate(member);

        //when-then
        assertThat(tokenManager.getMemberId(token).longValue()).isEqualTo(member.getId().longValue());
    }
}