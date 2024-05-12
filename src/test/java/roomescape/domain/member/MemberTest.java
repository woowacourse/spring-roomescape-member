package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("아이디와 비밀번호는 동일하지 않아야 한다.")
    @Test
    void sameEmailPassword() {
        // given
        String email = "email@email.com";
        String password = email;
        String name = "name";
        Role role = Role.USER;

        // when & then
        assertThatThrownBy(() -> new Member(null, name, email, password, role))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일과 비밀번호는 같을 수 없습니다.");
    }
}
