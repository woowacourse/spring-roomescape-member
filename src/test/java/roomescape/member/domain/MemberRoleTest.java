package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberRoleTest {

    @Test
    @DisplayName("문자열로부터, 해당하는 권한 Enum을 반환한다.")
    void findMemberRole() {
        assertThat(MemberRole.getMemberRole("USER")).isEqualTo(MemberRole.USER);
    }

    @Test
    @DisplayName("회원의 권한이 관리자가 아닌 경우, 참을 반환한다.")
    void isAdmin() {
        assertTrue(MemberRole.USER.isNotAdmin());
    }
}
