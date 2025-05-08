package roomescape.service.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import roomescape.domain.Reservation;

public record ReservationServiceResponse(
        Long id,
        String name,
        LocalDate date,
        InnerTime time,
        InnerTheme theme
) {

    public static ReservationServiceResponse from(Reservation reservation) {
        return new ReservationServiceResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                new InnerTime(
                        reservation.time().id(),
                        reservation.time().startAt()
                ),
                new InnerTheme(
                        reservation.theme().id(),
                        reservation.theme().name(),
                        reservation.theme().description(),
                        reservation.theme().thumbnail()
                )
        );
    }

    public record InnerTime(
            Long id,
            LocalTime startAt
    ) {
    }

    public record InnerTheme(
            Long id,
            String name,
            String description,
            String thumbnail
    ) {
    }
}
