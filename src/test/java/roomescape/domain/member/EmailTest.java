package roomescape.domain.member;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @Test
    @DisplayName("이메일이 null이면 예외가 발생한다")
    void nullCheck() {
        Assertions.assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일은 null이 될 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings={"q","qwe@qwe","qwe@qwe,com"})
    @DisplayName("이메일 형식에 맞지 않는다면 예외가 발생한다")
    void formatCheck(String email) {
        Assertions.assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("입력 값: %s,이메일 형식에 맞추어 입력해주세요", email));
    }
}
