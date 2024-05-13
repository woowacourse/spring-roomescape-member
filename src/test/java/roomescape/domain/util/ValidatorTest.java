package roomescape.domain.util;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.domain.exception.IllegalNullArgumentException;
import roomescape.domain.exception.IllegalRequestArgumentException;

class ValidatorTest {
    @DisplayName("인자 중 null이 있을 시 예외를 던진다.")
    @Test
    void validateNull() {
        assertThatThrownBy(() -> Validator.nonNull(null, "안녕", 1L))
                .isInstanceOf(IllegalNullArgumentException.class);
    }

    @DisplayName("인자 중 null이 없을 시 정상 작동한다.")
    @Test
    void nonNull() {
        assertThatCode(() -> Validator.nonNull("안녕", 1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("인자 중 빈 값이 있을 시 예외를 던진다.")
    @Test
    void validateEmpty() {
        assertThatThrownBy(() -> Validator.notEmpty("", "안녕"))
                .isInstanceOf(IllegalRequestArgumentException.class)
                .hasMessage("비어있는 값이 존재합니다.");
    }

    @DisplayName("인자 중 빈 값이 없을 시 정상 작동한다.")
    @Test
    void notEmpty() {
        assertThatCode(() -> Validator.notEmpty("안녕", "반가워"))
                .doesNotThrowAnyException();
    }

    @DisplayName("인자 중 최대 길이를 초과한 값이 있을 시 예외를 던진다.")
    @Test
    void validateOverSize() {
        assertThatThrownBy(() -> Validator.overSize(3, "안녕", "안녕하세요"))
                .isInstanceOf(IllegalRequestArgumentException.class)
                .hasMessage("문자열(%s) 최대 길이인 %d를 초과했습니다.".formatted("안녕하세요", 3));
    }

    @DisplayName("인자 중 최대 길이를 초과한 값이 없을 시 정상 작동한다.")
    @Test
    void underSize() {
        assertThatCode(() -> Validator.overSize(4, "안녕", "반가"))
                .doesNotThrowAnyException();
    }
}
