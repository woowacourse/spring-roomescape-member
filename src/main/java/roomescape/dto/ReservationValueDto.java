package roomescape.dto;

import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.Theme;
import roomescape.model.UserName;

public record ReservationValueDto(UserName userName, ReservationDateTime reservationDateTime, Theme theme) {
    public static ReservationValueDto of(Reservation reservation) {
        return new ReservationValueDto(reservation.getUserName(),
                reservation.getReservationDateTime(),
                reservation.getTheme());
    }
}
