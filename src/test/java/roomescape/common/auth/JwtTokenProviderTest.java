package roomescape.common.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;

class JwtTokenProviderTest {

    private static final String SECRET_KEY = "my-test-secret-key-my-test-secret-key";

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY);

    @DisplayName("토큰을 생성한다.")
    @Test
    void createToken() {
        //given
        Member member = Member.from(1L, "testName", "testEmail", "1234", MemberRole.ADMIN);

        //when
        String actual = jwtTokenProvider.createToken(member);

        //then
        assertThat(actual).isNotNull();
    }

    @DisplayName("토큰을 추출하여 사용자 식별값을 반환한다.")
    @Test
    void getSubjectFromPayloadBy() {
        //given
        Member member = Member.from(1L, "testName", "testEmail", "1234", MemberRole.ADMIN);

        String token = jwtTokenProvider.createToken(member);

        //when
        Long actual = jwtTokenProvider.getSubjectFromPayloadBy(token);

        //then
        assertThat(actual).isEqualTo(1L);
    }

}
