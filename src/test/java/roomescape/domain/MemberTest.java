package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("유저의 비밀번호가 일치하는지 확인한다.")
    void isMatchPasswordTest() {
        String password = "password";

        Member member = new Member(new Name("userA"), new Email("admin@email.com"), Role.USER, password);

        assertThat(member.isMatchPassword(password)).isTrue();
    }
}
