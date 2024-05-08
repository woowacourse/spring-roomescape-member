package roomescape.controller.reservation.dto;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationRequest(
        @NotNull
        LocalDate date,
        @NotNull
        Long timeId,
        @NotNull
        Long themeId) {

    public Reservation toDomain(final String name, final ReservationTime time, final Theme theme) {
        //TODO name 찾아오기

        return new Reservation(null, name, date, time, theme);
    }
}
