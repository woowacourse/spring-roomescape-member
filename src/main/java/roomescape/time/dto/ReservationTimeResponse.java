package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeResponse(long id, LocalTime startAt) {

    public ReservationTimeResponse(final ReservationTime reservationTime) {
        this(reservationTime.getId(), reservationTime.getStartAt());
    }

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("startAt")
    public LocalTime getFormattedStartAt() {
        return startAt;
    }
}
