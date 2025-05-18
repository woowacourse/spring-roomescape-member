package roomescape.reservation.domain.dto;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.reservationtime.domain.dto.ReservationTimeResDto;
import roomescape.theme.domain.dto.ThemeResDto;

public record ReservationResDto(
    Long id,
    Member member,
    LocalDate date,
    ReservationTimeResDto time,
    ThemeResDto theme
) {
}
