package roomescape.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class RoleTest {

    @DisplayName("Role을 생성한다")
    @ValueSource(strings = {"ADMIN", "MEMBER", "admin", "member"})
    @ParameterizedTest
    void when_createRole_then_created(String role) {
        // when, then
        assertThatCode(() -> Role.from(role))
                .doesNotThrowAnyException();
    }

    @DisplayName("Role이 잘못된 형식으로 생성하면 GUEST로 분류된다")
    @Test
    void when_createRoleWithInvalidFormat_then_throwException() {
        // when, then
        Role role = Role.from("INVALID");

        assertThat(role).isEqualTo(Role.GUEST);
    }
}
