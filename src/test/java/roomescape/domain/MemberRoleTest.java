package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.ForbiddenAuthorityException;

class MemberRoleTest {

    @DisplayName("어드민이 아닐 경우 예외가 발생한다.")
    @Test
    void validateAdminTest() {

        // when & then
        assertThatThrownBy(MemberRole.USER::validateAdmin)
                .isInstanceOf(ForbiddenAuthorityException.class)
                .hasMessage("관리자가 아닙니다.");
    }

}
