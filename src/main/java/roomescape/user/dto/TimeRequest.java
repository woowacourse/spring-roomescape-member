package roomescape.user.dto;

import java.time.LocalTime;

public record TimeRequest(
    LocalTime startAt,
    LocalTime finishAt
) {

}
