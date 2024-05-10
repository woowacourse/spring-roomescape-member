package roomescape.member.dto;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record CompletedReservation(long id, LocalDate date, ReservationTime time, Theme theme, Member member) {
}
