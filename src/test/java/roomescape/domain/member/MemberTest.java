package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.auth.InvalidAuthException;

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

    @DisplayName("name이 어드민 또는 admin인 경우 관리자 권한으로 생성한다.")
    @Test
    void createAsAdmin() {
        //given
        final String name = "어드민";
        final String email = "testEmail";
        final String password = "1234";

        //when
        Member actual = Member.fromWithoutId(name, email, password);

        //then
        assertThat(actual.getRole()).isEqualTo(MemberRole.ADMIN);
    }

    @DisplayName("name이 어드민 또는 admin이 아닌 경우 사용자 권한으로 생성한다.")
    @Test
    void createAsUser() {
        //given
        final String name = "사용자";
        final String email = "testEmail";
        final String password = "1234";

        //when
        Member actual = Member.fromWithoutId(name, email, password);

        //then
        assertThat(actual.getRole()).isEqualTo(MemberRole.USER);
    }

}
