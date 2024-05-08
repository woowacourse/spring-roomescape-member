package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.time.domain.ReservationTimeStatus;

import java.time.LocalTime;

public record ReservationTimeStatusResponseDto(Long timeId, LocalTime startAt, Boolean alreadyBooked) {

    public ReservationTimeStatusResponseDto(final ReservationTimeStatus reservationTimeStatus) {
        this(reservationTimeStatus.getId(), reservationTimeStatus.getStartAt(), reservationTimeStatus.getAlreadyBooked());
    }

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("startAt")
    public LocalTime getFormattedStartAt() {
        return startAt;
    }
}
