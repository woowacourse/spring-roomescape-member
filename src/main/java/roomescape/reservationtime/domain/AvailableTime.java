package roomescape.reservationtime.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public record AvailableTime(long timeId, LocalTime startAt, @JsonProperty("isAvailable") boolean isAvailable) {

}
