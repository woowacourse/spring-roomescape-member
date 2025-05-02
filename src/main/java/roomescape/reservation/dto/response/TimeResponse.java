package roomescape.reservation.dto.response;

import roomescape.reservation.entity.Time;

public record TimeResponse(
        Long id,
        String startAt
) {

    public static TimeResponse from(Time entity) {
        return new TimeResponse(entity.getId(), entity.getFormattedTime());
    }
}
