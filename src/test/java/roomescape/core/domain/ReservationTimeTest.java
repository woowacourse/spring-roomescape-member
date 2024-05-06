package roomescape.core.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.util.Fixture.ID;
import static roomescape.util.Fixture.START_AT;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTimeTest {
    @Test
    @DisplayName("시간을 생성한다.")
    void createReservationTime() {
        assertThatCode(() -> new ReservationTime(ID, START_AT))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("시간 생성 시, id가 null이면 예외가 발생한다")
    void throwExceptionWhenNullId() {
        assertThatThrownBy(() -> new ReservationTime(null, START_AT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시간 id는 null일 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("시간 생성 시, startAt이 null이나 빈값이면 예외가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void throwExceptionWhenEmptyStartAt(String emptyStartAt) {
        assertThatThrownBy(() -> new ReservationTime(ID, emptyStartAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시간은 null이나 빈 값일 수 없습니다.");
    }

    @Test
    @DisplayName("시간 생성 시, startAt아 유효한 시간 형식이 아니면 예외가 발생한다")
    void throwExceptionWhenInvalidTimeFormat() {
        assertThatThrownBy(() -> new ReservationTime(ID, "1시"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시간 형식이 잘못되었습니다.");
    }
}
