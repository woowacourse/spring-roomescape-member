package roomescape.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        InnerTime time,
        InnerTheme theme
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.member().name(),
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
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
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
