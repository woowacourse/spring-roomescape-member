package roomescape.reservation.dto.response;

import roomescape.member.dto.response.FindMemberOfReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.util.CustomDateTimeFormatter;

public record FindReservationResponse(
        Long id,
        FindMemberOfReservationResponse member,
        String date,
        FindTimeOfReservationsResponse time,
        FindThemeOfReservationResponse theme) {

    public static FindReservationResponse of(final Reservation reservation) {
        return new FindReservationResponse(
                reservation.getId(),
                FindMemberOfReservationResponse.of(reservation.getMember()),
                CustomDateTimeFormatter.getFormattedDate(reservation.getDate()),
                FindTimeOfReservationsResponse.of(reservation.getReservationTime()),
                FindThemeOfReservationResponse.of(reservation.getTheme())
        );
    }
}
