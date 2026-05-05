package roomescape.date.dto;

import roomescape.date.domain.ReservationDate;

import java.time.LocalDate;

public record ReservationDateDetailDto(
        Long id,
        LocalDate date
) {
    public static ReservationDateDetailDto from(ReservationDate reservationDate) {
        return new ReservationDateDetailDto(reservationDate.id(), reservationDate.date());
    }
}
