package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.custom.InvalidInputException;

class MemberTest {

    private static final long ID = 1L;
    private static final Role ROLE = Role.USER;
    private static final String NAME = "라젤";
    private static final String EMAIL = "razel@woowa.com";
    private static final String PASSWORD = "password";

    @DisplayName("멤버 생성시, 멤버 역할이 빈 값이면 예외를 던진다")
    @Test
    void createMemberTest_WhenRoleIsNull() {
        // when // then
        assertThatThrownBy(() -> new Member(ID, null, NAME, EMAIL, PASSWORD))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("멤버 역할은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("멤버 생성시, 멤버 명이 빈 값이면 예외를 던진다")
    @Test
    void createMemberTest_WhenNameIsNull() {
        // when // then
        assertThatThrownBy(() -> new Member(ID, ROLE, null, EMAIL, PASSWORD))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("멤버 명은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("멤버 생성시, 이메일이 빈 값이면 예외를 던진다")
    @Test
    void createMemberTest_WhenEmailIsNull() {
        // when // then
        assertThatThrownBy(() -> new Member(ID, ROLE, NAME, null, PASSWORD))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("이메일은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("멤버 생성시, 비밀 번호가 빈 값이면 예외를 던진다")
    @Test
    void createMemberTest_WhenPasswordIsNull() {
        // when // then
        assertThatThrownBy(() -> new Member(ID, ROLE, NAME, EMAIL, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("비밀 번호는 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("멤버 생성시, 멤버 명이 15자를 초과하면 예외를 던진다")
    @Test
    void createMemberTest_WhenNameLengthIsGreaterThan15Characters() {
        // given
        String nameExceedingMaxLength = "멤버이름제한을뚫는예시이름입니다";

        // when // then
        assertThatThrownBy(() -> new Member(ID, ROLE, nameExceedingMaxLength, EMAIL, PASSWORD))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("멤버 명은 15자를 초과할 수 없습니다");
    }

    @DisplayName("멤버 생성시, 이메일이 30자를 초과하면 예외를 던진다")
    @Test
    void createMemberTest_WhenEmailLengthIsGreaterThan30Characters() {
        // given
        String emailExceedingMaxLength = "emailaddressOverMaxLenth@email.com";

        // when // then
        assertThatThrownBy(() -> new Member(ID, ROLE, NAME, emailExceedingMaxLength, PASSWORD))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("이메일은 30자를 초과할 수 없습니다");
    }

    @DisplayName("멤버 생성시, 비밀 번호가 20자를 초과하면 예외를 던진다")
    @Test
    void createMemberTest_WhenPasswordLengthIsGreaterThan20Characters() {
        // given
        String passwordExceedingMaxLength = "thisisaverylongpassword";

        // when // then
        assertThatThrownBy(() -> new Member(ID, ROLE, NAME, EMAIL, passwordExceedingMaxLength))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("비밀 번호는 20자를 초과할 수 없습니다");
    }

    @DisplayName("멤버 생성시, 이메일이 형식에 맞지 않으면 예외를 던진다")
    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "missingatsign.com",
            "@no-local-part.com",
            "name@domain",
            "name@.com",
            "name@com."
    })
    void createMemberTest_WhenEmailFormatIsInvalid(String wrongEmailForm) {
        // when // then
        assertThatThrownBy(() -> new Member(ID, ROLE, NAME, wrongEmailForm, PASSWORD))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("올바르지 않은 이메일 형식입니다: " + wrongEmailForm);
    }
}
