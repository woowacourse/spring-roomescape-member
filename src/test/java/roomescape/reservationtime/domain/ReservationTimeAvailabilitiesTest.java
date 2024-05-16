package roomescape.reservationtime.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;

class ReservationTimeAvailabilitiesTest {

  @DisplayName("예약이 결정된 시간인지 검증후 결과를 저장한다.")
  @Test
  void checkIsAlreadyBookedTest() {
    // Given
    final ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(1, 30));
    final ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(2, 30));
    final ReservationTime reservationTime3 = new ReservationTime(3L, LocalTime.of(4, 30));
    final ReservationTime notBookedTime = new ReservationTime(4L, LocalTime.of(3, 30));
    final List<ReservationTime> reservationTimes = List.of(reservationTime1, reservationTime2,
        reservationTime3, notBookedTime);
    final Theme theme = Theme.of(1L, "켈리의 두근두근", "안녕", "사진 링크");

    final LocalDate date = LocalDate.now().plusDays(3);
    final List<Reservation> reservations = List.of(
        Reservation.createInstance(1L, "켈리", date, reservationTime1, theme),
        Reservation.createInstance(2L, "켈리", date, reservationTime2, theme),
        Reservation.createInstance(3L, "켈리", date, reservationTime3, theme)
    );

    // When
    final ReservationTimeAvailabilities reservationTimeAvailabilities = ReservationTimeAvailabilities.of(
        reservationTimes, reservations);

    // Then
    final Map<ReservationTime, Boolean> values = reservationTimeAvailabilities.getValues();
    assertThat(values.get(reservationTime1)).isTrue();
    assertThat(values.get(reservationTime2)).isTrue();
    assertThat(values.get(reservationTime3)).isTrue();
    assertThat(values.get(notBookedTime)).isFalse();
  }
}
