package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;

public record ReservationResponseDto(Long id, String name, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                     ReservationTimeResponseDto time) {

    public static ReservationResponseDto from(Reservation reservation, ReservationTime reservationTime) {
        ReservationTimeResponseDto timeResponseDto = ReservationTimeResponseDto.from(reservationTime);

        return new ReservationResponseDto(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                timeResponseDto);
    }
}
