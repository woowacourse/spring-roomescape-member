package roomescape.dto;

import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.Theme;
import roomescape.model.UserName;

public record ReservationValuesDto(UserName userName, ReservationDateTime reservationDateTime, Theme theme) {
    public static ReservationValuesDto of(final Reservation reservation) {
        return new ReservationValuesDto(
                reservation.getUserName(),
                reservation.getReservationDateTime(),
                reservation.getTheme());
    }
}
