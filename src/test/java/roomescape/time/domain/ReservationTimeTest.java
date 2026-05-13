package roomescape.time.domain;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.global.exception.validation.InvalidIdException;

class ReservationTimeTest {
    @Test
    void 식별자가_같은_경우_동등한_객체로_판단한다() {
        // given
        ReservationTime reservationTimeA = new ReservationTime(1L, LocalTime.of(10,1));
        ReservationTime reservationTimeB = new ReservationTime(1L, LocalTime.of(10,2));

        // when & then
        Assertions.assertThat(reservationTimeA).isEqualTo(reservationTimeB);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L})
    void 식별자가_0보다_작은_경우_예외가_발생한다(Long id) {
        // when & then
        Assertions.assertThatThrownBy(() -> new ReservationTime(id, LocalTime.of(10,1)))
                .isInstanceOf(InvalidIdException.class);
    }

    @Test
    void 식별자가_null_인_경우_예외가_발생한다() {
        //given
        Long id = null;

        // when & then
        Assertions.assertThatThrownBy(() -> new ReservationTime(id, LocalTime.of(10,1)))
                .isInstanceOf(InvalidIdException.class);
    }
}
