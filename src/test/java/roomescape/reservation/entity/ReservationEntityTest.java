package roomescape.reservation.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;
import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationEntityTest {
    @DisplayName("과거 날짜의 예약은 생성할 수 없다.")
    @Test
    void doesNotAllowedCreatingPastReservation() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate date = now.minusDays(1);
        ReservationTimeEntity time = ReservationTimeEntity.of(1L, LocalTime.of(10, 0));

        // when & then
        assertThatThrownBy(() -> {
            ReservationEntity.create("test", date, time, 1L);
        }).isInstanceOf(BadRequestException.class);
    }

    @DisplayName("과거 예약은 of 메서드로 로드할 수 있다.")
    @Test
    void loadPastReservation() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate date = now.minusDays(1);
        ReservationTimeEntity time = ReservationTimeEntity.of(1L, LocalTime.of(10, 0));

        // when & then
        assertThatCode(() -> {
            ReservationEntity.of(1L, "test", date, time, 1L);
        }).doesNotThrowAnyException();
    }
}
