package roomescape.user.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.user.domain.UserName;

class UserNameTest {
    @DisplayName("예약자 이름이 null일 경우 예외를 던진다.")
    @Test
    void validateTest_whenValueIsNull() {
        assertThatThrownBy(() -> new UserName(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("예약자 이름이 1자 미만일 경우 예외를 던진다.")
    @Test
    void validateTest_whenValueIsEmpty() {
        assertThatThrownBy(() -> new UserName(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 1글자 이상 20글자 이하이어야 합니다.");
    }

    @DisplayName("예약자 이름이 20자 초과일 경우 예외를 던진다.")
    @Test
    void validateTest_whenValueIsLong() {
        assertThatThrownBy(() -> new UserName("a".repeat(21)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 1글자 이상 20글자 이하이어야 합니다.");
    }
}
