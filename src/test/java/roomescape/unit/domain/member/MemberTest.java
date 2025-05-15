package roomescape.unit.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

class MemberTest {
    @Test
    void validate() {
        // given
        String name = "히스타";
        String email = "hista@woowa.jjang";
        String password = "1q2w3e4r!";
        Role role = Role.MEMBER;

        // when
        Assertions.assertThatNoException().isThrownBy(
                () -> new Member(1L, name, email, password, role)
        );
    }

    @Test
    void validateWithNullRole() {
        // given
        String name = "히스타";
        String email = "hista@woowa.jjang";
        String password = "1q2w3e4r!";
        Role role = null;

        // when
        Assertions.assertThatNoException().isThrownBy(
                () -> new Member(1L, name, email, password, role)
        );
    }

    @Test
    void validateWithNullName() {
        // given
        String name = null;
        String email = "hista@woowa.jjang";
        String password = "1q2w3e4r!";
        Role role = Role.MEMBER;

        // when
        Assertions.assertThatIllegalArgumentException().isThrownBy(
                () -> new Member(1L, name, email, password, role)
        ).withMessage("이름은 필수입니다.");
    }
}
