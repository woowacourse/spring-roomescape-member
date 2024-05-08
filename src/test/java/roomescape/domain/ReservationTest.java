package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.exception.InvalidDateException;
import roomescape.domain.exception.InvalidNameException;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    @DisplayName("예약에 아이디를 부여한다.")
    void assignId() {
        // given
        final Long id = 2L;
        final Reservation reservation = new Reservation(null, "Seyang", "2024-05-05", null, null);
        final Reservation expected = new Reservation(id, "Seyang", "2024-05-05", null, null);

        // when
        Reservation actual = reservation.assignId(id);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("예약에 시간을 부여한다.")
    void assignTime() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Reservation reservation = new Reservation(2L, new ReserveName("Seyang"), null, null, null);
        Reservation expected = new Reservation(2L, new ReserveName("Seyang"), null, reservationTime, null);

        // when
        Reservation actual = reservation.assignTime(reservationTime);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("빈 이름이 입력 될 경우 예외가 발생한다.")
    void validateEmptyName() {
        assertThatThrownBy(() -> new Reservation(null, "", "2024-04-24", null, null))
                .isInstanceOf(InvalidNameException.class);
    }

    @Test
    @DisplayName("빈 날짜가 입력 될 경우 예외가 발생한다.")
    void validateEmptyDate() {
        assertThatThrownBy(() -> new Reservation(null, "Seyang", "", null, null))
                .isInstanceOf(InvalidDateException.class);
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 날짜인경우 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"202020-12-13", "1-13-4", "2024-14-15"})
    void validateFormat(final String date) {
        assertThatThrownBy(() -> new Reservation(null, "Seyang", date, null, null))
                .isInstanceOf(InvalidDateException.class);
    }
}
