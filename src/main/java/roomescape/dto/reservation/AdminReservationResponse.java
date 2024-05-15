package roomescape.dto.reservation;

import java.time.LocalDate;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

public record AdminReservationResponse(long id, Member member, Theme theme, LocalDate date, ReservationTime time) {
    public static AdminReservationResponse from(Reservation reservation) {
        return new AdminReservationResponse(
                reservation.getId(),
                reservation.getMember(),
                reservation.getTheme(),
                reservation.getDate(),
                reservation.getTime()
        );
    }

}
