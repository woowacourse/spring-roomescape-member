package roomescape.domain.time.mapper;

import roomescape.domain.time.dto.response.TimeResponseDTO;
import roomescape.domain.time.entity.Time;

public class TimeMapper {

    public static TimeResponseDTO toResponseDTO(Time time) {
        return new TimeResponseDTO(time.getId(), time.getStartAt());
    }
}
