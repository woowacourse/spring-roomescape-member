package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.application.RoleNotFoundException;

class RoleTest {

    @Test
    @DisplayName("유효한 표현식으로 Role을 조회할 수 있다")
    void test1() {
        // given
        String expression = "admin";

        // when
        Role role = Role.getByExpression(expression);

        // then
        assertThat(role).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("대소문자 구분 없이 표현식으로 Role을 조회할 수 있다")
    void test2() {
        // given
        String expression = "MeMbEr";

        // when
        Role role = Role.getByExpression(expression);

        // then
        assertThat(role).isEqualTo(Role.MEMBER);
    }

    @Test
    @DisplayName("존재하지 않는 표현식으로 조회하면 예외가 발생한다")
    void test3() {
        // given
        String invalidExpression = "manager";

        // when & then
        assertThatThrownBy(() -> Role.getByExpression(invalidExpression))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    @DisplayName("같은 Role이면 권한이 있다고 판단한다")
    void test4() {
        // given
        Role role = Role.ADMIN;
        Role requiredRole = Role.ADMIN;

        // when
        boolean result = role.isAuthorizedFor(requiredRole);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("다른 Role이면 권한이 없다고 판단한다")
    void test5() {
        // given
        Role role = Role.MEMBER;
        Role requiredRole = Role.ADMIN;

        // when
        boolean result = role.isAuthorizedFor(requiredRole);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Role의 표현식을 반환할 수 있다")
    void test6() {
        // given
        Role role = Role.MEMBER;

        // when
        String expression = role.getExpression();

        // then
        assertThat(expression).isEqualTo("member");
    }
}
