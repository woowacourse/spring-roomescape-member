package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.util.Fixture.ID_1;
import static roomescape.util.Fixture.START_AT_16_00;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;

class ReservationTimeTest {
    @Test
    @DisplayName("시간을 생성한다")
    void createReservationTime() {
        assertThatCode(() -> new ReservationTime(ID_1, START_AT_16_00))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("시간 생성 시, startAt이 null이면 예외가 발생한다")
    void throwExceptionWhenStartAtNull() {
        assertThatThrownBy(() -> new ReservationTime(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시간은 null일 수 없습니다.");
    }

    @Test
    @DisplayName("시간 생성 시, startAt아 유효한 시간 형식이 아니면 예외가 발생한다")
    void throwExceptionWhenInvalidTimeFormat() {
        assertThatThrownBy(() -> new ReservationTime(ID_1, "1시"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시간 형식이 잘못되었습니다.");
    }

    @Test
    @DisplayName("예약 시간이 과거인지 여부를 반환한다.")
    void isPast() {
        final LocalTime pastTime = LocalTime.now().minusSeconds(1);
        final LocalTime futureTime = LocalTime.now().plusMinutes(1);

        assertAll(
                () -> assertThat(new ReservationTime(ID_1, pastTime).isPast()).isTrue(),
                () -> assertThat(new ReservationTime(ID_1, futureTime).isPast()).isFalse()
        );
    }

    @Test
    @DisplayName("예약 시간을 HH:mm 형식으로 반환한다.")
    void getStartAtString() {
        final LocalTime time = LocalTime.of(8, 1);
        final ReservationTime reservationTime = new ReservationTime(ID_1, time);

        assertThat(reservationTime.getStartAtString()).isEqualTo("08:01");
    }
}
