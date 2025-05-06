package roomescape.reservation.application.dto;

import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationName;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record CreateReservationRequest(ReservationName name, Theme theme, ReservationDate date, ReservationTime time) {
}
