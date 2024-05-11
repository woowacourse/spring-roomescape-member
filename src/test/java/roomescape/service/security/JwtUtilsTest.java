package roomescape.service.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import groovy.util.logging.Slf4j;
import roomescape.domain.Member;
import roomescape.domain.Role;

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
        Long decode = JwtUtils.decodeId(encodedToken);
        Assertions.assertThat(decode)
                .isOne();
    }

    @Test
    @DisplayName("사용자의 권한을 토큰에서 찾을 수 있다")
    void decodeRole_ShouldExtractRole() {
        // given
        Member member = new Member(1L, "name", "email", "password", Role.ADMIN);
        String encoded = JwtUtils.encode(member);

        // when
        Role role = JwtUtils.decodeRole(encoded);

        // then
        Assertions.assertThat(role).isSameAs(Role.ADMIN);
    }

    @Test
    @DisplayName("토큰에서 사용자 이름을 추출할 수 있다")
    void decodeName_ShouldExtractMemberName() {
        // given
        Member member = new Member(1L, "name", "email", "password", Role.ADMIN);
        String encoded = JwtUtils.encode(member);

        // when
        String name = JwtUtils.decodeName(encoded);

        // then
        Assertions.assertThat(name).isEqualTo(member.getName());
    }

}
