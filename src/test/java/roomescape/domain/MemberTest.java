package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MemberTest {

    @DisplayName("비어있는 ID로 멤버를 생성할 수 없다")
    @Test
    void cannotCreateBecauseNullId() {
        // when & then
        assertThatThrownBy(() -> new Member(null, "이름", "test@test.com", "비밀번호"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 ID로 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 이름으로 멤버를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullName(String name) {
        // when & then
        assertThatThrownBy(() -> new Member(1L, name, "test@test.com", "비밀번호"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 이름로 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("최대길이를 초과한 이름으로 멤버를 생성할 수 없다")
    @Test
    void cannotCreateBecauseTooLongName() {
        // given
        String tooLongName = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Member(1L, tooLongName, "test@test.com", "비밀번호"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 최대길이를 초과한 이름으로는 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 이메일로 멤버를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullEmail(String email) {
        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", email, "비밀번호"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 이메일로 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("최대길이를 초과한 이메일로 멤버를 생성할 수 없다")
    @Test
    void cannotCreateBecauseTooLongEmail() {
        // given
        String tooLongEmail = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", tooLongEmail, "비밀번호"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 최대길이를 초과한 이메일로는 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("올바르지 않은 형식의 이메일로 멤보를 생성할 수 없다.")
    @Test
    void cannotCreateBecauseInvalidEmail() {
        // given
        String invalidEmail = "invalidEmail";

        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", invalidEmail, "비밀번호"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 올바르지 않은 형식의 이메일로는 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 비밀번호로 멤버를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullPassword(String password) {
        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", "test@test.com", password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 이름으로 비밀번호를 생성할 수 없습니다.");
    }

    @DisplayName("최대길이를 초과한 비밀번호로 멤버를 생성할 수 없다")
    @Test
    void cannotCreateBecauseTooLongPassword() {
        // given
        String tooLongPassword = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", "test@test.com", tooLongPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 최대길이를 초과한 비밀번호로는 멤버를 생성할 수 없습니다.");
    }
}
