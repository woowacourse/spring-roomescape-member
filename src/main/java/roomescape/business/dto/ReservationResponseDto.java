package roomescape.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import roomescape.business.Reservation;

public record ReservationResponseDto(
        @JsonProperty("id") long id,
        @JsonProperty("name") String name,
        @JsonProperty("date") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @JsonProperty("time") ReservationTimeResponseDto time) {

    public static ReservationResponseDto from(Reservation reservation) {
        ReservationTimeResponseDto time = ReservationTimeResponseDto.from(reservation.getTime());
        return new ReservationResponseDto(
                reservation.getId(), reservation.getName(), reservation.getDate(), time);
    }
}
