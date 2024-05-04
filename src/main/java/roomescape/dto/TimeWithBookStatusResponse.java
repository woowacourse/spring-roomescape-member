package roomescape.dto;

import java.time.LocalTime;

public record TimeWithBookStatusResponse(Long timeId, LocalTime startAt, boolean alreadyBooked) {

}
