package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public record ReservationTimeStatusResponse(Long timeId, LocalTime startAt, Boolean alreadyBooked) {

    public ReservationTimeStatusResponse(final ReservationTimeStatus reservationTimeStatus) {
        this(reservationTimeStatus.id(), reservationTimeStatus.startAt(), reservationTimeStatus.alreadyBooked());
    }

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("startAt")
    public LocalTime getFormattedStartAt() {
        return startAt;
    }
}
