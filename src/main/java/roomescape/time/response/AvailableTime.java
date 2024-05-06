package roomescape.time.response;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record AvailableTime(long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt, boolean alreadyBooked) {
}
