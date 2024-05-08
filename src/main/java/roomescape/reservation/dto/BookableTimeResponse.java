package roomescape.reservation.dto;

import java.time.LocalTime;

public record BookableTimeResponse(LocalTime startAt, Long timeId, boolean alreadyBooked) {

}
