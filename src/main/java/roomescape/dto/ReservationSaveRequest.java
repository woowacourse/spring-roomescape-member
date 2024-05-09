package roomescape.dto;

import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationSaveRequest(
        String date,
        Long timeId,
        Long themeId
) {

    public Reservation toModel(
            final MemberResponse memberResponse,
            final ThemeResponse themeResponse,
            final ReservationTimeResponse timeResponse
    ) {
        final Member member = new Member(memberResponse.id(), new Name(memberResponse.name()), memberResponse.email());
        final ReservationTime time = new ReservationTime(timeResponse.id(), timeResponse.startAt());
        final Theme theme = new Theme(themeResponse.id(), themeResponse.name(), themeResponse.description(), themeResponse.thumbnail());
        return new Reservation(member, date, time, theme);
    }
}
