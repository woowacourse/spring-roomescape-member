package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("비밀번호가 일치하는지 확인한다")
    void password_same() {
        Member member = new Member("두리", "duri@duri.com", "1234", Role.USER);

        assertThat(member.isPasswordEqual("1234")).isTrue();
    }

    @Test
    @DisplayName("비밀번호가 일치하면 TRUE를 반환한다")
    void password_not_same() {
        Member member = new Member("두리", "duri@duri.com", "1234", Role.USER);

        assertThat(member.isPasswordEqual("1234")).isTrue();
    }

    @Test
    @DisplayName("어드민인지 확인한다")
    void is_admin() {
        Member member = new Member("두리", "duri@duri.com", "1234", Role.ADMIN);

        Assertions.assertAll(
            () -> assertThat(member.isAdmin()).isTrue(),
            () -> assertThat(member.isNotAdmin()).isFalse()
        );
    }

    @Test
    @DisplayName("어드민이 아닌지 확인한다")
    void is_not_admin() {
        Member member = new Member("두리", "duri@duri.com", "1234", Role.USER);

        Assertions.assertAll(
            () -> assertThat(member.isAdmin()).isFalse(),
            () -> assertThat(member.isNotAdmin()).isTrue()
        );
    }
}
