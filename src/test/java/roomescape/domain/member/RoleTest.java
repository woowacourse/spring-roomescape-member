package roomescape.domain.member;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RoleTest {
    @ParameterizedTest
    @ValueSource(strings = {"ADNIM,USER"})
    @DisplayName("역할이 아닌 문자열이 주어지면 예외가 발생한다")
    void validRole(String value) {
        Assertions.assertThatThrownBy(() -> Role.from(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("입력한 값: %s, 역할이 존재하지 않습니다", value));
    }
}
