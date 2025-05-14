package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @DisplayName("비어있는 이름으로 멤버를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullName(String name) {
        // when & then
        assertThatThrownBy(() -> new Member(1L, name, "test@test.com", "비밀번호", MemberRole.GENERAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 이름로 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("최대길이를 초과한 이름으로 멤버를 생성할 수 없다")
    @Test
    void cannotCreateBecauseTooLongName() {
        // given
        String tooLongName = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Member(1L, tooLongName, "test@test.com", "비밀번호", MemberRole.GENERAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 최대길이를 초과한 이름으로는 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 이메일로 멤버를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullEmail(String email) {
        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", email, "비밀번호", MemberRole.GENERAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 이메일로 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("최대길이를 초과한 이메일로 멤버를 생성할 수 없다")
    @Test
    void cannotCreateBecauseTooLongEmail() {
        // given
        String tooLongEmail = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", tooLongEmail, "비밀번호", MemberRole.GENERAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 최대길이를 초과한 이메일로는 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("올바르지 않은 형식의 이메일로 멤보를 생성할 수 없다.")
    @Test
    void cannotCreateBecauseInvalidEmail() {
        // given
        String invalidEmail = "invalidEmail";

        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", invalidEmail, "비밀번호", MemberRole.GENERAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 올바르지 않은 형식의 이메일로는 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 비밀번호로 멤버를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullPassword(String password) {
        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", "test@test.com", password, MemberRole.GENERAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 비밀번호로 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("유효하지 않은 형식의 비밀번호로는 회원을 생성할 수 없다")
    @ParameterizedTest
    @ValueSource(strings = {"asdfasdf1", "asdfasdf!", "12341234!", "a213!"})
    void cannotCreateBecauseInvalidPassword(String invalidPassword) {
        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", "test@test.com", invalidPassword, MemberRole.GENERAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 올바르지 않은 형식의 비밀번호로는 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 권한으로는 멤버를 생성할 수 없다")
    @Test
    void can() {
        // when & then
        assertThatThrownBy(() -> new Member(1L, "이름", "test@test.com", "asdfe123!", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 권한으로 멤버를 생성할 수 없습니다.");
    }

    @DisplayName("비밀번호가 동일한지 확인할 수 있다")
    @Test
    void canCheckSamePassword() {
        // given
        Member member = new Member(1L, "이름", "test@test.com", "asdf1234!", MemberRole.GENERAL);

        // when & then
        assertAll(
                () -> assertThat(member.comparePassword("asdf1234!")).isTrue(),
                () -> assertThat(member.comparePassword("zxcv5678!")).isFalse()
        );
    }
}
