package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MemberTest {
    @Test
    void 비밀번호가_일치하지_않으면_예외가_발생한다() {
        Member member = new Member(1L, "prin", "prin@gmail.com", "1q2w3e4r!", Role.MEMBER);

        assertThatThrownBy(() -> member.validatePassword("12345asdf", ""))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
