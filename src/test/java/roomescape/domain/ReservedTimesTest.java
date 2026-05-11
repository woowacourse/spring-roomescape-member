package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.reservation.DuplicateReservationException;

class ReservedTimesTest {

    @Test
    @DisplayName("특정 시간 ID의 예약 여부 및 가용 상태를 정확히 반환한다.")
    void checkAvailability() {
        // given
        ReservedTimes reservedTimes = new ReservedTimes(List.of(1L, 2L));

        // when & then
        assertThat(reservedTimes.isReserved(1L)).isTrue();
        assertThat(reservedTimes.isAvailable(1L)).isFalse();

        assertThat(reservedTimes.isReserved(3L)).isFalse();
        assertThat(reservedTimes.isAvailable(3L)).isTrue();
    }

    @Test
    @DisplayName("이미 예약된 시간에 대해 검증을 시도하면 예외가 발생한다.")
    void validateAvailable_ThrowsException() {
        // given
        ReservedTimes reservedTimes = new ReservedTimes(List.of(1L));

        // when & then
        assertThatThrownBy(() -> reservedTimes.validateAvailable(1L))
                .isInstanceOf(DuplicateReservationException.class);
    }
}
