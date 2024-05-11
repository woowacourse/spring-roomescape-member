package roomescape.reservation.dto.response;

import roomescape.member.model.Member;
import roomescape.reservation.model.Reservation;
import roomescape.util.CustomDateTimeFormatter;

public record CreateReservationResponse(Long id, Member member, String date, CreateTimeOfReservationsResponse time, CreateThemeOfReservationResponse theme) {
    public static CreateReservationResponse of(final Reservation reservation) {
        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getMember(),
                CustomDateTimeFormatter.getFormattedDate(reservation.getDate()),
                CreateTimeOfReservationsResponse.of(reservation.getReservationTime()),
                CreateThemeOfReservationResponse.of(reservation.getTheme())
        );
    }
}
