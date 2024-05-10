package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.reservation.model.Reservation;

public record CreateReservationResponse(Long id, String name, LocalDate date, CreateTimeOfReservationsResponse time, CreateThemeOfReservationResponse theme) {
    public static CreateReservationResponse from(final Reservation reservation) {
        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getMember().getName(), // TODO: CreateTimeOfReservationsResponse 처럼 변경
                reservation.getDate(),
                CreateTimeOfReservationsResponse.from(reservation.getReservationTime()),
                CreateThemeOfReservationResponse.from(reservation.getTheme())
        );
    }
}
