package roomescape.reservationtime.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.reservation.domain.Reservation;

public class ReservationTimeAvailabilities {

  private final Map<ReservationTime, Boolean> values;

  public ReservationTimeAvailabilities(final Map<ReservationTime, Boolean> values) {
    this.values = values;
  }

  public static ReservationTimeAvailabilities of(final List<ReservationTime> reservationTimes,
      final List<Reservation> reservations) {
    final Map<ReservationTime, Boolean> reservationTimeAvailabilities = new HashMap<>();

    for (final ReservationTime reservationTime : reservationTimes) {
      reservationTimeAvailabilities.put(reservationTime,
          isTimeAvailable(reservations, reservationTime));
    }

    return new ReservationTimeAvailabilities(reservationTimeAvailabilities);
  }

  private static boolean isTimeAvailable(final List<Reservation> reservations,
      final ReservationTime reservationTime) {
    return reservations.stream()
        .anyMatch(reservation -> reservation.getTime().equals(reservationTime));
  }

  public Map<ReservationTime, Boolean> getValues() {
    return values;
  }
}
