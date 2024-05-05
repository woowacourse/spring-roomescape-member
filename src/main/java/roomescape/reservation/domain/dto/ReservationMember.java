package roomescape.reservation.domain.dto;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;

public record ReservationMember(Reservation reservation, Member member) {
}
