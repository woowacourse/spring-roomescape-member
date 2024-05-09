package roomescape.auth.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.message.ExceptionMessage;

class UsersTest {

    @DisplayName("이름이 null이거나 공백인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void validateInvalidName(String name) {
        assertThatThrownBy(() -> new Users(1L, name, "email@email.com", "1234"))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage(ExceptionMessage.INVALID_USER_NAME.getMessage());
    }

    @DisplayName("이메일 형식에 맞지 않는 경우 예외가 발생한다.")
    @Test
    void validateInvalidEmail() {
        assertThatThrownBy(() -> new Users(1L, "hotea", "email.com", "1234"))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage(ExceptionMessage.INVALID_USER_NAME.getMessage());
    }

}
