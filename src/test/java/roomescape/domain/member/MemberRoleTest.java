package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.member.MemberException;

class MemberRoleTest {

    @DisplayName("생성시 존재하지 않는 권한이라면 예외가 발생한다.")
    @Test
    void notExists() {
        //given
        String role = "basic";

        //when //then
        assertThatThrownBy(() -> MemberRole.from(role))
                .isInstanceOf(MemberException.class)
                .hasMessage("존재하지 않는 권한 입니다.");
    }

    @DisplayName("생성시 존재하는 권한이라면 예외가 발생하지 않는다.")
    @Test
    void exists() {
        //given
        String role = "USER";

        //when //then
        assertThatCode(() -> MemberRole.from(role))
                .doesNotThrowAnyException();
    }

    @DisplayName("권한이 admin이라면 true를 반환한다.")
    @Test
    void isAdmin() {
        //given
        MemberRole role = MemberRole.ADMIN;

        //when
        boolean actual = role.isAdmin();

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("권한이 admin이 아니라면 false를 반환한다.")
    @Test
    void isNotAdmin() {
        //given
        MemberRole role = MemberRole.USER;

        //when
        boolean actual = role.isAdmin();

        //then
        assertThat(actual).isFalse();
    }

}
