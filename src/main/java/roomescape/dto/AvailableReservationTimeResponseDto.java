package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record AvailableReservationTimeResponseDto(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt, boolean alreadyBooked) {

}
