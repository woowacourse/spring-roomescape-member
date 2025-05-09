package roomescape.reservation.service.dto;

import java.time.LocalDate;
import roomescape.member.domain.Member;

public record ReservationInfo(Member member, LocalDate date, Long timeId, Long themeId) {

}
