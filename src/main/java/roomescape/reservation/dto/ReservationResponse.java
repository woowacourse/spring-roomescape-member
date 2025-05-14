package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.member.dto.MemberResponse;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.theme.dto.ThemeResponse;

public record ReservationResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme,
        MemberResponse member
) {

}