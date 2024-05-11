package roomescape.service.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import groovy.util.logging.Slf4j;
import roomescape.domain.Member;

@Slf4j
class JwtUtilsTest {

    @Test
    @DisplayName("사용자 정보를 바탕으로 Jwt 토큰을 발행한다")
    void decode_ShouldGenerateJwtToken() {
        // given
        Member user = new Member(1L, "name", "email", "password");

        // when
        String encodedToken = JwtUtils.encode(user);

        //then
        Long decode = JwtUtils.decode(encodedToken);
        Assertions.assertThat(decode)
                .isOne();
    }

}
