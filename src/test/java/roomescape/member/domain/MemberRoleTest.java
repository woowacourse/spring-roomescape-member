package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MemberRoleTest {

    @Test
    void findMemberRole() {
        assertThat(MemberRole.getMemberRole("USER")).isEqualTo(MemberRole.USER);
    }
}
