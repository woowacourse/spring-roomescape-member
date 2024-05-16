package roomescape.reservation.dto.response;

import roomescape.member.dto.response.FindMemberResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.theme.dto.response.FindThemeResponse;
import roomescape.util.CustomDateTimeFormatter;

public record FindReservationResponse(
        Long id,
        FindMemberResponse member,
        String date,
        FindReservationTimeResponse time,
        FindThemeResponse theme) {

    public static FindReservationResponse of(final Reservation reservation) {
        return new FindReservationResponse(
                reservation.getId(),
                FindMemberResponse.of(reservation.getMember()),
                CustomDateTimeFormatter.getFormattedDate(reservation.getDate()),
                FindReservationTimeResponse.of(reservation.getReservationTime()),
                FindThemeResponse.of(reservation.getTheme())
        );
    }
}
