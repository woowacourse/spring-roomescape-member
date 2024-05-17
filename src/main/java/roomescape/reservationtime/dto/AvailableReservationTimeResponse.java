package roomescape.reservationtime.dto;

import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record AvailableReservationTimeResponse(
    Long timeId,
    LocalTime startAt,
    boolean alreadyBooked
) {

  public static AvailableReservationTimeResponse of(
      final ReservationTime reservationTime,
      final boolean alreadyBooked
  ) {
    return new AvailableReservationTimeResponse(
        reservationTime.getId(),
        reservationTime.getStartAt(),
        alreadyBooked);
  }
}
