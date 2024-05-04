package roomescape.controller.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(

        @NotBlank //TODO not blank에서 null 체크되나?
        @NotNull
        String name,
        String date, //todo local date
        @NotNull
        Long timeId,
        @NotNull
        Long themeId) {

    public Reservation toDomain(final ReservationTime time, final Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }
}
