package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("유효한 값으로 Member를 생성할 수 있다")
    void test1() {
        // given
        Long id = 1L;
        String name = "미미";
        String email = "mimi@email.com";
        String password = "password";
        Role role = Role.MEMBER;

        Member expectedMemer = new Member(id, name, email, password, role);

        // when
        Member member = new Member(id, name, email, password, role);

        // then
        assertThat(member).isEqualTo(expectedMemer);
    }

    @Test
    @DisplayName("role 표현식을 통해 Member를 생성할 수 있다")
    void test2() {
        // given
        Long id = 1L;
        String name = "미미";
        String email = "mimi@email.com";
        String password = "password";
        String roleExpression = "MEMBER";

        Member expected = new Member(id, name, email, password, Role.MEMBER);

        // when
        Member member = Member.of(id, name, email, password, roleExpression);

        // then
        assertThat(member).isEqualTo(expected);
    }

    @Test
    @DisplayName("id가 null이면 예외가 발생한다")
    void test3() {
        // given
        Long id = null;

        // when & then
        assertThatThrownBy(() -> new Member(id, "name", "email", "pw", Role.MEMBER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] id가 null이 되어서는 안 됩니다.");
    }

    @Test
    @DisplayName("name이 null이면 예외가 발생한다")
    void test4() {
        // given
        String name = null;

        // when & then
        assertThatThrownBy(() -> new Member(1L, name, "email", "pw", Role.MEMBER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] name이 null이 되어서는 안 됩니다.");
    }

    @Test
    @DisplayName("email이 null이면 예외가 발생한다")
    void test5() {
        // given
        String email = null;

        // when & then
        assertThatThrownBy(() -> new Member(1L, "name", email, "pw", Role.MEMBER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] email이 null이 되어서는 안 됩니다.");
    }

    @Test
    @DisplayName("password가 null이면 예외가 발생한다")
    void test6() {
        // given
        String password = null;

        // when & then
        assertThatThrownBy(() -> new Member(1L, "name", "email", password, Role.MEMBER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] password가 null이 되어서는 안 됩니다.");
    }

    @Test
    @DisplayName("role이 null이면 예외가 발생한다")
    void test7() {
        // given
        Role role = null;

        // when & then
        assertThatThrownBy(() -> new Member(1L, "name", "email", "pw", role))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] role이 null이 되어서는 안 됩니다.");
    }
}
