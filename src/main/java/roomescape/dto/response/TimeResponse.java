package roomescape.dto.response;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.domain.ReservationTime;

public record TimeResponse(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public static TimeResponse from(ReservationTime time) {
        return new TimeResponse(time.getId(), time.getStartAt());
    }
}
