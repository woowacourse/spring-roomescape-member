package roomescape.dto.reservation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

import java.time.LocalDate;

public record UserReservationRequest(
        @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotNull @Positive Long timeId,
        @NotNull @Positive Long themeId
) {

    public Reservation toReservation(Time time, Theme theme, Member member) {
        return new Reservation(this.date, time, theme, member);
    }
}
