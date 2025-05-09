package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidAuthException;

class MemberTest {

    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다.")
    @Test
    void notMatchPassword() {
        //given
        Member member = Member.fromWithoutId("member1", "testEmail", "1234");

        String requestPassword = "123";
        //when //then
        assertThatThrownBy(() -> member.validatePassword(requestPassword))
                .isInstanceOf(InvalidAuthException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("비밀번호가 일치하면 예외가 발생하지 않는다.")
    @Test
    void matchPassword() {
        //given
        Member member = Member.fromWithoutId("member1", "testEmail", "1234");

        String requestPassword = "1234";
        //when //then
        assertThatCode(() -> member.validatePassword(requestPassword))
                .doesNotThrowAnyException();
    }

}
