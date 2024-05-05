package roomescape.domain.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.model.ValidateException;

class TimeTest {

    @Test
    @DisplayName("객체 생성 시, null이 존재하면 예외를 발생한다.")
    void validateConstructorParameterNull() {

        // when & then
        Assertions.assertThatThrownBy(() -> new Time(null))
                .isInstanceOf(ValidateException.class);
    }
}
