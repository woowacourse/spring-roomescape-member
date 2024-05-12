package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RoleTest {

    @DisplayName("역할 이름을 이용하여 역할을 찾아낸다.")
    @ParameterizedTest
    @EnumSource(Role.class)
    void of(Role expected) {

        String roleName = expected.getRoleName();

        Role actual = Role.of(roleName);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("잘못된 이름이 들어가면 예외가 발생한다.")
    @Test
    void ofInvalidName() {
        String roleName = "GUEST";

        assertThatThrownBy(() -> Role.of(roleName))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
