package roomescape.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

class TimeTest {

    @ParameterizedTest
    @CsvSource(value = {"09:59", "22:01"})
    void 오전_10시_오후_10시가_아니면_예외_처리한다(LocalTime value) {
        Assertions.assertThatThrownBy(() -> Time.create(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"10:00", "22:00"})
    void 오전_10시_오후_10시면_정상_생성된다(LocalTime value) {
        Time time = Time.create(value);

        Assertions.assertThat(time.getStartAt()).isEqualTo(value);
    }
}