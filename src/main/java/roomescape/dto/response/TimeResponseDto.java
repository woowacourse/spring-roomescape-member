package roomescape.dto.response;

import roomescape.dao.row.TimeRow;
import roomescape.domain.Time;

import java.time.LocalTime;

public record TimeResponseDto(
        Long id,
        LocalTime startAt
) {
    public static TimeResponseDto from(TimeRow time) {
        return new TimeResponseDto(time.id(), time.startAt());
    }

    public static TimeResponseDto from(Time time) {
        return new TimeResponseDto(time.getId(), time.getStartAt());
    }
}
