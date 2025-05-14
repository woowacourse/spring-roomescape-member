package roomescape.dto.other;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.MemberRole;

class AuthenticationInformationTest {

    @DisplayName("현재 인증정보가 관리자의 인증정보인지 검증할 수 있다")
    @Test
    void canCheckAdmin() {
        // given
        AuthenticationInformation adminInformation =
                new AuthenticationInformation(1L, "Kim", MemberRole.ADMIN);
        AuthenticationInformation memberInformation =
                new AuthenticationInformation(1L, "Kim", MemberRole.GENERAL);

        // when & then
        assertAll(
                () -> assertThat(adminInformation.isAdmin()).isTrue(),
                () -> assertThat(memberInformation.isAdmin()).isFalse()
        );
    }

}
