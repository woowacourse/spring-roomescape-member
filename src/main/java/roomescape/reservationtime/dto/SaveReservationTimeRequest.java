package roomescape.reservationtime.dto;

import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record SaveReservationTimeRequest(LocalTime startAt) {

  public ReservationTime toReservationTime() {
    return new ReservationTime(startAt);
  }
}
