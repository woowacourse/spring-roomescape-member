package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @ParameterizedTest
    @CsvSource(value = {"user, USER", "admin, ADMIN"})
    @DisplayName("사용자의 권한을 찾을 수 있다.")
    void findRole(String expression, Role expected) {
        final Role actual = Role.from(expression);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"USER, true", "ADMIN, false"})
    @DisplayName("일반 사용자인지 판별할 수 있다.")
    void findRole(Role role, boolean expected) {
        final boolean actual = role.isUser();

        assertThat(actual).isEqualTo(expected);
    }
}
