package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    @DisplayName("어드민이라면 true를 반환한다")
    void isAdmin() {
        assertThat(Role.ADMIN.isAdmin()).isTrue();
    }

    @Test
    @DisplayName("유저라면 false를 반환한다")
    void isAdminWhenUser() {
        assertThat(Role.USER.isAdmin()).isFalse();
    }
}
