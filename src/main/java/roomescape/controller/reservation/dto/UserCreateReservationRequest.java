package roomescape.controller.reservation.dto;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record UserCreateReservationRequest(
        @NotNull
        LocalDate date,

        @NotNull
        Long timeId,

        @NotNull
        Long themeId) {

    public Reservation toDomain(final Member member, final ReservationTime time, final Theme theme) {
        return new Reservation(null, member, date, time, theme);
    }
}
