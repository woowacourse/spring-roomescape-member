package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.util.Fixture.ID_1;
import static roomescape.util.Fixture.JOJO_EMAIL;
import static roomescape.util.Fixture.JOJO_NAME;
import static roomescape.util.Fixture.JOJO_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.BadRequestException;

class MemberTest {
    @Test
    @DisplayName("회원을 생성한다")
    void createMember() {
        assertThatCode(() -> new Member(JOJO_NAME, JOJO_EMAIL, JOJO_PASSWORD, Role.MEMBER))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("회원 생성 시, name이 null이거나 빈 값이면 예외가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void throwExceptionWhenEmptyName(final String emptyName) {
        assertThatThrownBy(() -> new Member(emptyName, JOJO_EMAIL, JOJO_PASSWORD, Role.MEMBER))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("사용자 이름은 null이나 빈 값일 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("회원 생성 시, email이 null이거나 빈 값이면 예외가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void throwExceptionWhenEmptyEmail(final String emptyEmail) {
        assertThatThrownBy(() -> new Member(JOJO_NAME, emptyEmail, JOJO_PASSWORD, Role.MEMBER))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("사용자 이메일은 null이나 빈 값일 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("회원 생성 시, password가 null이거나 빈 값이면 예외가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void throwExceptionWhenEmptyPassword(final String emptyPassword) {
        assertThatThrownBy(() -> new Member(JOJO_NAME, JOJO_EMAIL, emptyPassword, Role.MEMBER))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("사용자 비밀번호는 null이나 빈 값일 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("회원 생성 시, role이 null이거나 올바른 형식이 아니면 예외가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "member", "Admin"})
    void throwExceptionWhenInvalidRole(final String invalidRole) {
        assertThatThrownBy(() -> new Member(ID_1, JOJO_NAME, JOJO_EMAIL, JOJO_PASSWORD, invalidRole))
                .isInstanceOf(BadRequestException.class);
    }

    @ParameterizedTest
    @DisplayName("비밀번호 일치 여부를 반환한다.")
    @CsvSource({"12345, true", "1234, false"})
    void hasMatchedPassword(final String password, final boolean expected) {
        final Member member = new Member(JOJO_NAME, JOJO_EMAIL, "12345", Role.MEMBER);
        assertThat(member.hasMatchedPassword(password)).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("관리자 여부를 반환한다.")
    @CsvSource({"ADMIN, false", "MEMBER, true"})
    void isNotAdmin(final String role, final boolean expected) {
        final Member member = new Member(ID_1, JOJO_NAME, JOJO_EMAIL, JOJO_PASSWORD, role);
        assertThat(member.isNotAdmin()).isEqualTo(expected);
    }
}
