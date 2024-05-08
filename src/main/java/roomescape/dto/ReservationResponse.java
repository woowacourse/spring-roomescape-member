package roomescape.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.domain.Reservation;

public record ReservationResponse(Long id,
                                  String name,
                                  @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  TimeResponse time, ThemeResponse theme) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                TimeResponse.from(reservation.time()),
                ThemeResponse.from(reservation.theme()));
    }
}
