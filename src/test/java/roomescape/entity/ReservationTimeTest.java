package roomescape.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.exception.impl.InvalidReservationTimeException;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReservationTimeTest {

    @ParameterizedTest
    @CsvSource({"10:00", "23:00"})
    @DisplayName("시간으로 생성할 수 있다.")
    void canCreateWithTime(String timeStrValue) {
        //given
        //when
        //then
        assertThatCode(() -> ReservationTime.beforeSave(LocalTime.parse(timeStrValue)))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource({"09:59", "23:01"})
    @DisplayName("예약 가능한 시간이 아닐 때 예약하면 예외가 발생한다.")
    void whenInvalidTimeReservationThrowException(String timeStrValue) {
        //given
        //when
        //then
        assertThatThrownBy(() -> ReservationTime.beforeSave(LocalTime.parse(timeStrValue)))
                .isInstanceOf(InvalidReservationTimeException.class)
                .hasMessage("예약은 10시~23시로만 가능합니다.");
    }
}
