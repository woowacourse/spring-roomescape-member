package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public record ReservationTimeStatusResponseDto(Long timeId, LocalTime startAt, Boolean alreadyBooked) {

    public ReservationTimeStatusResponseDto(final ReservationTimeStatusDto reservationTimeStatusDto) {
        this(reservationTimeStatusDto.id(), reservationTimeStatusDto.startAt(), reservationTimeStatusDto.alreadyBooked());
    }

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("startAt")
    public LocalTime getFormattedStartAt() {
        return startAt;
    }
}
