package roomescape.dto.reservationDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.Theme.Theme;
import roomescape.domain.member.Member;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.dto.memberDto.MemberResponse;

public record ReservationResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme,
        MemberResponse member
) {

    public static ReservationResponse from(Reservation reservation) {
        ReservationTimeResponse reservationTimeResponse = getReservationTimeResponse(reservation.getTime());
        ThemeResponse themeResponse = getThemeResponse(reservation.getTheme());
        MemberResponse memberResponse = getMemberResponse(reservation.getMember());

        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                reservationTimeResponse,
                themeResponse,
                memberResponse
        );
    }

    private static ReservationTimeResponse getReservationTimeResponse(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }

    private static ThemeResponse getThemeResponse(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName().getName(),
                theme.getDescription().getDescription(),
                theme.getThumbnail().getThumbnail()
        );
    }

    private static MemberResponse getMemberResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName()
        );
    }
}
