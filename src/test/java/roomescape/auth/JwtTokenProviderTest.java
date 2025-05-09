package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    @DisplayName("토큰을 생성한다.")
    @Test
    void createToken() {
        //given
        Member member = Member.from(1L, "testName", "testEmail", "1234");

        //when
        String actual = jwtTokenProvider.createToken(member);

        //then
        assertThat(actual).isNotNull();
    }

}
