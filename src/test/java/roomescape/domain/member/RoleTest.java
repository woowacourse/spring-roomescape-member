package roomescape.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class RoleTest {

    @DisplayName("Role을 생성한다")
    @ValueSource(strings = {"ADMIN", "MEMBER", "admin", "member"})
    @ParameterizedTest
    void when_createRole_then_created(String role) {
        // when, then
        assertThatCode(() -> Role.from(role))
                .doesNotThrowAnyException();
    }

    @DisplayName("Role이 잘못된 형식으로 생성하면 예외가 발생한다")
    @Test
    void when_createRoleWithInvalidFormat_then_throwException() {
        // when, then
        assertThatThrownBy(() -> Role.from("INVALID"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
