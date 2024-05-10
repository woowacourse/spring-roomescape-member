package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {
    @DisplayName("이메일이 같으면 true를 반환한다.")
    @Test
    void hasSameEmail() {
        Member firstMember = new Member(1L, "name1", "email1", "password1", Role.USER);
        Member secondMember = new Member(2L, "name2", "email1", "password2", Role.USER);

        assertThat(firstMember.hasSameEmail(secondMember)).isTrue();
    }

    @DisplayName("이메일이 다르면 false를 반환한다.")
    @Test
    void hasNotSameEmail() {
        Member firstMember = new Member(1L, "name1", "email1", "password1", Role.USER);
        Member secondMember = new Member(2L, "name2", "email2", "password2", Role.USER);

        assertThat(firstMember.hasSameEmail(secondMember)).isFalse();
    }
}
