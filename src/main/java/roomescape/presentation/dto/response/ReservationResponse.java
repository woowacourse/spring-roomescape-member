package roomescape.presentation.dto.response;

import roomescape.domain.model.Member;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        LocalDate date,
        StartAtResponse time,
        ReservationThemeResponse theme
) {

    public static ReservationResponse of(final Long id, final Member member, final LocalDate date, final ReservationTime time, final Theme theme) {
        return new ReservationResponse(
                id,
                MemberResponse.from(member),
                date,
                StartAtResponse.from(time),
                ReservationThemeResponse.from(theme)
        );
    }
}
