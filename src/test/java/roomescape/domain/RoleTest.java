package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleTest {
    @DisplayName("Role 예외 테스트")
    @Test
    void invalidRoleTest() {
        assertThatThrownBy(() -> new Role("role"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
