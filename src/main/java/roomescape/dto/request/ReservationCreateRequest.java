package roomescape.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationCreateRequest(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                       Long memberId,
                                       Long timeId,
                                       Long themeId) {
    public Reservation createReservation(Member member, ReservationTime time, Theme theme) {
        return new Reservation(date, member, time, theme);
    }
}
