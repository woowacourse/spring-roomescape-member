package roomescape.application.dto;

import roomescape.domain.model.Member;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;

import java.time.LocalDate;

public record ReservationInfoResponse(
        Long id,
        Member member,
        LocalDate date,
        ReservationTime reservationTime,
        Theme theme
) {
}
