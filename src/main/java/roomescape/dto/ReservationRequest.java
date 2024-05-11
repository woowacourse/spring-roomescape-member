package roomescape.dto;

import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationRequest(LocalDate date, Long timeId, Long themeId, Long memberId) {

    public Reservation toEntity(final ReservationTime reservationTime, final Theme theme, final Member member) {
        return new Reservation(date, member, reservationTime, theme);
    }
}
